package com.example.florim.thirremjeshtrin;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.ListFeedback;
import layout.SendFeedback;

public class Profile extends AppCompatActivity {
    TextView txtUser;
    TextView txtEmail;
    TextView txtTelefon;
    TextView txtLocation;
    TextView txtRadius;
    TextView txtCategory;
    Button btnRequest;
    Button btnFeedback;
    Map<String ,String> accountData;
    String RepairmanID;
    AccountManager am;
    ConnectivityManager cm;
    com.roughike.bottombar.BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomBar = com.roughike.bottombar.BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.menu_main);
        mBottomBar.setDefaultTabPosition(1);
        mBottomBar.setOnMenuTabClickListener(new com.roughike.bottombar.OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.profile) {
                    Intent i=new Intent(Profile.this,Profile.class);
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


        setContentView(R.layout.profile);
        am = AccountManager.get(this);
        RepairmanID="";
        String Username="";
        String Email="";
        String Phone="";
        String Lat="";
        String Lon="";
        String Radius="";
        String Category="";
        String Location="";
        cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        accountData = Authenticator.findAccount(am, this);
        final boolean isUser=getIntent().getBooleanExtra("isUser",false);
        if(!isUser) {
            Username = getIntent().getStringExtra("Username");
            Email = getIntent().getStringExtra("Email");
            Phone = getIntent().getStringExtra("Phone");
            Lat = getIntent().getStringExtra("Lat");
            Lon = getIntent().getStringExtra("Lon");
            Radius = getIntent().getStringExtra("Radius");
            RepairmanID = getIntent().getStringExtra("UserID");
            Location=getLocation(Lat, Lon);
            Category=getIntent().getStringExtra("Category");
        }
        else {
            if (!accountData.get("Name").equals("")) {
                Username = accountData.get("Name");
            }
            if (!accountData.get("Email").equals("")) {
                Email = accountData.get("Email");
            }
            if (!accountData.get("Phone").equals("")) {
                Phone = accountData.get("Phone");
            }
            if (!accountData.get("Location").equals("")) {
                Location = accountData.get("Location");
            }
            if (!accountData.get("Category").equals("")) {
                Category = accountData.get("Category");
            }
            if(!accountData.get("Radius").equals("")){
                Radius=accountData.get("Radius");
            }
        }

        txtUser = (TextView) findViewById(R.id.txtUser);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtTelefon = (TextView) findViewById(R.id.txtTelefon);
        txtRadius = (TextView) findViewById(R.id.txtRadius);
        txtCategory=(TextView)findViewById(R.id.txtCategory);
        if(Username!="") {
            txtUser.setText(Username);
        }
        else{
            txtUser.setVisibility(View.INVISIBLE);
            ImageView imgUser=(ImageView) findViewById(R.id.imgProfile);
            imgUser.setVisibility(View.INVISIBLE);
        }
        if(Radius!="") {
            txtRadius.setText(Radius + " km");
        }
        else{
            txtRadius.setVisibility(View.INVISIBLE);
            ImageView imgRadius=(ImageView) findViewById(R.id.imgRadius_icon);
            imgRadius.setVisibility(View.INVISIBLE);
        }
        if(Phone!="") {
            txtTelefon.setText(Phone);
        }
        else{
            txtTelefon.setVisibility(View.INVISIBLE);
            ImageView imgTelefon=(ImageView) findViewById(R.id.imgTelefon_icon);
            imgTelefon.setVisibility(View.INVISIBLE);
        }
        if(Email!="") {
            txtEmail.setText(Email);
        }
        else{
            txtEmail.setVisibility(View.INVISIBLE);
            ImageView imgEmail=(ImageView) findViewById(R.id.imgEmail_icon);
            imgEmail.setVisibility(View.INVISIBLE);
        }
        if(Location!="") {
            txtLocation.setText(Location);
        }
        else{
            txtLocation.setVisibility(View.INVISIBLE);
            ImageView imgLocation=(ImageView) findViewById(R.id.imgLocation_icon);
            imgLocation.setVisibility(View.INVISIBLE);
        }
        if(Category!="") {
            txtCategory.setText(Category);
        }
        else{
            txtCategory.setVisibility(View.INVISIBLE);
            ImageView imgCategory=(ImageView) findViewById(R.id.imgCategory_icon);
            imgCategory.setVisibility(View.INVISIBLE);
        }
        btnRequest = (Button) findViewById(R.id.btnSendRequest);
        if (RepairmanID.equals(accountData.get("UserID"))|| isUser) {
            btnRequest.setText(R.string.log_out);
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                @Override
                public void onClick(View view) {
                    logOut(view);
                }
            });
        } else {
            btnRequest.setText(R.string.btnRequest);
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (accountData != null) {
                        String UserID = "";
                        for (Map.Entry<String, String> entry : accountData.entrySet()) {
                            if (entry.getKey().equals("UserID")) {
                                UserID = entry.getValue();
                            }
                        }

                        if (PermissionUtils.connectivityCheck(cm)) {

                            Map<String, String> params = new HashMap<>();
                            params.put("userID", UserID);
                            params.put("otherID", RepairmanID);
                            params.put("action", "request");
                            ProgressDialog progressDialog = new ProgressDialog(Profile.this, ProgressDialog.STYLE_SPINNER);
                            progressDialog.show();
                            ConnectToServer connectToServer = new ConnectToServer();
                            connectToServer.sendRequest(ConnectToServer.REQUEST, params, false);
                            List<Map<String, String>> result = connectToServer.results;
                            String ID = "";
                            if (result.get(0).get("ID") != null) {
                                ID = result.get(0).get("ID");
                            }
                            AlarmReceiver alarmReceiver = new AlarmReceiver();
                            alarmReceiver.setAlarm(Profile.this, ID);
                            progressDialog.hide();
                        } else{
                            Toast.makeText(Profile.this, R.string.no_connectivity, Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
        };
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("RepairmanID", RepairmanID );
                SendFeedback sendFeedback = new SendFeedback();
                sendFeedback.setArguments(bundle);
                ListFeedback listFeedback = new ListFeedback();
                listFeedback.setArguments(bundle);

                Intent i = new Intent(getApplicationContext(), FeedbackTab.class);
                i.putExtra("isUser", isUser);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void logOut(View v){
        if (PermissionUtils.connectivityCheck(cm)) {
            if(ContextCompat.checkSelfPermission(Profile.this, android.Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                am.removeAccountExplicitly(am.getAccountsByType(Authenticator.ACCOUNT_TYPE)[0]);
                Map<String,String> params=new HashMap<>();
                params.put("UserID",accountData.get("UserID"));
                params.put("Token","NULL");
                ConnectToServer connectToServer= new ConnectToServer();
                connectToServer.sendRequest(ConnectToServer.UPDATETOKEN,params,true);
                Intent intent=new Intent(this,Login.class);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(Profile.this, R.string.no_connectivity, Toast.LENGTH_LONG).show();
        }


    }

}
