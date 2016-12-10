package com.example.florim.thirremjeshtrin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FragmentConn extends AppCompatActivity implements OnMapReadyCallback{

   private List<Map<String,String>> results;
    private static String LIST_TAG="ListSearch";

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private String lat;
    private String lon;
    com.roughike.bottombar.BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String category = String.valueOf(getIntent().getIntExtra("category", -1));
        lat = String.valueOf(getIntent().getDoubleExtra("lat", -1));
        lon = String.valueOf(getIntent().getDoubleExtra("lon", -1));

        getDataFromServer(category, lat, lon);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_conn);
        mBottomBar = com.roughike.bottombar.BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.menu_main);
        mBottomBar.setDefaultTabPosition(1);
        mBottomBar.setOnMenuTabClickListener(new com.roughike.bottombar.OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.profile) {
                    Intent i=new Intent(FragmentConn.this,Profile.class);
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




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    private List<Map<String,String>> searchFromServer(String cat, String lat,String lon){
        ConnectToServer connectToServer=new ConnectToServer();
        Map<String,String> params=new HashMap<>();
        params.put("Cat",cat);
        params.put("Lat",lat);
        params.put("Lon",lon);
        connectToServer.sendRequest(ConnectToServer.SEARCH,params,false);
        return connectToServer.results;
    }
    private void getDataFromServer(String category,String lat,String lon){
        results=searchFromServer(category,lat,lon);
    }
    public List<Map<String,String>> getData(){
        return results;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(results != null) {
            int size = results.size();

            for (int i = 0; i < size; i++) {
                latlngs.add(new LatLng(Double.parseDouble(results.get(i).get("Lat")),
                        Double.parseDouble(results.get(i).get("Lon"))));
            }
            final String[] markers = new String[size];

            int sizeLatLon = latlngs.size();
            int countSize = 0;
            for (LatLng point : latlngs) {
                if (countSize < sizeLatLon) {
                    options.position(point);
                    options.draggable(true);
                    options.title(results.get(countSize).get("Username"));
                    options.snippet("Phone number: " + results.get(countSize).get("Phone"));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    markers[countSize] = results.get(countSize).get("Username");
                    googleMap.addMarker(options);
                }
                countSize++;
            }


            options.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
            options.title("You are here");
            options.snippet("Your current location!");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            googleMap.addMarker(options);

            CameraUpdate center =
                    CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), 13);
            mMap.moveCamera(center);



            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                LatLng latLng = null;
                int position=-1;
                @Override
                public void onMarkerDragStart(Marker arg0) {
                    for (int i = 0; i < markers.length; i++) {
                        if (markers[i].equals(arg0.getTitle())) {
                            position = i;
                            break;
                        }
                    }
                    latLng = arg0.getPosition();
                    // TODO Auto-generated method stub
                    arg0.showInfoWindow();
                    //arg0.setDraggable(false);
                }

                @SuppressWarnings("unchecked")
                @Override
                public void onMarkerDragEnd(Marker arg0) {
                    arg0.setPosition(latlngs.get(position));
                }

                @Override
                public void onMarkerDrag(Marker arg0) {
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker) {

                    int position = -1;
                    String title = marker.getTitle();
                    for (int i = 0; i < markers.length; i++) {
                        if (markers[i].equals(title)) {
                            position = i;
                            break;
                        }
                    }


                    if (position != -1) {
                        Intent intent = new Intent(getApplicationContext(), Profile.class);

                        String Username = results.get(position).get("Username");
                        String Email = results.get(position).get("Email");
                        String UserID = results.get(position).get("ID");
                        String Phone = results.get(position).get("Phone");
                        String Lat = results.get(position).get("Lat");
                        String Lon = results.get(position).get("Lon");
                        String Radius = results.get(position).get("Radius");

                        intent.putExtra("UserID", UserID);
                        intent.putExtra("Username", Username);
                        intent.putExtra("Lat", Lat);
                        intent.putExtra("Lon", Lon);
                        intent.putExtra("Radius", Radius);
                        intent.putExtra("Phone", Phone);
                        intent.putExtra("Email", Email);
                        startActivity(intent);
                    } else {
                        marker.showInfoWindow();
                    }


                    // Event was handled by our code do not launch default behaviour.
                    return true;
                }
            });
        }
        else
        {
            Toast.makeText(this,R.string.no_results, Toast.LENGTH_LONG).show();
        }

    }
}
