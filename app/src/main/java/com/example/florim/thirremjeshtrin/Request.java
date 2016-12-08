package com.example.florim.thirremjeshtrin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Request extends AppCompatActivity {
    private Button btnAccept;
    private Button btnRefuse;
    private TextView txtRequest;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        String message=getIntent().getStringExtra("Message");
        ID=getIntent().getStringExtra("ID");
        Toast.makeText(this,ID,Toast.LENGTH_LONG).show();
        txtRequest= (TextView) findViewById(R.id.txtRequest);
        txtRequest.setText(message);


    }
    public void onAcceptClick(View v){
        Map<String,String> params=new HashMap<>();
        params.put("ReqID",ID);
        params.put("status","1");
        ConnectToServer connectToServer=new ConnectToServer();
        connectToServer.sendRequest(ConnectToServer.CHECKREQUEST,params);
        finish();
    }
    public void onRefuseClick(View v){
        Map<String,String> params=new HashMap<>();
        params.put("ReqID",ID);
        params.put("status","0");
        ConnectToServer connectToServer=new ConnectToServer();
        connectToServer.sendRequest(ConnectToServer.CHECKREQUEST,params);
        Toast.makeText(Request.this,ID,Toast.LENGTH_LONG).show();
        finish();
    }

}
