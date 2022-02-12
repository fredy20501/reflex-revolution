package ca.unb.mobiledev.reflexrevolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.R;

public class GameOverActivity extends AppCompatActivity {

    private TextView scoreText;
    private Button replayButton;
    private Button menuButton;
    private GameMode gameMode;
    private Difficulty difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        scoreText = findViewById(R.id.scoreText);
        replayButton = findViewById(R.id.replayButton);
        menuButton = findViewById(R.id.menuButton);

        //Retrieve score
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            scoreText.setText("Score: " + extras.getInt("Score"));
            gameMode = (GameMode)extras.get("GameMode");
            difficulty = (Difficulty)extras.get("Difficulty");
        }

        //Start activity for new game if "Play Again" is hit
        replayButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
            intent.putExtra("GameMode", gameMode);
            intent.putExtra("Difficulty", difficulty);
            startActivity(intent);
            finish();
        });

        //Close this activity if "Menu" button is hit, returning to menu
        menuButton.setOnClickListener(v -> finish());
    }
}
