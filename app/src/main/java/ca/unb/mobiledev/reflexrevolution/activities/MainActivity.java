package ca.unb.mobiledev.reflexrevolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.BackgroundMusic;

public class MainActivity extends BackgroundMusicActivity {
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        // Initialize background music instance
        BackgroundMusic.initialize(this);

        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            // Prevent double-clicking
            if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
            lastClickTime = SystemClock.elapsedRealtime();

            // Start next activity
            Intent intent = new Intent(MainActivity.this, GameSettingsActivity.class);
            startActivity(intent);
        });

        Button howToPlayButton = findViewById(R.id.howToPlayButton);
        howToPlayButton.setOnClickListener(v -> {
            // Prevent double-clicking
            if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
            lastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(MainActivity.this, HowToPlayActivity.class);
            startActivity(intent);
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            // Prevent double-clicking
            if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
            lastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}