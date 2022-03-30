package ca.unb.mobiledev.reflexrevolution.instructions;


import android.widget.LinearLayout;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class TapInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.TapAction currentAction;

    private int index;

    public TapInstruction(LinearLayout layout, Callback callback, TouchDetector touchDetector) {
        super(layout, callback);
        this.touchDetector = touchDetector;
        this.index = 0;
        setup();
    }

    private void setup() {
        touchDetector.addListener(new TouchDetector.ActionListener() {
            @Override
            public void onSwipe(TouchDetector.SwipeAction action) {}
            @Override
            public void onTap(TouchDetector.TapAction action) {
                if (currentAction == TouchDetector.TapAction.DONT_TAP) fail();
                else if (currentAction == action) success();
            }
        });
        voiceCommands = getVoiceCommands("tap");
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        TouchDetector.TapAction[] actions = TouchDetector.TapAction.values();
        currentAction = actions[rand.nextInt(actions.length)];
    }

    // Second init which will only be called in tutorial mode
    @Override
    public void init(boolean success){
        super.init(success);
        // Initialize as a random action
        TouchDetector.TapAction[] actions = TouchDetector.TapAction.values();

        // Override previously set instruction with the next action in order
        // If there was no success, show same action as last time
        if(success) index = (index+1) % actions.length;
        currentAction = actions[index];
    }

    @Override
    public void display() {
        switch(currentAction) {
            case DOUBLE_TAP: addTextView("DOUBLE", LabelType.SECONDARY); break;
            case HOLD_TAP: addTextView("HOLD", LabelType.SECONDARY); break;
            case DONT_TAP: addTextView("DONT", LabelType.SECONDARY); break;
        }
        addTextView("TAP", LabelType.PRIMARY);
    }

    @Override
    public void enable() {}

    @Override
    public void disable() {}

    @Override
    public void timerFinished() {
        if (currentAction == TouchDetector.TapAction.DONT_TAP) success();
        else fail();
    }
}
