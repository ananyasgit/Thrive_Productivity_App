package com.example.project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewHabit extends AppCompatActivity {

    private EditText habitName;
    private EditText habitEndDate;

    private EditText target;

    private Spinner trackingFrequency;
    private Button newHabit;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        constraintLayout = findViewById(R.id.linearLayout2);
//        constraintLayout.getBackground().setAlpha(50);
        Drawable background = constraintLayout.getBackground();
        if (background != null) {
            background.setAlpha(50);
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        habitName = findViewById(R.id.habit_name);
        target = findViewById(R.id.target);
        habitEndDate = findViewById(R.id.habit_end_date);
        trackingFrequency = findViewById(R.id.habit_frequency);
        newHabit = findViewById(R.id.add_new_habit);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.freq, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trackingFrequency.setAdapter(adapter);

        habitEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment(habitEndDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        newHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHabit();
                Intent i = new Intent(getApplicationContext(), HabitTrackerActivity.class);
                startActivity(i);
            }
        });
    }

    private void addHabit() {
        Map<String,String> habit = new HashMap<>();
        habit.put("Habit Name", habitName.getText().toString());
        habit.put("Target", target.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String start = sdf.format(Calendar.getInstance().getTime());
        habit.put("Start Date", start);
        habit.put("End Date", habitEndDate.getText().toString());
        habit.put("Frequency", trackingFrequency.getSelectedItem().toString());
        habit.put("Progress", "0");

        db.collection("habits").document().set(habit);
    }
}