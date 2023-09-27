package com.example.roparmarketplace.tab_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.R;
import com.example.roparmarketplace.breakfastFragment.breakfastCouponAdapter;
import com.example.roparmarketplace.breakfastFragment.breakfastCouponModel;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class lunch_fragment extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private  breakfastCouponAdapter adapter;
    private List<breakfastCouponModel> breakfastCouponModelList;
    static int PERMISSION_CODE = 100;

    private FirebaseUser currentuser;
    public lunch_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lunch_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvLunchList);
        progressBar = view.findViewById(R.id.lunch_list_progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        breakfastCouponModelList = new ArrayList<>();
        adapter = new breakfastCouponAdapter(getActivity(),breakfastCouponModelList);
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentuser.getUid();
        ImageView baby = view.findViewById(R.id.gifBaby);
        Glide.with(getActivity()).load(R.drawable.baby).into(baby);
        progressBar.setVisibility(View.VISIBLE);
        final int[] c = {1};

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 breakfastCouponModelList.clear();
                for(DataSnapshot ds :snapshot.getChildren() ){
                    if(ds.exists())
                    {
                        String userId = ds.getKey().toString();
                        if(!userId.equals(currentUserId))
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
                                                    if(snapshot.child(nodenames.COUPON_TYPE).getValue().toString().trim().equals("Kanaka-lunch")||snapshot.child(nodenames.COUPON_TYPE).getValue().toString().trim().equals("Bhopal-lunch"))
                                                    {
                                                        if((snapshot.exists())) {
                                                            String coup_type = snapshot.child(nodenames.COUPON_TYPE).getValue().toString();
                                                            String coup_date = snapshot.child(nodenames.COUPON_DATE).getValue().toString();
                                                            String coup_price = snapshot.child(nodenames.COUPON_PRICE).getValue().toString();
                                                            String seller_name = snapshot.child(nodenames.COUPON_NAME).getValue()!=null?snapshot.child(nodenames.COUPON_NAME).getValue().toString():"";
                                                            String seller_phone = snapshot.child(nodenames.COUPON_PHONE).getValue().toString();
                                                            String seller_id= snapshot.child(nodenames.COUPON_ID).getValue().toString();
                                                            Boolean isCall = (boolean)snapshot.child(nodenames.COUPON_CALL).getValue();
                                                            databaseReference1.child(userId).getParent();
                                                            breakfastCouponModel breakfastCouponModel = new breakfastCouponModel(coup_type, coup_date, coup_price, seller_name,isCall,seller_phone,seller_id);
                                                            breakfastCouponModelList.add(breakfastCouponModel);
                                                            Collections.sort(breakfastCouponModelList, new Comparator<com.example.roparmarketplace.breakfastFragment.breakfastCouponModel>() {
                                                                @Override
                                                                public int compare(com.example.roparmarketplace.breakfastFragment.breakfastCouponModel o1, com.example.roparmarketplace.breakfastFragment.breakfastCouponModel o2) {
                                                                    return Integer.parseInt(o1.getCoupon_price())-Integer.parseInt(o2.getCoupon_price());
                                                                }
                                                            });
                                                            adapter.notifyDataSetChanged();
                                                            progressBar.setVisibility(View.GONE);
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}