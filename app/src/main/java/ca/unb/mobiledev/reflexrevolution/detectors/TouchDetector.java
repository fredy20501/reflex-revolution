package ca.unb.mobiledev.reflexrevolution.detectors;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TouchDetector implements View.OnTouchListener{

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private final GestureDetector gestureDetector;
    private final List<ActionListener> listeners;

    public void addListener(ActionListener listener) {
        this.listeners.add(listener);
    }

    public interface ActionListener {
        void onSwipe(SwipeAction action);
        void onTap(TapAction action);
    }

    private void tapDetected(TapAction action) {
        // Trigger all listeners
        for (ActionListener listener : listeners) {
            listener.onTap(action);
        }
    }
    private void swipeDetected(SwipeAction action) {
        // Trigger all listeners
        for (ActionListener listener : listeners) {
            listener.onSwipe(action);
        }
    }

    public TouchDetector(View view){
        listeners = new ArrayList<>();

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e){
                tapDetected(TapAction.TAP);
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e){
                tapDetected(TapAction.DOUBLE_TAP);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e){
                tapDetected(TapAction.HOLD_TAP);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                swipeDetected(SwipeAction.FLICK);

                if(Math.abs(diffX) > Math.abs(diffY)){
                    if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                        if(diffX > 0) swipeDetected(SwipeAction.SWIPE_RIGHT);
                        else swipeDetected(SwipeAction.SWIPE_LEFT);
                    }
                }
                else if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                    if(diffY > 0) swipeDetected(SwipeAction.SWIPE_DOWN);
                    else swipeDetected(SwipeAction.SWIPE_UP);
                }
                return true;
            }
        };

        gestureDetector = new GestureDetector(view.getContext(), gestureListener);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public enum TapAction {
        TAP,
        DOUBLE_TAP,
        HOLD_TAP,
        DONT_TAP
    }
    public enum Type {FLING,SWIPE}
    public enum SwipeAction {
        FLICK(Type.FLING),
        SWIPE_RIGHT(Type.SWIPE),
        SWIPE_LEFT(Type.SWIPE),
        SWIPE_UP(Type.SWIPE),
        SWIPE_DOWN(Type.SWIPE);

        private final Type type;
        SwipeAction(Type type) { this.type = type;}
        public Type getType(){ return this.type;}
    }
}
