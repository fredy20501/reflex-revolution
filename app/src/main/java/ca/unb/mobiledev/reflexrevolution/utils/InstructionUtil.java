package ca.unb.mobiledev.reflexrevolution.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;

public class InstructionUtil {

    public static ArrayList<Instruction> createInstructions(GameMode mode, Context context){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        ArrayList<Instruction> instructions = new ArrayList<>();

        //Construct list of instructions based on gamemode
        switch(mode){
            case BASIC:
                instructions.add(Instruction.BUTTON);
                instructions.add(Instruction.SWIPE_RIGHT);
                instructions.add(Instruction.SWIPE_LEFT);
                instructions.add(Instruction.SWIPE_UP);
                instructions.add(Instruction.SWIPE_DOWN);
                if (accelerometer != null) instructions.add(Instruction.SHAKE);
                if (accelerometer != null && gravitySensor != null) instructions.add(Instruction.JUMP);
                break;

            default:
                break;
        }
        return instructions;
    }
}


