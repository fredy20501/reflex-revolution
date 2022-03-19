package ca.unb.mobiledev.reflexrevolution.instructions;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.ViewGroup;

import ca.unb.mobiledev.reflexrevolution.detectors.RotationDetector;
import ca.unb.mobiledev.reflexrevolution.detectors.ShakeDetector;

public class FreezeInstruction extends Instruction {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private ShakeDetector shakeDetector;
    private RotationDetector rotationDetector;

    public FreezeInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        setup();
    }

    private void setup() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {}
            @Override
            public void onMove() {
                fail();
            }
        });
        rotationDetector = new RotationDetector();
        rotationDetector.setOnRotateListener(new RotationDetector.OnRotateListener() {
            @Override
            public void onRotate(RotationDetector.Action instr) {}
            @Override
            public void onMove() {
                fail();
            }
        });
        voiceCommands = getVoiceCommands("freeze");
    }

    @Override
    public void display() {
        addTextView("FREEZE", DEFAULT_TEXT_SIZE);
    }

    @Override
    public void enable() {
        setListeners();
    }

    @Override
    public void disable() {
        unsetListeners();
    }

    @Override
    public void timerFinished() {
        success();
    }

    private void setListeners() {
        sensorManager.registerListener(rotationDetector, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unsetListeners() {
        sensorManager.unregisterListener(rotationDetector);
        sensorManager.unregisterListener(shakeDetector);
    }
}
