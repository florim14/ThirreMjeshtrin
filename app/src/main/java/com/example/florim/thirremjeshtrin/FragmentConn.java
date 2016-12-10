package com.example.florim.thirremjeshtrin;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentConn extends AppCompatActivity implements OnMapReadyCallback{

   private List<Map<String,String>> results;
    private static String LIST_TAG="ListSearch";
    ProgressDialog progressDialog;

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private String lat;
    private String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String category = String.valueOf(getIntent().getIntExtra("category", -1));
        lat = String.valueOf(getIntent().getDoubleExtra("lat", -1));
        lon = String.valueOf(getIntent().getDoubleExtra("lon", -1));
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        getDataFromServer(category, lat, lon);
        super.onCreate(savedInstanceState);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressDialog.hide();
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

        HashMap<String, String> hmap = new HashMap<>();
        int size = results.size();
        String[] title = new String[size];
        String[] description = new String[size];

        for(int i = 0; i < size-1; i++)
        {
            hmap.put(results.get(i).get("Lat"), results.get(i).get("Lon"));
            title[i] = results.get(i).get("Username");
            description[i] = results.get(i).get("Phone");
        }


      /* Display content using Iterator*/
        Set set = hmap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            latlngs.add(new LatLng(Double.parseDouble(mentry.getKey().toString()),
                    Double.parseDouble(mentry.getValue().toString())));
        }

        int sizeLatLon = latlngs.size();
        int countSize = 0;
        for (LatLng point : latlngs) {
            if(countSize < sizeLatLon) {
                options.position(point);
                options.title(title[countSize]);
                options.snippet("Phone number: " + description[countSize]);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                googleMap.addMarker(options);
            }
            countSize++;
        }

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
        mMap.moveCamera(center);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(4);
        mMap.animateCamera(zoom);

        final Marker[] lastOpened = {null};

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                // Check if there is an open info window
                if (lastOpened[0] != null) {
                    // Close the info window
                    lastOpened[0].hideInfoWindow();

                    // Is the marker the same marker that was already open
                    if (lastOpened[0].equals(marker)) {
                        // Nullify the lastOpened object
                        lastOpened[0] = null;
                        // Return so that the info window isn't opened again
                        return true;
                    }
                }

                // Open the info window for the marker
                marker.showInfoWindow();
                // Re-assign the last opened such that we can close it later
                lastOpened[0] = marker;


                Intent OpenProfile = new Intent(getApplicationContext(), Profile.class );
                startActivity(OpenProfile);

                // Event was handled by our code do not launch default behaviour.
                return true;
            }
        });

    }
}
