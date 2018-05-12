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
        sunShape = findViewById(R.id.iv_sun);
        getSunTime("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400", new Runnable() {
            @Override
            public void run() {
                tvSunRiseTime.setText(sunTime.getSunRise());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path = new Path();
            path.arcTo(20f,50f,600f,600f,180f,180f,true);
            ObjectAnimator animator = ObjectAnimator.ofFloat(sunShape, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.start();

        }

    }



    public void getSunTime(String url, Runnable runnable){
        SunHttpResponseHandler handler = new SunHttpResponseHandler(sunTime, runnable);
        client.get(url, handler);
    }

}
