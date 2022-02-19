package ca.unb.mobiledev.reflexrevolution.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;

public class InstructionUtil {

    public static ArrayList<Instruction> createInstructions(GameMode mode, SensorManager sensorManager){
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        ArrayList<Instruction> instructions = new ArrayList<>();

        //Construct list of instructions based on gamemode
        switch(mode){
            case BASIC:
                instructions.add(Instruction.BUTTON);
                if (accelerometer != null) {
                    instructions.add(Instruction.SHAKE);
                    if (gravitySensor != null) {
                        instructions.add(Instruction.JUMP);
                    }
                }
                if (gyroscope != null) {
                    instructions.add(Instruction.TURN_LEFT);
                    instructions.add(Instruction.TURN_RIGHT);
                    instructions.add(Instruction.DONT_TURN);
                }
                if (accelerometer != null || gyroscope != null) {
                    instructions.add(Instruction.FREEZE);
                }
                break;

            default:
                break;
        }
        return instructions;
    }
}


