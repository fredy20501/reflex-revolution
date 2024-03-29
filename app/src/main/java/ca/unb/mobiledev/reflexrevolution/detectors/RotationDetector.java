package ca.unb.mobiledev.reflexrevolution.detectors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class RotationDetector implements SensorEventListener {

    private static final float ROTATION_THRESHOLD = 3.5f;
    private OnRotateListener mListener;
    private long lastEventTime = 0;

    public void setOnRotateListener(OnRotateListener listener) {
        this.mListener = listener;
    }

    public interface OnRotateListener {
        void onRotate(Action instr);
        void onMove();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Can do something if accuracy of the sensor changes
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (mListener != null && sensorType == Sensor.TYPE_GYROSCOPE) {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Ignore events that are too far apart (prevent false positive)
            if ((event.timestamp - lastEventTime)/1000000 > 500) {
                lastEventTime = event.timestamp;
                return;
            }
            lastEventTime = event.timestamp;

            // Any movement
            if (Math.abs(axisX) > ROTATION_THRESHOLD ||
                    Math.abs(axisY) > ROTATION_THRESHOLD ||
                    Math.abs(axisZ) > ROTATION_THRESHOLD) {
                mListener.onMove();
            }

            // Rotation along the x axis
            if (axisX > ROTATION_THRESHOLD) mListener.onRotate(Action.TILT_FORWARD);
            if (axisX < -ROTATION_THRESHOLD) mListener.onRotate(Action.TILT_BACKWARD);

            // Rotation along the y axis
            if (axisY > ROTATION_THRESHOLD) mListener.onRotate(Action.TWIST_RIGHT);
            if (axisY < -ROTATION_THRESHOLD) mListener.onRotate(Action.TWIST_LEFT);

            // Rotation along the z axis
            if (axisZ > ROTATION_THRESHOLD) mListener.onRotate(Action.TURN_LEFT);
            if (axisZ < -ROTATION_THRESHOLD) mListener.onRotate(Action.TURN_RIGHT);
        }
    }

    public enum Type {TILT, TURN, TWIST}
    public enum Action {
        TILT_FORWARD(Type.TILT),
        TILT_BACKWARD(Type.TILT),
        TURN_LEFT(Type.TURN),
        TURN_RIGHT(Type.TURN),
        TWIST_LEFT(Type.TWIST),
        TWIST_RIGHT(Type.TWIST);

        private final Type type;
        Action(Type type) {
            this.type = type;
        }
        public Type getType() {
            return this.type;
        }
    }
}