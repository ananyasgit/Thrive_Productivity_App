package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.number.IntegerWidth;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class WelcomeActivity extends AppCompatActivity {
    EditText userEmail;
    EditText userPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        userEmail = (EditText) findViewById(R.id.email);
        userPassword = (EditText) findViewById(R.id.password);
        progressBar=findViewById(R.id.progressBarlogin);
        mAuth= FirebaseAuth.getInstance();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("MAD Project");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Thrive", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Thrive", "Failed to read value.", error.toException());
            }
        });
    }

    public void onButtonSignupClicked(View view) {
        Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
        startActivity(intent);

    }

    public void onButtonLoginClicked(View view) {
        String email = userEmail.getText().toString().trim();
        String pass = userPassword.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Please enter a valid Email");
            userEmail.requestFocus();

        }
        if(userPassword.length()<6){
            userPassword.setError("Please enter atleast 6 character password");
            userPassword.requestFocus();

        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(WelcomeActivity.this,"User has successfully Signed In",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(WelcomeActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(WelcomeActivity.this,"User has failed Signed In",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
