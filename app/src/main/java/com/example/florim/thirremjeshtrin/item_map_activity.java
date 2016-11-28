package com.example.florim.thirremjeshtrin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class item_map_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_map_activity);
        String item;
        Intent intent = getIntent();
        item = intent.getStringExtra("item");
        ItemMap itemMap = (ItemMap) getSupportFragmentManager().findFragmentById(R.id.itemmap_frag);
        itemMap.updateInfo(item);
    }
}
