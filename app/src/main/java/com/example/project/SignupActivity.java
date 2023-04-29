package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText editTextFirstname;
    EditText editTextLastname;
    EditText editTextPassword;
    EditText editTextEmail;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextFirstname = (EditText) findViewById(R.id.firstname);
        editTextLastname = (EditText) findViewById(R.id.lastname);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signupButtonClicked(View v) {
        String txtFirstName = editTextFirstname.getText().toString().trim();
        String txtLastName = editTextLastname.getText().toString().trim();
        String txtEmail = editTextEmail.getText().toString().trim();
        String txtPassword = editTextPassword.getText().toString().trim();
        if (txtFirstName.isEmpty()) {
            editTextFirstname.setError("Please enter first name");
            editTextFirstname.requestFocus();
        }
        if (txtLastName.isEmpty()) {
            editTextLastname.setError("Please enter last name");
            editTextLastname.requestFocus();
        }
        if (txtPassword.isEmpty() || txtPassword.length() < 6) {
            editTextPassword.setError("Please enter password");
            editTextPassword.requestFocus();
        }
        if (txtEmail.isEmpty()) {
            editTextEmail.setError("Please enter email");
            editTextEmail.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(txtEmail,txtPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(txtFirstName, txtLastName, txtEmail, txtPassword);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(SignupActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });


                        }
                        else{
                            Toast.makeText(SignupActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                });



    }
}
