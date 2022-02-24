package ca.unb.mobiledev.reflexrevolution.detectors;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

//Author: Hailey Savoie
//Ideas taken from: https://riptutorial.com/android/example/16543/swipe-detection

public class SwipeDetector implements View.OnTouchListener{
    GestureDetector gestureDetector;
    private OnSwipeListener listener;

    public void setOnSwipeListener(OnSwipeListener listener) {
        this.listener = listener;
    }

    public interface OnSwipeListener {
        void onSwipe(Action action);
    }

    public SwipeDetector(Context context, View view){
        int SWIPE_THRESHOLD = 100;
        int SWIPE_VELOCITY_THRESHOLD = 100;

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
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
                        if(diffX > 0) listener.onSwipe(Action.SWIPE_RIGHT);
                        else listener.onSwipe(Action.SWIPE_LEFT);
                    }
                }
                else if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                    if(diffY > 0) listener.onSwipe(Action.SWIPE_DOWN);
                    else listener.onSwipe(Action.SWIPE_UP);
                }
                return true;
            }
        };

        gestureDetector = new GestureDetector(context, gestureListener);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public enum Action {
        SWIPE_RIGHT,
        SWIPE_LEFT,
        SWIPE_UP,
        SWIPE_DOWN
    }

}
