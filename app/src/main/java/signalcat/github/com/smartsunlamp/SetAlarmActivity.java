package signalcat.github.com.smartsunlamp;

import android.app.AlarmManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        Button btnSetAlarmOn = findViewById(R.id.btn_setAlarm);
        Button btnSetAlarmOff = findViewById(R.id.btn_setAlarmOff);

        alarmTimePicker = findViewById(R.id.timePicker);
        tvAlarmStatus = findViewById(R.id.tv_alarmStatus);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);



        btnSetAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarmText("Alarm On!");
                setCalendar();
                setAlarmText(getAlarmTime());
            }
        });

        btnSetAlarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarmText("Alarm Off!");
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
