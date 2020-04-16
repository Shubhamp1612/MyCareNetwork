package com.example.mycarenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private GridLayout mainGrid;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private String Latitude;
    private String Longitude;
    private FirebaseUser user;
    private FirebaseFirestore fStore;
    private static final String TAG = "MainActivity";

    public MainActivity() {
    }

    public interface OntaskCompleted{
        void onSuccess(String Latitude, String Longitude);
        void onfail();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //Hide Action Bar title text

        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        mainGrid = findViewById(R.id.gridHandle);
        String userID = user.getUid();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG,"Location permission granted");
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Toast.makeText(MainActivity.this, "Location permission not granted",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserProfile(userID);
        getLocation(new OntaskCompleted(){

            @Override
            public void onSuccess(String Lat, String Long) {
                Toast.makeText(MainActivity.this, "Locaton is: " + Lat + "," + Long ,
                        Toast.LENGTH_SHORT).show();
                Latitude = Lat;
                Longitude = Long;
            }

            @Override
            public void onfail() {

            }
        });

        clickCardEvent(mainGrid);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Signed out successfully" ,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent) ;
        }
        else if(item.getItemId()==R.id.ManageInsuranceProviders){
            Intent intent = new Intent(this, ManageInsProvidersActivity.class);
            startActivity(intent) ;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickCardEvent(GridLayout mainGrid)
    {
        for(int i=0;i<mainGrid.getChildCount();i++)
        {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalIndex = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(MainActivity.this, "Clicked at index: " + finalIndex,
//                            Toast.LENGTH_SHORT).show();
                    if(finalIndex == 1)
                    {
                        Intent intent = new Intent(MainActivity.this,HospitalsActivity.class);
                        startActivity(intent);
                    }
                    else if(finalIndex == 3) //clicked SOS!!
                    {
                        String userID = user.getUid();

                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(MainActivity.this, "SMS permission granted!!",
                                    Toast.LENGTH_SHORT).show();

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage("+16692489672",null,"SOS message alert from location " + Latitude + "," + Longitude,null,null);
                            Toast.makeText(MainActivity.this, "message sent",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    MY_PERMISSIONS_REQUEST_SEND_SMS);

                            Toast.makeText(MainActivity.this, "SMS permission not granted!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void getUserProfile(String uid)
    {
        DocumentReference docRef = fStore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");

                        Toast.makeText(MainActivity.this, "Welcome: " + firstName + " " + lastName,
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void getLocation(final OntaskCompleted listener)
    {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            listener.onSuccess(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));
                            Log.d(TAG,"location is" + location.getLatitude() + "," + location.getLongitude());
//                            Toast.makeText(MainActivity.this, "Location permission granted:" +location.getLatitude() + "," + location.getLongitude(),
//                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
