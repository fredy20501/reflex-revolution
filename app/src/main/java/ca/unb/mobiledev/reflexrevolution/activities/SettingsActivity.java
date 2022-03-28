package ca.unb.mobiledev.reflexrevolution.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

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
                // TODO: update music volume
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO: play a sample of music
            }
        });
        sfxVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO: update sfx volume
                // TODO: play a sample sound effect
            }
        });
        voiceVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO: update voice volume
                // TODO: play a sample voice line
            }
        });

        // Clear data button
        Button clearDataButton = findViewById(R.id.clearDataButton);
        clearDataButton.setOnLongClickListener(v -> {
            // TODO: clear data

            // Show feedback
            Toast.makeText(this, "Data cleared!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}