package ca.unb.mobiledev.reflexrevolution.instructions;


import android.widget.LinearLayout;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class SwipeInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.SwipeAction currentAction;
    private Random rand;

    public SwipeInstruction(LinearLayout layout, Callback callback, TouchDetector touchDetector) {
        super(layout, callback);
        this.touchDetector = touchDetector;
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
        currentAction = actions[rand.nextInt(actions.length)];
    }

    @Override
    public void display() {
        switch(currentAction) {
            case SWIPE_RIGHT: addTextView("RIGHT", LabelType.SECONDARY); break;
            case SWIPE_LEFT: addTextView("LEFT", LabelType.SECONDARY); break;
            case SWIPE_UP: addTextView("UP", LabelType.SECONDARY); break;
            case SWIPE_DOWN: addTextView("DOWN", LabelType.SECONDARY); break;
        }
        addTextView("SWIPE", LabelType.PRIMARY);
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
