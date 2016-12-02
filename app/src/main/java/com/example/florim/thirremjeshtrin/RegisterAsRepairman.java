package com.example.florim.thirremjeshtrin;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterAsRepairman extends AppCompatActivity {

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
    String selectedCat;
    Double longitude;
    Double latitude;




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
                    selectedCat = (String) adapterView.getItemAtPosition(i);

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
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();
                String radius = inputRadius.getText().toString().trim();
                String tel = inputTelephone.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !radius.isEmpty() && !tel.isEmpty()) {
                    if(password.equals(confirmPassword)) {
                        if(Geocoder.isPresent()){
                            try {
                                Geocoder gc = new Geocoder(getApplication());
                                List<Address> addresses= gc.getFromLocationName(selectedCountry, 5); // get the found Address Objects

                                Address address = addresses.get(0);

                                longitude = address.getLongitude();
                                latitude = address.getLatitude();

                            } catch (IOException e) {
                                Log.d("error", e.toString());
                            }
                        }
                        registerUser(name, email, password, longitude, latitude, radius, tel);
                    }else{
                        inputConfirmPassword.setError("Incorrect confirm password!");
                    }
                    //initVolleyCallback();
                    //mVolleyService = new VolleyService(mResultCallback,getApplicationContext());
                    //mVolleyService.postDataVolley(AppConfig.URL_REGISTER, params);
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
                              final String password, final Double latitude, final Double longitude, String radius, String tel) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("password", password);
        params.put("email", email);
        params.put("username", username);
        params.put("Lon", Double.toString(longitude));
        params.put("Lat", Double.toString(latitude));
        params.put("Phone", tel);
        params.put("Radius", radius);
        //params.put("category", category);

        ConnectToServer connectToServer=new ConnectToServer();

        connectToServer.sendRequest(this, ConnectToServer.REGISTER, params);

        List<Map<String,String>> response=connectToServer.results;

        Map<String, String> success = response.get(0);
        String successful = success.get("repairman");

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
    /*
    private void getLongLatFromCity(String city){
        double longitude;
        double latitude;

        if(Geocoder.isPresent()){
            try {
                String location = "theNameOfTheLocation";
                Geocoder gc = new Geocoder(this);
                List<Address> addresses= gc.getFromLocationName(city, 5); // get the found Address Objects

                Address address = addresses.get(0);

                longitude = address.getLongitude();
                latitude = address.getLatitude();

            } catch (IOException e) {

            }
        }
    }
    */
}
