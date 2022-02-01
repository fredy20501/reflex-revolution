package ca.unb.mobiledev.reflexrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Start Game Loop (this should probably be on a separate thread)
        gameLoop();
    }

    String currentInstruction = null;

    void gameLoop() {
        currentInstruction = getRandomInstruction();
        displayInstruction();
        startTimer();
    }

    String getRandomInstruction() {
        // Return random instruction
        return "";
    }

    void displayInstruction() {
        // Display UI based on currentInstruction
    }

    void resetUI() {
        // Hide the displayed UI to prepare for the next instruction
    }

    void startTimer() {
        // Display UI based on currentInstruction
    }

    void stopTimer() {
        // Display UI based on currentInstruction
    }

    void detectInput(String instruction) {
        if (instruction.equals(currentInstruction)) {
            // Correct input detected
            stopTimer();
            resetUI();
            gameLoop(); // Add small delay before next loop?
        }
    }

}