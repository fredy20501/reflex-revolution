package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            musicPlayer.stop();

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GameMode", GameMode.REVOLUTION);
            intent.putExtra("Difficulty", Difficulty.NOVICE);
            startActivity(intent);
        });
    }

    private void setMusicPlayer(){
        musicPlayer.create(this, R.raw.score);
        musicPlayer.setLooping(true);
        musicPlayer.start();
    }
}