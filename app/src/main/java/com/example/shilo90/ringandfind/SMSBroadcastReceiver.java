package com.example.shilo90.ringandfind;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public NotificationManager myNotificationManager;
    public static final int NOTIFICATION_ID = 2;
    public static MediaPlayer mp;
    public static NotificationManager notificationManager;

    public Context myContext;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        myContext = context;
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messageReceived = "";
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++)


            {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                messageReceived += msgs[i].getMessageBody().toString();
            }

            //---display the new SMS message---
            //Toast.makeText(context, messageReceived, Toast.LENGTH_SHORT).show();
            Log.i("MyApp", messageReceived);

            // Get the Sender Phone Number
            String senderPhoneNumber=msgs[0].getOriginatingAddress ();
            Log.i("MyApp", senderPhoneNumber);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            int power = sharedPref.getInt("power", 0);
            int radio = sharedPref.getInt("radio", 1);
            String text = sharedPref.getString("text", "");
            if (text.equals("")) {
                text = " ";
            }

            if (power == 0) {
                return;
            }

            if (!messageReceived.equals("ringring") && !messageReceived.equals("Ringring")) {
                return;
            }

            if (radio == 0){// && (!senderPhoneNumber.equals(text) || !(text.substring(1).equals(senderPhoneNumber.substring(4))))) {
                if (!senderPhoneNumber.equals(text)) {
                    if (sharedPref.getString("country", "").equals("DOM")){
                        String temp1 = "+1-809" + text.substring(1);
                        String temp2 = "+1-829" + text.substring(1);
                        String temp3 = "+1-849" + text.substring(1);
                        if(!senderPhoneNumber.equals(temp1) || !senderPhoneNumber.equals(temp2) || !senderPhoneNumber.equals(temp3)){
                            return;
                        }
                    }
                    else if (sharedPref.getString("country", "").equals("PRI")){
                        String temp1 = "+1-787" + text.substring(1);
                        String temp2 = "+1-939" + text.substring(1);
                        if(!senderPhoneNumber.equals(temp1) || !senderPhoneNumber.equals(temp2)){
                            return;
                        }
                    }
                    else {
                        text = "+" + sharedPref.getString("countryCode", "972") + text.substring(1);
                        if (!senderPhoneNumber.equals(text)) {
                            return;
                        }
                    }
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("screenOn", true);
            editor.commit();


            AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            switch (manager.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    Log.i("MyApp","Silent mode");
                    manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    Log.i("MyApp","Vibrate mode");
                    manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
            }

            manager.setStreamVolume(AudioManager.STREAM_RING, manager.getStreamMaxVolume(manager.STREAM_RING), 0);

            try {
                sendIntent(myContext);
                Log.i("MyApp", "send intent");
            } catch (IOException e) {
                Log.i("MyApp", e.toString());
                e.printStackTrace();
            }
                        startNotification(context);
            //displayAlert(context);
        }
    }

    private void startNotification(final Context context) {

        //Define Notification Manager
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_ring_volume_white_24dp)
                .setContentTitle(context.getResources().getText(R.string.app_name))
                .setContentText(context.getResources().getText(R.string.notification))
                .setSound(soundUri); //This sets the sound to play

        //Display notification
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }, 1200);

    }

    public void sendIntent(Context context) throws IOException {
        Intent i = new Intent(context, MainActivity2Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ring", 5);
        /*i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);*/
        context.startActivity(i);
    }
}