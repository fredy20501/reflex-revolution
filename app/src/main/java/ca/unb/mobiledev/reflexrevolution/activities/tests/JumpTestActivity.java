package ca.unb.mobiledev.reflexrevolution.activities.tests;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.detectors.JumpDetector;

public class JumpTestActivity extends AppCompatActivity {

    private TextView mainLabel;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gravitySensor;
    private JumpDetector jumpDetector;
    private int count;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mainLabel = findViewById(R.id.mainLabel);
        count = 0;

        // Get sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        // Initialize jump detector
        mainLabel.setText("Jump Detected: 0");
        jumpDetector = new JumpDetector();
        jumpDetector.setOnJumpListener(() -> mainLabel.setText("Jump Detected: "+(++count)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(jumpDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(jumpDetector, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(jumpDetector);
    }
}