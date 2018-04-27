package signalcat.github.com.smartsunlamp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Starts the ringtone service at the time the alarm goes off
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver", "Yes!");
        // Create an intent to the ringtone service
        Intent intent_toRingtone = new Intent(context, RingtonePlayService.class);

        // Start the ringtone service
        context.startService(intent_toRingtone);
    }
}
