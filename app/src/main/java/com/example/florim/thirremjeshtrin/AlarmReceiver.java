package com.example.florim.thirremjeshtrin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmet on 08-Dec-16.
 */
     /** When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
     * and then starts the IntentService {@code DataUpdaterService} to do some work.
     */
    public class AlarmReceiver extends BroadcastReceiver {

        /**
         * Callback method when the alarm is fired
         * @param context the context that has set the alarm
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
        /*
      The classes's ConnectivityManager, which provides information on the connectivity sections
     */
            String ID=intent.getStringExtra("ID");
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //Check if the device has connectivity,if so execute service
            if(PermissionUtils.connectivityCheck(cm)) {
                Map<String,String> params=new HashMap<>();
                params.put("ReqID",ID);

               ConnectToServer connectToServer=new ConnectToServer();

                connectToServer.sendRequest(ConnectToServer.CHECKREQUEST,params,true);

            }
        }



    /**
     * Sets an alarm that runs 30 min after it is created. When the
     * alarm fires, the app broadcasts an Intent to this BroadcastReceiver.
     * @param context The context of the activity that sets the alarm
     */
    public void setAlarm(Context context,String ID) {

            /*
      The app's AlarmManager, which provides access to the system alarm services.
     */
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ID",ID);

            /*
      The pending intent that is triggered when the alarm fires.
     */
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,Integer.valueOf(ID), intent, 0);

        /*
         * The call below specifies the alarm type, the trigger time, the interval at
         * which the alarm is fired, and the alarm's associated PendingIntent.
         * It uses the alarm type RTC_WAKEUP ("Real Time Clock" wake up), which wakes up
         * the device and triggers the alarm according to the time of the device's clock.
         */



        // Set the alarm to fire after 30 minutes, according to the device's
        // clock.
        alarmMgr.set(AlarmManager.RTC,
                System.currentTimeMillis() + 30*(60*1000), alarmIntent);


    }

}

