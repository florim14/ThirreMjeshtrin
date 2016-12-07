package com.example.florim.thirremjeshtrin;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentConn extends AppCompatActivity implements ListSearch.OnItemClickListener{

   private List<Map<String,String>> results;
    private static String LIST_TAG="ListSearch";
    private ListSearch list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String category= String.valueOf(getIntent().getIntExtra("category",-1));
        String lat=  String.valueOf(getIntent().getDoubleExtra("lat",-1));
        String lon= String.valueOf( getIntent().getDoubleExtra("lon",-1));
        dataToShow(category,lat,lon);
        super.onCreate(savedInstanceState);


        list= new ListSearch();
        list.results=results;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(android.R.id.content,list,LIST_TAG).disallowAddToBackStack().commit();
        setContentView(R.layout.activity_fragment_conn);




    }

    @Override
    public void itemSelected(String item) {
        ItemMap itemMap = (ItemMap) getSupportFragmentManager().findFragmentById(R.id.itemmap_frag);
        if(itemMap == null){
            Intent intent = new Intent(this, item_map_activity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        }else {
            itemMap.updateInfo(item);
        }

    }
    private List<Map<String,String>> searchFromServer(String cat, String lat,String lon){
        ConnectToServer connectToServer=new ConnectToServer();
        Map<String,String> params=new HashMap<>();
        params.put("Cat",cat);
        params.put("Lat",lat);
        params.put("Lon",lon);
        connectToServer.sendRequest(ConnectToServer.SEARCH,params);
        return connectToServer.results;
    }
    private void dataToShow(String category,String lat,String lon){
        results=searchFromServer(category,lat,lon);
    }
    public List<Map<String,String>> getData(){
        return results;
    }

}
