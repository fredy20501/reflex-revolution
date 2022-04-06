package ca.unb.mobiledev.reflexrevolution.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.BackgroundMusic;
import ca.unb.mobiledev.reflexrevolution.utils.LocalData;

public class SettingsActivity extends BackgroundMusicActivity {

    private MediaPlayer sfxPlayer;
    private MediaPlayer voicePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        LocalData.initialize(this);

        // Create media players
        sfxPlayer = MediaPlayer.create(this, R.raw.score);
        voicePlayer = MediaPlayer.create(this, R.raw.swipe_hailey);
        sfxPlayer.setOnCompletionListener(v -> sfxPlayer.seekTo(0));
        voicePlayer.setOnCompletionListener(v -> voicePlayer.seekTo(0));

        // Get volume sliders
        SeekBar musicVolume = findViewById(R.id.musicVolume);
        SeekBar sfxVolume = findViewById(R.id.sfxVolume);
        SeekBar voiceVolume = findViewById(R.id.voiceVolume);

        // Set the default values using local data
        musicVolume.setProgress(LocalData.getValue(LocalData.Value.VOLUME_MUSIC));
        sfxVolume.setProgress(LocalData.getValue(LocalData.Value.VOLUME_SFX));
        voiceVolume.setProgress(LocalData.getValue(LocalData.Value.VOLUME_VOICE));

        // Set change listeners
        musicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int newValue, boolean b) {
                // Update music volume
                BackgroundMusic.setVolume(newValue/100f);
                // Update preferences
                LocalData.setValue(LocalData.Value.VOLUME_MUSIC, newValue);
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
                // Update SFX volume & play a short clip
                float newVolume = seekBar.getProgress()/100f;
                sfxPlayer.setVolume(newVolume, newVolume);
                if (sfxPlayer.isPlaying()) sfxPlayer.seekTo(0);
                else sfxPlayer.start();
                // Update preferences
                LocalData.setValue(LocalData.Value.VOLUME_SFX, seekBar.getProgress());
            }
        });
        voiceVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Update voice volume & play a short clip
                float newVolume = seekBar.getProgress()/100f;
                voicePlayer.setVolume(newVolume, newVolume);
                if (voicePlayer.isPlaying()) voicePlayer.seekTo(0);
                else voicePlayer.start();
                // Update preferences
                LocalData.setValue(LocalData.Value.VOLUME_VOICE, seekBar.getProgress());
            }
        });

        // Clear data button
        Button clearDataButton = findViewById(R.id.clearDataButton);
        clearDataButton.setOnLongClickListener(v -> {
            LocalData.clearHighScores();

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