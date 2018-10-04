package com.merchant.cngmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class FirstActivity extends AppCompatActivity {


    public ImageButton cLbutton;
    public ImageButton  start;
    public EditText editText;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

       ImageView iview = (ImageView) findViewById(R.id.iView);

        iview.setImageResource(R.drawable.auto_rickshaw);

        start = (ImageButton) findViewById(R.id.startId);
        cLbutton = (ImageButton) findViewById(R.id.currentLocationbtn);
        editText = (EditText) findViewById(R.id.editTextId);
        cLbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(FirstActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Log.e("con", "Show something");
                    // \n is for new line
                   // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    Intent  intent = getIntent() ;
                   String location = gps.areaName;

                    editText.setGravity(Gravity.CENTER_HORIZONTAL);
                    editText.setText(location);
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    Log.e("gpsProblem", "Show something");
                    gps.showSettingsAlert();
                }

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FirstActivity.this,TabClass.class);
                startActivity(intent);
            }
        });



    }

}
