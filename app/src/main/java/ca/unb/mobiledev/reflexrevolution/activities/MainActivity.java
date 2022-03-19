package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.LoopMediaPlayer;

public class MainActivity extends AppCompatActivity {

    private LoopMediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicPlayer = LoopMediaPlayer.create(this, R.raw.menu_music);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            //Instead of stopping, pause the music player, since this activity is never destroyed
            //This way, we can reuse the same music player
            musicPlayer.pause();

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GameMode", GameMode.REVOLUTION);
            intent.putExtra("Difficulty", Difficulty.NOVICE);
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