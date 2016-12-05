package com.example.florim.thirremjeshtrin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSearch extends Fragment{

    ArrayList<String> listItems;
    SimpleAdapter adapter;
    String[] array;
    ListView listView;
    List<Map<String,String>> results;

    OnItemClickListener onItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = new Bundle();
        //bundle.putSerializable("location", getLocation(results));
        //MapsActivity fragobj = new MapsActivity();
       //fragobj.setArguments(bundle);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String category= String.valueOf(getActivity().getIntent().getIntExtra("category",-1));
        String lat=  String.valueOf(getActivity().getIntent().getDoubleExtra("lat",-1));
        String lon= String.valueOf( getActivity().getIntent().getDoubleExtra("lon",-1));
        dataToShow(category,lat,lon);
        View view = inflater.inflate(R.layout.activity_list_search, container, false);
        listView = (ListView)view.findViewById(R.id.lstView);
        adapter = new SimpleAdapter(getActivity(),results,android.R.layout.simple_list_item_1, new String[]{"Username","Email","Phone"},
                new int[] { android.R.id.text1 });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = array[i];
                onItemClickListener.itemSelected(item);
            }
        });
        
        return view;
    }

    public interface OnItemClickListener{
        public void itemSelected(String item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemClickListener = (OnItemClickListener) context;
        }catch (Exception e){}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
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
    private HashMap<String,String> getLocation(List<Map<String,String>> list){
        HashMap<String,String> location=new HashMap<>();
        String Lat="";
        String Lon="";
        for(Map<String,String>map:list){
            for(Map.Entry<String,String> entry:map.entrySet()){
                if(entry.getKey().equals("Lat")){
                    Lat=entry.getValue();
                }
                if(entry.getKey().equals("Lon")){
                    Lon=entry.getValue();
                }
                if(!Lat.equals("")&& !Lon.equals("")){
                    location.put(Lat,Lon);
                }

            }
        }
        return location;
    }
    public void dataToShow(String category,String lat,String lon){
        results=searchFromServer(category,lat,lon);
    }
}
