package ca.unb.mobiledev.reflexrevolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.utils.LocalData;

public class GameSettingsActivity extends BackgroundMusicActivity {

    private long lastClickTime = 0;
    private GameMode gameMode;
    private Difficulty difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_select);
        LocalData.initialize(this);

        // Game mode selection
        TextView gameModeDescriptionHeader = findViewById(R.id.gameModeDescriptionHeader);
        TextView gameModeDescription = findViewById(R.id.gameModeDescription);
        ChipGroup gameModeSelect = findViewById(R.id.gameModeGroup);
        gameModeSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.classic) gameMode = GameMode.CLASSIC;
            else if (checkedId == R.id.tactile) gameMode = GameMode.TACTILE;
            else if (checkedId == R.id.swipe) gameMode = GameMode.SWIPE;
            else if (checkedId == R.id.kinetic) gameMode = GameMode.KINETIC;
            else if (checkedId == R.id.keyboard) gameMode = GameMode.KEYBOARD;
            else if (checkedId == R.id.revolution) gameMode = GameMode.REVOLUTION;
            else gameMode = null;

            // Update text fields according to selection
            if (gameMode == null) {
                gameModeDescriptionHeader.setText("");
                gameModeDescription.setText("");
            }
            else {
                gameModeDescriptionHeader.setText(gameMode.getName());
                gameModeDescription.setText(gameMode.getDescription());
            }
        });

        // Difficulty selection
        TextView difficultyDescriptionHeader = findViewById(R.id.difficultyDescriptionHeader);
        TextView difficultyDescription = findViewById(R.id.difficultyDescription);
        ChipGroup difficultySelect = findViewById(R.id.difficultyGroup);
        difficultySelect.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.novice) difficulty = Difficulty.NOVICE;
            else if (checkedId == R.id.intermediate) difficulty = Difficulty.INTERMEDIATE;
            else if (checkedId == R.id.master) difficulty = Difficulty.MASTER;
            else difficulty = null;

            // Update text fields according to selection
            if (difficulty == null) {
                difficultyDescriptionHeader.setText("");
                difficultyDescription.setText("");
            }
            else {
                difficultyDescriptionHeader.setText(difficulty.getName());
                difficultyDescription.setText(difficulty.getDescription());
            }
        });

        // Set the default choices from preferences
        int gmId = LocalData.getValue(LocalData.Value.GAMEMODE);
        for (GameMode gm : GameMode.values()) {
            if (gm.getId() == gmId) {
                Chip chip = findViewById(gm.getChipId());
                chip.setChecked(true);
            }
        }
        int difficultyId = LocalData.getValue(LocalData.Value.DIFFICULTY);
        for (Difficulty diff : Difficulty.values()) {
            if (diff.getId() == difficultyId) {
                Chip chip = findViewById(diff.getChipId());
                chip.setChecked(true);
            }
        }

        // Start button
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            // Do nothing if gamemode & difficulty are not selected
            if (gameMode == null || difficulty == null) return;

            // Prevent double-clicking
            if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
            lastClickTime = SystemClock.elapsedRealtime();

            // Start game activity
            Intent intent = new Intent(GameSettingsActivity.this, GameActivity.class);
            intent.putExtra("GameMode", gameMode);
            intent.putExtra("Difficulty", difficulty);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save current selection when leaving this activity
        LocalData.setValue(LocalData.Value.GAMEMODE, gameMode.getId());
        LocalData.setValue(LocalData.Value.DIFFICULTY, difficulty.getId());
    }
}