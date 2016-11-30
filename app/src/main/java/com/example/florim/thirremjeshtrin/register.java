package com.example.florim.thirremjeshtrin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class register extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    //private SessionManager session;
    //private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Session manager
        //session = new SessionManager(getApplicationContext());

        // SQLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        /*
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(register.this,
                    WelcomeScreen.class);
            startActivity(intent);
            finish();
        }
        */

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    if(name.isEmpty()){
                        inputFullName.setError("Please enter your Username!");
                    }
                    if(email.isEmpty()){
                        inputEmail.setError("Please enter your email!");
                    }
                    if(password.isEmpty()){
                        inputPassword.setError("Please enter your password!");
                    }
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        login.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String username, final String email,
                              final String password) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("password", password);
        params.put("email", email);
        params.put("username", username);

        ConnectToServer connectToServer = new ConnectToServer();
        connectToServer.sendRequest(this, AppConfig.URL_REGISTER, params);
        List<Map<String, String>> result = connectToServer.results;
        Map<String, String> success = result.get(0);
        String successful = success.get("registration");

        if(successful.equals("Successful")){
            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, login.class);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),
                    successful, Toast.LENGTH_LONG).show();
        }
    }
}
