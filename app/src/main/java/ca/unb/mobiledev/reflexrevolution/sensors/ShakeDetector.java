package ca.unb.mobiledev.reflexrevolution.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

// This class was taken from http://jasonmcreynolds.com/?p=388
// Modified by: Frederic Verret

public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 3.0f;
    private static final float MOVE_THRESHOLD = 1.5f;
    private static final int SHAKE_SLOP_TIME_MS = 250;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        void onShake(int count);
        void onMove();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Can do something if accuracy of the sensor changes
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (mListener != null && sensorType == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // gForce will be close to 1 when there is no movement.
            double gForce = Math.sqrt(x * x + y * y + z * z);

            if (gForce > MOVE_THRESHOLD) mListener.onMove();
            if (gForce > SHAKE_THRESHOLD) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                mListener.onShake(mShakeCount);
            }
        }
    }
}