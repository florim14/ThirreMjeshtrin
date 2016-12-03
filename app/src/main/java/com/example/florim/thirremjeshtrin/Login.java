package com.example.florim.thirremjeshtrin;


import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Login extends AccountAuthenticatorActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private AccountManager mAccountManager;
    private final String TAG = this.getClass().getSimpleName();
    private String mAuthTokenType;
    private boolean isPermissionGranted;
    String authtoken; // this
    String accountType;
    String accountName;
    String password;


    public final static String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String AUTH_TYPE = "AUTH_TYPE";
    public final static String ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public static String REFRESH_TOKEN="Refresh";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isPermissionGranted=false;
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        Log.d(TAG, "onCreate");

        mAccountManager = AccountManager.get(getBaseContext());

        authtoken=FirebaseInstanceId.getInstance().getToken();
        //Log.d("TOKEN",authtoken);

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

        // If this is a first time adding, then this will be null
        accountType=getIntent().getStringExtra(ACCOUNT_TYPE);
        accountName = getIntent().getStringExtra(ACCOUNT_NAME);
        mAuthTokenType = Authenticator.ACCOUNT_TYPE;
        Map<String,String> accountData=Authenticator.findAccount(mAccountManager,this);
        if(accountData!=null){

            Toast.makeText(this, mAuthTokenType + ", accountName : " + accountName, Toast.LENGTH_LONG);
            for(Map.Entry<String,String>  entry:accountData.entrySet()){
                if(entry.getKey().equals("Token")){
                    if(!entry.getValue().equals(authtoken)){
                        sendRegistrationToServer(authtoken,accountData);
                    }
                }
            }

            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                accountName = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!accountName.isEmpty() && !password.isEmpty()) {
                    // login user
                    userSignIn();
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        register.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void userSignIn() {

        // You should probably call your server with user credentials and get
        // the authentication token here.
        // For demo, I have hard-coded it
        authtoken=FirebaseInstanceId.getInstance().getToken();
        mAuthTokenType=Authenticator.ACCOUNT_TYPE;
        ConnectToServer connectToServer=new ConnectToServer();
        Map<String,String> parameters=new HashMap<String,String>();
        parameters.put("account",accountName);
        parameters.put("password",password);
        parameters.put("token",authtoken);
        connectToServer.sendRequest(this,ConnectToServer.LOG_IN,parameters);

        List<Map<String,String>> response=connectToServer.results;

        String message="";
        String UserID="";
        String Email="";

        for (Map.Entry<String, String> entry : response.get(0).entrySet())
        {
            if(entry.getKey()=="error") {
                switch (Integer.valueOf(entry.getValue())) {
                    case 0:
                        message = "Incorrect password!";
                        break;
                    case 1:
                        message = "User doesn't exist!";
                        break;
                    case 2:
                        message = "Credentials missing.";
                        break;

                }
            }
                if(entry.getKey().toString().equals("UserID")){
                    UserID=entry.getValue();
                }
                if(entry.getKey().toString().equals("Email")){
                    Email=entry.getValue();
                }
            }



        if (message.equals("")) {
            Bundle data = new Bundle();
            data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
            data.putString(AccountManager.KEY_PASSWORD, password);
            data.putString(AccountManager.KEY_USERDATA, UserID);
            data.putString("Email",Email);
            // Some extra data about the user


            //Make it an intent to be passed back to the Android Authenticator
            final Intent res = new Intent();
            res.putExtras(data);

            //Create the new account with Account Name and TYPE
            final Account account = new Account(accountName, mAuthTokenType);

            //Add the account to the Android System
            if (mAccountManager.addAccountExplicitly(account, password, data)) {
                // worked
                Log.d(TAG, "Account added");
                mAccountManager.setAuthToken(account, mAuthTokenType, authtoken);
                setAccountAuthenticatorResult(data);
                setResult(RESULT_OK, res);
                // TODO: firebase login`
                mAuth.signInWithEmailAndPassword(Email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Logger.getLogger(Login.class.getName()).log(Level.ALL, "signInWithEmail:onComplete:" + task.isSuccessful());
                                //loginProgressDlg.dismiss();
                                if (!task.isSuccessful()) {
                                    Logger.getLogger(Login.class.getName()).log(Level.ALL, "signInWithEmail", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    ArrayList<String> defaultRoom = new ArrayList<String>();
                                    defaultRoom.add("home");
                                    UserList.user = new ChatUser(task.getResult().getUser().getUid(),task.getResult().getUser().getDisplayName(), task.getResult().getUser().getEmail(),true,defaultRoom);
                                    //startActivity(new Intent(Login.this, UserList.class));
                                    finish();
                                }

                            }
                        });
            } else {
                // guess not
                Log.d(TAG, "Account NOT added");
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        isPermissionGranted = PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    private void sendRegistrationToServer(String newToken,Map<String,String> Account){
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String> entry : Account.entrySet()) {
            if (entry.getKey().equals("UserID"))
                params.put("UserID", entry.getValue());
        }
        params.put("Token", newToken);
        String url = "http://200.6.254.247/thirremjeshtrin/updatetoken.php";
        ConnectToServer connectToServer = new ConnectToServer();
        connectToServer.sendRequest(this, url, params);
    }
}



