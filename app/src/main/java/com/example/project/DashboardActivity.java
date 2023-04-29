package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    Button pomodBtn, resourcesbtn, moodbtn,calendarBtn, habitBtn, editProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        pomodBtn = findViewById(R.id.pomodoroBtn);
        resourcesbtn=findViewById(R.id.resourcesBtn);
        moodbtn=findViewById(R.id.moodBtn);
        calendarBtn = findViewById(R.id.calendarBtn);
        habitBtn = findViewById(R.id.habitBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        pomodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PomodoroActivity.class);
                startActivity(intent);
            }
        });

        resourcesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
                startActivity(intent);
            }
        });
        habitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, HabitTrackerActivity.class);
                startActivity(intent);
            }
        });
        moodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MoodTrackerActivity.class);
                startActivity(intent);
            }
        });
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, Editprofile.class);
                startActivity(intent);
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,Calender_Events.class);
                startActivity(intent);
            }
        });

    }
}
