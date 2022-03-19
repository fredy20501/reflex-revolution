package ca.unb.mobiledev.reflexrevolution.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.instructions.DialInstruction;
import ca.unb.mobiledev.reflexrevolution.instructions.Instruction;
import ca.unb.mobiledev.reflexrevolution.instructions.TypeInstruction;
import ca.unb.mobiledev.reflexrevolution.utils.Difficulty;
import ca.unb.mobiledev.reflexrevolution.utils.GameMode;
import ca.unb.mobiledev.reflexrevolution.utils.InstructionManager;
import ca.unb.mobiledev.reflexrevolution.utils.LoopMediaPlayer;

public class GameActivity extends AppCompatActivity {
    //Time in ms
    //Could get rid of this and do random intervals or scale it off of score
    private final int TIME_BETWEEN_LOOPS = 1000;

    private TextView timeText;
    private TextView scoreText;
    private LinearLayout layout; //Layout we should add new UI elements to

    private CountDownTimer instructionTimer;
    private CountDownTimer resetTimer;
    private Instruction currentInstruction;
    private InstructionManager instructionManager;
    private GameMode gameMode;
    private Difficulty difficulty;

    private MediaPlayer scorePlayer;
    private MediaPlayer losePlayer;
    private LoopMediaPlayer musicPlayer;

    private int timeCount;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        //Retrieve game mode and difficulty
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            gameMode = (GameMode)extras.get("GameMode");
            difficulty = (Difficulty)extras.get("Difficulty");
        }

        timeText = findViewById(R.id.timerText);
        scoreText = findViewById(R.id.currentScoreText);
        layout = findViewById(R.id.layout);
        score = 0;

        instructionManager = new InstructionManager(layout, new Instruction.Callback() {
            @Override
            public void onSuccess() { instructionSuccess(); }
            @Override
            public void onFailure() { endGame(); }
        });
        instructionManager.generateInstructions(gameMode);

        resetTimer = new CountDownTimer(TIME_BETWEEN_LOOPS, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() { gameLoop(); }
        };

        setMediaPlayers();
        updateTimerText();
        updateScoreText();
        resetTimer();
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

        //Allow losePlayer to continue playing when the activity is closed
        //but stop it properly once it finishes playing
        losePlayer.setOnCompletionListener(v -> {
            losePlayer.stop();
            losePlayer.release();
            losePlayer = null;
        });
    }

    //Properly handle stopping all media players
    //Lose player will be handled by its onCompletionListener
    private void stopMediaPlayers(){
        scorePlayer.stop();
        scorePlayer.release();
        scorePlayer = null;

        musicPlayer.release();
        musicPlayer = null;
    }

    private void updateTimerText(){
        timeText.setText(getString(R.string.timerLabel, timeCount));
    }

    private void updateScoreText(){
        scoreText.setText(getString(R.string.scoreLabel, score));
    }

    //Stop current timer then call game loop after one second
    private void resetTimer() {
        if (instructionTimer != null) instructionTimer.cancel();
        if (resetTimer != null) {
            resetTimer.cancel();

            //Wait some delay before calling game loop
            resetTimer.start();
        }
    }

    //Get new timer count, then start it by "resuming"
    private void newTimer() {
        timeCount = scaleTimerFromScore();
        //If you need to type or dial, add extra time
        if(currentInstruction instanceof TypeInstruction) timeCount += 1000;
        else if(currentInstruction instanceof DialInstruction) timeCount += 2000;
        startTimer();
    }

    //"Resumes" timer by creating a new timer starting at timeCount
    //Might be off by 100ms because I can only get
    private void startTimer() {
        //Check that there is not already a timer running
        if (instructionTimer != null) instructionTimer.cancel();
        instructionTimer = new CountDownTimer(timeCount, 100) {
            @Override
            //onTick() is also called as soon as the counter starts, so call timeCount-- after updateText()
            public void onTick(long l) {
                updateTimerText();
                timeCount-= 100;
            }

            @Override
            //When timer hits zero, signal the instruction to take action
            public void onFinish() {
                currentInstruction.timerFinished();
            }
        }.start();
    }

    //Prepare and start new instruction loop
    private void gameLoop() {
        currentInstruction = instructionManager.getInstruction();
        currentInstruction.init();
        currentInstruction.display();
        currentInstruction.enable();
        currentInstruction.playVoiceCommand();

        updateMusicSpeed();
        newTimer();
    }

    //Clear all added UI elements
    private void resetUI() {
        layout.removeAllViews();
    }

    private void instructionSuccess() {
        //Play success sound effect
        scorePlayer.start();

        //Update score
        score++;
        updateScoreText();

        //Stop timer and clear UI, wait one second, then start the game loop (in resetTimer)
        currentInstruction.disable();
        resetUI();
        resetTimer();
    }

    //Ends the game, sending score and game options to the Game Over screen
    private void endGame(){
        // Stop music and play lose sound effect
        losePlayer.start();
        stopMediaPlayers();

        if (instructionTimer != null) instructionTimer.cancel();
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

    //Scuffed function that will give a scaled timer based on score.
    //Starts at ~3000ms and min value is ~1000ms
    //I literally came up with this from playing around in desmos so we might want to rework this later
    //Probably adjust this based on difficulty later
    private int scaleTimerFromScore() {
        return (int)Math.pow(2, -0.04*score + 11) + 1000;
    }

    //Returns a value between 0.5 and 1.5 based on the time the user has to complete the instruction
    //Scales linearly, with 0.5 as 5000ms, 1 as 3000ms, and 1.5 as 1000ms
    private float scaleSongSpeedFromTimer() {
        return (float)clamp(-(scaleTimerFromScore() - 3000.0)/4000.0 + 1, 0.5, 1.5);
    }

    //Clamp function using double for maximum compatibility
    private double clamp(double val, double min, double max){
        if(val > max) return max;
        else if (val < min) return min;
        return val;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Resume background music
        if (musicPlayer != null) musicPlayer.restart();
        // Re-enable listeners
        if (currentInstruction != null) currentInstruction.enable();
        // Restart the timer
        if (instructionTimer != null) startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Pause background music
        if (musicPlayer != null) musicPlayer.pause();
        // Disable listeners
        if (currentInstruction != null) currentInstruction.disable();
        // Stop the timer
        if (instructionTimer != null) instructionTimer.cancel();
    }
}