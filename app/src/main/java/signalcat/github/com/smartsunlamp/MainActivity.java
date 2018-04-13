package signalcat.github.com.smartsunlamp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
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
    final String BASE_URL = "http://192.168.1.148/";
    private Lamp lamp;
    // Brightness adjustment bar
    SeekBar seekBar;

    long lastTimeCmdWasSent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lamp = new Lamp();

        // Fetch all views
        Button btnOn = findViewById(R.id.button_on);
        Button btnOff = findViewById(R.id.button_off);
        final TextView tvBrightness = findViewById(R.id.tv_brightness);

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

        // Adjust the brightness level
        seekBar = findViewById(R.id.bar_brightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
                tvBrightness.setText("Current Progress:" + progressVal);

                if (lastTimeCmdWasSent + SEND_THRESHOLD < System.currentTimeMillis()) {
                    // Set the brightness level
                    sendCmd("/LED/" + progressVal + "/00");
                    lastTimeCmdWasSent = System.currentTimeMillis();
                    //Log.d("randomStuff", "Command was sent");
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
    public void sendCmd(String cmd){
        AsyncHttpClient client = new AsyncHttpClient();
        LampHttpResponseHandler handler = new LampHttpResponseHandler(lamp);
        client.get(BASE_URL + cmd, handler);
    }
}
