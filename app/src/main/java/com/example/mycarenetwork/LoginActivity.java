package com.example.mycarenetwork;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView loginTextView;
    private TextView registerTextView;
    private static final String TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginTextView = (TextView) findViewById(R.id.loginBtn);

        loginTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Toast toast = Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG);
//                toast.show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });

        registerTextView = (TextView) findViewById(R.id.registerBtn);

        registerTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Toast toast = Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG);
//                toast.show();
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }
}
