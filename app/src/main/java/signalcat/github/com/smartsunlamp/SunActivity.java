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
        //Button btnGetSunTime = findViewById(R.id.btn_getSunTime);
        final TextView tvSunRiseTime = findViewById(R.id.tv_sunRiseTime);

        getSunTime("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400", new Runnable() {
            @Override
            public void run() {
                tvSunRiseTime.setText(sunTime.getSunRise());
            }
        });

    }

    public void getSunTime(String url, Runnable runnable){
        SunHttpResponseHandler handler = new SunHttpResponseHandler(sunTime, runnable);
        client.get(url, handler);
    }

}
