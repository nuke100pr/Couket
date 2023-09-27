package com.example.roparmarketplace.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.roparmarketplace.R;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
    }
}