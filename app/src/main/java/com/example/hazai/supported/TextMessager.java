package com.example.hazai.supported;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;
import android.os.Handler;
import android.telephony.SmsManager;

public class TextMessager extends Activity {

    private static final int MAX_LOCATION_TRIES = 2;
    private static final int MAX_SEND_TRIES = 15;
    private static final int DICT_SENT_MSG_INDEX = 2; // Fixed array index for pebble watch to expect msg to be stored at in the communication's dictionary
    private static final String SENT_INTENT_STR = "sent";
    private static final int DICT_DELIVERED_MSG_INDEX = DICT_SENT_MSG_INDEX + 1;
    private static final String DLVR_INTENT_STR = "delivered";

    private static final UUID SUPPORTED_PEBBLE_APP_UUID = UUID.fromString("1b2a4b25-e8af-44c2-a53a-2d63f80aceca"); // TODO: UPDATE THIS PLEASE
    private PebbleKit.PebbleDataLogReceiver mDataLogReceiver = null;
    private String currentLocation = null;
    private static final String HELPMESSAGE = "I am in trouble. Please send help to ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_messager);

        //fake method for testing
        sendSMSMessages("12062519197");

        final Handler handler = new Handler();
        mDataLogReceiver = new PebbleKit.PebbleDataLogReceiver(SUPPORTED_PEBBLE_APP_UUID) {

            //@Override
            public void receiveData(Context context, UUID suppUuid, String phoneNumber) {
                // Send preset text message to given phoneNumber

                sendSMSMessages(phoneNumber);

            }
        };

        PebbleKit.registerDataLogReceiver(this, mDataLogReceiver);
        PebbleKit.requestDataLogsForApp(this, SUPPORTED_PEBBLE_APP_UUID);
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
//        final Handler handler = new Handler();
//        mDataLogReceiver = new PebbleKit.PebbleDataLogReceiver(SUPPORTED_PEBBLE_APP_UUID) {
//
//            //@Override
//            public void receiveData(Context context, UUID suppUuid, String phoneNumber) {
//                // Send preset text message to given phoneNumber
//
//                sendSMSMessages(phoneNumber);
//
//            }
//        };
//
//        PebbleKit.registerDataLogReceiver(this, mDataLogReceiver);
//        PebbleKit.requestDataLogsForApp(this, SUPPORTED_PEBBLE_APP_UUID);
    }

    private void sendSMSMessages(String phoneNumber) {
        boolean success = false;
        int attempts = 0;
        String address = getCurrentLocation();
        boolean sendStatus = false;

        while (!success && attempts < MAX_SEND_TRIES) {
            try {
                SmsManager smsm = SmsManager.getDefault();

                Intent sentIntent = new Intent(SENT_INTENT_STR);
                PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(),0,sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent dlvrIntent = new Intent(DLVR_INTENT_STR);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(),0,dlvrIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (address == null  ) {
                    address = "me. My location could not be determined at this time";
                }
                smsm.sendTextMessage(phoneNumber, null, HELPMESSAGE + address, sentPI, deliveredPI);
                success = true;
                smsSentBroadcastReceiver();
                smsDeliveredBroadcastReceiver();
            } catch (Exception e) {
                attempts++;
            }
        }
        if (!success) alertPebbleSMSSent(false);
    }

    // SMS send status (pending intent) broadcast receiver
    // Receives message notifying whether sms was sent successfully or not
    private void smsSentBroadcastReceiver() {
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean result = false;
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        result = true;
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        result = false;
                        break;
                }
                alertPebbleSMSSent(result);
            }
        }, new IntentFilter(SENT_INTENT_STR));
    }

    // SMS delivery status (pending intent) broadcast receiver
    // Receives message notifying whether sms was delivered successfully or not
    private void smsDeliveredBroadcastReceiver() {
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean result = true;
                alertPebbleSMSDelivered(result);
            }
        }, new IntentFilter(DLVR_INTENT_STR));
    }

    // To be called once SMS to emergency contacts sent successfully.
    // Alerts Pebble Watch App that sms were sent successfully.
    private void alertPebbleSMSSent(boolean smsSentSuccess) {
        // TODO: implement
        String msg = "false";
        if (smsSentSuccess) {
            msg = "true";
        }
        PebbleDictionary data = new PebbleDictionary();
        data.addString(2, msg);
        PebbleKit.sendDataToPebble(getApplicationContext(), SUPPORTED_PEBBLE_APP_UUID, data);
    }

    // To be called once SMS delivered to emergency contacts
    // Alerts Pebble Watch App that sms were delivered successfully.
    private void alertPebbleSMSDelivered(boolean smsDeliverySuccess) {
        // TODO: implement
        String msg = "false";
        if (smsDeliverySuccess) {
            msg = "true";
        }
        PebbleDictionary data = new PebbleDictionary();
        data.addString(3, msg);
        PebbleKit.sendDataToPebble(getApplicationContext(), SUPPORTED_PEBBLE_APP_UUID, data);
    }

    // Get current location from phone GPS
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
