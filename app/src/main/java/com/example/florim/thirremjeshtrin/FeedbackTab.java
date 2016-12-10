package com.example.florim.thirremjeshtrin;

import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;


public class FeedbackTab extends AppCompatActivity implements SendFeedback.OnFragmentInteractionListener, ListFeedback.OnFragmentInteractionListener{


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String RepairmanID;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_tab);

        RepairmanID= getIntent().getStringExtra("RepairmanID");
        UserID=getIntent().getStringExtra("UserID");
        Log.d("FeedbackTab: ",UserID+" "+RepairmanID);


        TabHost tabs=(TabHost)findViewById(R.id.TabHost); //Id of tab host

        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("tag1");//make a new tab

        spec.setContent(R.id.Tab1);  //What is in the tab (not an activity but rather a view)
        spec.setIndicator("Reviews"); //Name of tab
        tabs.addTab(spec); //Add it
        ListFeedback list=new ListFeedback();
        list.RepairmanID=RepairmanID;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.Tab1,list,"").disallowAddToBackStack().commit();


        if(!RepairmanID.equals(UserID)){
        spec=tabs.newTabSpec("tag2"); //Same thing here
        spec.setContent(R.id.Tab2);
        spec.setIndicator("Send Feedback");
        tabs.addTab(spec);
            SendFeedback list2=new SendFeedback();
            list.RepairmanID=RepairmanID;
            list2.UserID=UserID;
            ft.add(R.id.Tab2,list2,"").disallowAddToBackStack().commit();
        }




        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

                if(s.equals("tag1")){

                }
                else if(s.equals("tag2")){

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
