package com.example.florim.thirremjeshtrin;


import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gresa on 29-Nov-16.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static String TAG="Thirremjeshtrin:";

    private AccountManager am;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        AccountManager am=AccountManager.get(this);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String newToken){
        Map<String,String> Account=Authenticator.findAccount(am,getApplicationContext());
        if(Account!=null) {
            Map<String, String> params = new HashMap<>();
            for (Map.Entry<String, String> entry : Account.entrySet()) {
                if (entry.getKey().equals("UserID"))
                    params.put("UserID", entry.getValue());
            }
            params.put("Token", newToken);
            String url = "http://200.6.254.247/thirremjeshtrin/updatetoken.php";
            ConnectToServer connectToServer = new ConnectToServer();
            connectToServer.sendRequest(url, params);
        }
        else{
            showNotification(newToken);
        }

    }
    private void showNotification(String newToken) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_energy_lightning_power_electric_electricity_logo_b)// notification icon
                .setContentTitle("Notification!") // title for notification
                .setContentText("Hello word") // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(this, Login.class);
        intent.putExtra(Login.REFRESH_TOKEN,newToken);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
