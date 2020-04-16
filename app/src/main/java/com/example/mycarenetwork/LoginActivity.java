package com.example.mycarenetwork;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView loginTextView;
    private TextView registerTextView;
    private EditText textEmail;
    private EditText textPassword;
    private FirebaseAuth mAuth;

    private static final String TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        loginTextView = (TextView) findViewById(R.id.loginBtn);
        textEmail = findViewById(R.id.txtEmail);
        textPassword = findViewById(R.id.txtPassword);

        loginTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Toast toast = Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG);
//                toast.show();
//                Intent intent = new Intent(LoginActivity.this, AddProviderActivity.class);
//                startActivity(intent);
                signIn(textEmail.getText().toString(),textPassword.getText().toString());
                return true;
            }
        });

        registerTextView = (TextView) findViewById(R.id.registerBtn);

        registerTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Logged in successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException noUserPresent) {
                                Toast.makeText(LoginActivity.this, "Account with email does not exist!",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                Toast.makeText(LoginActivity.this, "Password Incorrect!",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
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

        return valid;
    }
}
