package ca.unb.mobiledev.reflexrevolution.detectors;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class TapDetector implements View.OnTouchListener{
    GestureDetector gestureDetector;

    private OnTapListener mListener;

    public TapDetector(Context context, View view){

        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e){
                mListener.sendTapDetected(Action.TAP);
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e){
                mListener.sendTapDetected(Action.DOUBLE_TAP);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e){
                mListener.sendTapDetected(Action.HOLD_TAP);
            }
        };
        gestureDetector = new GestureDetector(context, listener);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public interface OnTapListener {
        void sendTapDetected(Action action);
    }

    public void setOnTapListener(TapDetector.OnTapListener listener) {
        this.mListener = listener;
    }

    public enum Action {
        TAP,
        DOUBLE_TAP,
        HOLD_TAP,
        DONT_TAP
    }
}
