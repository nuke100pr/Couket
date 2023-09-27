package com.example.roparmarketplace.chatTabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.roparmarketplace.R;
import com.google.android.material.tabs.TabLayout;

public class requestsAndChats extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_and_chats);
        tabLayout = findViewById(R.id.tabs2);
        viewpager = findViewById(R.id.view_pager_tabs2);
        request_chat_tabs_adapter adapter = new request_chat_tabs_adapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }

}