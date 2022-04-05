package ca.unb.mobiledev.reflexrevolution.activities.tests;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.detectors.RotationDetector;

public class RotateTestActivity extends AppCompatActivity {

    private TextView mainLabel;
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private RotationDetector rotationDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mainLabel = findViewById(R.id.mainLabel);

        // Get sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Initialize rotation detector
        rotationDetector = new RotationDetector();
        rotationDetector.setOnRotateListener(new RotationDetector.OnRotateListener() {
            @Override
            public void onRotate(RotationDetector.Action instr) {
                mainLabel.setText(instr.toString());
            }
            @Override
            public void onMove() {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(rotationDetector, gyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(rotationDetector);
    }
}