package signalcat.github.com.smartsunlamp.Models;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Lamp class parses JSON response and store date
 *
 */

public class Lamp {

    private Date bootTime;
    private Date time;
    private Date alarm;
    private long alarmHour;
    private long alarmMinute;
    private long fadeInMinutes;
    private long brightness;
    private String expDrive;
    private long remainSecs;

    public void setBootTime(long bootTime) {
        this.bootTime = new Date(bootTime * 1000);
    }

    public void setTime(long time) {
        this.time = new Date(time * 1000);
    }

    public void setAlarm(long alarm) {
        this.alarm = new Date(alarm * 1000);
    }

    public void setAlarmHour(long alarmHour) {
        this.alarmHour = alarmHour;
    }

    public void setAlarmMinute(long alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public void setFadeInMinutes(long fadeInMinutes) {
        this.fadeInMinutes = fadeInMinutes;
    }

    public void setBrightness(long brightness) {
        this.brightness = brightness;
    }

    public void setExpDrive(String expDrive) {
        this.expDrive = expDrive;
    }

    public void setRemainSecs(long remainSecs) {
        this.remainSecs = remainSecs;
    }

    public Date getBootTime() { return bootTime; }

    public Date getTime()
    {
        return time;
    }

    public Date getAlarm()
    {
        return alarm;
    }

    public long getAlarmHour()
    {
        return alarmHour;
    }

    public long getAlarmMinute()
    {
        return alarmMinute;
    }

    public long getFadeInMinutes()
    {
        return fadeInMinutes;
    }

    public long getBrightness()
    {
        return brightness;
    }

    public String getExpDrive()
    {
        return expDrive;
    }

    public long getRemainSecs()
    {
        return remainSecs;
    }

    public Lamp() {
        bootTime = new Date(0L);
        time = new Date(0L);
        alarm = new Date(0L);
        alarmHour = 0;
        alarmMinute = 0;
        fadeInMinutes = 0;
        brightness = 0;
        expDrive = "";
        remainSecs = 0;
    }

    // Parse JSON object
    public void fromJSON(JSONObject lampObject) {
        try {

            bootTime = new Date(lampObject.getLong("bootTime") * 1000);
            Log.d("BOOTTIME", bootTime.toString());

            time = new Date(lampObject.getLong("time") * 1000);
            Log.d("TIME", time.toString());

            alarm = new Date(lampObject.getLong("alarm") * 1000);
            Log.d("ALARM", alarm.toString());

            alarmHour = lampObject.getLong("alarmHour");
            Log.d("ALARMHOUR", String.valueOf(alarmHour));

            alarmMinute = lampObject.getLong("alarmMinute");
            Log.d("ALARMMINITE", String.valueOf(alarmMinute));

            fadeInMinutes = lampObject.getLong("fadeInMinutes");
            Log.d("FADEINMINUTES", String.valueOf(fadeInMinutes));

            brightness = lampObject.getLong("brightness");
            Log.d("BRIGHTNESS", String.valueOf(brightness));

            expDrive = lampObject.getString("expDrive");
            Log.d("EXPDRIVE", expDrive);

            remainSecs = lampObject.getLong("remainingSecondsInTransition");
            Log.d("REMAINSECS", String.valueOf(remainSecs));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
