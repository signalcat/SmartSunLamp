package signalcat.github.com.smartsunlamp.Models;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Lamp class creates objects that parse the JSON response
 * from the circuit board
 */

public class Lamp {

    private long time;
    private long alarm;
    private long alarmHour;
    private long alarmMinute;
    private long fadeInMinutes;
    private long brightness;
    private String expDrive;
    private long remainSecs;
    private long bootTime;

    public long getTime()
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

    public void fromJson(JSONObject jsonObject) throws JSONException {
        brightness = jsonObject.getLong("brightness");
    }

    public Lamp() {
        time = 0;
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
