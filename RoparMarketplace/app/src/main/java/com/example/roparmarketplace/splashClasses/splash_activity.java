package com.example.roparmarketplace.splashClasses;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.login_activity.login_activity;


public class splash_activity extends AppCompatActivity {



    private ImageView splash_screen_logo;
    private TextView company_name,couket_logo;
    private Animation animation;
    private Animation animation2;
    private MediaPlayer notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if(getSupportActionBar()!=null)
        {
           getSupportActionBar().hide();
        }
        splash_screen_logo = findViewById(R.id.splash_screen_logo);
        company_name = findViewById(R.id.company_name);
        couket_logo = findViewById(R.id.couket_logo);

        notification = MediaPlayer.create(splash_activity.this,R.raw.notification);



        animation = AnimationUtils.loadAnimation(this,R.anim.splash_activity);
        animation2 =AnimationUtils.loadAnimation(this,R.anim.splash_activity2);

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                notification.start();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                notification.stop();
                startActivity(new Intent(splash_activity.this, login_activity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        couket_logo.startAnimation(animation);
        company_name.startAnimation(animation2);

    }
}
