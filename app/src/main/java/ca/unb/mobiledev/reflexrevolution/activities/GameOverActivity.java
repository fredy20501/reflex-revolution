package ca.unb.mobiledev.reflexrevolution.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;

public class GameOverActivity extends AppCompatActivity {

    private int score = 0;
    private GameMode gameMode = GameMode.CLASSIC;
    private Difficulty difficulty = Difficulty.INTERMEDIATE;
    private static final String PREFS_FILE_NAME = "AppPrefs";
    private static final String HIGH_SCORE_KEY = "HIGH_SCORE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        initSharedPreferences();

        //Retrieve information
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            score = extras.getInt("Score");
            gameMode = (GameMode)extras.get("GameMode");
            difficulty = (Difficulty)extras.get("Difficulty");
        }

        // Get UI elements
        TextView highScoreText = findViewById(R.id.highScore);
        TextView scoreText = findViewById(R.id.score);
        TextView gameModeValue = findViewById(R.id.gameModeValue);
        TextView difficultyValue = findViewById(R.id.difficultyValue);
        Button replayButton = findViewById(R.id.replayButton);
        Button menuButton = findViewById(R.id.menuButton);

        // Set values
        highScoreText.setText(String.valueOf(readHighScoreFromSharedPreferences()));
        scoreText.setText(String.valueOf(score));
        gameModeValue.setText(gameMode.getName());
        difficultyValue.setText(difficulty.getName());

        if (score > readHighScoreFromSharedPreferences()) {
            // Get and edit high score
            writeHighScoreToSharedPreferences(score);
            highScoreText.setText(String.valueOf(score));
        }

        // Replay button to start new game
        replayButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
            intent.putExtra("GameMode", gameMode);
            intent.putExtra("Difficulty", difficulty);
            startActivity(intent);
            finish();
        });

        // Menu button to close this activity, returning to game select screen
        menuButton.setOnClickListener(v -> finish());
    }

    private void initSharedPreferences() {
        prefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    private int readHighScoreFromSharedPreferences() {
        return prefs.getInt(HIGH_SCORE_KEY, 0);
    }

    private void writeHighScoreToSharedPreferences(int score) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HIGH_SCORE_KEY, score);
        editor.apply();
    }

}
