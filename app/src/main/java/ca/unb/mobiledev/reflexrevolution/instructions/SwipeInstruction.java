package ca.unb.mobiledev.reflexrevolution.instructions;


import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.SwipeDetector;

public class SwipeInstruction extends Instruction {

    private SwipeDetector.Action currentAction;
    private Random rand;

    public SwipeInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
    }

    @Override
    protected void setup() {
        rand = new Random();
        SwipeDetector swipeDetector = new SwipeDetector(context, layout);
        swipeDetector.setOnSwipeListener(action -> {
            if (currentAction == action) success();
            else fail(); // Wrong direction
        });
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        SwipeDetector.Action[] actions = SwipeDetector.Action.values();
        currentAction = actions[rand.nextInt(actions.length)];
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
