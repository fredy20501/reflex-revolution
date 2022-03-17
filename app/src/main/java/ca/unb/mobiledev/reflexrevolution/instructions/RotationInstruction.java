package ca.unb.mobiledev.reflexrevolution.instructions;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.detectors.RotationDetector;

public class RotationInstruction extends Instruction {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private RotationDetector rotationDetector;
    private RotationDetector.Action currentAction;
    private Random rand;

    // We need 3 voice command arrays instead of just voiceCommands since there are 3 possible actions
    private int[] tiltVoiceCommands;
    private int[] turnVoiceCommands;
    private int[] twistVoiceCommands;

    public RotationInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        setup();
    }

    private void setup() {
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

    // Overridden version to handle multiple rotation types
    @Override
    public int getVoiceCommand(){
        if(currentAction == RotationDetector.Action.TILT_BACKWARD ||
                currentAction == RotationDetector.Action.TILT_FORWARD){
            voiceCommands = tiltVoiceCommands;
        }
        else if(currentAction == RotationDetector.Action.TURN_LEFT ||
                currentAction == RotationDetector.Action.TURN_RIGHT){
            voiceCommands = turnVoiceCommands;
        }
        else{
            voiceCommands = twistVoiceCommands;
        }
        return super.getVoiceCommand();
    }

    @Override
    protected void setVoiceCommands() {
        tiltVoiceCommands = new int[]{R.raw.tilt_carter};
        turnVoiceCommands = new int[]{R.raw.turn_carter};
        twistVoiceCommands = new int[]{R.raw.twist_carter};
    }
}
