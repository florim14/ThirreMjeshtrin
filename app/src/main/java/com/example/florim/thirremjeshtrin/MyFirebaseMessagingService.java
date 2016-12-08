package com.example.florim.thirremjeshtrin;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.media.RingtoneManager;
import android.media.SoundPool;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Gresa on 29-Nov-16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static String TAG="FirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG,"ReqID: "+remoteMessage.getData().get("ID"));
        sendNotification(remoteMessage);

    }
    private void sendNotification(RemoteMessage remoteMessage){
        String type=remoteMessage.getData().get("type");
        String from=remoteMessage.getData().get("From");
        String ID=remoteMessage.getData().get("ID");
        Intent intent;
        PendingIntent pIntent=null;
        if(type!=null && ID!=null && from!=null) {
            if (type.equals("request")) {
                AlarmReceiver alarmReceiver = new AlarmReceiver();
                alarmReceiver.setAlarm(this, ID);
                intent = new Intent(this, Request.class);
                intent.putExtra("from", from);
                intent.putExtra("ID", ID);
                intent.putExtra("Message", remoteMessage.getNotification().getBody() + ID);
                pIntent = PendingIntent.getActivity(this, 0, intent, 0);
            }
        }
        



// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()+ID)
                .setSmallIcon(R.drawable.ic_wrench_png_19773)
                .setContentIntent(pIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    }

