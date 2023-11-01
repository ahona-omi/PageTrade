package com.example.pagetrade;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    //FloatingActionButton goo;
    float v=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout =findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        //goo= findViewById(R.id.fab_google);

        tabLayout.addTab(tabLayout.newTab().setText("Log In"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter= new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tabView = (TextView) tab.getCustomView();
                if (tabView != null) {
                    tabView.setTypeface(tabView.getTypeface(), Typeface.BOLD);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabView = (TextView) tab.getCustomView();
                if (tabView != null) {
                    tabView.setTypeface(tabView.getTypeface(), Typeface.NORMAL);
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //goo.setTranslationY(300);
        tabLayout.setTranslationY(300);

        //goo.setAlpha(v);
        tabLayout.setAlpha(v);

        //goo.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(400).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(800).start();
    }
}