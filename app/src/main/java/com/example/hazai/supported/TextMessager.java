package com.example.hazai.supported;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.getpebble.android.kit.PebbleKit;
import java.util.UUID;
import android.os.Handler;
import android.telephony.SmsManager;

public class TextMessager extends Activity {

    private static final int MAX_LOCATION_TRIES = 2;
    private static final int MAX_SEND_TRIES = 15;
    private static final String SENT_INTENT_STR = "sent";

    private static final UUID SUPPORTED_PEBBLE_APP_UUID = UUID.fromString("TO-BE-UPDATED"); // TODO: UPDATE THIS PLEASE
    private PebbleKit.PebbleDataLogReceiver mDataLogReceiver = null;

    private static final String HELPMESSAGE = "I am in trouble. Please send help to ";
    private String location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_messager);
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
                sendSMSMessages(phoneNumber);
            }
        };

        PebbleKit.registerDataLogReceiver(this, mDataLogReceiver);
        PebbleKit.requestDataLogsForApp(this, SUPPORTED_PEBBLE_APP_UUID);
    }

    private void sendSMSMessages(String phoneNumber) {
        boolean success = false;
        int attempts = 0;
        String address = getLocation();
        boolean sendStatus = false;

        while (!success && attempts < MAX_SEND_TRIES) {
            try {
                SmsManager smsm = SmsManager.getDefault();
                String sentIntent = new Intent(SENT_INTENT_STR);
                PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(),0,sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (address != null) {
                    address = "me. My location could not be determined at this time";
                }
                smsm.sendTextMessage(phoneNumber, sentPI, HELPMESSAGE + address, null, null);
                success = true;
                smsSentBroadcastReceiver();
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

    // To be called once SMS to emergency contacts sent successfully.
    // Alerts Pebble Watch App that sms were sent successfully.
    private void alertPebbleSMSSent(boolean smsSentSuccess) {
        // TODO: implement

    }

    // Get current location from phone GPS
    private String getLocation() {

        return "";
    }

}
