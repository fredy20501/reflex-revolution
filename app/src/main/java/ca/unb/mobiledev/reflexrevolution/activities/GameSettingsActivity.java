package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;

public class GameSettingsActivity extends AppCompatActivity {

    private GameMode gameMode;
    private Difficulty difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        // Game mode selection
        TextView gameModeDescriptionHeader = findViewById(R.id.gameModeDescriptionHeader);
        TextView gameModeDescription = findViewById(R.id.gameModeDescription);
        ChipGroup gameModeSelect = findViewById(R.id.gameModeGroup);
        gameModeSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.classic) {
                gameModeDescriptionHeader.setText(R.string.classic);
                gameModeDescription.setText(R.string.description_classic);
                gameMode = GameMode.CLASSIC;
            }
            else if (checkedId == R.id.tactile) {
                gameModeDescriptionHeader.setText(R.string.tactile);
                gameModeDescription.setText(R.string.description_tactile);
                gameMode = GameMode.TACTILE;
            }
            else if (checkedId == R.id.swipe) {
                gameModeDescriptionHeader.setText(R.string.swipe);
                gameModeDescription.setText(R.string.description_swipe);
                gameMode = GameMode.SWIPE;
            }
            else if (checkedId == R.id.kinetic) {
                gameModeDescriptionHeader.setText(R.string.kinetic);
                gameModeDescription.setText(R.string.description_kinetic);
                gameMode = GameMode.KINETIC;
            }
            else if (checkedId == R.id.keyboard) {
                gameModeDescriptionHeader.setText(R.string.keyboard);
                gameModeDescription.setText(R.string.description_keyboard);
                gameMode = GameMode.KEYBOARD;
            }
            else if (checkedId == R.id.revolution) {
                gameModeDescriptionHeader.setText(R.string.revolution);
                gameModeDescription.setText(R.string.description_revolution);
                gameMode = GameMode.REVOLUTION;
            }
            else {
                Log.i(this.getClass().getSimpleName(), "NO GAMEMODE SELECTED");
                gameModeDescriptionHeader.setText("");
                gameModeDescription.setText("");
                gameMode = null;
            }
        });

        // Difficulty selection
        TextView difficultyDescriptionHeader = findViewById(R.id.difficultyDescriptionHeader);
        TextView difficultyDescription = findViewById(R.id.difficultyDescription);
        ChipGroup difficultySelect = findViewById(R.id.difficultyGroup);
        difficultySelect.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.novice) {
                difficultyDescriptionHeader.setText(R.string.novice);
                difficultyDescription.setText(R.string.description_novice);
                difficulty = Difficulty.NOVICE;
            }
            else if (checkedId == R.id.intermediate) {
                difficultyDescriptionHeader.setText(R.string.intermediate);
                difficultyDescription.setText(R.string.description_intermediate);
                difficulty = Difficulty.INTERMEDIATE;
            }
            else if (checkedId == R.id.master) {
                difficultyDescriptionHeader.setText(R.string.master);
                difficultyDescription.setText(R.string.description_master);
                difficulty = Difficulty.MASTER;
            }
            else {
                Log.i(this.getClass().getSimpleName(), "NO DIFFICULTY SELECTED");
                difficultyDescriptionHeader.setText("");
                difficultyDescription.setText("");
                difficulty = null;
            }
        });

        // Set the default choices
        // TODO: store the choice in shared preferences
        Chip classic = findViewById(R.id.classic);
        classic.setChecked(true);
        Chip intermediate = findViewById(R.id.intermediate);
        intermediate.setChecked(true);

        // Start button
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            // Do nothing if gamemode & difficulty are not selected
            if (gameMode == null || difficulty == null) return;

            // Start game activity
            Intent intent = new Intent(GameSettingsActivity.this, GameActivity.class);
            intent.putExtra("GameMode", gameMode);
            intent.putExtra("Difficulty", difficulty);
            startActivity(intent);
        });
    }
}