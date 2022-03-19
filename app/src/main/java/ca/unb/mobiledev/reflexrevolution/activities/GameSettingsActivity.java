package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;

public class GameSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameSettingsActivity.this, GameActivity.class);
            intent.putExtra("GameMode", GameMode.REVOLUTION);
            intent.putExtra("Difficulty", Difficulty.NOVICE);
            startActivity(intent);
        });
    }
}