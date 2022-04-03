package ca.unb.mobiledev.reflexrevolution.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.BackgroundMusic;

public class SettingsActivity extends BackgroundMusicActivity {

    private MediaPlayer sfxPlayer;
    private MediaPlayer voicePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Create media players
        sfxPlayer = MediaPlayer.create(this, R.raw.score);
        voicePlayer = MediaPlayer.create(this, R.raw.swipe_hailey);
        sfxPlayer.setOnCompletionListener(v -> sfxPlayer.seekTo(0));
        voicePlayer.setOnCompletionListener(v -> voicePlayer.seekTo(0));

        // Get volume sliders
        SeekBar musicVolume = findViewById(R.id.musicVolume);
        SeekBar sfxVolume = findViewById(R.id.sfxVolume);
        SeekBar voiceVolume = findViewById(R.id.voiceVolume);

        // Set the default values
        // TODO: store the choice in shared preferences
        musicVolume.setProgress(100);
        sfxVolume.setProgress(100);
        voiceVolume.setProgress(100);

        // Set change listeners
        musicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Update music volume
                BackgroundMusic.setVolume(i/100f);
                // TODO: update preferences & update game music with those preferences
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sfxVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float newVolume = seekBar.getProgress()/100f;
                sfxPlayer.setVolume(newVolume, newVolume);
                if (sfxPlayer.isPlaying()) sfxPlayer.seekTo(0);
                else sfxPlayer.start();
                // TODO: update sfx volume preferences & update game activity's media players to use sound preferences
            }
        });
        voiceVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float newVolume = seekBar.getProgress()/100f;
                voicePlayer.setVolume(newVolume, newVolume);
                if (voicePlayer.isPlaying()) voicePlayer.seekTo(0);
                else voicePlayer.start();
                // TODO: update voice volume preferences & update Instruction media player to use preferences
            }
        });

        // Clear data button
        Button clearDataButton = findViewById(R.id.clearDataButton);
        clearDataButton.setOnLongClickListener(v -> {
            // TODO: clear data from preferences

            // Show feedback
            Toast.makeText(this, "Data cleared!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sfxPlayer.stop();
        sfxPlayer.release();
        sfxPlayer = null;
        voicePlayer.stop();
        voicePlayer.release();
        voicePlayer = null;
    }
}