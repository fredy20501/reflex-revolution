package ca.unb.mobiledev.reflexrevolution.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.utils.BackgroundMusic;

public class BackgroundMusicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BackgroundMusic.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BackgroundMusic.onStop();
    }
}
