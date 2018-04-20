package signalcat.github.com.smartsunlamp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import signalcat.github.com.smartsunlamp.Models.SunTime;
import signalcat.github.com.smartsunlamp.httpResponseHandler.SunHttpResponseHandler;

public class SunActivity extends AppCompatActivity {

    private SunTime sunTime;
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun);

        sunTime = new SunTime();
        Button btnGetSunTime = findViewById(R.id.btn_getSunTime);
        //final TextView tvSunRiseTime = findViewById(R.id.tv_sunRiseTime);


        btnGetSunTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSunTime();
            }
        });

    }

    public void getSunTime(){
        SunHttpResponseHandler handler = new SunHttpResponseHandler(sunTime);
        client.get("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400", handler);
    }

}
