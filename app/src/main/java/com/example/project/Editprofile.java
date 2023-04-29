package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Editprofile extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private ImageView mProfileImageView;
    private Button mSaveChangesButton;

    private Uri mProfileImageUri;
    private Bitmap mProfileImageBitmap;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Find views by ID
        mNameEditText = findViewById(R.id.edit_profile_name);
        mEmailEditText = findViewById(R.id.edit_profile_email);
        mPasswordEditText = findViewById(R.id.edit_profile_password);
        mProfileImageView = findViewById(R.id.edit_profile_image);
        mSaveChangesButton = findViewById(R.id.edit_profile_save_btn);

        // Set click listener for the profile image view
        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to select a profile image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        // Set click listener for the save changes button
        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes to the user's profile
                saveChanges();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI and display it in the image view
            mProfileImageUri = data.getData();
            try {
                mProfileImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mProfileImageUri);
                mProfileImageView.setImageBitmap(mProfileImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveChanges() {
        // Get the entered name, email, and password
        String name = mNameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        // Check if any fields are empty
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

                    // Create a map with the user's data
        Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("email", email);

            // Update the user's data in Firestore
            DocumentReference userRef = mFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
            userRef.update(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User document updated");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error updating user document", e);
                        }
                    });

            // Update the user's password in Firebase Auth
            mAuth.getCurrentUser().updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated");
                            } else {
                                Log.e(TAG, "Error updating user password", task.getException());
                            }
                        }
                    });

            // Upload the profile image to Firebase Storage
            if (mProfileImageUri != null) {
                StorageReference imageRef = mStorageRef.child("users/" + mAuth.getCurrentUser().getUid() + "/profile.jpg");
                UploadTask uploadTask = imageRef.putFile(mProfileImageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Profile image uploaded");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error uploading profile image", e);
                    }
                });
            }

            // Display a toast message to indicate that changes were saved
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
        }
}
