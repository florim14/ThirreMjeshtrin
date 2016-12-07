package com.example.florim.thirremjeshtrin;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Gresa on 30-Nov-16.
 */
/**
 * Utility class for access to runtime permissions.
 */
abstract class PermissionUtils  {

    public static final int ACCOUNTS_REQUEST_PERMISSION = 1;
    public static final int LOCATION_REQUEST_PERMISSION = 2;


    public static boolean checkPermission(Activity activity, String permission, int requestId) {

        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED ) {

            // Permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);
            return false;

        }

        return true;

    }


    public static boolean isGPSEnabled(LocationManager lm, Activity app) {
        boolean isLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isLocationEnabled) {
            Toast.makeText(app, R.string.request_enable_gps, Toast.LENGTH_SHORT).show();
        }
        return isLocationEnabled;

    }

    public static boolean connectivityCheck(ConnectivityManager cm, Activity app) {

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {

            Toast.makeText(app, R.string.no_connectivity, Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;

    }


    public static boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                              @NonNull int[] grantResults) {
        boolean isPermissionGranted;

        switch (requestCode) {
            case ACCOUNTS_REQUEST_PERMISSION: {

            }
            case LOCATION_REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true;


                } else {
                    isPermissionGranted = false;

                }
                break;
            }
            default: {
                isPermissionGranted = false;
                break;
            }

        }
        return isPermissionGranted;


    }
}


