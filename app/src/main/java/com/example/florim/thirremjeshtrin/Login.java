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
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

        // If this is a first time adding, then this will be null
        authtoken=getIntent().getStringExtra(REFRESH_TOKEN);
        accountType=getIntent().getStringExtra(ACCOUNT_TYPE);
        accountName = getIntent().getStringExtra(ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(AUTH_TYPE);
        Map<String,String> accountData=Authenticator.findAccount(mAccountManager,this);
        if(accountData!=null){

            Toast.makeText(this, mAuthTokenType + ", accountName : " + accountName, Toast.LENGTH_LONG);
            if(authtoken!=null){
                sendRegistrationToServer(authtoken,accountData);
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
                        RegisterAs.class);
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
        ConnectToServer connectToServer=new ConnectToServer();
        Map<String,String> parameters=new HashMap<String,String>();
        parameters.put("name",accountName);
        parameters.put("password",password);
        parameters.put("token",authtoken);
        //connectToServer.sendRequest(this,ConnectToServer.LOG_IN,parameters);

        List<Map<String,String>> response=connectToServer.results;

        String message="";
        String UserID="";

        for (Map.Entry<String, String> entry : response.get(0).entrySet())
        {
            if(entry.getKey()=="error") {
                switch(Integer.valueOf(entry.getValue())){
                    case 1: message="User doesn't exist!"; break;
                    case 2: message="Credentials missing."; break;

                }
                if(entry.getKey()=="UserID"){
                    UserID=entry.getValue();
                }
            }
        }


        if (message.equals("")) {
            Bundle data = new Bundle();
            data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
            data.putString(AccountManager.KEY_PASSWORD, password);
            // Some extra data about the user
            Bundle userData = new Bundle();
            userData.putString("UserID", UserID);
            data.putBundle(AccountManager.KEY_USERDATA, userData);

            //Make it an intent to be passed back to the Android Authenticator
            final Intent res = new Intent();
            res.putExtras(data);

            //Create the new account with Account Name and TYPE
            final Account account = new Account(accountName, mAuthTokenType);


            //Add the account to the Android System
            if (mAccountManager.addAccountExplicitly(account, password, userData)) {
                // worked
                Log.d(TAG, "Account added");
                mAccountManager.setAuthToken(account, mAuthTokenType, authtoken);
                setAccountAuthenticatorResult(data);
                setResult(RESULT_OK, res);
                finish();
            } else {
                // guess not
                Log.d(TAG, "Account NOT added");
            }

        }
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





