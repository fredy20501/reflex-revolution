package ca.unb.mobiledev.reflexrevolution;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
        swipeListener = new SwipeListener(relativeLayout);

        score = 0;
        r = new Random();
        directions = new String[]{"right", "left", "up", "down"};

        newDirection();
    }

    private void newDirection(){
        index = r.nextInt(4 - 0) + 0;
        swipeText.setText("Swipe " + directions[index]);
    }

    private void updateScore(){
        scoreText.setText("Score: " + score);
    }

    private class SwipeListener implements View.OnTouchListener{
        GestureDetector gestureDetector;

        SwipeListener(View view){
            int SWIPE_THRESHOLD = 100;
            int SWIPE_VELOCITY_THRESHOLD = 100;

            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDown(MotionEvent e){
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();

                    if(Math.abs(diffX) > Math.abs(diffY)){
                        if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                            if(diffX > 0)
                                onSwipeRight();
                            else
                                onSwipeLeft();
                        }
                    }
                    else if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                        if(diffY > 0)
                            onSwipeDown();
                        else
                            onSwipeUp();
                    }
                    return true;
                }
            };

            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        public void onSwipeRight(){
            if(directions[index].equals("right")) {
                swipeText.setText("Correct! Right");
                score++;
                updateScore();
                newDirection();
            }
            else
                swipeText.setText("Fail! Right");
        }

         public void onSwipeLeft(){
             if(directions[index].equals("left")) {
                 swipeText.setText("Correct! Left");
                 score++;
                 updateScore();
                 newDirection();
             }
             else
                 swipeText.setText("Fail! Left");
         }

         public void onSwipeUp(){
             if(directions[index].equals("up")) {
                 swipeText.setText("Correct! Up");
                 score++;
                 updateScore();
                 newDirection();
             }
             else
                 swipeText.setText("Fail! Up");
         }

         public void onSwipeDown(){
             if(directions[index].equals("down")) {
                 swipeText.setText("Correct! Down");
                 score++;
                 updateScore();
                 newDirection();
             }
             else
                 swipeText.setText("Fail! Down");
         }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }
}