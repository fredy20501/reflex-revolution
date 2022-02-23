package ca.unb.mobiledev.reflexrevolution.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InstructionUtil {

    private static final float DEFAULT_TEXT_SIZE = 80;
    private static final float SMALL_TEXT_SIZE = 16;

    public static ArrayList<Instruction> createInstructions(GameMode mode, SensorManager sensorManager){
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        ArrayList<Instruction> instructions = new ArrayList<>();

        //Construct list of instructions based on gamemode
        switch(mode){
            case BASIC:
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
                instructions.add(Instruction.TAP);
                instructions.add(Instruction.DOUBLE_TAP);
                instructions.add(Instruction.HOLD);
                instructions.add(Instruction.DONT_TAP);

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
        //label and label2 will be used in many cases, so initialize them here and set properties
        //to make the switch cleaner
        TextView label = new TextView(context);
        label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        TextView label2 = new TextView(context);
        label2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        label2.setTextSize(TypedValue.COMPLEX_UNIT_SP, SMALL_TEXT_SIZE);

        switch (instruction){
            case TAP:
                label.setText("TAP");
                layout.addView(label);
                break;

            case DOUBLE_TAP:
                label2.setText("DOUBLE");
                label.setText("TAP");

                layout.addView(label2);
                layout.addView(label);
                break;

            case HOLD:
                label2.setText("HOLD");
                label.setText("TAP");

                layout.addView(label2);
                layout.addView(label);
                break;

            case DONT_TAP:
                label2.setText("DON'T");
                label.setText("TAP");

                layout.addView(label2);
                layout.addView(label);
                break;

            case SWIPE:
                break;

            case SHAKE:
                label.setText("SHAKE");
                layout.addView(label);
                break;

            case JUMP:
                label.setText("JUMP");
                layout.addView(label);
                break;

                case TURN_LEFT:
                label.setText("TURN LEFT");
                layout.addView(label);
                break;

            case TURN_RIGHT:
                label.setText("TURN RIGHT");
                layout.addView(label);
                break;

            case DONT_TURN:
                label.setText("DON'T TURN");
                layout.addView(label);
                break;

            case FREEZE:
                label.setText("FREEZE");
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


