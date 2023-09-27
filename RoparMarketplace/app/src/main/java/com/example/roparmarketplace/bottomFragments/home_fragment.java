package com.example.roparmarketplace.bottomFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.couponSellDetails.coupon_sell_details;
import com.example.roparmarketplace.mainActivity.MainActivity;
import com.example.roparmarketplace.tab_fragments.tabs;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class home_fragment extends Fragment {

    private Button button_tabs;
    private FloatingActionButton plus_button;



    public home_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_tabs = view.findViewById(R.id.button_tabs);
        button_tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), tabs.class));
            }
        });
         plus_button = view.findViewById(R.id.plus_button);
         plus_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 delCoupons();

                 startActivity(new Intent(getActivity(), coupon_sell_details.class));
             }
         });
    }

    public void delCoupons()
    {
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot DS : snapshot.getChildren()) {
                    if (snapshot.exists()) {
                      //  Toast.makeText(getContext(), "" + "hello", Toast.LENGTH_SHORT).show();
                        String curruser = DS.getKey();
                        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(curruser).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                  //  Toast.makeText(getContext(), "" + "hi" + snapshot.getKey(), Toast.LENGTH_SHORT).show();
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        if (ds.exists()) {
                                           // Toast.makeText(getContext(), "" + "bye", Toast.LENGTH_SHORT).show();
                                            String timestamp = ds.child(nodenames.COUPON_TIME).getValue().toString();
                                            long milliSeconds = Long.parseLong(timestamp);
                                            String cpnTime = sfd.format(milliSeconds);
                                            String delTime = sfd.format(System.currentTimeMillis());
                                            try {
                                                Date d1 = sfd.parse(cpnTime);
                                                Date d2 = sfd.parse(delTime);
                                                long difHours = (d2.getTime() - d1.getTime()) / (1000 * 60 * 60);
                                                //Toast.makeText(getContext(), "" + difHours, Toast.LENGTH_SHORT).show();

                                                if (difHours >=7) {
                                                  //  Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

                                                    FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(curruser).child(ds.getKey()).removeValue();
                                                    FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(curruser).child(nodenames.COUPONS_PUT).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                             FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(curruser).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(curruser).child(nodenames.COUPONS_PUT).setValue((int) snapshot.getChildrenCount());
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });


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
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}


