package signalcat.github.com.smartsunlamp;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import signalcat.github.com.smartsunlamp.Models.SunTime;
import signalcat.github.com.smartsunlamp.httpResponseHandler.SunHttpResponseHandler;

public class SunActivity extends AppCompatActivity {

    private SunTime sunTime;
    private ImageView sunShape;
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun);

        sunTime = new SunTime();
        //Button btnGetSunTime = findViewById(R.id.btn_getSunTime);
        final TextView tvSunRiseTime = findViewById(R.id.tv_sunRiseTime);
        final TextView tvSunSetTime = findViewById(R.id.tv_sunSetTime);
        sunShape = findViewById(R.id.iv_sun);
        getSunTime("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400", new Runnable() {
            @Override
            public void run() {
                tvSunRiseTime.setText(sunTime.getSunRise());
                tvSunSetTime.setText(sunTime.getSunSet());
                float angle = getSunLocation(sunTime.getSunRise(),sunTime.getSunSet());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Path path = new Path();
                    path.arcTo(20f,50f,600f,600f,180f,180f,true);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(sunShape, View.X, View.Y, path);
                    animator.setDuration(1000);
                    animator.start();

                }

            }
        });

    }

    public void getSunTime(String url, Runnable runnable){
        SunHttpResponseHandler handler = new SunHttpResponseHandler(sunTime, runnable);
        client.get(url, handler);
    }

    /** This function is to calculate the angle for the current sun location
     * @return angel: to determine the sun animation
     * max angel: 180
     * min angel: 0
     * */
    public float getSunLocation(String sunRiseTime, String sunSetTime) {
        float angle = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String curTime = sdf.format(new Date());
        String[] parseResult = curTime.split(":");
        float curTimeInMin = Integer.valueOf(parseResult[0]) * 60 + Integer.valueOf(parseResult[1]);

        String[] sunRiseParsed = sunRiseTime.split(" ");
        String[] sunRiseParsedInMin = sunRiseParsed[0].split(":");
        float sunRiseInMin = Integer.valueOf(sunRiseParsedInMin[0]) * 60 + Integer.valueOf(sunRiseParsedInMin[1]);

        String[] sunSetParsed = sunSetTime.split(" ");
        String[] sunSetParsedInMin = sunSetParsed[0].split(":");
        float sunSetInMin = Integer.valueOf(sunSetParsedInMin[0]) * 60 + Integer.valueOf(sunSetParsedInMin[1]);

        Log.e("curTime", String.valueOf(curTimeInMin));
        Log.e("sunrise", String.valueOf(sunRiseInMin));
        Log.e("sunset", String.valueOf(sunSetInMin));

//        Date sunRiseTime;
//        Date sunSetTime;
//        float totalTime = sunSetTime - sunRiseTime;
//        if (curTime >= sunRiseTime && curTime <= sunSetTime) {
//            // Assume the sun is moving at a fixed speed
//            angle = ((curTime - sunRiseTime) / totalTime) * 180;
//        }
        return angle;
    }

}
