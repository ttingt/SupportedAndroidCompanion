package Parser;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by hazai on 27/06/15.
 */
public class ParseHospital {
    public ParseHospital(){

    }

    public String parse(String jSONOfPlaces) {
        String nearestHospitalName = "";
        int nearestDistance = 100000;
        JSONTokener token = new JSONTokener(jSONOfPlaces);
        try {
            JSONObject obj = new JSONObject(token);

            JSONArray venues = obj.getJSONObject("response").getJSONArray("venues");
            for (int x = 0; x < venues.length();x++) {
                JSONObject a = venues.getJSONObject(x);
                int distance = a.getJSONObject("location").getInt("distance");
                if (distance < nearestDistance){
                    nearestDistance = distance;
                    nearestHospitalName = a.getString("name");
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return nearestHospitalName;
    }
    public double parseLat(String jSONOfPlaces) {
        double nearestHospitalLat = 0;
        int nearestDistance = 100000;
        JSONTokener token = new JSONTokener(jSONOfPlaces);
        try {
            JSONObject obj = new JSONObject(token);

            JSONArray venues = obj.getJSONObject("response").getJSONArray("venues");
            for (int x = 0; x < venues.length();x++) {
                JSONObject a = venues.getJSONObject(x);
                int distance = a.getJSONObject("location").getInt("distance");
                if (distance < nearestDistance){
                    nearestDistance = distance;
                    nearestHospitalLat = a.getJSONObject("location").getDouble("lat");
                    Log.i("lat", nearestHospitalLat + "");
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return nearestHospitalLat;
    }
    public double parseLon(String jSONOfPlaces) {
        double nearestHospitalLon = 0;
        int nearestDistance = 100000;
        JSONTokener token = new JSONTokener(jSONOfPlaces);
        try {
            JSONObject obj = new JSONObject(token);

            JSONArray venues = obj.getJSONObject("response").getJSONArray("venues");
            for (int x = 0; x < venues.length();x++) {
                JSONObject a = venues.getJSONObject(x);
                int distance = a.getJSONObject("location").getInt("distance");
                if (distance < nearestDistance){
                    nearestDistance = distance;
                    nearestHospitalLon = a.getJSONObject("location").getDouble("lng");
                    Log.i("lon", nearestHospitalLon + "");
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return nearestHospitalLon;
    }


}
