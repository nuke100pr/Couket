package com.example.roparmarketplace.drawerFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class your_fragment extends Fragment {


    private TextView couponsSold,couponsPut,coupon1,coupon2,coupon3,contests_won,likes,money_earned;
    public your_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_your_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        couponsSold = view.findViewById(R.id.couponsSold);
        couponsPut = view.findViewById(R.id.couponsPut);
        coupon1 = view.findViewById(R.id.coupon1);
        coupon2 = view.findViewById(R.id.coupon2);
        coupon3 = view.findViewById(R.id.coupon3);
        contests_won = view.findViewById(R.id.contestsWon);
        likes = view.findViewById(R.id.likes);
        money_earned=view.findViewById(R.id.money_earned);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.COUPON_SOLD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                couponsSold.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.COUPONS_PUT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                couponsPut.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot ds : snapshot.getChildren())
                {

                    i+=1;
                    if(i==1)
                    {
                        long time = Long.parseLong(ds.child(nodenames.COUPON_TIME).getValue().toString());
                        time +=25200000;
                        String s = sfd.format(time);
                        coupon1.setText(s);

                    }
                    else if(i==2)
                    {
                        long time = Long.parseLong(ds.child(nodenames.COUPON_TIME).getValue().toString());
                        time +=25200000;
                        String s = sfd.format(time);
                        coupon2.setText(s);
                    }
                    else
                    {
                        long time = Long.parseLong(ds.child(nodenames.COUPON_TIME).getValue().toString());
                        time +=25200000;
                        String s = sfd.format(time);
                        coupon3.setText(s);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.CONTESTS_TAKEN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contests_won.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.MONEY_EARNED).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                money_earned.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.PROFILE_VIEWS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}