package ca.unb.mobiledev.reflexrevolution.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;

public class HowToPlayActivity extends BackgroundMusicActivity {
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play);

        // Get UI elements
        Button[] practiceButtons = {
            findViewById(R.id.practiceTapButton),
            findViewById(R.id.practiceSwipeButton),
            findViewById(R.id.practiceShakeButton),
            findViewById(R.id.practiceJumpButton),
            findViewById(R.id.practiceFreezeButton),
            findViewById(R.id.practiceTurnButton),
            findViewById(R.id.practiceTiltButton),
            findViewById(R.id.practiceTwistButton),
            findViewById(R.id.practiceTypeButton),
            findViewById(R.id.practiceDialButton)
        };
        // Set click handler for all buttons
        for (Button button : practiceButtons) {
            button.setOnClickListener(this::practiceButtonHandler);
        }
    }

    private void practiceButtonHandler(View view) {
        // Prevent double-clicking
        if (SystemClock.elapsedRealtime() - lastClickTime < 500) return;
        lastClickTime = SystemClock.elapsedRealtime();

        // Check which button was pressed
        GameMode gameMode = null;
        if (view.getId() == R.id.practiceTapButton) gameMode = GameMode.TAP_PRACTICE;
        else if (view.getId() == R.id.practiceSwipeButton) gameMode = GameMode.SWIPE_PRACTICE;
        else if (view.getId() == R.id.practiceShakeButton) gameMode = GameMode.SHAKE_PRACTICE;
        else if (view.getId() == R.id.practiceJumpButton) gameMode = GameMode.JUMP_PRACTICE;
        else if (view.getId() == R.id.practiceFreezeButton) gameMode = GameMode.FREEZE_PRACTICE;
        else if (view.getId() == R.id.practiceTurnButton) gameMode = GameMode.TURN_PRACTICE;
        else if (view.getId() == R.id.practiceTiltButton) gameMode = GameMode.TILT_PRACTICE;
        else if (view.getId() == R.id.practiceTwistButton) gameMode = GameMode.TWIST_PRACTICE;
        else if (view.getId() == R.id.practiceTypeButton) gameMode = GameMode.TYPE_PRACTICE;
        else if (view.getId() == R.id.practiceDialButton) gameMode = GameMode.DIAL_PRACTICE;
        if (gameMode == null) return;

        // Start next activity
        Intent intent = new Intent(HowToPlayActivity.this, GameActivity.class);
        intent.putExtra("GameMode", gameMode);
        intent.putExtra("Difficulty", Difficulty.NOVICE);
        startActivity(intent);
    }
}