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
import android.telephony.SmsManager;

public class TextMessager extends Activity {

    private static final int MAX_LOCATION_TRIES = 10;
    private static final int MAX_SEND_TRIES = 15;

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

                boolean success = false;
                int attempts = 0;

                while (!success && attempts < MAX_SEND_TRIES) {
                    try {
                        // Get the current location of the user
                        String address = null;
                        for (int i = 0; i < MAX_LOCATION_TRIES; i++) {
                            address = getLocation();
                            if (address) break;
                        }
                        // TODO: if address cannot be determined
                        if (address) {
                            SmsManager smsm = SmsManager.getDefault();
                            PendingIntent smsPI;
                            String SENT = "SMS_SENT";
                            smsPI = PendingIntent.getBroadcast(this,0,new Intent(SENT), 0);
                            smsm.sendTextMessage(phoneNumber, null, HELPMESSAGE + address, smsPI, null);
                            alertPebbleSMSSent();
                            success = true;
                        } else {
                            attempts++;
                        }
                    } catch (Exception e) {
                        attempts++;
                    }
                }

            }
        };

        PebbleKit.registerDataLogReceiver(this, mDataLogReceiver);
        PebbleKit.requestDataLogsForApp(this, SUPPORTED_PEBBLE_APP_UUID);
    }

    // To be called once SMS to emergency contacts sent successfully.
    // Alerts Pebble Watch App that sms were sent successfully.
    private void alertPebbleSMSSent() {
        // TODO: implement
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
