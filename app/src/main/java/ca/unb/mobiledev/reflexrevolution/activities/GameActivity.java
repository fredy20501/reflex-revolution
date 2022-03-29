package ca.unb.mobiledev.reflexrevolution.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
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
    private LinearLayout instructionSpace;
    private ImageButton pauseButton;
    private ViewGroup pauseOverlay;
    private ViewGroup resumeSection;

    private CountDownTimer resetTimer;
    private Instruction currentInstruction;
    private InstructionManager instructionManager;
    private GameMode gameMode;
    private Difficulty difficulty;

    private int score;
    private boolean isGamePaused = false;
    private boolean isTimerDelayed = false;

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
        pauseButton = findViewById(R.id.pauseButton);
        pauseOverlay = findViewById(R.id.pauseOverlay);
        resumeSection = findViewById(R.id.resumeSection);

        // Set up the game
        initialize();

        // Start the game
        startResetTimer();
    }

    private void initialize() {
        score = 0;
        updateScoreText();

        // Set up UI interactions
        pauseButton.setOnClickListener(v -> {
            // Show overlay, hide current instruction
            pauseOverlay.setVisibility(View.VISIBLE);
            instructionSpace.setVisibility(View.INVISIBLE);
            isGamePaused = true;
            pauseGame();
        });
        resumeSection.setOnClickListener(v -> {
            // Hide overlay, show current instruction
            pauseOverlay.setVisibility(View.GONE);
            instructionSpace.setVisibility(View.VISIBLE);
            isGamePaused = false;
            resumeGame();
            if (isTimerDelayed) {
                isTimerDelayed = false;
                instructionTimerAnimation.start();
            }
        });

        // Set up the instructions
        instructionManager = new InstructionManager(instructionSpace, new Instruction.Callback() {
            @Override
            public void onSuccess() { instructionSuccess(); }
            @Override
            public void onFailure() { endGame(); }
        });
        instructionManager.generateInstructions(gameMode);

        // Set up the reset timer (used as delay between instructions)
        resetTimer = new CountDownTimer(TIME_BETWEEN_LOOPS, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() { gameLoop(); }
        };

        // Set up the instruction timer (progress bar animation)
        instructionTimerAnimation = ObjectAnimator.ofInt(timeProgressBar, "progress", 100, 0);
        instructionTimerAnimation.setInterpolator(new LinearInterpolator());
        instructionTimerAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                if (currentInstruction != null) currentInstruction.timerFinished();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        //Hide score and high score if this is a tutorial
        if(gameMode.isTutorial()){
            scoreText.setVisibility(View.INVISIBLE);
            findViewById(R.id.highScore).setVisibility(View.INVISIBLE);
        }
    }

    private void updateScoreText(){
        scoreText.setText(String.valueOf(score));
    }

    // Stop current timer then call game loop after one second
    private void startResetTimer() {
        instructionTimerAnimation.cancel();
        //Wait some delay before calling game loop
        resetTimer.cancel();
        resetTimer.start();
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
        if (!isGamePaused) instructionTimerAnimation.start();
        else isTimerDelayed = true;
    }

    // Prepare and start new instruction loop
    private void gameLoop() {
        currentInstruction = instructionManager.getInstruction();
        currentInstruction.init();
        currentInstruction.display();
        if (!isGamePaused) currentInstruction.enable();
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
    private int scaleTimerFromScore() { return (int)Math.pow(2, -0.04*score + 11) + 1000; }

    private void pauseGame() {
        if (currentInstruction != null) currentInstruction.disable();
        instructionTimerAnimation.pause();
    }

    private void resumeGame() {
        if (currentInstruction != null) currentInstruction.enable();
        if (!isGamePaused) instructionTimerAnimation.resume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    @Override
    public void onBackPressed(){
        currentInstruction = null;
        instructionTimerAnimation.cancel();
        resetTimer.cancel();
        finish();
    }
}