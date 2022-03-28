package ca.unb.mobiledev.reflexrevolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;

public class MainActivity extends AppCompatActivity {

    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            // Prevent double-clicking
            if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
            lastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(MainActivity.this, GameSettingsActivity.class);
            startActivity(intent);
        });

//        Button howToPlayButton = findViewById(R.id.howToPlayButton);
//        howToPlayButton.setOnClickListener(v -> {
//            // Prevent double-clicking
//            if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
//            lastClickTime = SystemClock.elapsedRealtime();
//            Intent intent = new Intent(MainActivity.this, HowToPlayActivity.class);
//            startActivity(intent);
//        });

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