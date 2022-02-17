package ca.unb.mobiledev.reflexrevolution.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
                instructions.add(Instruction.TAP);
                instructions.add(Instruction.DOUBLETAP);
                instructions.add(Instruction.HOLD);
                instructions.add(Instruction.DONTTAP);
                if (accelerometer != null) instructions.add(Instruction.SHAKE);
                if (accelerometer != null && gravitySensor != null) instructions.add(Instruction.JUMP);
                //instructions.add(Instruction.BUTTON); Legacy instruction previously used for testing
                break;

            default:
                break;
        }
        return instructions;
    }

    //Display UI elements for the instruction passed in
    public static void displayInstruction(Instruction instruction, ViewGroup layout, Context context){
        TextView label;
        switch (instruction){
            case TAP:
                label = new TextView(context);
                label.setText("TAP");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                layout.addView(label);
                break;

            case DOUBLETAP:
                label = new TextView(context);
                label.setText("DOUBLE\nTAP");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                layout.addView(label);
                break;

            case HOLD:
                label = new TextView(context);
                label.setText("HOLD");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                layout.addView(label);
                break;

            case DONTTAP:
                label = new TextView(context);
                label.setText("DON'T\nTAP");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                layout.addView(label);
                break;

            case SWIPE:
                break;

            case SHAKE:
                label = new TextView(context);
                label.setText("SHAKE");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                layout.addView(label);
                break;

            case JUMP:
                label = new TextView(context);
                label.setText("JUMP");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                layout.addView(label);
                break;

            //Legacy instruction previously used for testing
            /*case BUTTON:
                Button button = new Button(this);
                button.setText("PRESS");
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                button.setOnClickListener(v -> detectInput(Instruction.BUTTON));
                layout.addView(button);
                break;*/
        }
    }
}


