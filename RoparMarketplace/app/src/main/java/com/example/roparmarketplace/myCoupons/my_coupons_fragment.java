package com.example.roparmarketplace.myCoupons;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.modelClasses.my_coupons_Model;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class my_coupons_fragment extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private my_coupons_Adapter adapter;
    private List<my_coupons_Model> my_coupons_modelList;

    private FirebaseUser currentuser;


    public my_coupons_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_coupons_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvMyCoupons);
        progressBar = view.findViewById(R.id.my_coupons_progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_coupons_modelList= new ArrayList<>();
        adapter = new my_coupons_Adapter(getActivity(),my_coupons_modelList);
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentuser.getUid();
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_coupons_modelList.clear();
                for(DataSnapshot ds :snapshot.getChildren() ){
                    if(ds.exists())
                    {
                        String userId = ds.getKey().toString();
                        if(userId.equals(currentUserId))
                        {
                            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot ds :snapshot.getChildren() ){
                                        String requestId = ds.getKey().toString();
                                        if(ds.exists())
                                        {
                                            databaseReference.child(userId).child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    //if(snapshot.child(nodenames.COUPON_TYPE).getValue().toString().trim().equals("Kanaka-breakfast")||snapshot.child(nodenames.COUPON_TYPE).getValue().toString().trim().equals("Bhopal-breakfast"))
                                                   // {
                                                        if((snapshot.exists())) {
                                                            String coup_type = snapshot.child(nodenames.COUPON_TYPE).getValue().toString();
                                                            String coup_date = snapshot.child(nodenames.COUPON_DATE).getValue().toString();
                                                            String coup_price = snapshot.child(nodenames.COUPON_PRICE).getValue().toString();
                                                            String seller_name = snapshot.child(nodenames.COUPON_NAME).getValue()!=null?snapshot.child(nodenames.COUPON_NAME).getValue().toString():"";



                                                            databaseReference1.child(userId).getParent();
                                                            my_coupons_Model mcm = new my_coupons_Model(coup_type, coup_date,requestId, coup_price);
                                                            my_coupons_modelList.add(mcm);
                                                            adapter.notifyDataSetChanged();

                                                            progressBar.setVisibility(View.GONE);
                                                       // }

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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}