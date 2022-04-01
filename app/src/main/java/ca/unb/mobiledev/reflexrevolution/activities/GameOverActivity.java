package ca.unb.mobiledev.reflexrevolution.activities;

import static ca.unb.mobiledev.reflexrevolution.utils.LocalData.getHighScore;
import static ca.unb.mobiledev.reflexrevolution.utils.LocalData.initialize;
import static ca.unb.mobiledev.reflexrevolution.utils.LocalData.setHighScore;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        initialize(GameOverActivity.this);

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
        scoreText.setText(String.valueOf(score));
        highScoreText.setText(String.valueOf(getHighScore(gameMode, difficulty)));
        gameModeValue.setText(gameMode.getName());
        difficultyValue.setText(difficulty.getName());

        if (score > getHighScore(gameMode, difficulty)) {
            // Get and edit high score
            setHighScore(score, gameMode, difficulty);
            highScoreText.setText(String.valueOf(getHighScore(gameMode, difficulty)));
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

}
