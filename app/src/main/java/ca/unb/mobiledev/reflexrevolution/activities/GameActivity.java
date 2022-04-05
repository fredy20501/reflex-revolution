package ca.unb.mobiledev.reflexrevolution.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.res.ResourcesCompat;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.instructions.DialInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.Instruction;
import ca.unb.mobiledev.reflexrevolution.instructions.TypeInstruction;
import ca.unb.mobiledev.reflexrevolution.utils.BackgroundMusic;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.utils.InstructionManager;
import ca.unb.mobiledev.reflexrevolution.utils.LocalData;
import ca.unb.mobiledev.reflexrevolution.utils.LoopMediaPlayer;

public class GameActivity extends AppCompatActivity {
    private final int TIME_BETWEEN_LOOPS = 1000;
    private final int DISPLAY_SUCCESS_TIME = 1500;
    private final double MIN_SONG_SPEED = 0.5;
    private final double MAX_SONG_SPEED = 1.5;

    private ObjectAnimator instructionTimerAnimation;
    private ProgressBar timeProgressBar;
    private TextView scoreText;
    private TextView highScoreText;
    private LinearLayout instructionSpace;
    private ImageButton pauseButton;
    private ViewGroup pauseOverlay;
    private ViewGroup resumeSection;
    private Button exitButton;
    private TextView feedbackText;

    private CountDownTimer resetTimer;
    private CountDownTimer feedbackTimer;
    private Instruction currentInstruction;
    private InstructionManager instructionManager;
    private GameMode gameMode;
    private Difficulty difficulty;

    private MediaPlayer scorePlayer;
    private MediaPlayer losePlayer;
    private LoopMediaPlayer musicPlayer;
    
    private int score;
    private boolean success;
    private boolean isGamePaused = false;
    private boolean isTimerDelayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        LocalData.initialize(GameActivity.this);

        // Retrieve game mode and difficulty
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            gameMode = (GameMode)extras.get("GameMode");
            difficulty = (Difficulty)extras.get("Difficulty");
        }

        // Force background music to stop
        BackgroundMusic.onStop();

        // Get UI elements
        scoreText = findViewById(R.id.score);
        highScoreText = findViewById(R.id.highScore);
        instructionSpace = findViewById(R.id.instructionSpace);
        timeProgressBar = findViewById(R.id.progressBar);
        pauseButton = findViewById(R.id.pauseButton);
        pauseOverlay = findViewById(R.id.pauseOverlay);
        resumeSection = findViewById(R.id.resumeSection);
        exitButton = findViewById(R.id.exitButton);

        // Create feedback text UI element
        ContextThemeWrapper textFeedbackStyle = new ContextThemeWrapper(this, R.style.successFeedback);
        feedbackText = new TextView(textFeedbackStyle);
        feedbackText.setText(R.string.fail);
        feedbackText.setGravity(Gravity.CENTER);
        feedbackText.setTypeface(ResourcesCompat.getFont(this, R.font.rocknroll_one));

        // Set up the game
        initialize();

        // Start the game
        startResetTimer();
    }

    private void initialize() {
        success = false;
        score = 0;
        updateScoreText();
        updateHighScoreText();

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
                currentInstruction.playVoiceCommand();
                updateMusicSpeed();
            }
        });
        exitButton.setOnClickListener(v -> onBackPressed());

        // Set up the instructions
        instructionManager = new InstructionManager(instructionSpace, new Instruction.Callback() {
            @Override
            public void onSuccess() { instructionSuccess(); }
            @Override
            public void onFailure() {
                if(!gameMode.isPractice()) endGame();
                else instructionFailure();
            }
        });
        instructionManager.generateInstructions(gameMode);

        // Set up the reset timer (used as delay between instructions)
        resetTimer = new CountDownTimer(TIME_BETWEEN_LOOPS, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() { gameLoop(); }
        };
        // Set up the feedback timer (used as delay for showing feedback)
        feedbackTimer = new CountDownTimer(DISPLAY_SUCCESS_TIME, 500) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                resetUI();
                startResetTimer();
            }
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

        //Hide score and high score if this is a practice
        if(gameMode.isPractice()){
            scoreText.setVisibility(View.INVISIBLE);
            findViewById(R.id.highScore).setVisibility(View.GONE);
            findViewById(R.id.highScoreTrophy).setVisibility(View.GONE);
            findViewById(R.id.exitButton).setVisibility(View.VISIBLE);
        }

        setMediaPlayers();
    }

    //Set up the media players
    private void setMediaPlayers(){
        //Create media players, and set their sound files
        scorePlayer = MediaPlayer.create(this, R.raw.score);
        losePlayer = MediaPlayer.create(this, R.raw.lose);
        musicPlayer = LoopMediaPlayer.create(this, R.raw.game_music);
        updateMusicSpeed();

        //Rewind sound when it ends so that it can be played again later
        scorePlayer.setOnCompletionListener(v -> scorePlayer.seekTo(0));

        //If in the main game, allow losePlayer to continue playing when the activity is closed
        //but stop it properly once it finishes playing
        if(!gameMode.isPractice()) losePlayer.setOnCompletionListener(v -> stopLosePlayer());
        //If we are in a practice, rewind it when it finishes
        else losePlayer.setOnCompletionListener(v -> scorePlayer.seekTo(0));
    }

    //Properly handle stopping all media players
    //Lose player will be handled by its onCompletionListener
    private void stopMediaPlayers(){
        if (scorePlayer != null) {
            scorePlayer.stop();
            scorePlayer.release();
            scorePlayer = null;
        }
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    private void stopLosePlayer(){
        if (losePlayer != null) {
            losePlayer.stop();
            losePlayer.release();
            losePlayer = null;
        }
    }

    private void updateScoreText(){
        scoreText.setText(String.valueOf(score));
    }

    private void updateHighScoreText(){
        highScoreText.setText(String.valueOf(LocalData.getHighScore(gameMode, difficulty)));
    }

    // Stop current timer then call game loop after one second
    private void startResetTimer() {
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

        // Call proper init function depending on if this is practice mode or not
        if(gameMode.isPractice()) currentInstruction.init(success);
        else currentInstruction.init();

        // Display the instruction
        currentInstruction.display();

        if (!isGamePaused) {
            currentInstruction.playVoiceCommand();
            currentInstruction.enable();
            updateMusicSpeed();
        }

        newTimer();
    }

    // Clear all added UI elements & reset gravity
    private void resetUI() {
        instructionSpace.removeAllViews();
        int defaultGravity = Gravity.CENTER;
        instructionSpace.setGravity(defaultGravity);
    }

    private void showFeedback(){
        instructionSpace.addView(feedbackText);
    }

    private void instructionSuccess() {
        // Play success sound effect
        scorePlayer.start();

        // Update score if not in practice
        if(!gameMode.isPractice()) {
            score++;
            updateScoreText();
        }

        // Set success state before calling next instruction
        success = true;
        nextInstruction();
    }

    private void instructionFailure(){
        // Play fail sound
        losePlayer.start();

        // Set success state before calling next instruction
        success = false;
        nextInstruction();
    }

    // Stop timer and clear UI, wait one second, then start the game loop (in resetTimer)
    private void nextInstruction(){
        instructionTimerAnimation.cancel();
        currentInstruction.disable();
        resetUI();

        // If practice & failed the instruction, show feedback
        if(gameMode.isPractice() && !success) {
            showFeedback();
            feedbackTimer.start();
        }
        // Else start next instruction as normal
        else startResetTimer();
    }

    // Ends the game, sending score and game options to the Game Over screen
    private void endGame(){
        // Stop music and play lose sound effect
        losePlayer.start();
        // Stop timer
        instructionTimerAnimation.cancel();

        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("Score", score);
        intent.putExtra("GameMode", gameMode);
        intent.putExtra("Difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    //Scale music playback speed based on how much time the player has to complete the instruction
    private void updateMusicSpeed(){
        musicPlayer.setPlaybackSpeed(scaleSongSpeedFromTimer());
    }

    // Scuffed function that will give a scaled timer based on score.
    // Starts at ~3000ms and min value is ~1000ms
    private int scaleTimerFromScore() { return (int)Math.pow(2, -0.04*score + 11) + 1000; }

    //Returns a value between 0.5 and 1.5 based on the time the user has to complete the instruction
    //Scales linearly, with 0.5 as 5000ms, 1 as 3000ms, and 1.5 as 1000ms
    private float scaleSongSpeedFromTimer() {
        return (float)clamp(-(scaleTimerFromScore() - 3000.0)/4000.0 + 1, MIN_SONG_SPEED, MAX_SONG_SPEED);
    }

    //Clamp function using double for maximum compatibility
    private double clamp(double val, double min, double max){
        if(val > max) return max;
        else if (val < min) return min;
        return val;
    }

    private void pauseGame() {
        if (musicPlayer != null) musicPlayer.pause();
        if (currentInstruction != null) currentInstruction.disable();
        instructionTimerAnimation.pause();
    }

    private void resumeGame() {
        if (musicPlayer != null) musicPlayer.restart();
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
    protected void onDestroy() {
        super.onDestroy();
        stopMediaPlayers();
    }

    @Override
    public void onBackPressed(){
        currentInstruction = null;
        instructionTimerAnimation.cancel();
        resetTimer.cancel();
        feedbackTimer.cancel();
        stopLosePlayer();
        stopMediaPlayers();
        finish();
    }
}