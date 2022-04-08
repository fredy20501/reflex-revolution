package ca.unb.mobiledev.reflexrevolution.instructions;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.LinearLayout;

import ca.unb.mobiledev.reflexrevolution.detectors.JumpDetector;

public class JumpInstruction extends Instruction {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gravitySensor;
    private JumpDetector jumpDetector;

    public JumpInstruction(LinearLayout layout, Callback callback) {
        super(layout, callback);
        setup();
    }

    private void setup() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        jumpDetector = new JumpDetector();
        jumpDetector.setOnJumpListener(this::success);
        voiceCommands = getVoiceCommands("jump");
    }

    @Override
    public void display() {
        addTextView("JUMP", LabelType.PRIMARY);
    }

    @Override
    public int getMinDuration() {
        return 750;
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
        sensorManager.registerListener(jumpDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(jumpDetector, gravitySensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void unsetListeners() {
        sensorManager.unregisterListener(jumpDetector);
    }
}
