package ca.unb.mobiledev.reflexrevolution.instructions;


import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TapDetector;

public class TapInstruction extends Instruction {

    private TapDetector.Action currentAction;
    private Random rand;
    private final boolean isDemo;
    private int index;

    public TapInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        this.isDemo = false;
        index = 0;
    }

    // Extra constructor to enable demo mode
    public TapInstruction(ViewGroup layout, Callback callback, boolean isDemo) {
        super(layout, callback);
        this.isDemo = isDemo;
        index = 0;
    }

    @Override
    protected void setup() {
        rand = new Random();
        TapDetector tapDetector = new TapDetector(context, layout);
        tapDetector.setOnTapListener(action -> {
            if (currentAction == TapDetector.Action.DONT_TAP) fail();
            else if (currentAction == action) success();
        });
    }

    @Override
    public void init() {
        super.init();
        TapDetector.Action[] actions = TapDetector.Action.values();
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
        if (currentAction == TapDetector.Action.DONT_TAP) success();
        else fail();
    }
}
