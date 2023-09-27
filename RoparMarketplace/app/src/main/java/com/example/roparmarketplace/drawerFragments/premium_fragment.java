package com.example.roparmarketplace.drawerFragments;

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
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class premium_fragment extends Fragment {
   private TextInputEditText supportReq;
   private Button btn;

    public premium_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_premium_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supportReq = view.findViewById(R.id.supportReq);
        btn = view.findViewById(R.id.btnSendSupport);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                supportReq.setVisibility(View.GONE);
               DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(nodenames.SUPPORT).push();
               HashMap hashMap = new HashMap();
               hashMap.put("message",supportReq.getText().toString().trim());
               databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(getContext(), "We will reply soon.", Toast.LENGTH_SHORT).show();
                   }
               });


            }
        });
    }
}