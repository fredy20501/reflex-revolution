package ca.unb.mobiledev.reflexrevolution.utils;

import android.content.Context;

import ca.unb.mobiledev.reflexrevolution.R;

// Controller class for handling background music
public abstract class BackgroundMusic {
    private static LoopMediaPlayer musicPlayer;
    private static int startCounter = 0;

    public static void initialize(Context context) {
        musicPlayer = LoopMediaPlayer.create(context, R.raw.menu_music);
    }

    public static void onStart() {
        startCounter++;
        if (startCounter == 1) {
            musicPlayer.restart();
        }
    }

    public static void onStop() {
        startCounter--;
        if (startCounter == 0) {
            musicPlayer.pause();
        }
        else if (startCounter < 0) startCounter = 0;
    }
}
