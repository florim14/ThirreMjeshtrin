package com.example.florim.thirremjeshtrin;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.*;

public class MainActivity extends AppCompatActivity {

    com.roughike.bottombar.BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomBar = com.roughike.bottombar.BottomBar.attach(this, savedInstanceState);
        //noinspection deprecation
        mBottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.profile)
                {
                    com.example.florim.thirremjeshtrin.BottomBar bottom = new com.example.florim.thirremjeshtrin.BottomBar();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, bottom).commit();

                }
                else if (menuItemId == R.id.message)
                {
                    com.example.florim.thirremjeshtrin.BottomBar bottom = new com.example.florim.thirremjeshtrin.BottomBar();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, bottom).commit();
                }
                else if (menuItemId != R.id.notification)
                {
                    com.example.florim.thirremjeshtrin.BottomBar bottom = new com.example.florim.thirremjeshtrin.BottomBar();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, bottom).commit();
                }

            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
        mBottomBar.mapColorForTab(0,"#F44346");
        mBottomBar.mapColorForTab(1,"#795548");
        mBottomBar.mapColorForTab(2,"#E91E63");

        BottomBarBadge unread;
        unread = mBottomBar.makeBadgeForTabAt(1, "#FF0000", 13);
        unread.show();
    }
}
