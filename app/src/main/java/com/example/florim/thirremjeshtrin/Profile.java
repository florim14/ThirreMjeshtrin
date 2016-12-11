package com.example.florim.thirremjeshtrin;

import android.accounts.AccountManager;
import android.app.Fragment;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Profile extends Fragment {
    TextView txtUser;
    TextView txtEmail;
    TextView txtTelefon;
    TextView txtLocation;
    TextView txtRadius;
    TextView txtCategory;
    Button btnRequest;
    Button btnFeedback;
    Button btnChat;
    public static Map<String ,String> accountData;
    String RepairmanID;
    AccountManager am;
    ConnectivityManager cm;

    String UserID="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the layout for this fragment @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);


        RepairmanID="";
        String Username="";
        String Email="";
        String Phone="";
        String Lat="";
        String Lon="";
        String Radius="";
        String Category="";
        String Location="";

        cm =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        UserID=accountData.get("UserID");

        final boolean isUser=getActivity().getIntent().getBooleanExtra("isUser",false);
        if(!isUser) {
            Username = getActivity().getIntent().getStringExtra("Username");
            Email = getActivity().getIntent().getStringExtra("Email");
            Phone = getActivity().getIntent().getStringExtra("Phone");
            Lat = getActivity().getIntent().getStringExtra("Lat");
            Lon = getActivity().getIntent().getStringExtra("Lon");
            Radius = getActivity().getIntent().getStringExtra("Radius");
            RepairmanID = getActivity().getIntent().getStringExtra("RepairmanID");
            Location=getLocation(Lat, Lon);
            Category=getActivity().getIntent().getStringExtra("Category");
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

        txtUser = (TextView) view.findViewById(R.id.txtUser);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtLocation = (TextView) view.findViewById(R.id.txtLocation);
        txtTelefon = (TextView) view.findViewById(R.id.txtTelefon);
        txtRadius = (TextView) view.findViewById(R.id.txtRadius);
        txtCategory=(TextView)view.findViewById(R.id.txtCategory);
        if(Username!="") {
            txtUser.setText(Username);
        }
        else{
            txtUser.setVisibility(View.GONE);
            ImageView imgUser=(ImageView) view.findViewById(R.id.imgProfile);
            imgUser.setVisibility(View.GONE);
        }
        if(Radius!="") {
            txtRadius.setText(Radius + " km");
        }
        else{
            txtRadius.setVisibility(View.GONE);
            ImageView imgRadius=(ImageView) view.findViewById(R.id.imgRadius_icon);
            imgRadius.setVisibility(View.GONE);

        }
        if(Phone!="") {
            txtTelefon.setText(Phone);
        }
        else{
            txtTelefon.setVisibility(View.GONE);
            ImageView imgTelefon=(ImageView) view.findViewById(R.id.imgTelefon_icon);
            imgTelefon.setVisibility(View.GONE);
        }
        if(Email!="") {
            txtEmail.setText(Email);
        }
        else{
            txtEmail.setVisibility(View.GONE);
            ImageView imgEmail=(ImageView) view.findViewById(R.id.imgEmail_icon);
            imgEmail.setVisibility(View.GONE);
        }
        if(Location!="") {
            txtLocation.setText(Location);
        }
        else{
            txtLocation.setVisibility(View.GONE);
            ImageView imgLocation=(ImageView) view.findViewById(R.id.imgLocation_icon);
            imgLocation.setVisibility(View.GONE);
        }
        if(Category!="") {
            txtCategory.setText(Category);
        }
        else{
            txtCategory.setVisibility(View.GONE);
            ImageView imgCategory=(ImageView) view.findViewById(R.id.imgCategory_icon);
            imgCategory.setVisibility(View.GONE);
        }
        btnRequest = (Button) view.findViewById(R.id.btnSendRequest);
        btnChat = (Button) view.findViewById(R.id.btnChat);
        if (RepairmanID.equals(accountData.get("UserID"))|| isUser) {
            btnRequest.setText(R.string.log_out);
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                @Override
                public void onClick(View view) {
                    logOut(view);
                }
            });
            btnChat.setVisibility(View.GONE);
        } else {
            btnRequest.setText(R.string.btnRequest);
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   onRequestClick(view);
                }
            });
        }




        return view;
    }

    public void onRequestClick(View v){
        if (accountData != null) {
            UserID = "";
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

                ConnectToServer connectToServer = new ConnectToServer();
                connectToServer.sendRequest(ConnectToServer.REQUEST, params, false);
                List<Map<String, String>> result = connectToServer.results;
                String ID = "";
                if (result.get(0).get("ID") != null) {
                    ID = result.get(0).get("ID");
                }
                AlarmReceiver alarmReceiver = new AlarmReceiver();
                alarmReceiver.setAlarm(getActivity(), ID);
            } else{
                Toast.makeText(getActivity(), R.string.no_connectivity, Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getLocation(String Lat, String Lon) {
        Geocoder g = new Geocoder(getActivity());
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
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            am.removeAccountExplicitly(am.getAccountsByType(Authenticator.ACCOUNT_TYPE)[0]);
            Map<String,String> params=new HashMap<>();
            params.put("UserID",accountData.get("UserID"));
            params.put("Token","NULL");
            ConnectToServer connectToServer= new ConnectToServer();
            connectToServer.sendRequest(ConnectToServer.UPDATETOKEN,params,true);
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getActivity(),Login.class);
            startActivity(intent);
        }
        }
        else{
            Toast.makeText(getActivity(), R.string.no_connectivity, Toast.LENGTH_LONG).show();
        }


    }

}
