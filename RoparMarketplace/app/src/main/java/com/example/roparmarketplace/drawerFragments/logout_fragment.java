package com.example.roparmarketplace.drawerFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class logout_fragment extends Fragment {

    private CheckBox chatCb ,couponCb;

    public logout_fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_logout_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatCb = view.findViewById(R.id.checkBoxChatNotifications);
        couponCb = view.findViewById(R.id.checkBoxCouponNotifications);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.NOTIFICATION_TRUTH_CHATS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean truth = snapshot.getValue()!=null?(boolean)snapshot.getValue():false;
                chatCb.setChecked(truth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.NOTIFICATION_TRUTH_COUPONS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean truth = snapshot.getValue()!=null?(boolean)snapshot.getValue():false;
                couponCb.setChecked(truth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.NOTIFICATION_TRUTH_CHATS).setValue(isChecked);

            }
        });
        couponCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUser.getUid()).child(nodenames.NOTIFICATION_TRUTH_COUPONS).setValue(isChecked);

            }
        });
    }
}