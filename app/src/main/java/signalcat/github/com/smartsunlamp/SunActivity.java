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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import signalcat.github.com.smartsunlamp.Models.SunTime;
import signalcat.github.com.smartsunlamp.httpResponseHandler.LampHttpResponseHandler;
import signalcat.github.com.smartsunlamp.httpResponseHandler.SunHttpResponseHandler;

import static signalcat.github.com.smartsunlamp.MainActivity.BASE_URL;
import static signalcat.github.com.smartsunlamp.MainActivity.lamp;

public class SunActivity extends AppCompatActivity {

    private SunTime sunTime;
    private ImageView sunShape;
    private AsyncHttpClient client = new AsyncHttpClient();
    private TextView tvSunRiseTime;
    private TextView tvSunSetTime;
    public Switch switch_sunRise;
    private int sunRiseHH;
    private int sunRiseMM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun);

        sunTime = new SunTime();
        findView();

        getSunTime("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400", new Runnable() {
            @Override
            public void run() {
                tvSunRiseTime.setText(sunTime.getSunRise());
                tvSunSetTime.setText(sunTime.getSunSet());
                float angle = getSunLocation(sunTime.getSunRise(),sunTime.getSunSet());
                //Log.e("angle", String.valueOf(angle));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Path path = new Path();
                    path.arcTo(20f,50f,600f,600f,180f,angle,true);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(sunShape, View.X, View.Y, path);
                    animator.setDuration(1000);
                    animator.start();

                }

            }
        });

        switch_sunRise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton switchView, boolean isChecked) {
                if (isChecked) {
                    lamp.setSunRiseTrigger(true);
                    sendCmd("setAlarm/" + String.valueOf(sunRiseHH) + "/" + String.valueOf(sunRiseMM));
                } else {
                    sendCmd("clearAlarm");
                    lamp.setSunRiseTrigger(false);
                }
            }
        });


    }

    public void findView() {
        tvSunRiseTime = findViewById(R.id.tv_sunRiseTime);
        tvSunSetTime = findViewById(R.id.tv_sunSetTime);
        sunShape = findViewById(R.id.iv_sun);
        switch_sunRise = findViewById(R.id.switch_sunRise);
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
        Log.e("curTimeReal", curTime);
        //String curTime = "08:11:11";
        String[] parseResult = curTime.split(":");
        float curTimeInMin = Integer.valueOf(parseResult[0]) * 60 + Integer.valueOf(parseResult[1]);

        String[] sunRiseParsed = sunRiseTime.split(" ");
        String[] sunRiseParsedInMin = sunRiseParsed[0].split(":");
        sunRiseHH = Integer.valueOf(sunRiseParsedInMin[0]);
        sunRiseMM = Integer.valueOf(sunRiseParsedInMin[1]);
//        sunRiseHH = 16;
//        sunRiseMM = 30;
        float sunRiseInMin;

        if (sunRiseParsed[1].equals("AM")) {
            sunRiseInMin = sunRiseHH * 60 + sunRiseMM;
        } else {
            sunRiseInMin = (sunRiseHH + 12) * 60 + sunRiseMM;
        }

        String[] sunSetParsed = sunSetTime.split(" ");
        String[] sunSetParsedInMin = sunSetParsed[0].split(":");
        float sunSetInMin;
        if (sunSetParsed[1].equals("AM")) {
            sunSetInMin = Integer.valueOf(sunSetParsedInMin[0]) * 60 + Integer.valueOf(sunSetParsedInMin[1]);
        } else {
            sunSetInMin = (Integer.valueOf(sunSetParsedInMin[0]) + 12) * 60 + Integer.valueOf(sunSetParsedInMin[1]);
        }

        Log.e("curTime", String.valueOf(curTimeInMin));
        Log.e("sunrise", String.valueOf(sunRiseInMin));
        Log.e("sunset", String.valueOf(sunSetInMin));
        float totalTime = sunSetInMin - sunRiseInMin;
        if (curTimeInMin >= sunRiseInMin && curTimeInMin <= sunSetInMin) {
            // Assume the sun is moving at a fixed speed
            angle = ((curTimeInMin - sunRiseInMin) / totalTime) * 180;
            Log.e("angle", String.valueOf(angle));
        }
        return angle;
    }

    /**
     * This function send out commands through http request
     * @param cmd different commands to control the lamp
     */
    public void sendCmd(String cmd, Runnable runnable){
        LampHttpResponseHandler handler = new LampHttpResponseHandler(lamp, runnable);
        client.get(BASE_URL + cmd, handler);
    }

    public void sendCmd(String cmd){
        sendCmd(cmd, null);
    }

}
