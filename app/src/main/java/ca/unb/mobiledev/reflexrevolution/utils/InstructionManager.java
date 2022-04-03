package ca.unb.mobiledev.reflexrevolution.utils;


import android.widget.LinearLayout;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;
import ca.unb.mobiledev.reflexrevolution.instructions.DialInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.FreezeInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.Instruction;
import ca.unb.mobiledev.reflexrevolution.instructions.JumpInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.RotationInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.ShakeInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.SwipeInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.TapInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.TypeInstruction;

public class InstructionManager {

    private final LinearLayout layout;
    private final Instruction.Callback callback;
    private final TouchDetector touchDetector;

    private final ArrayList<AbstractMap.Entry<Instruction, Float>> instructions;
    private final Random rand;
    private float totalProbability;

    public InstructionManager(LinearLayout layout, Instruction.Callback callback){
        this.layout = layout;
        this.callback = callback;
        this.touchDetector = new TouchDetector(layout);
        rand = new Random();
        instructions = new ArrayList<>();
        totalProbability = 0;
    }

    public void generateInstructions(GameMode gameMode) {
        instructions.clear();
        totalProbability = 0;

        // Construct list of instructions based on game mode
        switch(gameMode){
            case CLASSIC:
                addEntry(new TapInstruction(layout, callback, touchDetector), 4);
                addEntry(new SwipeInstruction(layout, callback, touchDetector), 6);
                addEntry(new ShakeInstruction(layout, callback), 2);
                break;
            case TACTILE:
                addEntry(new TapInstruction(layout, callback, touchDetector), 4);
                addEntry(new SwipeInstruction(layout, callback, touchDetector), 6);
                break;
            case SWIPE_PRACTICE:
            case SWIPE:
                addEntry(new SwipeInstruction(layout, callback, touchDetector), 6);
                break;
            case KINETIC:
                addEntry(new ShakeInstruction(layout, callback), 2);
                addEntry(new JumpInstruction(layout, callback), 2);
                addEntry(new FreezeInstruction(layout, callback), 2);
                addEntry(new RotationInstruction(layout, callback), 6);
                break;
            case KEYBOARD:
                addEntry(new TypeInstruction(layout, callback), 1);
                addEntry(new DialInstruction(layout, callback), 1);
                break;
            case REVOLUTION:
                addEntry(new TapInstruction(layout, callback, touchDetector), 4);
                addEntry(new SwipeInstruction(layout, callback, touchDetector), 6);
                addEntry(new ShakeInstruction(layout, callback), 2);
                addEntry(new JumpInstruction(layout, callback), 2);
                addEntry(new FreezeInstruction(layout, callback), 2);
                addEntry(new RotationInstruction(layout, callback), 6);
                addEntry(new TypeInstruction(layout, callback), 2);
                addEntry(new DialInstruction(layout, callback), 2);
                break;
            case TAP_PRACTICE:
                addEntry(new TapInstruction(layout, callback, touchDetector), 4);
                break;
            case SHAKE_PRACTICE:
                addEntry(new ShakeInstruction(layout, callback), 2);
                break;
            case JUMP_PRACTICE:
                addEntry(new JumpInstruction(layout, callback), 2);
                break;
            case ROTATION_PRACTICE:
                addEntry(new RotationInstruction(layout, callback), 6);
                break;
            case FREEZE_PRACTICE:
                addEntry(new FreezeInstruction(layout, callback), 2);
                break;
            case TYPE_PRACTICE:
                addEntry(new TypeInstruction(layout, callback), 2);
                break;
            case DIAL_PRACTICE:
                addEntry(new DialInstruction(layout, callback), 2);
                break;
        }
    }

    // Return an instruction from the list according to their probabilities
    public Instruction getInstruction() {
        if (instructions.size() == 0) return null;

        // Check the probabilities of each instruction
        float p = rand.nextFloat();
        for (int i=0; i<instructions.size(); i++) {
            AbstractMap.Entry<Instruction, Float> entry = instructions.get(i);
            float probability = entry.getValue() / totalProbability;
            if (p < probability) return entry.getKey();
            p -= probability;
        }

        // If no probability matched, return random one
        // This should only happen if p is exactly 1.0
        int randomIndex = rand.nextInt(instructions.size());
        return instructions.get(randomIndex).getKey();
    }

    // Add a new entry to the list of instructions
    private void addEntry(Instruction instr, float relativeProbability) {
        AbstractMap.SimpleImmutableEntry<Instruction, Float> newEntry = 
                new AbstractMap.SimpleImmutableEntry<>(instr, relativeProbability);
        totalProbability += relativeProbability;
        instructions.add(newEntry);
    }
}


