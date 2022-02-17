package ca.unb.mobiledev.reflexrevolution.activities.tests;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.activities.MainActivity;
import ca.unb.mobiledev.reflexrevolution.sensors.TapDetector;
import ca.unb.mobiledev.reflexrevolution.utils.Instruction;

public class TapTestActivity extends AppCompatActivity {
    Random r;
    TextView tapText;
    Instruction[] instructions;
    Instruction currentInstruction;
    TapDetector tapDetector;
    ConstraintLayout mainLayout;
    int numInputs;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tapText = findViewById(R.id.mainLabel);
        mainLayout = findViewById(R.id.mainLayout);

        numInputs = 0;
        r = new Random();
        instructions = new Instruction[]{Instruction.TAP, Instruction.DOUBLETAP, Instruction.HOLD};

        tapDetector = new TapDetector(this, mainLayout);
        tapDetector.setOnTapListener(instruction -> handleInput(instruction));

        newInstruction();
    }

    private void newInstruction(){
        currentInstruction = instructions[r.nextInt(instructions.length)];
        switch(currentInstruction){
            case TAP:
                tapText.setText("Tap: " + numInputs);
                break;
            case DOUBLETAP:
                tapText.setText("Double Tap: " + numInputs);
                break;
            case HOLD:
                tapText.setText("Hold: " + numInputs);
                break;
            default:
                tapText.setText("");
                break;
        }
    }

    private void handleInput(Instruction instruction){
        if (instruction == currentInstruction){
            numInputs++;
            newInstruction();
        }
    }
}
