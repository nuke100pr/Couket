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

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.contestSetting;
import com.example.roparmarketplace.questions;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class contest_fragment extends Fragment {

   private Button enterContest,setContest;

    public contest_fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contest_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enterContest = view.findViewById(R.id.enterContest);
        setContest = view.findViewById(R.id.setContest);

        enterContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("1","");
                hashMap.put("2","");
                hashMap.put("3","");
                hashMap.put("4","");
                hashMap.put("5","");
                hashMap.put("6","");
                hashMap.put("7","");
                hashMap.put("8","");
                hashMap.put("9","");
                hashMap.put("10","");

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference().child(nodenames.INDIVIDUAL).child(currentUser.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      startActivity(new Intent(getActivity(),questions.class));
                    }
                });
            }
        });
        setContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), contestSetting.class));
            }
        });
    }
}