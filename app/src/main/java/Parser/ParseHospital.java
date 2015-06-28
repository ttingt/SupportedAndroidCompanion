package Parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashSet;
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
        String nearestHospitalAddress = "";
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
//                    nearestHospitalAddress = a.getJSONObject("location").getString("address");
                    nearestHospitalName = a.getString("name");
//                    Log.i("testParse", nearestHospitalName);
                    Log.i("testParse", nearestHospitalName);

                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return "WOOOT";
    }
}
