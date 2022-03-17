package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.R;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMusicPlayer();

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

    //Set up the music player
    private void setMusicPlayer(){
        musicPlayer = MediaPlayer.create(this, R.raw.menu_music);
        musicPlayer.setLooping(true);
        musicPlayer.start();
    }

    //On resume, restart the music player from the beginning of the track
    @Override
    protected void onResume() {
        super.onResume();
        if(musicPlayer != null) {
            musicPlayer.seekTo(0);
            musicPlayer.start();
        }
    }
}