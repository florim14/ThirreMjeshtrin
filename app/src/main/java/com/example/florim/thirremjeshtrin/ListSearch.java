package com.example.florim.thirremjeshtrin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSearch extends android.app.Fragment {

    ArrayList<String> listItems;
    SimpleAdapter adapter;
    String[] array;
    ListView listView;
    TextView txtNoData;
    public List<Map<String,String>> results;

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
        View view = inflater.inflate(R.layout.activity_list_search, container,false);
        listView = (ListView)view.findViewById(R.id.lstView);
        listView.setVisibility(View.INVISIBLE);


        if(results!=null) {
            adapter = new SimpleAdapter(getActivity(), results, android.R.layout.simple_expandable_list_item_2, new String[]{"Username", "Email"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //String item = array[i];
                    //onItemClickListener.itemSelected(item);
                    String Username="";
                    String Email="";
                    String UserID="";
                    String Phone="";
                    String Lat="";
                    String Lon="";
                    String Radius="";
                    for(Map.Entry<String,String> entry:results.get((int)l).entrySet()){
                        if(entry.getKey().equals("Username")){
                            Username=entry.getValue();
                        }
                        if(entry.getKey().equals("ID")){
                            UserID=entry.getValue();
                        }
                        if(entry.getKey().equals("Email")){
                            Email=entry.getValue();
                        }
                        if(entry.getKey().equals("Phone")){
                            Phone=entry.getValue();
                        }
                        if(entry.getKey().equals("Lat")){
                            Lat=entry.getValue();
                        }
                        if(entry.getKey().equals("Lon")){
                            Lon=entry.getValue();
                        }
                        if(entry.getKey().equals("Radius")){
                            Radius=entry.getValue();
                        }

                    }


                    Intent intent=new Intent(getActivity(),Profile.class);
                    intent.putExtra("UserID",UserID);
                    intent.putExtra("Username",Username);
                    intent.putExtra("Lat",Lat);
                    intent.putExtra("Lon",Lon);
                    intent.putExtra("Radius",Radius);
                    intent.putExtra("Phone",Phone);
                    intent.putExtra("Email" ,Email);
                    startActivity(intent);


                }
            });
        }
        else{

            listView.setVisibility(View.INVISIBLE);
        }
        
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

}
