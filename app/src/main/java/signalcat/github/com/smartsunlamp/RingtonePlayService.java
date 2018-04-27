package signalcat.github.com.smartsunlamp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by hezhang on 4/27/18.
 */

public class RingtonePlayService extends Service {

    MediaPlayer mediaPlayer;
    boolean isRunning;
    boolean isOn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Fetch the extra string from the intent
        String alarmState = intent.getExtras().getString("alarm");

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

        // No music, press "Alarm on" ->  play music
        if (!isRunning && isOn) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bird_sounds);
            mediaPlayer.start();
            isRunning = true;

        }
        // Music on, press "Alarm off" -> stop music
        else if (isRunning && !isOn) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            isRunning = false;
            isOn = false;
        }
        // No music, press "Alarm off" -> do nothing
        else if (!isRunning && !isOn) {
            isRunning = false;
            isOn = false;
        }
        // Music on, press "Alarm on" -> do nothing
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

}

