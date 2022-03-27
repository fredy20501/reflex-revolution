package ca.unb.mobiledev.reflexrevolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.LoopMediaPlayer;

public class MainActivity extends AppCompatActivity {

    private LoopMediaPlayer musicPlayer;
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

            //Instead of stopping, pause the music player, since this activity is never destroyed
            //This way, we can reuse the same music player
            musicPlayer.pause();

            // Start next activity
            Intent intent = new Intent(MainActivity.this, GameSettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Restart the music player from the beginning of the track
        if(musicPlayer != null) musicPlayer.restart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Pause background music
        if (musicPlayer != null) musicPlayer.pause();
    }
}