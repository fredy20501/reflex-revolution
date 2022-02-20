package ca.unb.mobiledev.reflexrevolution;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeListener extends SwipeActivity implements View.OnTouchListener{
        GestureDetector gestureDetector;

        SwipeListener(Context context, View view){
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

            gestureDetector = new GestureDetector(context, listener);
            view.setOnTouchListener(this);
        }

        public void onSwipeRight(){
            if(getDirection().equals("right")) {
                //swipeText.setText("Correct! Right");
                incrementScore();
                updateScore();
                newDirection();
            }
            else
                updateText();
        }

        public void onSwipeLeft(){
            if(getDirection().equals("left")) {
                //swipeText.setText("Correct! Left");
                incrementScore();
                updateScore();
                newDirection();
            }
            else
                updateText();
        }

        public void onSwipeUp(){
            if(getDirection().equals("up")) {
                //swipeText.setText("Correct! Up");
                incrementScore();
                updateScore();
                newDirection();
            }
            else
                updateText();
        }

        public void onSwipeDown(){
            if(getDirection().equals("down")) {
                //swipeText.setText("Correct! Down");
                incrementScore();
                updateScore();
                newDirection();
            }
            else
                updateText();
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
}
