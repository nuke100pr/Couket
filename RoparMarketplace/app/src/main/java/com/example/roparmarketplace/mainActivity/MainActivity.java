package com.example.roparmarketplace.mainActivity;

import static com.example.roparmarketplace.utility_classes.util.connectionAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.R;
import com.example.roparmarketplace.bottomFragments.auction_fragment;
import com.example.roparmarketplace.bottomFragments.contest_fragment;
import com.example.roparmarketplace.bottomFragments.home_fragment;
import com.example.roparmarketplace.drawerFragments.request_chats_fragment;
import com.example.roparmarketplace.drawerFragments.company_fragment;
import com.example.roparmarketplace.drawerFragments.logout_fragment;
import com.example.roparmarketplace.drawerFragments.premium_fragment;
import com.example.roparmarketplace.drawerFragments.your_fragment;
import com.example.roparmarketplace.login_activity.login_activity;
import com.example.roparmarketplace.profileClasses.profile_fragment;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationBarView barView;
    private TextView drawer_profile_name, drawer_profile_email;
    private ImageView ivProfileDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference().child(nodenames.OTHERS).child(nodenames.VERSION).child("version").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.getValue().toString().equals(nodenames.VERSION_NO))
                {

                    if(snapshot.getValue().toString().equals("404"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("WILL BE BACK IN COUPLE OF HOURS");
                        builder.setTitle("SERVER CRASH!!!");
                        builder.setCancelable(false);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else {
                        // Toast.makeText(MainActivity.this, "PLZ DOWNLOAD THE LATEST VERSION", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("INSTALL LATEST VERSION");
                        builder.setTitle("OLD VERSION DETECTED!!!");
                        builder.setCancelable(false);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                   /* firebaseAuth.signOut();
                    finish();
                    moveTaskToBack(true);*/
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(curruser.getUid()).child(nodenames.ONLINE).setValue(true);
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(curruser.getUid()).child(nodenames.ONLINE).onDisconnect().setValue(false);

        navigationView = findViewById(R.id.navigation_view_drawer);
        toolbar = findViewById(R.id.toolbar_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_drawer);
        View headerView = navigationView.getHeaderView(0);

        drawer_profile_name = (TextView) headerView.findViewById(R.id.drawer_profile_name);
        drawer_profile_email = (TextView) headerView.findViewById(R.id.drawer_profile_email);
        ivProfileDrawer = (ImageView) headerView.findViewById(R.id.profile_photo3);


        updateDrawerProfile();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

       /* LocalTime localTime = LocalTime.now();
        if ((localTime.isAfter(LocalTime.of(1, 40))) && (localTime.isBefore(LocalTime.of(1, 50)))) {
            FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).removeValue();
        }*/

       /* SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(curruser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                   String timestamp= ds.child(nodenames.COUPON_TIME).getValue().toString();
                   long milliSeconds= Long.parseLong(timestamp);
                   String cpnTime = sfd.format(milliSeconds);
                   HashMap hashMap = new HashMap();
                   hashMap.put(nodenames.TIMESTAMP_CURRENT, ServerValue.TIMESTAMP);
                   final long[] milliSeconds2 = new long[1];
                   FirebaseDatabase.getInstance().getReference().child(nodenames.OTHERS).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference().child(nodenames.OTHERS).child(nodenames.TIMESTAMP_CURRENT).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    milliSeconds2[0]=Long.parseLong(snapshot.getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });

                   String delTime = sfd.format(milliSeconds2[0]);
                    try {
                        Date d1 = sfd.parse(cpnTime);
                        Date d2 = sfd.parse(delTime);
                        long difHours = (d1.getTime()-d2.getTime())/(1000*60*60);
                        Toast.makeText(MainActivity.this, ""+difHours, Toast.LENGTH_SHORT).show();
                        if(difHours>7)
                        {
                            FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(curruser.getUid()).child(ds.getKey()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(curruser.getUid()).child(nodenames.COUPONS_PUT).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int noCpn = Integer.parseInt(snapshot.getValue().toString());
                                     noCpn -=1;
                                     FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(curruser.getUid()).child(nodenames.COUPONS_PUT).setValue(noCpn);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.about_nav:
                        loadFragment(new request_chats_fragment(), false);
                        break;
                    case R.id.your_nav:
                        loadFragment(new your_fragment(), false);
                        break;
                    case R.id.company_nav:
                        loadFragment(new company_fragment(), false);
                        break;
                    case R.id.premium_nav:
                        loadFragment(new premium_fragment(), false);
                        break;
                    case R.id.logout_nav:
                        loadFragment(new logout_fragment(), false);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        loadFragment2(new home_fragment());

        barView = findViewById(R.id.bottomNavigationView);
        barView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        loadFragment2(new home_fragment());
                        break;
                    case R.id.nav_profile:
                        loadFragment2(new profile_fragment());
                        break;
                    case R.id.nav_auction:
                        loadFragment2(new auction_fragment());
                        break;
                    case R.id.nav_contest:
                        loadFragment2(new contest_fragment());
                        break;
                }
                return true;

            }
        });


    }



    private void loadFragment2(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, fragment);

        ft.commit();
        ft.replace(R.id.container, fragment);
    }

    private boolean doubleBackPressed = false;
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
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


    private void loadFragment(Fragment fragment, boolean bool) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, fragment);

        ft.commit();
        if (bool) {
            ft.add(R.id.container, fragment);
        } else {
            ft.replace(R.id.container, fragment);
        }
    }

    public void onClickLogout(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setTitle("Log Out?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReference = rootRef.child(nodenames.TOKENS).child(currentUser.getUid());
            databaseReference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.ONLINE).setValue(false);
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, login_activity.class));
                        finishAffinity();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.something_went_wrong, task.getException()), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void updateDrawerProfile() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(userId);
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();

        drawer_profile_name.setText(name);
        drawer_profile_email.setText(email);
        FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        Uri serverfileuri = firebaseUser1.getPhotoUrl();

        if (serverfileuri != null) {
            Glide.with(this)
                    .load(serverfileuri).placeholder(R.drawable.loading)
                    .error(R.drawable.profile_pic)
                    .into(ivProfileDrawer);
        }


    }



}