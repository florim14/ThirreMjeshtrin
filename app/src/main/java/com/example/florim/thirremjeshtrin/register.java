package com.example.florim.thirremjeshtrin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class register extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private ProgressDialog pDialog;
    private static final String TAG = register.class.getSimpleName();

    IResult mResultCallback = null;
    VolleyService mVolleyService;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //private SessionManager session;
    //private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                // TODO updateUI(user);
                // [END_EXCLUDE]
            }
        };

        // Session manager
        //session = new SessionManager(getApplicationContext());

        //initVolleyCallback();
        //mVolleyService = new VolleyService(mResultCallback,getApplicationContext());
        
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


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
                String confirmPassword = inputConfirmPassword.getText().toString().trim();
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(password.equals(confirmPassword)) {
                        registerUser(name, email, password);
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
                        inputConfirmPassword.setError("Confirm Password field cannot be empry");
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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    /*
    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("registration");

                    if (success.equals("Successful")) {
                        // User successfully stored in
                        // Now store the user in sqlite

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                register.this,
                                Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message

                        Toast.makeText(getApplicationContext(),
                                success, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }
    */
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void registerUser(final String username, final String email, final String password) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("password", password);
        params.put("email", email);
        params.put("username", username);

        ConnectToServer connectToServer=new ConnectToServer();
        connectToServer.sendRequest(this,ConnectToServer.REGISTER, params);
        List<Map<String,String>> response=connectToServer.results;

        Map<String, String> success = response.get(0);
        String successful = success.get("registration");

        if(successful.equals("Successful")){
            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
            // firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(register.this, "Auth failed",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // TODO: Create a ASyncTask for this do not use the GUI Process
                                final ArrayList<String> defaultRoom = new ArrayList<String>();
                                defaultRoom.add("home");

                                // Update the user profile information
                                final FirebaseUser user = task.getResult().getUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .setPhotoUri(Uri.parse("http://1.bp.blogspot.com/-GKLGUFqEMZw/Tq8bXvXqzBI/AAAAAAAAAA0/0RTAmj2IfVU/s1600/250608_213063775394201_201787589855153_659638_3960990_n.jpg"))
                                        .build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Logger.getLogger(register.class.getName()).log(Level.ALL, "User profile updated.");
                                            // Construct the ChatUser
                                            UserList.user = new ChatUser(user.getUid(),username, email,true,defaultRoom);
                                            // Setup link to users database
                                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(UserList.user);
                                            // startActivity(new Intent(register.this, UserList.class));
                                            Intent i = new Intent(register.this, Login.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), successful, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
/*
    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    String success = jObj.getString("registration");

                    //boolean error = jObj.getBoolean("error");
                    if (success.equals("Successful")) {
                        // User successfully stored in
                        // Now store the user in sqlite


                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                register.this,
                                Login.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        //String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                success, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
*/
}
