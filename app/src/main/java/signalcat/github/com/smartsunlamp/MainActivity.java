package signalcat.github.com.smartsunlamp;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import signalcat.github.com.smartsunlamp.Models.Lamp;
import signalcat.github.com.smartsunlamp.httpResponseHandler.LampHttpResponseHandler;

/**
 * This is the main screen of the smart sun lamp.
 *
 */
public class MainActivity extends AppCompatActivity
{
    // The local network IP address
    public final static String BASE_URL = "http://192.168.1.12/";
//    final String BASE_URL = "http://192.168.1.118/";
    private Lamp lamp;
    SeekBar seekBar;
    // Limits how often the seekBar sends commands to the wakeup light in milliseconds.
    final static int SEND_THRESHOLD = 50;
    long lastTimeCmdWasSent = 0;

    AsyncHttpClient client = new AsyncHttpClient();

    Button btnOn;
    Button btnOff;
    Button btnToSun;
    Button btnToAlarm;
    Button btnToMap;
    TextView tvBrightness;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();

        // Check if network is connected and display info
        displayNetworkInfo();

        // Initialize lamp object
        lamp = new Lamp();

        // Check the current lamp brightness and update seekBar position
        sendCmd("status", new Runnable(){
            @Override
            public void run() {
                Log.d("BRIGHTNESS", String.valueOf(lamp.getBrightness()));
                seekBar.setProgress((int)lamp.getBrightness());
            }
        });

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(100);
               //sendCmd("on");
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                //sendCmd("off");
            }
        });

        // Go to SunActivity
        btnToSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SunActivity.class);
                startActivity(i);
            }
        });

        // Go to AlarmActivity
        btnToAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SetAlarmActivity.class);
                startActivity(i);
            }
        });

        // Go to map
        btnToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });



        // Adjust the brightness level
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
                tvBrightness.setText("Current Progress:" + progressVal);
                if (lastTimeCmdWasSent + SEND_THRESHOLD < System.currentTimeMillis()) {
                    // Set the brightness
                    sendCmd("/LED/" + progressVal + "/00");
                    lastTimeCmdWasSent = System.currentTimeMillis();
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

    // Check the current lamp brightness and update seekBar position
    // when we come back from other activities in the app
    @Override
    protected void onStart() {
        super.onStart();
        sendCmd("status", new Runnable(){
            @Override
            public void run() {
                Log.d("BRIGHTNESS", String.valueOf(lamp.getBrightness()));
                seekBar.setProgress((int)lamp.getBrightness());
            }
        });
    }

    public void getViews() {
        btnOn = findViewById(R.id.button_on);
        btnOff = findViewById(R.id.button_off);
        btnToSun = findViewById(R.id.btn_toSun);
        btnToAlarm = findViewById(R.id.btn_toAlarm);
        btnToMap = findViewById(R.id.btn_toMap);
        tvBrightness = findViewById(R.id.tv_brightness);
        seekBar = findViewById(R.id.bar_brightness);
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

    /**
     * Check network is connected and display toast message
     */
    public void displayNetworkInfo() {
        Toast.makeText(getApplicationContext(), getNetworkName(), Toast.LENGTH_LONG).show();
    }

    /**
     * Returns the network the phone is currently connected to.
     *
     * @return "1 Network Unavailable" indicates the phone has no connection,
     * "2 Mobile Network" indicates the phone is connected to a cellular service, and
     * any other value indicates an WiFi SSID with a preceeding "3 ".
     *
     */
    private String getNetworkName(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        // If there is no active network, return not available
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return "1 Network unavailable";
        }

        // If there is an active network, check if it's wifi network
        if (activeNetworkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
            return "2 Mobile data";
        }
        // If it's wifi network, return the SSID
        WifiManager wifiManager =
                (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return "3 " + wifiInfo.getSSID();
    }

}


