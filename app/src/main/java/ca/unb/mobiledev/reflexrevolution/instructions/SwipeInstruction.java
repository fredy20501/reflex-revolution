package ca.unb.mobiledev.reflexrevolution.instructions;


import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ca.unb.mobiledev.reflexrevolution.detectors.RotationDetector;
import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class SwipeInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.SwipeAction currentAction;
    private final Map<TouchDetector.SwipeAction, String[]> textLabels = new HashMap<>();
    private Integer[] swipeVoiceCommands;
    private Integer[] flickVoiceCommands;

    private int index;

    public SwipeInstruction(LinearLayout layout, Callback callback, TouchDetector touchDetector) {
        super(layout, callback);
        this.touchDetector = touchDetector;
        this.index = 0;
        setup();
    }

    private void setup() {
        textLabels.put(TouchDetector.SwipeAction.SWIPE_RIGHT, new String[] {"RIGHT", "OPPOSITE OF LEFT"});
        textLabels.put(TouchDetector.SwipeAction.SWIPE_LEFT, new String[] {"LEFT", "OPPOSITE OF RIGHT"});
        textLabels.put(TouchDetector.SwipeAction.SWIPE_UP, new String[] {"UP", "OPPOSITE OF DOWN"});
        textLabels.put(TouchDetector.SwipeAction.SWIPE_DOWN, new String[] {"DOWN", "OPPOSITE OF UP"});

        touchDetector.addListener(new TouchDetector.ActionListener() {
            @Override
            public void onSwipe(TouchDetector.SwipeAction action) {
                if (currentAction != null && currentAction.getType() == action.getType()) {
                    // Same type and same instruction
                    if (currentAction == action) success();
                        // Same type but different instruction (wrong direction)
                    else fail();
                }
            }
            @Override
            public void onTap(TouchDetector.TapAction action) {}
        });
        swipeVoiceCommands = getVoiceCommands("swipe");
        flickVoiceCommands = getVoiceCommands("flick");
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        TouchDetector.SwipeAction[] actions = TouchDetector.SwipeAction.values();
        currentAction = actions[rand.nextInt(actions.length)];

        // Set the proper voice commands
        switch(currentAction.getType()) {
            case SWIPE: voiceCommands = swipeVoiceCommands; break;
            case FLICK: voiceCommands = flickVoiceCommands; break;
        }
    }

    // Second init which will only be called in tutorial mode
    @Override
    public void init(boolean success){
        // No need to set voice commands, as this calls
        // init() first
        super.init(success);
        // Initialize as a random action
        TouchDetector.SwipeAction[] actions = TouchDetector.SwipeAction.values();

        // Override previously set instruction with the next action in order
        // If there was no success, show same action as last time
        if(success) index = (index+1) % actions.length;
        currentAction = actions[index];
    }

    @Override
    public void display() {
        if (currentAction.getType() == TouchDetector.Type.SWIPE) {
            // Add small label for swipe instructions
            int index = rand.nextInt(2);
            String displayTxt = Objects.requireNonNull(textLabels.get(currentAction))[index];
            addTextView(displayTxt, LabelType.SECONDARY);
            // Main label
            addTextView("SWIPE", LabelType.PRIMARY);
        }
        else {
            // Main label
            addTextView("FLICK", LabelType.PRIMARY);
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
