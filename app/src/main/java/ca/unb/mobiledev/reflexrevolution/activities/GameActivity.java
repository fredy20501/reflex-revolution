package ca.unb.mobiledev.reflexrevolution.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.instructions.DialInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.Instruction;
import ca.unb.mobiledev.reflexrevolution.instructions.TypeInstruction;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.utils.InstructionManager;

public class GameActivity extends AppCompatActivity {
    private final int TIME_BETWEEN_LOOPS = 1000;

    private ObjectAnimator instructionTimerAnimation;
    private ProgressBar timeProgressBar;
    private TextView scoreText;
    private LinearLayout instructionSpace; //Layout we should add new UI elements to

    private CountDownTimer resetTimer;
    private Instruction currentInstruction;
    private InstructionManager instructionManager;
    private GameMode gameMode;
    private Difficulty difficulty;

    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Retrieve game mode and difficulty
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            gameMode = (GameMode)extras.get("GameMode");
            difficulty = (Difficulty)extras.get("Difficulty");
        }

        // Get UI elements
        scoreText = findViewById(R.id.score);
        instructionSpace = findViewById(R.id.instructionSpace);
        timeProgressBar = findViewById(R.id.progressBar);

        // Set up the game
        initialize();

        // Start the game
        startResetTimer();
    }

    private void initialize() {
        score = 0;
        updateScoreText();

        // Set up the instructions
        instructionManager = new InstructionManager(instructionSpace, new Instruction.Callback() {
            @Override
            public void onSuccess() { instructionSuccess(); }
            @Override
            public void onFailure() { endGame(); }
        });
        instructionManager.generateInstructions(gameMode);

        // Setup the reset timer (used as delay between instructions)
        resetTimer = new CountDownTimer(TIME_BETWEEN_LOOPS, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() { gameLoop(); }
        };

        // Setup the instruction timer (progress bar animation)
        instructionTimerAnimation = ObjectAnimator.ofInt(timeProgressBar, "progress", 100, 0);
        instructionTimerAnimation.setInterpolator(new LinearInterpolator());
        instructionTimerAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                currentInstruction.timerFinished();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
    }

    private void updateScoreText(){
        scoreText.setText(String.valueOf(score));
    }

    // Stop current timer then call game loop after one second
    private void startResetTimer() {
        instructionTimerAnimation.cancel();
        if (resetTimer != null) {
            resetTimer.cancel();

            //Wait some delay before calling game loop
            resetTimer.start();
        }
    }

    // Get new timer count, then start it
    private void newTimer() {
        int timerDuration = scaleTimerFromScore();

        // If you need to type or dial, add extra time
        // TODO: abstract this into the Instruction class, to be used by all instructions)
        if(currentInstruction instanceof TypeInstruction) timerDuration += 1000;
        else if(currentInstruction instanceof DialInstruction) timerDuration += 2000;

        // Start the timer animation
        instructionTimerAnimation.setDuration(timerDuration);
        instructionTimerAnimation.start();
    }

    // Prepare and start new instruction loop
    private void gameLoop() {
        currentInstruction = instructionManager.getInstruction();
        currentInstruction.init();
        currentInstruction.display();
        currentInstruction.enable();
        newTimer();
    }

    // Clear all added UI elements & reset gravity
    private void resetUI() {
        instructionSpace.removeAllViews();
        int defaultGravity = Gravity.CENTER;
        instructionSpace.setGravity(defaultGravity);
    }

    private void instructionSuccess() {
        // Update score
        score++;
        updateScoreText();

        // Stop timer and clear UI, wait one second, then start the game loop (in resetTimer)
        currentInstruction.disable();
        resetUI();
        startResetTimer();
    }

    // Ends the game, sending score and game options to the Game Over screen
    private void endGame(){
        instructionTimerAnimation.cancel();
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("Score", score);
        intent.putExtra("GameMode", gameMode);
        intent.putExtra("Difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    // Scuffed function that will give a scaled timer based on score.
    // Starts at ~3000ms and min value is ~1000ms
    private int scaleTimerFromScore() {
        return (int)Math.pow(2, -0.04*score + 11) + 1000;
    }

    // Resume timer if app was closed
    @Override
    protected void onResume() {
        super.onResume();
        if (currentInstruction != null) currentInstruction.enable();
        instructionTimerAnimation.resume();
    }

    // Make sure timer doesn't keep going with app closed
    @Override
    protected void onPause() {
        super.onPause();
        if (currentInstruction != null) currentInstruction.disable();
        instructionTimerAnimation.pause();
    }
}