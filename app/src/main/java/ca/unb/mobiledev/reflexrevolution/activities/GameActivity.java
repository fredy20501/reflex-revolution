package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.sensors.JumpDetector;
import ca.unb.mobiledev.reflexrevolution.sensors.TapDetector;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.utils.Instruction;
import ca.unb.mobiledev.reflexrevolution.utils.InstructionUtil;
import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.sensors.ShakeDetector;

public class GameActivity extends AppCompatActivity {
    //Time in ms
    //Could get rid of this and do random intervals or scale it off of score
    private final int TIME_BETWEEN_LOOPS = 1000;

    private TextView timeText;
    private TextView scoreText;
    private LinearLayout layout; //Layout we should add new UI elements to
    private ConstraintLayout containerLayout; //Top level layout containing all other views

    private CountDownTimer timer;
    private Instruction currentInstruction = null;
    private ArrayList<Instruction> instructions;
    private Random rand;
    private GameMode gameMode;
    private Difficulty difficulty;

    private SensorManager sensorManager;
    private Sensor accelerationSensor;
    private Sensor gravitySensor;
    private ShakeDetector shakeDetector;
    private JumpDetector jumpDetector;
    private TapDetector tapDetector;

    private int timeCount;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        //Retrieve gamemode and difficulty
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            gameMode = (GameMode)extras.get("GameMode");
            difficulty = (Difficulty)extras.get("Difficulty");
        }

        rand = new Random();
        timeText = findViewById(R.id.timerText);
        scoreText = findViewById(R.id.currentScoreText);
        layout = findViewById(R.id.layout);
        containerLayout = findViewById(R.id.containerLayout);
        score = 0;

        tapDetector = new TapDetector(this, containerLayout);
        tapDetector.setOnTapListener(instruction -> detectInput(instruction));
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        instructions = InstructionUtil.createInstructions(gameMode, this);
        initializeSensors();
        updateTimerText();
        updateScoreText();
        resetTimer();
    }

    private void initializeSensors() {
        // Get sensors
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        // Initialize detectors
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(count -> {
            if (count > 1) detectInput(Instruction.SHAKE);
        });
        jumpDetector = new JumpDetector();
        jumpDetector.setOnJumpListener(() -> detectInput(Instruction.JUMP));
    }

    private void updateTimerText(){
        timeText.setText("Timer: " + timeCount);
    }

    private void updateScoreText(){
        scoreText.setText("Score: " + score);
    }

    //Stop current timer then call gameloop after one second
    private void resetTimer(){
        if(timer != null) {
            timer.cancel();
        }
        //Wait one second before calling gameloop
        //We could also change this so that the time between is random or scales off of score
        new CountDownTimer(TIME_BETWEEN_LOOPS, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                gameLoop();
            }
        }.start();
    }

    //Get new timer count, then start it by "resuming"
    private void newTimer(){
        timeCount = scaleTimerFromScore();
        resumeTimer();
    }

    //"Resumes" timer by creating a new timer starting at timeCount
    //Might be off by 100ms because I can only get
    private void resumeTimer(){
        //Check that there is not already a timer running
        if(timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(timeCount, 100) {
            @Override
            //onTick() is also called as soon as the counter starts, so call timeCount-- after updateText()
            public void onTick(long l) {
                updateTimerText();
                timeCount-= 100;
            }

            @Override
            //When timer hits zero, end game unless instruction requires no input
            public void onFinish() {
                if(currentInstruction == Instruction.DONTTAP){
                    detectInput(Instruction.DONTTAP);
                }
                else {
                    endGame();
                }
            }
        }.start();
    }

    //Prepare and start new instruction loop
    private void gameLoop() {
        currentInstruction = getRandomInstruction();
        InstructionUtil.displayInstruction(currentInstruction, layout, this);
        registerListeners();
        newTimer();
    }

    //Returns a random instruction from instructions
    private Instruction getRandomInstruction() { return instructions.get(rand.nextInt(instructions.size())); }

    //Clear all added UI elements
    private void resetUI() {
        layout.removeAllViews();
    }

    //Handle any inputs received
    private void detectInput(Instruction instruction) {
        //If instruction is don't tap and a tap input is received, end game
        if(currentInstruction == Instruction.DONTTAP  &&
                (instruction == Instruction.TAP ||
                 instruction == Instruction.DOUBLETAP ||
                 instruction == Instruction.HOLD)) {
            endGame();
        }
        if (instruction == currentInstruction) {
            // Correct input detected

            //Update score
            score++;
            updateScoreText();

            //Stop timer and clear UI, wait one second, then start the gameloop (in resetTimer)
            resetTimer();
            resetUI();
            unregisterListeners();
        }
    }

    //Ends the game, sending score and game options to the Game Over screen
    private void endGame(){
        //In case user taps on don't tap instruction
        if(timer != null){
            timer.cancel();
        }
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("Score", score);
        intent.putExtra("GameMode", gameMode);
        intent.putExtra("Difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    //Scuffed function that will give a scaled timer based on score.
    //Starts at ~3000ms and min value is ~1000ms
    //I literally came up with this from playing around in desmos so we might want to rework this later
    //Probably adjust this based on difficulty later
    private int scaleTimerFromScore() {
        return (int)Math.pow(2, -0.04*score + 11) + 1000;
    }

    private void registerListeners() {
        if (currentInstruction == null) return;
        switch (currentInstruction) {
            case SHAKE:
                sensorManager.registerListener(shakeDetector, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case JUMP:
                sensorManager.registerListener(jumpDetector, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(jumpDetector, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
                break;
        }
    }

    private void unregisterListeners() {
        if (currentInstruction == null) return;
        switch (currentInstruction) {
            case SHAKE:
                sensorManager.unregisterListener(shakeDetector);
                break;
            case JUMP:
                sensorManager.unregisterListener(jumpDetector);
                break;
        }
    }

    //Resume timer if app was closed
    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
        if(timer != null) resumeTimer();
    }

    //Make sure timer doesn't keep going with app closed
    @Override
    protected void onPause() {
        super.onPause();
        unregisterListeners();
        timer.cancel();
    }
}