package com.example.florim.thirremjeshtrin;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gresa on 29-Nov-16.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        String url="http://200.6.254.247/thirremjeshtrin/updatetoken.php";
        Map<String,String> params=new HashMap<>();
        params.put("UserID",)
        ConnectToServer connectToServer=new ConnectToServer()
        sendRegistrationToServer(refreshedToken);
    }
}
