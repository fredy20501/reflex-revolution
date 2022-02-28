package ca.unb.mobiledev.reflexrevolution.instructions;


import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class TapInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.TapAction currentAction;
    private Random rand;
    private final boolean isDemo;
    private int index;

    public TapInstruction(ViewGroup layout, Callback callback, TouchDetector touchDetector) {
        super(layout, callback);
        this.touchDetector = touchDetector;
        this.isDemo = false;
        index = 0;
        setup();
    }

    // Extra constructor to enable demo mode
    public TapInstruction(ViewGroup layout, Callback callback, TouchDetector touchDetector, boolean isDemo) {
        super(layout, callback);
        this.touchDetector = touchDetector;
        this.isDemo = isDemo;
        index = 0;
        setup();
    }

    private void setup() {
        rand = new Random();
        touchDetector.addListener(new TouchDetector.ActionListener() {
            @Override
            public void onSwipe(TouchDetector.SwipeAction action) {}
            @Override
            public void onTap(TouchDetector.TapAction action) {
                if (currentAction == TouchDetector.TapAction.DONT_TAP) fail();
                else if (currentAction == action) success();
            }
        });
    }

    @Override
    public void init() {
        super.init();
        TouchDetector.TapAction[] actions = TouchDetector.TapAction.values();
        if (isDemo) {
            // Initialize as the next action in order
            currentAction = actions[index];
            index = (index + 1) % actions.length;
        }
        else {
            // Initialize as a random action
            currentAction = actions[rand.nextInt(actions.length)];
        }
    }

    @Override
    public void display() {
        switch(currentAction) {
            case DOUBLE_TAP: addTextView("DOUBLE", SMALL_TEXT_SIZE); break;
            case HOLD_TAP: addTextView("HOLD", SMALL_TEXT_SIZE); break;
            case DONT_TAP: addTextView("DONT", SMALL_TEXT_SIZE); break;
        }
        addTextView("TAP", DEFAULT_TEXT_SIZE);
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
