package ca.unb.mobiledev.reflexrevolution.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {
    private static final String PREFS_FILE_NAME = "AppPrefs";
    private static final String HIGH_SCORE_KEY = "HIGH_SCORE_KEY";
    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static String getHighScoreKey(GameMode gameMode, Difficulty difficulty) {
        return "HIGH_SCORE_KEY" + gameMode.getName() + "_" + difficulty.getName();
    }

    public static int getHighScore(GameMode gameMode, Difficulty difficulty) {
        return prefs.getInt(getHighScoreKey(gameMode, difficulty), 0);
    }

    public static void setHighScore(int score, GameMode gameMode, Difficulty difficulty) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getHighScoreKey(gameMode, difficulty), score);
        editor.apply();
    }
}