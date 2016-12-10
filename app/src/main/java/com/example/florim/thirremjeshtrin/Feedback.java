package com.example.florim.thirremjeshtrin;

import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feedback extends AppCompatActivity {

    EditText feedback;
    Button btnSendFeedback;
    EditText rating;
    TextView info;
    RatingBar rb;

    String RepairmanID;
    ConnectToServer connectToServer;
    Map<String ,String> accountData;
    AccountManager am;
    float ratingValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        am = AccountManager.get(this);
        accountData = Authenticator.findAccount(am, this);

        feedback = (EditText) findViewById(R.id.txtFeedback);
        btnSendFeedback = (Button) findViewById(R.id.btnFeedback);
        info = (TextView) findViewById(R.id.txtInfo);
        rb = (RatingBar) findViewById(R.id.ratingBar);

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue = v;
            }
        });

        info.setVisibility(View.GONE);
        connectToServer = new ConnectToServer();

        RepairmanID = getIntent().getStringExtra("RepairmanID");

        btnSendFeedback.setOnClickListener(new View.OnClickListener() {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean connectivity = PermissionUtils.connectivityCheck(cm);

            @Override
            public void onClick(View view) {

                String feed = feedback.getText().toString();
                if(!feed.isEmpty()) {
                    Map<String, String> params = new HashMap<>();
                    params.put("UserID", accountData.get("UserID"));
                    params.put("RepID", RepairmanID);
                    params.put("Feedback", feed);
                    params.put("Rating", Float.toString(ratingValue));

                    if (connectivity) {
                        connectToServer = new ConnectToServer();
                        connectToServer.sendRequest(connectToServer.FEEDBACK, params, true);
                        List<Map<String, String>> response = connectToServer.results;
                        if (!response.isEmpty()) {
                            Map<String, String> result = new HashMap<String, String>();
                            result = response.get(0);
                            if (result.get("message") == "Inserted") {
                                Toast.makeText(getApplicationContext(), "Inserted successfully!", Toast.LENGTH_LONG).show();
                            } else {
                            }
                        } else {

                        }
                    }
                }
                else{
                    if(feed.isEmpty()){
                        feedback.setError("");
                    }
                }
            }
        });
    }
}
