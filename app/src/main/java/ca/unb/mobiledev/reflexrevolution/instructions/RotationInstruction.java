package ca.unb.mobiledev.reflexrevolution.instructions;


import static android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.RotationDetector;

public class RotationInstruction extends Instruction {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private RotationDetector rotationDetector;
    private RotationDetector.Action currentAction;
    private Random rand;

    public RotationInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
    }

    @Override
    protected void setup() {
        rand = new Random();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        rotationDetector = new RotationDetector();
        rotationDetector.setOnRotateListener(new RotationDetector.OnRotateListener() {
            @Override
            public void onRotate(RotationDetector.Action instr) {
                if (currentAction.getType() == instr.getType()) {
                    // Same type and same instruction
                    if (currentAction == instr) success();
                    // Same type but different instruction (wrong direction)
                    else fail();
                }
            }
            @Override
            public void onMove() {}
        });
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        RotationDetector.Action[] actions = RotationDetector.Action.values();
        currentAction = actions[rand.nextInt(actions.length)];
    }

    @Override
    public void display() {
        switch(currentAction) {
            case TILT_FORWARD: addTextView("FORWARD", SMALL_TEXT_SIZE); break;
            case TILT_BACKWARD: addTextView("BACKWARD", SMALL_TEXT_SIZE); break;
            case TURN_RIGHT:
            case TWIST_RIGHT: addTextView("RIGHT", SMALL_TEXT_SIZE); break;
            case TURN_LEFT:
            case TWIST_LEFT: addTextView("LEFT", SMALL_TEXT_SIZE); break;
        }
        switch(currentAction.getType()) {
            case TURN: addTextView("TURN", DEFAULT_TEXT_SIZE); break;
            case TWIST: addTextView("TWIST", DEFAULT_TEXT_SIZE); break;
            case TILT: addTextView("TILT", DEFAULT_TEXT_SIZE); break;
        }
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
        sensorManager.registerListener(rotationDetector, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unsetListeners() {
        sensorManager.unregisterListener(rotationDetector);
    }
}
