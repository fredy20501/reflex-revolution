package ca.unb.mobiledev.reflexrevolution.detectors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class RotationDetector implements SensorEventListener {

    private static final float ROTATION_THRESHOLD = 3.0f;
    private static final float MOVE_THRESHOLD = 1.5f;
    private OnRotateListener mListener;

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

            // Any movement
            if (Math.abs(axisX) > MOVE_THRESHOLD ||
                    Math.abs(axisY) > MOVE_THRESHOLD ||
                    Math.abs(axisZ) > MOVE_THRESHOLD) {
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
        TURN_RIGHT(Type.TURN),
        TURN_LEFT(Type.TURN),
        TWIST_RIGHT(Type.TWIST),
        TWIST_LEFT(Type.TWIST);

        private final Type type;
        Action(Type type) {
            this.type = type;
        }
        public Type getType() {
            return this.type;
        }
    }
}