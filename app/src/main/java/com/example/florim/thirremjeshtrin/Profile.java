package com.example.florim.thirremjeshtrin;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.SendFeedback;

public class Profile extends AppCompatActivity {
    TextView txtUser;
    TextView txtEmail;
    TextView txtTelefon;
    TextView txtLocation;
    TextView txtRadius;
    Button btnRequest;
    Button btnFeedback;
    Map<String ,String> accountData;
    String RepairmanID;
    AccountManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        am = AccountManager.get(this);
        accountData = Authenticator.findAccount(am, this);
        String Username = getIntent().getStringExtra("Username");
        String Email = getIntent().getStringExtra("Email");
        String Phone = getIntent().getStringExtra("Phone");
        String Lat = getIntent().getStringExtra("Lat");
        String Lon = getIntent().getStringExtra("Lon");
        String Radius = getIntent().getStringExtra("Radius");
        RepairmanID = getIntent().getStringExtra("UserID");
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtTelefon = (TextView) findViewById(R.id.txtTelefon);
        txtRadius = (TextView) findViewById(R.id.txtRadius);

        txtUser.setText(Username);
        txtRadius.setText(Radius + " km");
        txtTelefon.setText(Phone);
        txtEmail.setText(Email);
        txtLocation.setText(getLocation(Lat, Lon));
        btnRequest = (Button) findViewById(R.id.btnSendRequest);
        if (RepairmanID.equals(accountData.get("UserID"))) {
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
                        Map<String, String> params = new HashMap<>();
                        params.put("userID", UserID);
                        params.put("otherID", RepairmanID);
                        params.put("action", "request");
                        ProgressDialog progressDialog=new ProgressDialog(Profile.this,ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        ConnectToServer connectToServer = new ConnectToServer();
                        connectToServer.sendRequest(ConnectToServer.REQUEST, params,false);
                        List<Map<String,String>> result=connectToServer.results;
                        String ID="";
                        if(result.get(0).get("ID")!=null) {
                            ID = result.get(0).get("ID");
                        }
                        AlarmReceiver alarmReceiver = new AlarmReceiver();
                        alarmReceiver.setAlarm(Profile.this,ID);
                        progressDialog.hide();

                    } else {
                        Toast.makeText(Profile.this, R.string.not_registered, Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("RepairmanID", RepairmanID );
                SendFeedback sendFeedback = new SendFeedback();
                sendFeedback.setArguments(bundle);

                Intent i = new Intent(getApplicationContext(), FeedbackTab.class);
                startActivity(i);
            }
        });
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

}
