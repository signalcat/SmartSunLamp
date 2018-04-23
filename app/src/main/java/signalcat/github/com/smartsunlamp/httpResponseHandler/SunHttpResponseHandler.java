package signalcat.github.com.smartsunlamp.httpResponseHandler;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import signalcat.github.com.smartsunlamp.Models.SunTime;

/**
 * Created by hezhang on 4/19/18.
 */

public class SunHttpResponseHandler extends JsonHttpResponseHandler {
    private SunTime sunTime;
    private Runnable runnable;

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        Log.d("HttpResponse", "Failed");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONObject sunObject = response;
        sunTime.fromJSON(sunObject);
        if (runnable != null) {
            runnable.run();
        }
    }

    public SunHttpResponseHandler(SunTime sunTime, Runnable runnable){
        super();
        this.sunTime = sunTime;
        this.runnable = runnable;
    }
}

