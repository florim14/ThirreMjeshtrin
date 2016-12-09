package com.example.florim.thirremjeshtrin;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.roughike.bottombar.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private double locLong=-1;
    private double locLat=-1;
    private LocationManager mLocationManager;
    private Location mLocation;
    com.roughike.bottombar.BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mBottomBar = com.roughike.bottombar.BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.menu_main);
        mBottomBar.setOnMenuTabClickListener(new com.roughike.bottombar.OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.profile) {
                    Intent i=new Intent(Main2Activity.this,Profile.class);
                    i.putExtra("isUser",true);
                    startActivity(i);
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.inbox) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        mBottomBar.mapColorForTab(0,"#F44346");
        mBottomBar.mapColorForTab(1,"#795548");

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
    @Override
    public void onLocationChanged(Location loc) {
        locLat = loc.getLatitude();
        locLong = loc.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    public void onImageClick(View v){
        getLocation();
        if(locLat!=-1 && locLong!=-1){
            int category=Integer.valueOf(v.getTag().toString());

           Intent i= new Intent(this,FragmentConn.class);
            i.putExtra("category", category);
            i.putExtra("lat", locLat);
            i.putExtra("lon", locLong);
           startActivity(i);


        }

    }
    private void getLocation(){
        if (PermissionUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, PermissionUtils.LOCATION_REQUEST_PERMISSION)) {

            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (mLocation == null) {
                Criteria c = new Criteria();
                c.setAccuracy(Criteria.ACCURACY_COARSE);
                c.setPowerRequirement(Criteria.POWER_LOW);
                String bestProvider = mLocationManager.getBestProvider(c, true);
                mLocation = mLocationManager.getLastKnownLocation(bestProvider);
                mLocationManager.requestLocationUpdates(bestProvider, 5000, 100, this);
            } else {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, this);
            }
            if (mLocation != null) {
                locLat = mLocation.getLatitude();
                locLong = mLocation.getLongitude();

            } else {
                Toast.makeText(this, R.string.no_provider_error, Toast.LENGTH_SHORT).show();

            }
        }
    }

}
