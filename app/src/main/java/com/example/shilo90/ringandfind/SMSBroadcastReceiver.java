package com.example.shilo90.ringandfind;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public NotificationManager myNotificationManager;
    public static final int NOTIFICATION_ID = 2;

    @Override
    public void onReceive(Context context, Intent intent)
    {
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
                        text = "+" + sharedPref.getString("countryCode", "") + text.substring(1);
                        if (!senderPhoneNumber.equals(text)) {
                            return;
                        }
                    }
                }
            }



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

            notification2(context);

        }
    }

    private void notification(Context context) {
        /*myNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        CharSequence NotificationTicket = "Ring To Find";
        CharSequence NotificationTitle = "Ring To Find";
        CharSequence NotificationContent = "your phone is on RING mode!";

        Notification notification = new Notification(R.mipmap.ic_launcher, NotificationTicket, 0);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, NotificationTitle, NotificationContent, contentIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        myNotificationManager.notify(NOTIFICATION_ID, notification);*/

        //Define Notification Manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_notifications_active_white_24dp)
                .setContentTitle("Ring To Find")
                .setContentText("your phone is on RING mode!")
                .setSound(soundUri); //This sets the sound to play

//Display notification
        notificationManager.notify(2, mBuilder.build());

    }

    public void notification2(Context context) {

        final int id = 2;
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        int icon = R.mipmap.ic_notifications_active_white_24dp;
        CharSequence tickerText = "Ring To Find";
        long when = System.currentTimeMillis();

        Notification checkin_notification = new Notification(icon, tickerText,
                when);
        CharSequence contentTitle = "Ring To Find";
        CharSequence contentText = "your phone is on RING mode!";

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        checkin_notification.setLatestEventInfo(context, contentTitle,
                contentText, contentIntent);
        checkin_notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, checkin_notification);

    }
}