package com.merchant.cngmeter;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import android.os.Handler ;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 7/26/2016.
 */
public class TabClass extends AppCompatActivity{
    private Handler handler = new Handler();
    Thread t ;
    GPSTracker gps;
    double x1=0.0,y1=0.0,x2=0.0,y2=0.0;
    double distance =0.0;
    double fare = 40.0;
    EditText editText ;
    EditText timeText;
    TextView fareText;
    static final Double Radius = 6371.00;
    Timer timer = new Timer();
    int iClicks = 0 ;
    int totalTime;
    CheckBox waitingCheckbox ;
    int temp =0;
    int flag = 1;
    Button fareButton ;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapactivitylayout);
        TabHost tabhost1 = (TabHost) findViewById(R.id.tHost);
        TabHost.TabSpec spec1 = tabhost1.newTabSpec("Map Mode");
        tabhost1.setup();
        spec1.setIndicator("Map Mode");
        spec1.setContent(R.id.layout1);
        tabhost1.addTab(spec1);
        spec1 = tabhost1.newTabSpec("Normal Mode");
        spec1.setIndicator("Normal Mode");
        spec1.setContent(R.id.layout2);
        fareButton = (Button)findViewById(R.id.fareId);
        editText = (EditText)findViewById(R.id.distanceId);
        fareText = (TextView)findViewById(R.id.fareText);
        tabhost1.addTab(spec1);
        timeText = (EditText) findViewById(R.id.timeBox);
        waitingCheckbox = (CheckBox)findViewById(R.id.waitingBox);

        waitingCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // task to be done every 1000 milliseconds

                                timeText.setText(String.valueOf(totalTime=iClicks++));
                                if (waitingCheckbox.isChecked() == false) timer.cancel();
                            }
                        });

                    }



                }, 0, 60000);

            }
        });
    update();
        fareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(distance>2000){

                    fare = (fare+ distance*.012)+(totalTime*2);
                    fareText.setText(Double.toString(fare)+" tk");
                }


            }
        });

    }
    public void update() {

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                gps = new GPSTracker(TabClass.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {
                    x1 = x2;
                    y1 = y2;
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    x2 = latitude;
                    y2 = longitude;
                    distance =loc(x1, y1, x2, y2); //calculate distace

                    editText.setText(new DecimalFormat("##.###").format(distance));

                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    handler.postDelayed(this, 5000);
                }
                else
                {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    //gps.showSettingsAlert();
                    //gps.stopUsingGPS();
                }
            }
        };

        handler.postDelayed(r, 5000);

    }
    double loc(double lat1,double lon1,double lat2,double lon2){

        Location locationA = new Location("place one");


        locationA.setLatitude(x1);
        locationA.setLongitude(y1);

        Location locationB = new Location("place two ");

        locationB.setLatitude(x2);
        locationB.setLongitude(y2);

        double result =   locationA.distanceTo(locationB);

       if(result>700 && flag == 1){
           result = 0.0;
           flag= 0 ;
       }

        distance = distance + result;
        return  distance;
        /*double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        distance = distance +dist;
        return distance;*/

    }

}

