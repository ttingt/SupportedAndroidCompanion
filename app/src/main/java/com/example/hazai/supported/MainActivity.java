package com.example.hazai.supported;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import com.getpebble.android.kit.PebbleKit;


public class MainActivity extends Activity {
    private String phoneNumber = "12062519197";
    public String currentLocation = "SSSSSSSSSSSSSSSSSSSSSS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to last known Pebble watch
        boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
        Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));
        // Detect pebble connection
        PebbleKit.registerPebbleConnectedReceiver(getApplicationContext(), new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(getLocalClassName(), "Pebble connected.");
            }
        });
        // Detect pebble disconnection
        PebbleKit.registerPebbleDisconnectedReceiver(getApplicationContext(), new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(getLocalClassName(), "Pebble disconnected.");
            }
        });
        Intent i = new Intent(MainActivity.this, TextMessager.class);

        Button btnSendSMS = (Button) findViewById(R.id.goToText);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getCurrentLocation();
                sendSMS(phoneNumber, "My Location is at " + currentLocation);
                Log.i("texting", currentLocation);

                       /*here i can send message to emulator 5556. In Real device
                                                               you can change number  */

            }
        });
        Button btnSetLocation = (Button) findViewById(R.id.goToText);
        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getCurrentLocation();
                sendSMS(phoneNumber, "My Location is at " + currentLocation);
                Log.i("texting", currentLocation);

                       /*here i can send message to emulator 5556. In Real device
                                                               you can change number  */

            }
        });

//        startActivity(i);
//        finish();

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

    private String getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                currentLocation = Double.toString(lat) + ", " + Double.toString(lon);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        return currentLocation;
    }


}
