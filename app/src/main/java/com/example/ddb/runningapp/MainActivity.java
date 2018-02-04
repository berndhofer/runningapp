package com.example.ddb.runningapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ddb.runningapp.background.LocationService;

public class MainActivity extends AppCompatActivity {
    private View mLayout;
    private final int PERMISSION_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
     //  getApplicationContext().deleteDatabase("LocationDB"); // delete here if there's a DB issue
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);
        startService(new Intent(this, LocationService.class));


        initOnClickListener();

    }

    private void initOnClickListener(){
        Button newBtn = (Button) findViewById(R.id.btnNew);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTracking();

            }
        });

        Button showBtn = (Button) findViewById(R.id.btnShow);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this,ViewC.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
            }
        });
    }

    private void showTracking() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start
            Snackbar.make(mLayout,
                    "Location permission is available. Starting Map.",
                    Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent( MainActivity.this,ViewB.class );
            startActivity(intent);
        } else {
            // Permission is missing and must be requested.
            requestingPermissions();
        }
        // END_INCLUDE(startCamera)
    }


    private void requestingPermissions(){

        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Location access is required to display the location.",
                    Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting location permission.",
                    Snackbar.LENGTH_LONG).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST);
        }
    }


}
