package ca.unb.mobiledev.reflexrevolution.sensors;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import ca.unb.mobiledev.reflexrevolution.utils.Instruction;

public class TapDetector implements View.OnTouchListener{
    GestureDetector gestureDetector;

    private OnTapListener mListener;

    public TapDetector(Context context, View view){

        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e){
                mListener.sendTapDetected(Instruction.TAP);
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e){
                mListener.sendTapDetected(Instruction.DOUBLETAP);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e){
                mListener.sendTapDetected(Instruction.HOLD);
            }
        };
        gestureDetector = new GestureDetector(context, listener);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public interface OnTapListener {
        void sendTapDetected(Instruction instruction);
    }

    public void setOnTapListener(TapDetector.OnTapListener listener) {
        this.mListener = listener;
    }
}
