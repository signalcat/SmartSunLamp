package signalcat.github.com.smartsunlamp.Models;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Lamp class creates objects that parse the JSON response
 * from the circuit board
 */

public class Lamp {

    private Date time;
    private long alarm;
    private long alarmHour;
    private long alarmMinute;
    private long fadeInMinutes;
    private long brightness;
    private String expDrive;
    private long remainSecs;
    private long bootTime;

    public void setTime(long time) {
        this.time = new Date(time * 1000);
    }

    public void setAlarm(long alarm) {
        this.alarm = alarm;
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

    public void setBootTime(long bootTime) {
        this.bootTime = bootTime;
    }

    public Date getTime()
    {
        return time;
    }

    public long getAlarm()
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

    public long getBootTime() { return bootTime; }

    public void fromJSON(JSONObject lampObject) {
        try {
            time = new Date(lampObject.getLong("time") * 1000);
            Log.d("TIME", time.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//        private long alarm;
//        private long alarmHour;
//        private long alarmMinute;
//        private long fadeInMinutes;
//        private long brightness;
//        private String expDrive;
//        private long remainSecs;
//        private long bootTime;

    public Lamp() {
        time = new Date(0L);
        alarm = 0;
        alarmHour = 0;
        alarmMinute = 0;
        fadeInMinutes = 0;
        brightness = 0;
        expDrive = "";
        remainSecs = 0;
        bootTime = 0;
    }

}
