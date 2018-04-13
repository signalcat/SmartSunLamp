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
 * This is a custom httpResponseHandler
 */

public class LampHttpResponseHandler extends JsonHttpResponseHandler
{
    private Lamp lamp;

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        Log.d("HttpResponse", "Failed");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
    {
        JSONObject lampObject = response;
        // Parse JSON and store date in the lamp object
        lamp.fromJSON(lampObject);

    }

    public LampHttpResponseHandler(Lamp lamp) {
        super();
        this.lamp = lamp;
    }

}
