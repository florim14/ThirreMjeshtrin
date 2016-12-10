package com.example.florim.thirremjeshtrin;

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

public class GiveFeedback extends AppCompatActivity {

    EditText feedback;
    Button btnSendFeedback;
    TextView info;
    RatingBar rb;

    String RepairmanID;
    String UserID;
    ConnectToServer connectToServer;

    float ratingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);




            RepairmanID = getIntent().getStringExtra("RepairmanID");
            UserID = getIntent().getStringExtra("UserID");



        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final boolean connectivity = PermissionUtils.connectivityCheck(cm);

        feedback = (EditText)findViewById(R.id.txtFeedback);
        btnSendFeedback = (Button)findViewById(R.id.btnFeedback);
        info = (TextView)findViewById(R.id.txtInfo);
        rb = (RatingBar)findViewById(R.id.ratingBar);
            rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    ratingValue = v;
                }
            });
            btnSendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String feed = feedback.getText().toString();
                    if (!feed.isEmpty()) {
                        final Map<String, String> params = new HashMap<>();
                        params.put("UserID", UserID);
                        params.put("RepID", RepairmanID);
                        params.put("Feedback", feed);
                        params.put("Rating", Float.toString(ratingValue));
                        if(connectivity){
                        connectToServer = new ConnectToServer();
                        connectToServer.sendRequest(connectToServer.FEEDBACK, params, true);
                        List<Map<String, String>> response = connectToServer.results;
                        Map<String, String> result = new HashMap<String, String>();
                        result = response.get(0);
                        if (result.get("message") == "INSERTED") {
                            Toast.makeText(getApplicationContext(), "SENT!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_LONG).show();
                        }
                    }else {
                            Toast.makeText(getApplicationContext(), R.string.no_connectivity, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (feed.isEmpty()) {
                            feedback.setError("");
                        }
                    }
                }
            });


    }
}
