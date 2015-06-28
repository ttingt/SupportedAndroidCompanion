package com.example.hazai.supported;

import android.app.Activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    private String phoneNumber = "12062519197";
    public String currentLocation = "SSSSSSSSSSSSSSSSSSSSSS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSendSMS = (Button) findViewById(R.id.goToText);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentLocation();
                sendSMS(phoneNumber, "My Location is at " + currentLocation);
                Log.i("texting", currentLocation);

                       /*here i can send message to emulator 5556. In Real device
                                                               you can change number  */

            }
        });
        Button btmLoc = (Button) findViewById(R.id.goToText);
        btmLoc.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                currentLocation();
                sendSMS(phoneNumber, "My Location is at " + currentLocation);
                Log.i("texting", currentLocation);

                       /*here i can send message to emulator 5556. In Real device
                                                               you can change number  */

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

//    private String getCurrentLocation() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                double lat = location.getLatitude();
//                double lon = location.getLongitude();
//                currentLocation = Double.toString(lat) + ", " + Double.toString(lon);
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            public void onProviderEnabled(String provider) {
//            }
//
//            public void onProviderDisabled(String provider) {
//            }
//        };
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        return currentLocation;
//    }

    public void currentLocation() {
        String provider = LocationManager.GPS_PROVIDER;
        final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                double myLat = location.getLatitude();
                double myLon = location.getLongitude();
                currentLocation = Double.toString(myLat) + ", " + Double.toString(myLon);

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
    }
}
