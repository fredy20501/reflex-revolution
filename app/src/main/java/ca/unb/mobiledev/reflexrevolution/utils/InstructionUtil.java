package ca.unb.mobiledev.reflexrevolution.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InstructionUtil {

    private static final float DEFAULT_TEXT_SIZE = 80;

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

                //Legacy instruction previously used for testing
                //instructions.add(Instruction.BUTTON);
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
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
                layout.addView(label);
                break;

            case DOUBLETAP:
                label = new TextView(context);
                label.setText("DOUBLE\nTAP");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
                layout.addView(label);
                break;

            case HOLD:
                label = new TextView(context);
                label.setText("HOLD");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
                layout.addView(label);
                break;

            case DONTTAP:
                TextView label2 = new TextView(context);
                label = new TextView(context);
                label2.setText("DON'T");
                label.setText("TAP");

                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);

                label2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                layout.addView(label2);
                layout.addView(label);
                break;

            case SWIPE:
                break;

            case SHAKE:
                label = new TextView(context);
                label.setText("SHAKE");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
                layout.addView(label);
                break;

            case JUMP:
                label = new TextView(context);
                label.setText("JUMP");
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
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


