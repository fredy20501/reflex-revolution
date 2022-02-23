package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.sensors.JumpDetector;
import ca.unb.mobiledev.reflexrevolution.sensors.RotationDetector;
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
    private CountDownTimer resetTimer;
    private boolean gettingNewInstruction; //Keep track of when we are getting a new instruction
    private Instruction currentInstruction = null;
    private ArrayList<Instruction> instructions;
    private ArrayList<Instruction> dontInstructions;
    private Random rand;
    private GameMode gameMode;
    private Difficulty difficulty;

    private SensorManager sensorManager;
    private Sensor accelerationSensor;
    private Sensor gravitySensor;
    private Sensor gyroscopeSensor;
    private ShakeDetector shakeDetector;
    private JumpDetector jumpDetector;
    private RotationDetector rotationDetector;
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
        gettingNewInstruction = false;
        timeText = findViewById(R.id.timerText);
        scoreText = findViewById(R.id.currentScoreText);
        layout = findViewById(R.id.layout);
        containerLayout = findViewById(R.id.containerLayout);
        score = 0;

        dontInstructions = new ArrayList<>();
        dontInstructions.add(Instruction.DONT_TURN);
        dontInstructions.add(Instruction.FREEZE);
        dontInstructions.add(Instruction.DONT_TAP);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        instructions = InstructionUtil.createInstructions(gameMode, sensorManager);
        initializeSensors();
        updateTimerText();
        updateScoreText();
        resetTimer();
    }

    private void initializeSensors() {
        // Get sensors
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Initialize detectors
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) { if (count > 1) detectInput(Instruction.SHAKE); }
            @Override
            public void onMove() { detectInput(Instruction.MOVE); }
        });
        jumpDetector = new JumpDetector();
        jumpDetector.setOnJumpListener(() -> detectInput(Instruction.JUMP));
        rotationDetector = new RotationDetector();
        rotationDetector.setOnJumpListener(new RotationDetector.OnRotateListener() {
            @Override
            public void onRotate(Instruction instr) { detectInput(instr); }
            @Override
            public void onMove() { detectInput(Instruction.MOVE); }
        });
        tapDetector = new TapDetector(this, containerLayout);
        tapDetector.setOnTapListener(instruction -> detectInput(instruction));
    }

    private void updateTimerText(){
        timeText.setText("Timer: " + timeCount);
    }

    private void updateScoreText(){
        scoreText.setText("Score: " + score);
    }

    //Stop current timer then call gameloop after one second
    private void resetTimer(){
        if(timer != null) timer.cancel();
        if(resetTimer != null) resetTimer.cancel();
        //Wait one second before calling gameloop
        //We could also change this so that the time between is random or scales off of score
        resetTimer = new CountDownTimer(TIME_BETWEEN_LOOPS, 1000) {
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
        if(timer != null) timer.cancel();

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
                if (dontInstructions.contains(currentInstruction)) {
                    detectInput(currentInstruction);
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
        gettingNewInstruction = false;
    }

    //Returns a random instruction from instructions
    private Instruction getRandomInstruction() { return instructions.get(rand.nextInt(instructions.size())); }

    //Clear all added UI elements
    private void resetUI() {
        layout.removeAllViews();
    }

    //Handle any inputs received
    private void detectInput(Instruction instruction) {
        if (
            (currentInstruction == Instruction.TURN_LEFT && instruction == Instruction.TURN_RIGHT) ||
            (currentInstruction == Instruction.TURN_RIGHT && instruction == Instruction.TURN_LEFT)
        ) {
            // Wrong turn
            endGame();
        }
        else if (currentInstruction == Instruction.DONT_TURN &&
                (instruction == Instruction.TURN_LEFT || instruction == Instruction.TURN_RIGHT)) {
            // Turned when not supposed to
            endGame();
        }
        else if (currentInstruction == Instruction.FREEZE && instruction == Instruction.MOVE) {
            // Moved when not supposed to
            endGame();
        }
        //If instruction is don't tap and a tap input is received, end game
        else if(currentInstruction == Instruction.DONT_TAP && instruction == Instruction.TAP) {
            endGame();
        }
        else if (instruction == currentInstruction && !gettingNewInstruction) {
            // Correct input detected
            gettingNewInstruction = true;

            //Update score
            score++;
            updateScoreText();

            //Stop timer and clear UI, wait one second, then start the gameloop (in resetTimer)
            resetUI();
            resetTimer();
            unregisterListeners();
        }
    }

    //Ends the game, sending score and game options to the Game Over screen
    private void endGame() {
        if(timer != null) timer.cancel();
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
            case TURN_LEFT:
            case TURN_RIGHT:
            case DONT_TURN:
                sensorManager.registerListener(rotationDetector, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case FREEZE:
                sensorManager.registerListener(shakeDetector, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(rotationDetector, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
            case TURN_LEFT:
            case TURN_RIGHT:
            case DONT_TURN:
                sensorManager.unregisterListener(rotationDetector);
                break;
            case FREEZE:
                sensorManager.unregisterListener(shakeDetector);
                sensorManager.unregisterListener(rotationDetector);
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
        if(timer != null) timer.cancel();
    }
}