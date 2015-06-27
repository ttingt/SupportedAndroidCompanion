package com.example.hazai.supported;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.getpebble.android.kit.PebbleKit;
import java.util.UUID;
import android.os.Handler;

public class TextMessager extends Activity {

    private static final UUID SUPPORTED_PEBBLE_APP_UUID = UUID.fromString("TO-BE-UPDATED"); // TODO: UPDATE THIS PLEASE
    private PebbleKit.PebbleDataLogReceiver mDataLogReceiver = null;
    private static String currentLocation = null;
    private static final String HELPMESSAGE = "I am in trouble. Please send help to " + currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_messager);
        getCurrentLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDataLogReceiver != null) {
            unregisterReceiver(mDataLogReceiver);
            mDataLogReceiver = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        mDataLogReceiver = new PebbleKit.PebbleDataLogReceiver(SUPPORTED_PEBBLE_APP_UUID) {

            //@Override
            public void receiveData(Context context, UUID suppUuid, String phoneNumber) {
                // Send preset text message to given phoneNumber


            }
        };

        PebbleKit.registerDataLogReceiver(this, mDataLogReceiver);
        PebbleKit.requestDataLogsForApp(this, SUPPORTED_PEBBLE_APP_UUID);
    }

    // Get current location from phone GPS
    private void getCurrentLocation() {
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
    }


}
