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

import java.util.ArrayList;

public class ListSearch extends Fragment{

    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    String[] array;
    ListView listView;

    OnItemClickListener onItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_search, container, false);
        listView = (ListView)view.findViewById(R.id.lstView);
        array = getResources().getStringArray(R.array.array_country);
        //listItems.addAll(Arrays.asList(array));
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.headline_list_layout, R.id.row_item, array);
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
}
