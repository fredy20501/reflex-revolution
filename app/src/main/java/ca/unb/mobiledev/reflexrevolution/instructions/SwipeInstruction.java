package ca.unb.mobiledev.reflexrevolution.instructions;


import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class SwipeInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.SwipeAction currentAction;
    private Random rand;
    private final Map<TouchDetector.SwipeAction, String[]> textLabels = new HashMap<>();
    private Integer[] swipeCommands;
    private Integer[] flickCommands;

    public SwipeInstruction(ViewGroup layout, Callback callback, TouchDetector touchDetector) {
        super(layout, callback);
        this.touchDetector = touchDetector;
        setup();
    }

    private void setup() {
        rand = new Random();
        textLabels.put(TouchDetector.SwipeAction.SWIPE_RIGHT, new String[] {"RIGHT", "OPPOSITE OF LEFT"});
        textLabels.put(TouchDetector.SwipeAction.SWIPE_LEFT, new String[] {"LEFT", "OPPOSITE OF RIGHT"});
        textLabels.put(TouchDetector.SwipeAction.SWIPE_UP, new String[] {"UP", "OPPOSITE OF DOWN"});
        textLabels.put(TouchDetector.SwipeAction.SWIPE_DOWN, new String[] {"DOWN", "OPPOSITE OF UP"});

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
        swipeCommands = getVoiceCommands("swipe");
        flickCommands = getVoiceCommands("flick");
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        TouchDetector.SwipeAction[] actions = TouchDetector.SwipeAction.values();
        currentAction = actions[rand.nextInt(actions.length)];
        switch(currentAction.getType()) {
            case SWIPE: voiceCommands = swipeCommands; break;
            case FLICK: voiceCommands = flickCommands; break;
        }
    }
    @Override
    public void display() {
        if (currentAction.getType() == TouchDetector.Type.SWIPE) {
            // Add small label for swipe instructions
            int index = rand.nextInt(2);
            String displayTxt = Objects.requireNonNull(textLabels.get(currentAction))[index];
            addTextView(displayTxt, SMALL_TEXT_SIZE);
            // Main label
            addTextView("SWIPE", DEFAULT_TEXT_SIZE);
        }
        else {
            // Main label
            addTextView("FLICK", DEFAULT_TEXT_SIZE);
        }
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
