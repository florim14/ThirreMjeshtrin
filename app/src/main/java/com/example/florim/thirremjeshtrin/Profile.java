package com.example.florim.thirremjeshtrin;

import android.accounts.AccountManager;
import android.location.Address;
import android.location.Geocoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity {
    TextView textView;
    TextView textView2;
    Button btnRequest;
    Button btnFeedback;
    Map<String ,String> accountData;
    String RepairmanID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AccountManager am= AccountManager.get(this);
        accountData=Authenticator.findAccount(am,this);
        String Username = getIntent().getStringExtra("Username");
        String Email = getIntent().getStringExtra("Email");
        String Phone = getIntent().getStringExtra("Phone");
        String Lat = getIntent().getStringExtra("Lat");
        String Lon = getIntent().getStringExtra("Lon");
        String Radius = getIntent().getStringExtra("Radius");
         RepairmanID= getIntent().getStringExtra("UserID");
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(Username + " " + Email + " " + Phone + " " + Radius);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(getLocation(Lat, Lon));


    }

    private String getLocation(String Lat, String Lon) {
        Geocoder g = new Geocoder(this);
        try {
            List<Address> address = g.getFromLocation(Double.valueOf(Lat), Double.valueOf(Lon), 1);
            if (address.isEmpty()) {
                return "";
            }
            return address.get(0).getLocality();
        } catch (IOException e) {
            return "";
        }
    }




    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
    public void onRequestClick(View v) {
        if(accountData!=null) {
            String UserID="";
            for(Map.Entry<String,String> entry:accountData.entrySet()) {
                if (entry.getKey().equals("UserID")) {
                    UserID = entry.getValue();
                }
            }
            Map<String, String> params = new HashMap<>();
            params.put("UserID",UserID);
            params.put("OtherID",RepairmanID);
            params.put("Action","request");
            Toast.makeText(this,UserID+"  "+ RepairmanID,Toast.LENGTH_LONG).show();
            ConnectToServer connectToServer=new ConnectToServer();
            connectToServer.sendRequest(ConnectToServer.REQUEST,params);
        }
        else{
            Toast.makeText(this,R.string.not_registered,Toast.LENGTH_LONG).show();
    }
}
}
