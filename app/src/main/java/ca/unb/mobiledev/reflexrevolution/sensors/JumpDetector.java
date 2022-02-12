package ca.unb.mobiledev.reflexrevolution.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class JumpDetector implements SensorEventListener {

    private static final float JUMP_THRESHOLD_FORCE = 5.0f;

    private OnJumpListener mListener;
    private float[] upVector;

    public void setOnJumpListener(OnJumpListener listener) {
        this.mListener = listener;
    }

    public interface OnJumpListener {
        void onJump();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Can do something if accuracy of the sensor changes
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener != null) {
            int sensorType = event.sensor.getType();
            if (sensorType == Sensor.TYPE_GRAVITY) {
                // Get a normalized up vector based on gravity
                float[] gravityVector = event.values;
                vectorNormalize(gravityVector);
                vectorInverse(gravityVector);
                upVector = gravityVector;
            }
            else if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION) {
                if (upVector == null) return;

                // Scalar projection of the acceleration onto gravity vector
                float[] acceleration = event.values;
                float upComponent = vectorDotProduct(acceleration, upVector);

                if (upComponent > JUMP_THRESHOLD_FORCE) {
                    mListener.onJump();
                }
            }
        }
    }

    private double vectorMagnitude(float[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    private void vectorNormalize(float[] v) {
        double magnitude = vectorMagnitude(v);
        v[0] = (float) (v[0] / magnitude);
        v[1] = (float) (v[1] / magnitude);
        v[2] = (float) (v[2] / magnitude);
    }

    private void vectorInverse(float[] v) {
        v[0] = -v[0];
        v[1] = -v[1];
        v[2] = -v[2];
    }

    private float vectorDotProduct(float[] v1, float[] v2) {
        return (v1[0]*v2[0]) + (v1[1]*v2[1]) + (v1[2]*v2[2]);
    }
}