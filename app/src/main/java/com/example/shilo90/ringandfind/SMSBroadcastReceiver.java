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
            messageReceived = messageReceived.toLowerCase();
            // Get the Sender Phone Number
            String senderPhoneNumber=msgs[0].getOriginatingAddress ();
            Log.i("MyApp", senderPhoneNumber);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            int power = sharedPref.getInt("power", 0);
            int radio = sharedPref.getInt("radio", 1);
            String text1 = sharedPref.getString("text1", "");
            String text2 = sharedPref.getString("text2", "");
            String text3 = sharedPref.getString("text3", "");
            if (text1.equals("")) {
                text1 = " ";
            }

            if (text2.equals("")) {
                text2 = " ";
            }

            if (text3.equals("")) {
                text3 = " ";
            }

            if (power == 0) {
                return;
            }

            if (!messageReceived.equals("ringring") && !messageReceived.equals("ringring ")) {
                return;
            }

            if (radio == 0){// && (!senderPhoneNumber.equals(text) || !(text.substring(1).equals(senderPhoneNumber.substring(4))))) {
                Boolean b = CheckEqualNumber(senderPhoneNumber, text1);
                if (!b) {
                    b = CheckEqualNumber(senderPhoneNumber, text2);
                    if (!b) {
                        b = CheckEqualNumber(senderPhoneNumber, text3);
                    }
                    if (!b) {
                        return;
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

            //start main activity intent, delay of 1.5 seconds
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendIntent(myContext);
                        Log.i("MyApp", "send intent");
                    } catch (IOException e) {
                        Log.i("MyApp", e.toString());
                        e.printStackTrace();
                    }
                }
            }, 1500);

            //send notification
            startNotification(context);
            //displayAlert(context);
        }
    }

    private void startNotification(final Context context) {

        //Define Notification Manager
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        //ofir help
        //Uri soundUri = Uri.parse("android.resource://com.example.shilo90.ringandfind" + R.raw.ringring2);

        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ringring1long);



        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_ring_volume_white_24dp)
                .setContentTitle(context.getResources().getText(R.string.app_name))
                .setContentText(context.getResources().getText(R.string.notification))
                .setSound(sound); //This sets the sound to play

        //Display notification
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }, 5000);

    }

    public void sendIntent(Context context) throws IOException {
        Intent i = new Intent(context, MainActivity2Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ring", 5);
        context.startActivity(i);
    }

    public Boolean CheckEqualNumber(String Sender, String textInTextBox)
    {
        if (!Sender.equals(textInTextBox)){
            try {
                String a = textInTextBox.substring(1);
                String b = Sender.substring(Sender.length()-a.length());
                if (!a.equals(b)) {
                    return false;
                }
            }
            catch (Exception e) {

                return false;
            }
        }
        return true;
    }
}