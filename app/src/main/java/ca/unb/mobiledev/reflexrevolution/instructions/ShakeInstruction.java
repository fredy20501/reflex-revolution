package ca.unb.mobiledev.reflexrevolution.instructions;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.ViewGroup;

import ca.unb.mobiledev.reflexrevolution.detectors.ShakeDetector;

public class ShakeInstruction extends Instruction {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    public ShakeInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
    }

    @Override
    protected void setup() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(count -> {
            if (count > 1) success();
        });
    }

    @Override
    public void display() {
        addTextView("SHAKE", DEFAULT_TEXT_SIZE);
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
        fail();
    }

    private void setListeners() {
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unsetListeners() {
        sensorManager.unregisterListener(shakeDetector);
    }
}
