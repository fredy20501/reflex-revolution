package ca.unb.mobiledev.reflexrevolution.activities.tests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

@SuppressLint("SetTextI18n")
public class SwipeTestActivity extends AppCompatActivity {

    private TextView swipeText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        swipeText = findViewById(R.id.mainLabel);
        swipeText.setText("Swipe in any direction");

        TouchDetector touchDetector = new TouchDetector(mainLayout);
        touchDetector.addListener(new TouchDetector.ActionListener() {
            @Override
            public void onSwipe(TouchDetector.SwipeAction action) {
                updateLabel(action);
            }
            @Override
            public void onTap(TouchDetector.TapAction action) {}
        });
    }

    protected void updateLabel(TouchDetector.SwipeAction action){
        swipeText.setText(""+action);
    }
}
