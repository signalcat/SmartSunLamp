package signalcat.github.com.smartsunlamp.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hezhang on 4/19/18.
 */

public class SunTime {

    private String sunRise;
    private String status;
    private String sunSet;
    private float latitude;
    private float longitude;

    public String getStatus() {
        return status;
    }

    public String getSunRise() {
        return sunRise;
    }

    public String getSunSet() { return sunSet; }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public SunTime() {
        sunRise = "";
        sunSet = "";
        status = "";
        latitude = 0;
        longitude = 0;
    }

    public void fromJSON(JSONObject sunObject){
        try{
            sunObject = sunObject.getJSONObject("results");
            sunRise = sunObject.getString("sunrise");
            sunSet = sunObject.getString("sunset");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
