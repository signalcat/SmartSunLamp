package signalcat.github.com.smartsunlamp;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import signalcat.github.com.smartsunlamp.Models.Lamp;
import signalcat.github.com.smartsunlamp.httpResponseHandler.LampHttpResponseHandler;

/**
 * This is the main screen of the smart sun lamp.
 *
 */
public class MainActivity extends AppCompatActivity
{
    // Limits how often the seekbar sends commands to the wakeup light
    // in milliseconds.
    final static int SEND_THRESHOLD = 50;
    final String BASE_URL = "http://192.168.1.12/";
//    final String BASE_URL = "http://192.168.1.118/";
    private Lamp lamp;
    // Brightness adjustment bar
    SeekBar seekBar;
    // Light bulb image brightness indicator
    private ImageView imageView_bulb;
    private Bitmap bitmap;

    long lastTimeCmdWasSent = 0;

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lamp = new Lamp();
        sendCmd("status", new Runnable(){
            @Override
            public void run() {
                Log.d("BRIGHTNESS", String.valueOf(lamp.getBrightness()));
                seekBar.setProgress((int)lamp.getBrightness());
            }
        });


        // Fetch all views
        Button btnOn = findViewById(R.id.button_on);
        Button btnOff = findViewById(R.id.button_off);
        Button btnToSun = findViewById(R.id.btn_toSun);
        final TextView tvBrightness = findViewById(R.id.tv_brightness);

//        // Create a bitmap object and apply it to imageView
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.light_bulb);
//        imageView_bulb = findViewById(R.id.imageView_lightBulb);
//        imageView_bulb.setImageBitmap(bitmap);
//
//        // Initialize the image processing class for the lightBulb
//        final ImageThread imageThread = new ImageThread(imageView_bulb, bitmap);
//        imageThread.start();

        // When On button is pressed, send on command
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(100);
               //sendCmd("on");
            }
        });

        // When Off button is pressed, send off command
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                //sendCmd("off");
            }
        });

        // Go to the sunActivity
        btnToSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SunActivity.class);
                startActivity(i);
            }
        });

        // Adjust the brightness level
        seekBar = findViewById(R.id.bar_brightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
                tvBrightness.setText("Current Progress:" + progressVal);

                //imageThread.adjustBrightness(progressVal);

                if (lastTimeCmdWasSent + SEND_THRESHOLD < System.currentTimeMillis()) {
                    // Set the brightness
                    sendCmd("/LED/" + progressVal + "/00");
                    lastTimeCmdWasSent = System.currentTimeMillis();
                    //Log.d("randomStuff", "Command was sent");
//                    imageThread.adjustBrightness(progressVal);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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


