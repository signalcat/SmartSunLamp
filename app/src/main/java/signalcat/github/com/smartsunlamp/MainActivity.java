package signalcat.github.com.smartsunlamp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    Button btnOn;
    Button btnOff;
    Button btnToSun;
    Button btnToAlarm;
    Button btnToMap;
    TextView tvBrightness;
    ImageView ivLightBar1;
    ImageView ivLightBar2;
    ImageView ivLightBar3;
    ImageView ivLightBar4;
    ImageView ivLightBar5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

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
                lightBarAnimation((int)lamp.getBrightness());
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
                lightBarAnimation(progressVal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    // synchronize the state whenever the screen is restored
    // or there is a configuration change (i.e screen rotation)
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_menu_first:
                Intent i = new Intent(MainActivity.this, SetAlarmActivity.class);
                startActivity(i);
                break;
            case R.id.nav_menu_second:
                Intent j = new Intent(MainActivity.this, SunActivity.class);
                startActivity(j);
        }
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawer.openDrawer(GravityCompat.START);
//                return true;
//        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        ivLightBar1 = findViewById(R.id.iv_lightBar1);
        ivLightBar2 = findViewById(R.id.iv_lightBar2);
        ivLightBar3 = findViewById(R.id.iv_lightBar3);
        ivLightBar4 = findViewById(R.id.iv_lightBar4);
        ivLightBar5 = findViewById(R.id.iv_lightBar5);
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

    private void lightBarAnimation(int progressVal) {
        if (progressVal < 15) {
            ivLightBar1.setVisibility(View.INVISIBLE);
            ivLightBar2.setVisibility(View.INVISIBLE);
            ivLightBar3.setVisibility(View.INVISIBLE);
            ivLightBar4.setVisibility(View.INVISIBLE);
            ivLightBar5.setVisibility(View.INVISIBLE);
        } else if (progressVal >= 15 && progressVal < 30) {
            ivLightBar1.setVisibility(View.VISIBLE);
            ivLightBar2.setVisibility(View.INVISIBLE);
            ivLightBar3.setVisibility(View.INVISIBLE);
            ivLightBar4.setVisibility(View.INVISIBLE);
            ivLightBar5.setVisibility(View.INVISIBLE);
        } else if (progressVal >= 30 && progressVal < 45) {
            ivLightBar1.setVisibility(View.VISIBLE);
            ivLightBar2.setVisibility(View.VISIBLE);
            ivLightBar3.setVisibility(View.INVISIBLE);
            ivLightBar4.setVisibility(View.INVISIBLE);
            ivLightBar5.setVisibility(View.INVISIBLE);
        } else if (progressVal >= 45 && progressVal < 60) {
            ivLightBar1.setVisibility(View.VISIBLE);
            ivLightBar2.setVisibility(View.VISIBLE);
            ivLightBar3.setVisibility(View.VISIBLE);
            ivLightBar4.setVisibility(View.INVISIBLE);
            ivLightBar5.setVisibility(View.INVISIBLE);
        } else if (progressVal >= 60 && progressVal < 75) {
            ivLightBar1.setVisibility(View.VISIBLE);
            ivLightBar2.setVisibility(View.VISIBLE);
            ivLightBar3.setVisibility(View.VISIBLE);
            ivLightBar4.setVisibility(View.VISIBLE);
            ivLightBar5.setVisibility(View.INVISIBLE);
        } else {
            ivLightBar1.setVisibility(View.VISIBLE);
            ivLightBar2.setVisibility(View.VISIBLE);
            ivLightBar3.setVisibility(View.VISIBLE);
            ivLightBar4.setVisibility(View.VISIBLE);
            ivLightBar5.setVisibility(View.VISIBLE);

        }
    }

}


