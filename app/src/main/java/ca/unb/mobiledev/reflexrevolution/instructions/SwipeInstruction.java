package ca.unb.mobiledev.reflexrevolution.instructions;


import android.view.ViewGroup;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class SwipeInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.SwipeAction currentAction;
    private Random rand;
    private Random rand2;
    private int index;
    private String[] rightOptions = {"RIGHT", "OPPOSITE OF LEFT"};
    private String[] leftOptions = {"LEFT", "OPPOSITE OF RIGHT"};
    private String[] upOptions = {"UP", "OPPOSITE OF DOWN"};
    private String[] downOptions = {"DOWN", "OPPOSITE OF UP"};
    private String displayTxt;

    public SwipeInstruction(ViewGroup layout, Callback callback, TouchDetector touchDetector) {
        super(layout, callback);
        this.touchDetector = touchDetector;
        setup();
    }

    private void setup() {
        rand = new Random();
        touchDetector.addListener(new TouchDetector.ActionListener() {
            @Override
            public void onSwipe(TouchDetector.SwipeAction action) {
                if (currentAction.getType() == action.getType()) {
                    // Same type and same instruction
                    if (currentAction == action) success();
                        // Same type but different instruction (wrong direction)
                    else fail();
                }
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
        rand2 = new Random();
        index = rand2.nextInt(rightOptions.length - 0) + 0; //used rightOptions; they are all the same length.

        switch(currentAction) {
            case SWIPE_RIGHT:
                displayTxt = rightOptions[index]; //either "RIGHT" or "OPPOSITE OF LEFT"
                addTextView(displayTxt, SMALL_TEXT_SIZE);
                break;
            case SWIPE_LEFT:
                displayTxt = leftOptions[index];
                addTextView(displayTxt, SMALL_TEXT_SIZE);
                break;
            case SWIPE_UP:
                displayTxt = upOptions[index];
                addTextView (displayTxt, SMALL_TEXT_SIZE);
                break;
            case SWIPE_DOWN:
                displayTxt = downOptions[index];
                addTextView(displayTxt, SMALL_TEXT_SIZE);
                break;
        }
        if(currentAction == TouchDetector.SwipeAction.FLICK)
            addTextView("FLICK", DEFAULT_TEXT_SIZE);
        else
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
