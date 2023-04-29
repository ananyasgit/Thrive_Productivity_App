package com.example.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMood extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user = FirebaseAuth.getInstance().getUid();
    TextView title;
    TextView subtitle;
    TextView quote;
    TextView selectdate;
    TextView felt;
    TextView because;
    TextView descbox;
    ImageView calendar;
    Button happy;
    Button sad;
    Button neutral;
    Button angry;
    Button addmood;
    int moodid = MoodTrackerActivity.getmoodid()+1;
    int boolselect = -1;

    String moodtype;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        quote = findViewById(R.id.titlequote);

        selectdate = findViewById(R.id.selectdate);
        felt = findViewById(R.id.felt);
        because = findViewById(R.id.because);
        calendar = findViewById(R.id.calendar);
        descbox = findViewById(R.id.descbox);
        happy = findViewById(R.id.happy);
        sad = findViewById(R.id.sad);
        angry = findViewById(R.id.angry);
        addmood = findViewById(R.id.addmood);
        neutral = findViewById(R.id.neutral);

        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(
                        AddMood.this,
                        //android.R.style.Widget_Holo_ActionBar_Solid,
                        dateSetListener,
                        year, month, day);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                d.show();

            }
        });


        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                selectdate.setText(dayOfMonth + "/" + month + "/" + year);
                Log.d("AddMoodActivity", "date:" + dayOfMonth + "/" + month + "/" + year);
            }
        };
        happy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                moodtype = "happy";
                Toast.makeText(AddMood.this, "I feel happy", Toast.LENGTH_SHORT).show();
                if(boolselect == 1){
                    addmood.setEnabled(true);

                }

            }
        });
        sad.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                moodtype = "sad";
                Toast.makeText(AddMood.this, "I feel sad", Toast.LENGTH_SHORT).show();
                if(boolselect == 1){
                    addmood.setEnabled(true);



                }
            }
        });
        neutral.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                moodtype = "neutral";
                Toast.makeText(AddMood.this, "I feel neutral", Toast.LENGTH_SHORT).show();
                if(boolselect == 1){
                    addmood.setEnabled(true);

                }
            }
        });
        angry.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                moodtype = "angry";
                Toast.makeText(AddMood.this, "I feel angry", Toast.LENGTH_SHORT).show();
                if(boolselect == 1){
                    addmood.setEnabled(true);

                }
            }
        });


        selectdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolselect =1;
                if(moodtype !=null){
                    addmood.setEnabled(true);

                }

            }
        });




        addmood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.addmood) {
                    createMood(user, selectdate.getText().toString(), descbox.getText().toString(), moodtype, moodid);
                    moodid++;

                    Intent j = new Intent(getApplicationContext(),MoodTrackerActivity.class);
                    startActivity(j);
                }
            }
        });



    }


    public void createMood(String UID, String date, String desc, String mood, final Integer moodnum) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", moodnum);
        user.put("date", date);
        user.put("description", desc);
        user.put("moodtype", mood);


        db.collection("mood").document().set(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.w("SUCCESS", "DocumentSnapshot added with ID:");
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("AddingMood Failed", "Error adding document", e);
            }
        });
    }
}