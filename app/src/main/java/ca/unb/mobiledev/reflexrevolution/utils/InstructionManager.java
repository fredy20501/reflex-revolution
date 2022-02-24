package ca.unb.mobiledev.reflexrevolution.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.ViewGroup;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.instructions.Instruction;
import ca.unb.mobiledev.reflexrevolution.instructions.JumpInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.ShakeInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.TapInstruction;

public class InstructionManager {

    private final ViewGroup layout;
    private final Instruction.Callback callback;

    private final ArrayList<AbstractMap.Entry<Instruction, Float>> instructions;
    private final Random rand;
    private float totalProbability;
    private int index;
    private int instructionCount;

    public InstructionManager(ViewGroup layout, Instruction.Callback callback){
        this.layout = layout;
        this.callback = callback;
        rand = new Random();
        instructions = new ArrayList<>();
        totalProbability = 0;
        index = 0;
        instructionCount = 0;
    }

    public void generateInstructions(GameMode gameMode) {
        SensorManager sensorManager = 
                (SensorManager) layout.getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        
        instructions.clear();
        totalProbability = 0;

        // Construct list of instructions based on game mode
        switch(gameMode){
            case BASIC:
                addEntry(new TapInstruction(layout, callback), 4);
                if (accelerometer != null) {
                    addEntry(new ShakeInstruction(layout, callback), 1);
                    if (gravitySensor != null) {
                        addEntry(new JumpInstruction(layout, callback), 1);
                    }
                }
                break;
            case DEMO:
                addEntry(new TapInstruction(layout, callback, true), 4);
                addEntry(new ShakeInstruction(layout, callback), 1);
                addEntry(new JumpInstruction(layout, callback), 1);
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

    // Return the next instruction from the list (in order)
    public Instruction getNextInstruction() {
        AbstractMap.Entry<Instruction, Float> entry = instructions.get(index);
        instructionCount++;
        if (instructionCount >= entry.getValue()) {
            index = (index + 1) % instructions.size();
            instructionCount = 0;
        }
        return entry.getKey();
    }

    // Add a new entry to the list of instructions
    private void addEntry(Instruction instr, float relativeProbability) {
        AbstractMap.SimpleImmutableEntry<Instruction, Float> newEntry = 
                new AbstractMap.SimpleImmutableEntry<>(instr, relativeProbability);
        totalProbability += relativeProbability;
        instructions.add(newEntry);
    }
}


