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
    private String currentInstruction = null;
    private ArrayList<String> instructions;

    private int timeCount;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        timeText = findViewById(R.id.timerText);
        scoreText = findViewById(R.id.currentScoreText);
        layout = findViewById(R.id.layout);
        score = 0;

        createInstructions();
        updateTimerText();
        updateScoreText();
        resetTimer();
    }

    //Fill the instructions list with instructions
    private void createInstructions(){
        instructions = new ArrayList<>();

        //Add instructions here
        instructions.add("Button"); //Temp instruction for testing
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

    //Create a new timer that counts down from 5, and closes the activity once it hits 0
    private void newTimer(){
        timeCount = scaleTimerFromScore();
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
    private String getRandomInstruction() {
        Random rand = new Random();
        //Get a random instruction from the instructions arraylist
        String randomInstruction = instructions.get(rand.nextInt(instructions.size()));
        return randomInstruction;
    }

    //Display UI elements for the current instruction, and set up any necessary input receivers
    private void displayInstruction() {
        if(currentInstruction.equals("Button")){
            Button button = new Button(this);
            button.setText("Press");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detectInput("Button");
                }
            });

            layout.addView(button);
        }
    }

    //Clear all added UI elements
    private void resetUI() {
        layout.removeAllViews();
    }

    //Handle any inputs received
    private void detectInput(String instruction) {
        if (instruction.equals(currentInstruction)) {
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
    private int scaleTimerFromScore(){
        int scaledTimer = (int)Math.pow(2, -0.04*score + 11) + 1000;
        return scaledTimer;
    }
}
