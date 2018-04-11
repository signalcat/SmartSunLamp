package signalcat.github.com.smartsunlamp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import signalcat.github.com.smartsunlamp.Models.Lamp;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String BASE_URL = "http://192.168.1.137:2015/";

        Button btnOn = findViewById(R.id.button_on);
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(
                        BASE_URL,
                        new TextHttpResponseHandler()
                        {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                            {
                                Log.d("HttpResponse", "Failed");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String response)
                            {
//                        Gson gson = new GsonBuilder().create();
//                        Lamp lamp = gson.fromJson(response, Lamp.class);
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
