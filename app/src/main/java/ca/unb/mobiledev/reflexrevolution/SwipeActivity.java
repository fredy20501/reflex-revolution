package ca.unb.mobiledev.reflexrevolution;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_activity);

        relativeLayout = findViewById(R.id.relative_layout);
        swipeText = findViewById(R.id.text_view);
        scoreText = findViewById(R.id.text_view2);
        swipeListener = new SwipeListener(this, relativeLayout);

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
}
