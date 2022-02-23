package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GameMode", GameMode.BASIC);
            intent.putExtra("Difficulty", Difficulty.NORMAL);
            startActivity(intent);
        });
    }
}