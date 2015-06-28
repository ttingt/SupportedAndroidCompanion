package com.example.hazai.supported;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.getpebble.android.kit.PebbleKit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {
    private String phoneNumber = "12062519197";
    public String currentLocation = "SSSSSSSSSSSSSSSSSSSSSS";
    /**
     * FourSquare URLs. You must complete the client_id and client_secret with values
     * you sign up for.
     */
    private static String FOUR_SQUARE_URL = "https://api.foursquare.com/v2/venues/explore";
    private static String FOUR_SQUARE_CLIENT_ID = "BPKIFJQC1JBXO2NGVROY5E30MTTGLBBSRORZFMYTTWCI2WHB";
    private static String FOUR_SQUARE_CLIENT_SECRET = "YQEOI4125F5KFVCIYWKABWATWZXFD25UL0VEN0LLIQWNPA1N";
    private String myGPS = "47.6492420,-122.3505970";

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
                new GetHospital().execute();
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

    private class GetHospital extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {

            String listOfPlaces = "";
            try {
                listOfPlaces = makeRoutingCall("https://api.foursquare.com/v2/venues/explore?ll=49.264865,-123.252782&" +
                        "client_id=BPKIFJQC1JBXO2NGVROY5E30MTTGLBBSRORZFMYTTWCI2WHB&" +
                        "client_secret=YQEOI4125F5KFVCIYWKABWATWZXFD25UL0VEN0LLIQWNPA1N&v=20150322&radius=2500&photos=1");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return listOfPlaces;
        }

        private String makeRoutingCall(String httpRequest) throws MalformedURLException, IOException {
            URL url = new URL(httpRequest);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            InputStream in = client.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String returnString = br.readLine();
            client.disconnect();
            return returnString;
        }

        protected void onPostExecute(String jSONOfPlaces) {

        }
    }

}
