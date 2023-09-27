package com.example.roparmarketplace.couponSellDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roparmarketplace.mainActivity.MainActivity;
import com.example.roparmarketplace.R;

public class coupon_sell_done_alert extends AppCompatActivity {

    private Button button,button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_sell_done_alert);
        button = findViewById(R.id.button77);
        button2 = findViewById(R.id.button27);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(coupon_sell_done_alert.this, MainActivity.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(coupon_sell_done_alert.this,coupon_sell_details.class));
            }
        });
    }
    private Boolean doubleBackPressed = false;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

            if(doubleBackPressed)
            {
                finishAffinity();

            }
            else
            {
                doubleBackPressed=true;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPressed = false;
                    }
                },2000);

            }

        }
    }
