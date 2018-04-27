package signalcat.github.com.smartsunlamp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    TextView tvAlarmStatus;
    final Calendar calendar = Calendar.getInstance();
    Context context; //?
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        this.context = this; //?

        Button btnSetAlarmOn = findViewById(R.id.btn_setAlarm);
        Button btnSetAlarmOff = findViewById(R.id.btn_setAlarmOff);
        alarmTimePicker = findViewById(R.id.timePicker);
        tvAlarmStatus = findViewById(R.id.tv_alarmStatus);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Create an intent to the Alarm Receiver class
        final Intent intent_toAlarmReceiver = new Intent(this.context, AlarmReceiver.class);

        // Press on button to start alarm
        btnSetAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarmText("Alarm On!");
                setCalendar();
                setAlarmText(getAlarmTime());

                // Put in extra string into intent to indicate on button is pressed
                intent_toAlarmReceiver.putExtra("alarm", "alarm on");

                // Create a pending intent that delay the intent until the specified calendar time
                pendingIntent = PendingIntent.getBroadcast(context, 0,
                        intent_toAlarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

                // Set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);
            }
        });

        // Press off button to stop alarm ? mute/delete?
        btnSetAlarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarmText("Alarm Off!");
                alarmManager.cancel(pendingIntent);

                // tells the clock off button is pressed
                intent_toAlarmReceiver.putExtra("alarm", "alarm off");

                // Stop the ringtone
                sendBroadcast(intent_toAlarmReceiver);
            }
        });
    }

    public void setAlarmText(String text) {
        tvAlarmStatus.setText(text);
    }

    public void setCalendar() {
        // For lower API
        if (Build.VERSION.SDK_INT < 23) {
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
        }
    }

    public String getAlarmTime() {
        String hour, min;
        if (Build.VERSION.SDK_INT < 23) {
            hour = String.valueOf(alarmTimePicker.getCurrentHour());
            min = String.valueOf(alarmTimePicker.getCurrentMinute());
        } else {
            hour = String.valueOf(alarmTimePicker.getHour());
            min = String.valueOf(alarmTimePicker.getMinute());
        }
        return "Alarm set to: " + hour + ":" + min;
    }
}
