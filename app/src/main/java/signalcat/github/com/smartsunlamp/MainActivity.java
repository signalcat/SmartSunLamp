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
    final String BASE_URL = "http://192.168.1.12/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOn = findViewById(R.id.button_on);
        Button btnOff = findViewById(R.id.button_off);
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCmd("on");
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCmd("off");
            }
        });
    }

    public void sendCmd(String cmd){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                BASE_URL + cmd,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                    {
                        Log.d("HttpResponse", "Failed");
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
//                        Gson gson = new GsonBuilder().create();
//                        Lamp lamp = gson.fromJson(response, Lamp.class);
                        Toast.makeText(getApplicationContext(), "Get response!", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
