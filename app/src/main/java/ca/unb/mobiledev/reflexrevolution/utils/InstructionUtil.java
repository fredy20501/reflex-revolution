package ca.unb.mobiledev.reflexrevolution.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;

public class InstructionUtil {

    public static ArrayList<Instruction> createInstructions(GameMode mode, Context context){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        ArrayList<Instruction> instructions = new ArrayList<>();
        //Construct list of instructions based on gamemode
        switch(mode){
            case BASIC:
                instructions.add(Instruction.BUTTON);
                Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                if (accelerometer != null) instructions.add(Instruction.SHAKE);
                break;

            default:
                break;
        }
        return instructions;
    }
}


