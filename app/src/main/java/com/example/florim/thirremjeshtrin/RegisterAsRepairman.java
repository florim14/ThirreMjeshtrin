package com.example.florim.thirremjeshtrin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterAsRepairman extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {
    private ImageButton btnLocation;
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private EditText inputTelephone;
    private EditText inputRadius;
    private Spinner spinner;
    private Spinner spinnerCat;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterCat;
    private String[] array;
    private String[] arrayCat;
    String selectedCountry;
    int selectedCat;
    Double longitude;
    Double latitude;
    boolean isDataValid;
    private LocationManager mLocationManager;
    private Location mLocation;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_repairman);
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        inputTelephone = (EditText) findViewById(R.id.tel);
        inputRadius = (EditText) findViewById(R.id.radius);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        btnLocation= (ImageButton) findViewById(R.id.btnLocation);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        spinner = (Spinner) findViewById(R.id.spin);
        array = getResources().getStringArray(R.array.array_city);

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, array){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(i > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    selectedCountry = (String) adapterView.getItemAtPosition(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCat = (Spinner) findViewById(R.id.spinCategory);
        arrayCat = getResources().getStringArray(R.array.array_category);

        adapterCat = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayCat){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapterCat.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adapterCat);


        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(i > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    selectedCat = i;

                }
                else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                isDataValid=true;
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();
                String radius = inputRadius.getText().toString().trim();
                String tel = inputTelephone.getText().toString().trim();
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !radius.isEmpty() && !tel.isEmpty()) {
                    if(password.equals(confirmPassword)) {

                        if(!Validation.validateData(name,Validation.USERNAME_REGEX)){
                            inputFullName.setError(getString(R.string.invalid_username));
                            isDataValid=false;
                        }
                        if(!Validation.validateData(email,Validation.EMAIL_REGEX)){
                            inputEmail.setError(getString(R.string.invalid_email));
                            isDataValid=false;
                        }
                        if(!Validation.validateData(password,Validation.PASSWORD_REGEX)){
                            inputPassword.setError(getString(R.string.invalid_password));
                            isDataValid=false;
                        }
                        if(!Validation.validateData(tel,Validation.NUMBER_REGEX)){
                            inputTelephone.setError(getString(R.string.only_numbers));
                            isDataValid=false;
                        }
                        if(!Validation.validateData(tel,Validation.NUMBER_REGEX)){
                            inputRadius.setError(getString(R.string.only_numbers));
                            isDataValid=false;
                        }


                        try {
                            Geocoder gc = new Geocoder(RegisterAsRepairman.this);
                            if(longitude==null && latitude==null) {
                                List<Address> addresses = gc.getFromLocationName(selectedCountry, 5);
                                Address address = addresses.get(0);
                                longitude = address.getLongitude();
                                latitude = address.getLatitude();

                            }



                        } catch (IOException e) {
                            Log.d("error", e.toString());
                        }
                        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        boolean connectivity=PermissionUtils.connectivityCheck(cm);

                        if(isDataValid && connectivity) {
                            registerUser(name, email, password, latitude, longitude, radius, tel, selectedCat);
                        }
                        else {
                            Toast.makeText(RegisterAsRepairman.this,R.string.no_connectivity,Toast.LENGTH_LONG).show();
                        }


                    }
                    else{
                        inputConfirmPassword.setError("Incorrect confirm password!");
                    }

                } else {
                    if(name.isEmpty()){
                        inputFullName.setError("Username field cannot be empty!");
                    }
                    if(email.isEmpty()){
                        inputEmail.setError("Email field cannot be empty!");
                    }
                    if(password.isEmpty()){
                        inputPassword.setError("Password field cannot be empty!");
                    }
                    if(confirmPassword.isEmpty()){
                        inputConfirmPassword.setError("Confirm Password field cannot be empty");
                    }
                    if(radius.isEmpty()){
                        inputConfirmPassword.setError("Radius field cannot be empty");
                    }
                    if(tel.isEmpty()){
                        inputConfirmPassword.setError("Telephone field cannot be empty");
                    }
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(i);
                finish();
            }
        });

    }
    private void registerUser(final String username, final String email,
                              final String password, final Double latitude, final Double longitude, String radius, String tel,int category) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("password", password);
        params.put("email", email);
        params.put("username", username);
        params.put("Lon", Double.toString(longitude));
        params.put("Lat", Double.toString(latitude));
        params.put("Phone", tel);
        params.put("Radius", radius);
        params.put("Category", String.valueOf(category));

        ConnectToServer connectToServer=new ConnectToServer();

        connectToServer.sendRequest(ConnectToServer.REGISTER, params,false);

        List<Map<String,String>> response=connectToServer.results;

        Map<String, String> success = response.get(0);
        String successful = success.get("registration");

        if(successful.equals("Successful")){
            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),
                    successful, Toast.LENGTH_LONG).show();
        }
    }
    public void onLocationClick(View v){
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
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();
                Geocoder gc = new Geocoder(RegisterAsRepairman.this);
                try{
                    List<Address> addresses = gc.getFromLocation(latitude,longitude,5);
                    Address address = addresses.get(0);
                    selectedCountry = address.getLocality();
                    spinner.setSelection(getIndex(spinner, selectedCountry));
                } catch(IOException e){

                }


            } else {
                Toast.makeText(this, R.string.no_provider_error, Toast.LENGTH_SHORT).show();

            }
        }
    }


    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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
}
