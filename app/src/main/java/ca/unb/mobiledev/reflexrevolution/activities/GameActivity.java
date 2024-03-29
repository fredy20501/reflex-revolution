package ca.unb.mobiledev.reflexrevolution.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
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
import ca.unb.mobiledev.reflexrevolution.instructions.Instruction;
import ca.unb.mobiledev.reflexrevolution.utils.BackgroundMusic;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.utils.InstructionManager;
import ca.unb.mobiledev.reflexrevolution.utils.LocalData;
import ca.unb.mobiledev.reflexrevolution.utils.LoopMediaPlayer;

public class GameActivity extends AppCompatActivity {
    private final int TIME_BETWEEN_LOOPS = 1000;
    private final int FEEDBACK_DURATION = 1500;
    @SuppressWarnings("FieldCanBeLocal")
    private final float MAX_SONG_SPEED = 1.75f;

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
    private float maxScore;
    private float extraDuration;

    private LoopMediaPlayer musicPlayer;
    private SoundPool soundPool;
    private int scoreSfx;
    private int loseSfx;
    private float sfxVolume;

    private int score;
    private boolean success;
    private boolean isGamePaused = false;
    private boolean isTimerDelayed = false;
    private boolean isDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        LocalData.initialize(GameActivity.this);

        // Retrieve game mode and difficulty
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gameMode = (GameMode)extras.get("GameMode");
            difficulty = (Difficulty)extras.get("Difficulty");
        }
        else {
            // Pick defaults if not given in extras
            gameMode = GameMode.CLASSIC;
            difficulty = Difficulty.INTERMEDIATE;
        }
        maxScore = difficulty.getMaxScore();
        extraDuration = difficulty.getExtraDuration();

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
            public void onFinish() { if (!isDestroyed) gameLoop(); }
        };
        // Set up the feedback timer (used as delay for showing feedback)
        feedbackTimer = new CountDownTimer(FEEDBACK_DURATION, 500) {
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
    private void setMediaPlayers() {
        // Create sound pool for sound effects
        AudioAttributes audioAttributes = new AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool
                .Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        // Load sound effects
        scoreSfx = soundPool.load(this, R.raw.score, 1);
        loseSfx = soundPool.load(this, R.raw.lose, 1);
        sfxVolume = LocalData.getValue(LocalData.Value.VOLUME_SFX)/100f;

        // Create media player for background music
        float musicVolume = LocalData.getValue(LocalData.Value.VOLUME_MUSIC)/100f;
        musicPlayer = LoopMediaPlayer.create(this, R.raw.game_music);
        musicPlayer.setVolume(musicVolume);
        updateMusicSpeed();
    }

    // Properly handle stopping all media players
    private void stopMediaPlayers() {
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
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
        // Start the timer animation
        instructionTimerAnimation.setDuration(getTimerDuration());
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

        // Start instruction timer
        newTimer();

        if (!isGamePaused) {
            updateMusicSpeed();
            currentInstruction.playVoiceCommand();
            currentInstruction.enable();
        }
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
        soundPool.play(scoreSfx, sfxVolume, sfxVolume, 0, 0, 1);

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
        soundPool.play(loseSfx, sfxVolume, sfxVolume, 0, 0, 1);

        // Set success state before calling next instruction
        success = false;
        nextInstruction();
    }

    // Stop timer and clear UI, wait one second, then start the game loop (in resetTimer)
    private void nextInstruction(){
        instructionTimerAnimation.cancel();
        if (currentInstruction != null) currentInstruction.disable();
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
        soundPool.play(loseSfx, sfxVolume, sfxVolume, 0, 0, 1);
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
        if (musicPlayer != null) musicPlayer.setPlaybackSpeed(getMusicMultiplier());
    }

    // Scale the duration linearly with the score
    private int getTimerDuration() {
        int minDuration = currentInstruction.getMinDuration();
        return (int)((1 - score/maxScore) * extraDuration) + minDuration;
    }

    // Scale the music speed linearly with the score
    private float getMusicMultiplier() {
        return (score/maxScore) * (MAX_SONG_SPEED - 1) + 1;
    }

    private void pauseGame() {
        if (musicPlayer != null) musicPlayer.pause();
        if (currentInstruction != null) currentInstruction.disable();
        instructionTimerAnimation.pause();
    }

    private void resumeGame() {
        isDestroyed = false;
        if (musicPlayer != null) musicPlayer.restart();
        if (currentInstruction != null) currentInstruction.enable();
        if (!isGamePaused) instructionTimerAnimation.resume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isGamePaused) resumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentInstruction = null;
        instructionTimerAnimation.cancel();
        resetTimer.cancel();
        feedbackTimer.cancel();
        stopMediaPlayers();
    }

    @Override
    public void onBackPressed(){
        isDestroyed = true;
        super.onBackPressed();
    }
}