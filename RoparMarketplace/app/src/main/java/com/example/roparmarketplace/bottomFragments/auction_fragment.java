package com.example.roparmarketplace.bottomFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.breakfastFragment.breakfastCouponAdapter;
import com.example.roparmarketplace.newsSetting;
import com.example.roparmarketplace.news_Adapter;
import com.example.roparmarketplace.news_Model;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class auction_fragment extends Fragment {

    private RecyclerView rvNews;
    private TextView emptyNews;
    private Button btnEx;
    private news_Adapter adapter;
    private List<news_Model> news_modelList;

       public auction_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_auction_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyNews = view.findViewById(R.id.empty_news);
        rvNews = view.findViewById(R.id.rvNews);
        btnEx = view.findViewById(R.id.btnExclusive);


        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        news_modelList= new ArrayList<>();
        adapter = new news_Adapter(getActivity(),news_modelList);
        rvNews.setAdapter(adapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child(nodenames.NEWS_VALUES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        if(ds.exists())
                        {
                         String news_title = ds.child(nodenames.NEWS_TITLE).getValue().toString();
                         String news_time = ds.child(nodenames.NEWS_TIME).getValue().toString();
                         String news_content = ds.child(nodenames.NEWS_CONTENT).getValue().toString();
                         String news_photo= ds.child(nodenames.NEWS_PHOTO).getValue().toString();

                         news_Model news_model = new news_Model(news_photo,news_content,news_title,news_time);
                         news_modelList.add(news_model);
                         adapter.notifyDataSetChanged();
                         emptyNews.setVisibility(View.GONE);


                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), newsSetting.class));

            }
        });






    }
}