package ca.unb.mobiledev.reflexrevolution.instructions;


import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TapDetector;

public class TapInstruction extends Instruction {

    private TapDetector.Action currentAction;
    private Random rand;

    public TapInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
    }

    @Override
    protected void setup() {
        rand = new Random();
        TapDetector tapDetector = new TapDetector(context, layout);
        tapDetector.setOnTapListener(action -> {
            if (!done) {
                if (currentAction == TapDetector.Action.DONT_TAP) {
                    done = true;
                    callback.onFailure();
                }
                else if (currentAction == action) {
                    done = true;
                    callback.onSuccess();
                }
            }
        });
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        TapDetector.Action[] actions = TapDetector.Action.values();
        currentAction = actions[rand.nextInt(actions.length)];
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
        if (!done) {
            done = true;
            if (currentAction == TapDetector.Action.DONT_TAP) callback.onSuccess();
            else callback.onFailure();
        }
    }
}
