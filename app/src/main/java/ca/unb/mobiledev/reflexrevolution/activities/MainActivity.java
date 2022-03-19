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
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            // Prevent double-clicking
            if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
            lastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(MainActivity.this, GameSettingsActivity.class);
            startActivity(intent);
        });
    }
}