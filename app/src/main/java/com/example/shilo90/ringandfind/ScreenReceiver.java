package com.example.shilo90.ringandfind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ScreenReceiver extends BroadcastReceiver {
    public ScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean b = sharedPref.getBoolean("screenOn", false);
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT) && b)
        {
            Intent intent1 = new Intent(context,MainActivity2Activity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("ring", 5);
            context.startActivity(intent1);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("screenOn", false);
        editor.commit();
    }
}
