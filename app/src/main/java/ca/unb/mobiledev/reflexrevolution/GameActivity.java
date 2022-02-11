package ca.unb.mobiledev.reflexrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    //Time in ms
    //Could get rid of this and do random intervals or scale it off of score
    private final int TIME_BETWEEN_LOOPS = 1000;

    private TextView timeText;
    private TextView scoreText;
    private LinearLayout layout; //Layout we should add new UI elements to

    private CountDownTimer timer;
    private Instruction currentInstruction = null;
    private ArrayList<Instruction> instructions;
    private Random rand;
    private GameMode gameMode;
    private Difficulty difficulty;

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
        score = 0;

        instructions = InstructionUtil.createInstructions(gameMode);
        updateTimerText();
        updateScoreText();
        resetTimer();
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
            //When timer hits zero, start GameOverActivity and pass it the score
            public void onFinish() {
                Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                intent.putExtra("Score", score);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    //Prepare and start new instruction loop
    private void gameLoop() {
        currentInstruction = getRandomInstruction();
        displayInstruction();
        newTimer();
    }

    //Returns a random instruction from instructions
    private Instruction getRandomInstruction() { return instructions.get(rand.nextInt(instructions.size())); }

    //Display UI elements for the current instruction, and set up any necessary input receivers
    private void displayInstruction() {
        switch (currentInstruction){
            case BUTTON:
                Button button = new Button(this);
                button.setText("Press");
                button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                    detectInput(Instruction.BUTTON);
                }
                });

                layout.addView(button);
                break;

            default:
                break;
        }
    }

    //Clear all added UI elements
    private void resetUI() {
        layout.removeAllViews();
    }

    //Handle any inputs received
    private void detectInput(Instruction instruction) {
        if (instruction == currentInstruction) {
            // Correct input detected

            //Update score
            score++;
            updateScoreText();

            //Stop timer and clear UI, wait one second, then start the gameloop (in resetTimer)
            resetTimer();
            resetUI();
        }
    }

    //Scuffed function that will give a scaled timer based on score.
    //Starts at ~3000ms and min value is ~1000ms
    //I literally came up with this from playing around in desmos so we might want to rework this later
    //Probably adjust this based on difficulty later
    private int scaleTimerFromScore(){
        int scaledTimer = (int)Math.pow(2, -0.04*score + 11) + 1000;
        return scaledTimer;
    }

    //Resume timer if app was closed
    @Override
    protected void onResume() {
        super.onResume();
        if(timer != null) {
            resumeTimer();
        }
    }

    //Make sure timer doesn't keep going with app closed
    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}