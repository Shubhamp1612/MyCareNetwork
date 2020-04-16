package com.example.mycarenetwork;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddProviderActivity extends AppCompatActivity {

    private static final String TAG = "Add Provider Activity";
    private CardView addProviderButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private EditText textproviderName;
    private EditText textProviderPhone;
    private EditText textProviderEmail;
    private String textProviderAddress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprovider);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        addProviderButton = findViewById(R.id.addProviderCard);
        textproviderName = findViewById(R.id.txtProviderName);
        textProviderEmail = findViewById(R.id.txtProviderEmail);
        textProviderPhone = findViewById(R.id.txtProviderPhone);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

//        textProviderAddress = (EditText) findViewById(R.id.autocomplete_fragment);

        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        EditText ed = (EditText) autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);

        ed.setHint("Provider Address");


// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                textProviderAddress = place.getAddress();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        addProviderButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Toast toast = Toast.makeText(AddProviderActivity.this, "Adress is: " + textProviderAddress, Toast.LENGTH_LONG);
//                toast.show();

                Map<String, Object> insuranceProvider = new HashMap<>();
                insuranceProvider.put("providerName", textproviderName.getText().toString());
                insuranceProvider.put("providerPhone", textProviderPhone.getText().toString());
                insuranceProvider.put("providerEmail", textProviderEmail.getText().toString());
                insuranceProvider.put("providerAddress", textProviderAddress);
                DocumentReference documentReference = fStore.collection("insuranceProviders").document(textproviderName.getText().toString());
                documentReference
                        .set(insuranceProvider)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast toast = Toast.makeText(AddProviderActivity.this, "Provider '" + textproviderName.getText().toString() + "' added successfully!" ,Toast.LENGTH_LONG);
                                toast.show();
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                Intent intent = new Intent(AddProviderActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                return true;
            }
        });


    }
}
