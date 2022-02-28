package ca.unb.mobiledev.reflexrevolution.instructions;


import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class SwipeInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.SwipeAction currentAction;
    private Random rand;
    private final boolean isDemo;
    private int index;

    public SwipeInstruction(ViewGroup layout, Callback callback, TouchDetector touchDetector) {
        super(layout, callback);
        this.touchDetector = touchDetector;
        this.isDemo = false;
        index = 0;
        setup();
    }

    // Extra constructor to enable demo mode
    public SwipeInstruction(ViewGroup layout, Callback callback, TouchDetector touchDetector, boolean isDemo) {
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
            public void onSwipe(TouchDetector.SwipeAction action) {
                if (currentAction == action) success();
                else fail(); // Wrong direction
            }
            @Override
            public void onTap(TouchDetector.TapAction action) {}
        });
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        TouchDetector.SwipeAction[] actions = TouchDetector.SwipeAction.values();
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
            case SWIPE_RIGHT: addTextView("RIGHT", SMALL_TEXT_SIZE); break;
            case SWIPE_LEFT: addTextView("LEFT", SMALL_TEXT_SIZE); break;
            case SWIPE_UP: addTextView("UP", SMALL_TEXT_SIZE); break;
            case SWIPE_DOWN: addTextView("DOWN", SMALL_TEXT_SIZE); break;
        }
        addTextView("SWIPE", DEFAULT_TEXT_SIZE);
    }

    @Override
    public void enable() {}

    @Override
    public void disable() {}

    @Override
    public void timerFinished() {
        fail();
    }
}
