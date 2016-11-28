package com.example.florim.thirremjeshtrin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by vigan on 11/28/2016.
 */

public class ItemMap extends Fragment {

    TextView txtItemHello;
    String[] country;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listmap_fragment, container, false);
        txtItemHello = (TextView)view.findViewById(R.id.txtItemName);
        return view;
    }
    public void updateInfo(String itemSelected){
        txtItemHello.setText("Hello " + itemSelected);
    }
}
