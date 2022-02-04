package ca.unb.mobiledev.reflexrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerTestActivity extends AppCompatActivity {

    private Button resetButton;
    private TextView testText;
    private TextView resetText;
    private CountDownTimer timer;
    private int timeCount;
    private int resetCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_test);

        testText = findViewById(R.id.timerText);
        resetText = findViewById(R.id.resetText);
        resetButton = findViewById(R.id.resetButton);
        resetCount = 0;

        updateResetText();
        newTimer();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void updateTimerText(){
        testText.setText("Timer: " + timeCount);

    }

    private void updateResetText(){
        resetText.setText("Resets: " + resetCount);
    }

    private void resetTimer(){
        timer.cancel();
        resetCount++;
        updateResetText();
        newTimer();
    }

    //create a new timer that counts down from 5, and closes the activity once it hits 0
    private void newTimer(){
        timeCount = 5;
        timer = new CountDownTimer(5000, 1000) {
            @Override
            //onTick() is also called as soon as the counter starts, so call timeCount-- after updateText()
            public void onTick(long l) {
                updateTimerText();
                timeCount--;
            }

            @Override
            //When timer hits zero, start GameOverActivity and pass it the "score"
            public void onFinish() {
                Intent intent = new Intent(TimerTestActivity.this, GameOverActivity.class);
                intent.putExtra("Score", resetCount);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
