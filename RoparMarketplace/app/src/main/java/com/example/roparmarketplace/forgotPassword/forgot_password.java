package com.example.roparmarketplace.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.roparmarketplace.R;

public class forgot_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();


    }
}