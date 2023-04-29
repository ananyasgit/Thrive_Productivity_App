package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);
        lottieAnimationView.animate().translationX(-3000).setDuration(5000).setStartDelay(4000);
        TextView textView = findViewById(R.id.textView);
        textView.animate().translationX(3000).setDuration(5000).setStartDelay(4000); // Set a longer delay for the textView animation

        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(6000); // Adjust the sleep time to match the combined animation time
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent=new Intent(SplashActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();

    }
}
