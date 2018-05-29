package signalcat.github.com.smartsunlamp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import static signalcat.github.com.smartsunlamp.MainActivity.lamp;

/**
 * Created by hezhang on 5/28/18.
 */

public class AlarmDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Disable sun rise auto turn on?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lamp.setSunRiseTrigger(false);
                        lamp.setAlarmTrigger(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: enable multiple alarms?
                    }
                });
        return builder.create();
    }
}
