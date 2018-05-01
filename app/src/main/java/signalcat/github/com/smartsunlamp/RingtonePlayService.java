package signalcat.github.com.smartsunlamp;

import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import signalcat.github.com.smartsunlamp.httpResponseHandler.LampHttpResponseHandler;

/**
 * Created by hezhang on 4/27/18.
 */

public class RingtonePlayService extends Service {

    MediaPlayer mediaPlayer;
    boolean isRunning;
    boolean isOn;
    AsyncHttpClient client;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Fetch the extra string from the intent
        String alarmState = intent.getExtras().getString("alarm");
        String alarmSound = intent.getExtras().getString("sound");
        Log.e("Alarm sound:", alarmSound);
        Log.e("Alarm state:", alarmState);

        // Converts extra string from the intent to startIds
        assert alarmState != null;
        switch (alarmState) {
            case "alarm on":
                isOn = true;
                break;
            case "alarm off":
                isOn = false;
                break;
            default:
                isOn = false;
                break;
        }
        // No music, press "Alarm on": play music
        if (!isRunning && isOn) {
            if (alarmSound.equalsIgnoreCase("ocean")) {
                mediaPlayer = MediaPlayer.create(this, R.raw.ocean);
            } else if (alarmSound.equalsIgnoreCase("stream")) {
                mediaPlayer = MediaPlayer.create(this, R.raw.stream);
            } else if (alarmSound.equalsIgnoreCase("wetland")) {
                mediaPlayer = MediaPlayer.create(this, R.raw.wetland);
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.bird_sounds);
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            // Create notification banner when the alarm goes off
            CreateNotification();
            isRunning = true;

        }
        // Music on, press "Alarm off": stop music
        else if (isRunning && !isOn) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            isRunning = false;
            isOn = false;
        }
        // No music, press "Alarm off": do nothing
        else if (!isRunning && !isOn) {
            isRunning = false;
            isOn = false;
        }
        // Music on, press "Alarm on": do nothing
        else if (this.isRunning && isOn) {
            isRunning = false;
            isOn = false;
        }
        else {
            Log.e("else", "Some how you reach this");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped
        Log.e("on Destroy called", "ta da");
        super.onDestroy();
        isRunning = false;
    }

    private void CreateNotification() {
        // Set up an intent goes to the setAlarm window
        Intent intent_toSetAlarm = new Intent(this.getApplicationContext(), SetAlarmActivity.class);

        // Turn on the light when the alarm goes off
        client= new AsyncHttpClient();
        client.get("http://192.168.1.12/on", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("Alarm request:", "Success!");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Alarm request:", "Failed!");
            }
        });

        // Set up a pending intent to go back to alarm screen
        PendingIntent pendingIntent_setAlarm = PendingIntent
                .getActivity(this, 0, intent_toSetAlarm, 0);

        // Set up the notification service
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        // Make the notification parameters
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "AlarmChannel")
                        .setSmallIcon(R.mipmap.ic_alarm_notification)
                        .setContentTitle("Alarm is off!")
                        .setContentText("Click me!")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setContentIntent(pendingIntent_setAlarm)
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Alarm channel";
            String description = "Channel for alarm";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("AlarmChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
        }

        // Set up the notification call command
        notificationManager.notify(0, mBuilder.build());

    }

}

