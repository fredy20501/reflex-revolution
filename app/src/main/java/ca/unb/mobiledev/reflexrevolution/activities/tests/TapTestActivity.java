package ca.unb.mobiledev.reflexrevolution.activities.tests;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.sensors.TapDetector;
import ca.unb.mobiledev.reflexrevolution.utils.Instruction;

public class TapTestActivity extends AppCompatActivity {
    TextView tapText;
    TextView holdText;
    TextView doubleTapText;
    TapDetector tapDetector;
    ConstraintLayout mainLayout;
    int numTaps;
    int numHolds;
    int numDoubleTaps;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mainLayout = findViewById(R.id.mainLayout);
        tapText = findViewById(R.id.mainLabel);
        holdText = findViewById(R.id.mainLabel2);
        doubleTapText = findViewById(R.id.mainLabel3);
        updateValues();

        numTaps = 0;
        numHolds = 0;
        numDoubleTaps = 0;

        tapDetector = new TapDetector(this, mainLayout);
        tapDetector.setOnTapListener(instruction -> handleInput(instruction));
    }

    private void updateValues(){
        tapText.setText("Tap: " + numTaps);
        doubleTapText.setText("Double Tap: " + numDoubleTaps);
        holdText.setText("Hold: " + numHolds);
    }

    private void handleInput(Instruction instruction){
        switch(instruction){
            case TAP: numTaps++; break;
            case DOUBLE_TAP: numDoubleTaps++; break;
            case HOLD: numHolds++; break;
        }
        updateValues();
    }
}
