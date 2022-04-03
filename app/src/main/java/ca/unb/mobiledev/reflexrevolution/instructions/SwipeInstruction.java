package ca.unb.mobiledev.reflexrevolution.instructions;


import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class SwipeInstruction extends Instruction {

    private final TouchDetector touchDetector;
    private TouchDetector.SwipeAction currentAction;
    private TouchDetector.SwipeAction[] actions;
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
        actions = TouchDetector.SwipeAction.values();
    }

    @Override
    public void init() {
        super.init();
        // Initialize as a random action
        currentAction = actions[rand.nextInt(actions.length)];
        setVoiceCommands();
    }

    // Second init which will only be called in practice mode
    @Override
    public void init(boolean success){
        super.init(success);
        // Initialize as next instruction in sequence (if success)
        // Else keep the same instruction
        if(success) index = (index+1) % actions.length;
        currentAction = actions[index];
        setVoiceCommands();
    }

    // Set the proper voice commands based on current instruction
    private void setVoiceCommands() {
        switch(currentAction.getType()) {
            case SWIPE: voiceCommands = swipeVoiceCommands; break;
            case FLICK: voiceCommands = flickVoiceCommands; break;
        }
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
