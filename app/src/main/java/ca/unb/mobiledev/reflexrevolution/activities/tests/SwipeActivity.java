package ca.unb.mobiledev.reflexrevolution.activities.tests;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.detectors.SwipeDetector;

@SuppressLint("SetTextI18n")
public class SwipeActivity extends AppCompatActivity {

    private TextView swipeText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        swipeText = findViewById(R.id.mainLabel);
        swipeText.setText("Swipe in any direction");

        SwipeDetector swipeDetector = new SwipeDetector(this, mainLayout);
        swipeDetector.setOnSwipeListener(this::updateLabel);
    }

    protected void updateLabel(SwipeDetector.Action action){
        swipeText.setText(""+action);
    }
}
