package ca.unb.mobiledev.reflexrevolution.sensors;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeListener implements View.OnTouchListener{
        GestureDetector gestureDetector;
        private OnSwipeListener listener;

        public void setOnSwipeListener(OnSwipeListener listener) {
            this.listener = listener;
        }

        public interface OnSwipeListener {
            //void onSwipe(Instruction direction);
            void onSwipeLeft();
            void onSwipeRight();
            void onSwipeUp();
            void onSwipeDown();
        }

        SwipeListener(Context context, View view){
            int SWIPE_THRESHOLD = 100;
            int SWIPE_VELOCITY_THRESHOLD = 100;

            GestureDetector.SimpleOnGestureListener listener2 = new GestureDetector.SimpleOnGestureListener(){
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
                                listener.onSwipeRight();
                            else
                                listener.onSwipeLeft();
                        }
                    }
                    else if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                        if(diffY > 0)
                            listener.onSwipeDown();
                        else
                            listener.onSwipeUp();
                    }
                    return true;
                }
            };

            gestureDetector = new GestureDetector(context, listener2);
            view.setOnTouchListener(this);
        }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
}
