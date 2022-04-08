package ca.unb.mobiledev.reflexrevolution.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {
    private static final String PREFS_FILE_NAME = "ReflexRevolutionPrefs";
    private static final String HIGH_SCORE_KEY = "HIGH_SCORE_KEY_";
    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static String getHighScoreKey(GameMode gameMode, Difficulty difficulty) {
        return HIGH_SCORE_KEY + gameMode.getName() + "_" + difficulty.getName();
    }

    public static int getHighScore(GameMode gameMode, Difficulty difficulty) {
        // Make sure prefs was initialized
        if (prefs == null) return 0;

        return prefs.getInt(getHighScoreKey(gameMode, difficulty), 0);
    }

    public static void setHighScore(int score, GameMode gameMode, Difficulty difficulty) {
        // Make sure prefs was initialized
        if (prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getHighScoreKey(gameMode, difficulty), score);
        editor.apply();
    }

    // Reset all high scores to 0
    public static void clearHighScores() {
        // Make sure prefs was initialized
        if (prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        for (GameMode gameMode : GameMode.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                editor.putInt(getHighScoreKey(gameMode, difficulty), 0);
            }
        }
        editor.apply();
    }

    public enum Value {
        VOLUME_MUSIC(100),
        VOLUME_SFX(100),
        VOLUME_VOICE(100),
        GAMEMODE(GameMode.CLASSIC.getId()),
        DIFFICULTY(Difficulty.INTERMEDIATE.getId());

        private final int defaultValue;
        Value(int defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    public static int getValue(Value value) {
        // Make sure prefs was initialized
        if (prefs == null) return value.defaultValue;

        return prefs.getInt(value.name(), value.defaultValue);
    }

    public static void setValue(Value value, int newValue) {
        // Make sure prefs was initialized
        if (prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(value.name(), newValue);
        editor.apply();

    }
}