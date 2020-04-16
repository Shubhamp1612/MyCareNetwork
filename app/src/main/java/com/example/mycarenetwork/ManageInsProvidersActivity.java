package com.example.mycarenetwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class ManageInsProvidersActivity extends AppCompatActivity {

    private CardView addProviderCardView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageproviders);

        addProviderCardView = findViewById(R.id.addProviderCard);

        addProviderCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(ManageInsProvidersActivity.this, AddProviderActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}
