package ca.unb.mobiledev.reflexrevolution.instructions;


import android.widget.LinearLayout;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class TapInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.TapAction currentAction;
    private TouchDetector.TapAction[] actions;

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
        actions = TouchDetector.TapAction.values();
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        currentAction = actions[rand.nextInt(actions.length)];
    }

    // Second init which will only be called in practice mode
    @Override
    public void init(boolean success){
        super.init(success);
        // Initialize as next instruction in sequence (if success)
        // Else keep the same instruction
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
    public int getMinDuration() {
        int duration = 1000;
        switch (currentAction) {
            case TAP:
            case DONT_TAP:
            case DOUBLE_TAP:
                duration = 750; break;
            case HOLD_TAP:
                duration = 1250; break;
        }
        return duration;
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
