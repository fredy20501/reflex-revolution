package ca.unb.mobiledev.reflexrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    private TextView scoreText;
    private Button replayButton;
    private Button menuButton;

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
        }

        //Start activity for new game if "Play Again" is hit
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, TimerTestActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Close this activity if "Menu" button is hit, returning to menu
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
