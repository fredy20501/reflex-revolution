package ca.unb.mobiledev.reflexrevolution.detectors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import ca.unb.mobiledev.reflexrevolution.utils.Instruction;

public class RotationDetector implements SensorEventListener {

    private static final float ROTATION_THRESHOLD = 3.0f;
    private static final float MOVE_THRESHOLD = 1.5f;
    private OnRotateListener mListener;

    public void setOnJumpListener(OnRotateListener listener) {
        this.mListener = listener;
    }

    public interface OnRotateListener {
        void onRotate(Instruction instr);
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
            if (axisX > ROTATION_THRESHOLD) mListener.onRotate(Instruction.TILT_FORWARD);
            if (axisX < -ROTATION_THRESHOLD) mListener.onRotate(Instruction.TILT_BACKWARD);

            // Rotation along the y axis
            if (axisY > ROTATION_THRESHOLD) mListener.onRotate(Instruction.TILT_RIGHT);
            if (axisY < -ROTATION_THRESHOLD) mListener.onRotate(Instruction.TILT_LEFT);

            // Rotation along the z axis
            if (axisZ > ROTATION_THRESHOLD) mListener.onRotate(Instruction.TURN_LEFT);
            if (axisZ < -ROTATION_THRESHOLD) mListener.onRotate(Instruction.TURN_RIGHT);
        }
    }
}