package com.example.mycarenetwork;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private TextView registerTextView;
    private EditText textFirstName;
    private EditText textLastName;
    private EditText textEmail;
    private EditText textPassword;
    private EditText textConfirmPassword;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //Hide Action Bar title text

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        registerTextView = findViewById(R.id.loginBtn);
        textFirstName = findViewById(R.id.txtFirstName);
        textLastName = findViewById(R.id.txtLastName);
        textEmail = findViewById(R.id.txtEmail);
        textPassword = findViewById(R.id.txtPassword);
        textConfirmPassword = findViewById(R.id.txtConfirmPassword);

                registerTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                createAccount(textEmail.getText().toString(),textPassword.getText().toString());
                return true;
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = textEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textEmail.setError("Required.");
            valid = false;
        } else {
            textEmail.setError(null);
        }

        String password = textPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            textPassword.setError("Required.");
            valid = false;
        } else {
            textPassword.setError(null);
        }

        String confirmPassword = textConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            textConfirmPassword.setError("Required.");
            valid = false;
        } else {
            textConfirmPassword.setError(null);
        }


        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            userId = mAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            user.put("firstName",textFirstName.getText().toString());
                            user.put("lastName",textLastName.getText().toString());
                            user.put("email",textEmail.getText().toString());
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"User Profile Created Succesfully");
                                    Toast toast = Toast.makeText(SignUpActivity.this, "Account and user profile successfully: ", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            });

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);

//                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
