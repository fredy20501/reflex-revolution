package ca.unb.mobiledev.reflexrevolution.instructions;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.LinearLayout;

import ca.unb.mobiledev.reflexrevolution.detectors.RotationDetector;

public class RotationInstruction extends Instruction {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private RotationDetector rotationDetector;
    private RotationDetector.Action currentAction;
    private RotationDetector.Action[] actions;

    private Integer[] tiltVoiceCommands;
    private Integer[] turnVoiceCommands;
    private Integer[] twistVoiceCommands;

    private int index;

    public RotationInstruction(LinearLayout layout, Callback callback) {
        super(layout, callback);
        this.index = 0;
        setup();
    }

    private void setup() {
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

        tiltVoiceCommands = getVoiceCommands("tilt");
        turnVoiceCommands = getVoiceCommands("turn");
        twistVoiceCommands = getVoiceCommands("twist");
        actions = RotationDetector.Action.values();
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        currentAction = actions[rand.nextInt(actions.length)];
        setVoiceCommands();
    }

    @Override
    public void init(boolean success){
        super.init(success);
        // Initialize as next instruction in sequence (if success)
        // Else keep the same instruction
        if(success) index = (index+1) % actions.length;
        currentAction = actions[index];
        setVoiceCommands();
    }

    // Set the proper voice commands based on current instruction
    private void setVoiceCommands() {
        switch(currentAction.getType()) {
            case TURN: voiceCommands = turnVoiceCommands; break;
            case TWIST: voiceCommands = twistVoiceCommands; break;
            case TILT: voiceCommands = tiltVoiceCommands; break;
        }
    }

    @Override
    public void display() {
        switch(currentAction) {
            case TILT_FORWARD: addTextView("FORWARD", LabelType.SECONDARY); break;
            case TILT_BACKWARD: addTextView("BACKWARD", LabelType.SECONDARY); break;
            case TURN_RIGHT:
            case TWIST_RIGHT: addTextView("RIGHT", LabelType.SECONDARY); break;
            case TURN_LEFT:
            case TWIST_LEFT: addTextView("LEFT", LabelType.SECONDARY); break;
        }
        switch(currentAction.getType()) {
            case TURN: addTextView("TURN", LabelType.PRIMARY); break;
            case TWIST: addTextView("TWIST", LabelType.PRIMARY); break;
            case TILT: addTextView("TILT", LabelType.PRIMARY); break;
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
