package com.example.florim.thirremjeshtrin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentConn extends AppCompatActivity implements ListSearch.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
