package com.example.roparmarketplace.tab_fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.tab_fragments.view_pager_tabs_Adapter;
import com.google.android.material.tabs.TabLayout;


public class tabs extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewpager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        tabLayout = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.view_pager_tabs);
        view_pager_tabs_Adapter adapter = new view_pager_tabs_Adapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);


    }
}