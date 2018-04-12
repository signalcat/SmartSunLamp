package signalcat.github.com.smartsunlamp.httpResponseHandler;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import signalcat.github.com.smartsunlamp.Models.Lamp;

/**
 * Created by hezhang on 4/11/18.
 */

public class LampHttpResponseHandler extends JsonHttpResponseHandler
{
    public Lamp lamp;
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        Log.d("HttpResponse", "Failed");
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
    {
        try{
            //JSONObject lampObject = response;
            //String brightness = lampObject.getString("brightness");
            lamp.fromJson(response);
            Log.d("BRIGHTNESS", String.valueOf(lamp.getBrightness()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public LampHttpResponseHandler() {
        super();
        lamp = new Lamp();
    }

}
