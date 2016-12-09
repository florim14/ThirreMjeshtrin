package com.example.florim.thirremjeshtrin;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Qitu hmap e bajna me hashmap qe na vjen prej database
        HashMap<String, String> hmap = new HashMap<String, String>();

      /*Adding elements to HashMap*/

        /*then we remove those*/
        hmap.put("12.334343", "33.43434");
        hmap.put("25.3535", "35.1654");
        hmap.put("5.65", "21.16546532");
        hmap.put("20.1651", "20.16512");
        hmap.put("17.26156", "18.1651");

      /* Display content using Iterator*/
        Set set = hmap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            latlngs.add(new LatLng(Double.parseDouble(mentry.getKey().toString()),
                    Double.parseDouble(mentry.getValue().toString())));
        }

        //latlngs.add(new LatLng(20.1651, 20.16512));

        for (LatLng point : latlngs) {
            options.position(point);
            options.title("someTitle");
            options.snippet("someDesc");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(options);
        }

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(25, 28));
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


               // Intent intent = new Intent(getApplicationContext(), )
                Toast.makeText(getApplicationContext(), "Click3", Toast.LENGTH_SHORT).show();

                // Event was handled by our code do not launch default behaviour.
                return true;
            }
        });

    }
}