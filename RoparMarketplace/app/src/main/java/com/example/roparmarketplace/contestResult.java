package com.example.roparmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class contestResult extends AppCompatActivity {

    private RecyclerView rvResult;
    private resultAdapter adapter;
    private List<resultModel> resultModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_resullt);
        rvResult = findViewById(R.id.rvResult);

        rvResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        resultModelList= new ArrayList<>();
        adapter = new resultAdapter(contestResult.this,resultModelList);
        rvResult.setAdapter(adapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();



        ref.child(nodenames.INDIVIDUAL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds.exists())
                    {
                        String userId = ds.getKey();
                        final String[] userName = new String[1];

                        String photo = userId+".jpg";
                        final int[] score = {0};
                        ref.child(nodenames.CONTEST_RESULTS).child(userId).child(nodenames.SCORE).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    score[0] = Integer.parseInt(snapshot.getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        ref.child(nodenames.USERS).child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    userName[0] = snapshot.getValue().toString();
                                    resultModel resultModel = new resultModel(snapshot.getValue().toString(),photo, score[0]+""," ");
                                    resultModelList.add(resultModel);
                                    adapter.notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                       // ref.child(nodenames.CONTEST_RESULTS).child(userId).child(nodenames.)


                       /* Collections.sort(resultModelList, new Comparator<com.example.roparmarketplace.resultModel>() {
                            @Override
                            public int compare(com.example.roparmarketplace.resultModel o1, com.example.roparmarketplace.resultModel o2) {
                                int score1 = Integer.parseInt(o1.getScore());
                                int score2 = Integer.parseInt(o2.getScore());
                                int time1= Integer.parseInt(o1.getTime());
                                int time2 = Integer.parseInt(o2.getTime());
                                if(score1-score2 !=0)
                                {
                                    return score2 - score1;
                                }
                                else
                                {
                                    return time1 - time2;
                                }
                            }
                        });*/

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }
}