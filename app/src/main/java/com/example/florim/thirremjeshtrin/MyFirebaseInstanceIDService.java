package com.example.florim.thirremjeshtrin;


import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
            ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean connectivity=PermissionUtils.connectivityCheck(cm);
            if(connectivity) {
                ConnectToServer connectToServer = new ConnectToServer();
                connectToServer.sendRequest(ConnectToServer.UPDATETOKEN, params, true);
            }
        }

    }
}
