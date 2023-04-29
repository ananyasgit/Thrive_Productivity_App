package com.example.project;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PomodoroActivity extends AppCompatActivity {

    private int currentProgress = 0;
    private ProgressBar progressBar;
    private Button startProgressButton;
    private Button resetButton;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        progressBar = findViewById(R.id.progressBar);
        startProgressButton = findViewById(R.id.startProgress);
        resetButton = findViewById(R.id.resetProgress);
        timerTextView = findViewById(R.id.timer);

        countDownTimer = new CountDownTimer(25*60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentProgress++;

                if (currentProgress <= 60) {
                    // update the progress bar for the current minute
                    progressBar.setProgress(currentProgress);
                } else {
                    // reset the progress bar for the next minute
                    currentProgress = 1;
                    progressBar.setProgress(currentProgress);
                }

                progressBar.setMax(25*60); // set the maximum progress to 60 (1 minute)

                // update the timer text view with the remaining time
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Time's up! Take a break.", Toast.LENGTH_LONG).show();
                countDownTimer = new CountDownTimer(5*60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        currentProgress++;

                        if (currentProgress <= 60) {
                            // update the progress bar for the current minute
                            progressBar.setProgress(currentProgress);
                        } else {
                            // reset the progress bar for the next minute
                            currentProgress = 1;
                            progressBar.setProgress(currentProgress);
                        }

                        progressBar.setMax(5*60); // set the maximum progress to 60 (1 minute)

                        // update the timer text view with the remaining time
                        long minutes = millisUntilFinished / 60000;
                        long seconds = (millisUntilFinished / 1000) % 60;
                        timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "Break's over! Back to work.", Toast.LENGTH_LONG).show();
                        resetTimer();
                    }
                }.start();
            }
        };

        startProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.start();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        timerTextView.setText("25:00"); // set the initial value for the timer text view
    }

    private void resetTimer() {
        countDownTimer.cancel();
        currentProgress = 0;
        progressBar.setProgress(currentProgress);
        timerTextView.setText("25:00");
    }
}

