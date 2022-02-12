package ca.unb.mobiledev.reflexrevolution.activities.tests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.sensors.ShakeDetector;

public class ShakeTestActivity extends AppCompatActivity {

    private TextView shakeText;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_test);

        shakeText = findViewById(R.id.shakeText);

        // Get accelerometer sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (accelerometer == null){
            shakeText.setText("No accelerometer found. :(");
            return;
        }

        // Initialize shake detector
        shakeText.setText("Shake Detected: 0");
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(count -> shakeText.setText("Shake Detected: "+count));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }
}