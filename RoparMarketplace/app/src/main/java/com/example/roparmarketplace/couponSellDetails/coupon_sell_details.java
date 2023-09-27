package com.example.roparmarketplace.couponSellDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.example.roparmarketplace.utility_classes.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.HashMap;

public class coupon_sell_details extends AppCompatActivity {

    private Button coupon_sell_done,send_note;
    private SeekBar seekBar;
    private TextView price_display;
    private Spinner spinner,spinner2;
    private Switch aswitch;
    private Switch radioButton;
    private TextInputEditText customNotification;
    private final int[] count = {0};
    private int t;
    private static Boolean isOpen=true;
    private String[] coupon_types = {"Kanaka-breakfast","Kanaka-lunch","Kanaka-dinner","Bhopal-breakfast","Bhopal-lunch","Bhopal-dinner"};







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_sell_details);

        LocalDate currentDate = LocalDate.now();
        LocalDate nextDay = currentDate.plusDays(1);
        LocalDate nextnextDay = currentDate.plusDays(2);
        String date1 = currentDate.toString();
        String date2 = nextDay.toString();
        String date3 = nextnextDay.toString();
        String[] coupon_dates ={date1,date2,date3};

        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(coupon_sell_details.this, android.R.layout.simple_spinner_item,coupon_types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final String[] coupon_TYPE = new String[1];

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                coupon_TYPE[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)spinner.getSelectedView()).setError("Error message");
            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(coupon_sell_details.this, android.R.layout.simple_spinner_item,coupon_dates);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        final String[] coupon_DATE = new String[1];

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                coupon_DATE[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)spinner.getSelectedView()).setError("Error message");
            }
        });

        seekBar = findViewById(R.id.seekBar);
        price_display = findViewById(R.id.price_display);
        final String[] final_price = new String[1];
        final_price[0] ="0";

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                price_display.setText("Rs"+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               coupon_sell_done.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                  final_price[0] = String.valueOf(seekBar.getProgress());
            }
        });

        final Boolean[] isCall = new Boolean[1];
        isCall[0]=false;

        aswitch = findViewById(R.id.switch1);
        aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    isCall[0] = isChecked;

            }
        });



        radioButton =findViewById(R.id.switch2);
        customNotification = findViewById(R.id.tvCustomNotification2);
        final Boolean[] isNotification = new Boolean[1];
        final String[] notification = new String[1];
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                     customNotification.setVisibility(View.VISIBLE);


                }
                else
                {
                    customNotification.setVisibility(View.GONE);

                }
                isNotification[0] = isChecked;

            }
        });





             coupon_sell_done = findViewById(R.id.coupon_sell_done);
             String id = firebaseUser.getUid();


        final Boolean[] truth = {true};
            coupon_sell_done.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    mRootRef.child(nodenames.USERS).child(firebaseUser.getUid()).child(nodenames.COUPONS_PUT).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            count[0]=Integer.parseInt(snapshot.getValue().toString());
                            t=count[0];
                            if(t>2)
                            {
                                truth[0] = false;
                            }
                          // Toast.makeText(coupon_sell_details.this, ""+t+truth[0], Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                        notification[0] = customNotification.getText().toString().trim();
                        String userId = firebaseUser.getUid().toString();
                        DatabaseReference databaseReference = mRootRef.child(nodenames.REQUESTS).child(userId).push();
                        String coupon_id = databaseReference.getKey();
                        String newRequestId = databaseReference.getKey();
                        String name = firebaseUser.getDisplayName();


                        mRootRef.child(nodenames.USERS).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                String phone = snapshot.child(nodenames.PHONE).getValue().toString();
                                HashMap hashMap = new HashMap();
                                hashMap.put(nodenames.COUPON_TYPE, coupon_TYPE[0]);
                                hashMap.put(nodenames.COUPON_DATE, coupon_DATE[0]);
                                hashMap.put(nodenames.COUPON_PRICE, final_price[0]);
                                hashMap.put(nodenames.COUPON_CALL, isCall[0]);
                                hashMap.put(nodenames.COUPON_NOTIFICATION, notification[0]);
                                hashMap.put(nodenames.COUPON_NAME, name);
                                hashMap.put(nodenames.COUPON_PHONE, phone);
                                hashMap.put(nodenames.COUPON_ID, userId);
                                hashMap.put(nodenames.COUPON_TIME, ServerValue.TIMESTAMP);
                                hashMap.put(nodenames.COUPON_IS_NOTIFICATION,true);


                                if (truth[0]) {
                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(userId).child(coupon_id).child(nodenames.COUPON_IS_NOTIFICATION).setValue(true);

                                                count[0] += 1;
                                                mRootRef.child(nodenames.USERS).child(userId).child(nodenames.COUPONS_PUT).setValue(count[0]);
                                                if ((!notification[0].equals(""))) {

                                                    FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                                if (ds.exists()) {
                                                                    if (!ds.getKey().equals(userId)) {

                                                                        FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(userId).child(coupon_id).child(nodenames.COUPON_IS_NOTIFICATION).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                Boolean val = snapshot.getValue()!=null?(boolean)snapshot.getValue():false;
                                                                                if(val)
                                                                                {

                                                                                    FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(userId).child(coupon_id).child(nodenames.COUPON_IS_NOTIFICATION).setValue(false);
                                                                                    FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(ds.getKey()).child(nodenames.NOTIFICATION_TRUTH_COUPONS).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            Boolean tru = snapshot.getValue()!=null?(boolean)snapshot.getValue():false;
                                                                                            if(tru)
                                                                                            {
                                                                                                util.sendNotification(coupon_sell_details.this, "New " + coupon_TYPE[0] + " Coupon", notification[0], ds.getKey());
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });


                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });





                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                                }
                                                finishAffinity();
                                                Intent intent = new Intent(coupon_sell_details.this, coupon_sell_done_alert.class);
                                                intent.putExtra("coupon_type",coupon_TYPE[0]);
                                                intent.putExtra("notification",notification[0]);

                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(coupon_sell_details.this, R.string.sell_request_failed, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(coupon_sell_details.this, R.string.sell_request_failed, Toast.LENGTH_SHORT).show();
                                }


                        }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }





            });



           /* send_note = findViewById(R.id.send_notification);
            send_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.exists()) {
                                    if (!ds.getKey().equals(id)) {

                                        util.sendNotification(coupon_sell_details.this, "New " + coupon_TYPE[0] + " Coupon", notification[0], ds.getKey());

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });*/



    }

}