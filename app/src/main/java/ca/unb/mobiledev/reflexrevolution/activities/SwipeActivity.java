package ca.unb.mobiledev.reflexrevolution.activities;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.sensors.SwipeListener;
import ca.unb.mobiledev.reflexrevolution.utils.Instruction;

//Author: Hailey Savoie
//Ideas taken from: https://riptutorial.com/android/example/16543/swipe-detection

public class SwipeActivity extends AppCompatActivity {

    private Random r;
    private int index;
    private int score;
    private TextView swipeText;
    private TextView scoreText;
    private String[] directions;
    private SwipeListener swipeListener;
    private RelativeLayout relativeLayout;
    private ArrayList<Instruction> instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_activity);

        relativeLayout = findViewById(R.id.relative_layout);
        swipeText = findViewById(R.id.text_view);
        scoreText = findViewById(R.id.text_view2);
        instructions = new ArrayList<>();
        instructions.add(Instruction.SWIPE_RIGHT);
        instructions.add(Instruction.SWIPE_LEFT);
        instructions.add(Instruction.SWIPE_UP);
        instructions.add(Instruction.SWIPE_DOWN);

        swipeListener.setOnSwipeListener(new SwipeListener.OnSwipeListener() {
            @Override
            public void onSwipeLeft() {
                if(getDirection().equals("left"))
                    correctAnswer();
                else
                    updateText();
            }

            @Override
            public void onSwipeRight() {
                if(getDirection().equals("right"))
                    correctAnswer();
                else
                    updateText();
            }

            @Override
            public void onSwipeUp(){
                if(getDirection().equals("up"))
                    correctAnswer();
                else
                    updateText();
            }

            @Override
            public void onSwipeDown(){
                if(getDirection().equals("down"))
                    correctAnswer();
                else
                    updateText();
            }
        });

        score = 0;
        r = new Random();
        directions = new String[]{"right", "left", "up", "down"};

        newDirection();
    }

    protected void newDirection(){
        index = r.nextInt(4 - 0) + 0;
        swipeText.setText("Swipe " + directions[index]);
    }

    protected void updateScore(){
        scoreText.setText("Score: " + score);
    }

    protected void incrementScore(){
        score++;
    }

    protected void updateText(){
        swipeText.setText("Fail");
    }

    protected String getDirection(){
        return directions[index];
    }

    protected void correctAnswer(){
        incrementScore();
        updateScore();
        newDirection();
    }
}
