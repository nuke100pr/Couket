package com.example.roparmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.material.chip.ChipGroup;
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
import java.util.HashMap;
import java.util.List;

public class questions extends AppCompatActivity {
     private RecyclerView rvQuestions;
     private Button submit;
     private question_Adapter adapter;
     private List<question_Model> question_modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        rvQuestions = findViewById(R.id.rvContestQuestions);
        submit = findViewById(R.id.submit_contest);


        rvQuestions.setLayoutManager(new LinearLayoutManager(questions.this));
        question_modelList= new ArrayList<>();
        adapter = new question_Adapter(questions.this,question_modelList);
        rvQuestions.setAdapter(adapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ref.child(nodenames.CONTEST_VALUES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        String questionNo = ds.child(nodenames.QUESTION_NO).getValue().toString();
                        String photo =ds.child(nodenames.QUESTION_PHOTO).getValue().toString();
                        String correctOption = ds.child(nodenames.CORRECT_OPTION).getValue().toString();

                        question_Model q = new question_Model(photo,questionNo,correctOption);
                        question_modelList.add(q);
                        Collections.sort(question_modelList, new Comparator<question_Model>() {
                            @Override
                            public int compare(question_Model o1, question_Model o2) {
                                return Integer.parseInt(o1.getQuestionNo())-Integer.parseInt(o2.getQuestionNo());
                            }
                        });
                        adapter.notifyDataSetChanged();


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ref.child(nodenames.INDIVIDUAL).child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final int[] score = {0};
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            if(ds.exists())
                            {
                                if(!ds.getValue().equals("")) {
                                    if ((boolean) ds.getValue()) {
                                        score[0]++;
                                    }
                                }
                            }

                        }
                        HashMap hash =new HashMap();
                        hash.put(nodenames.SCORE, score[0]);
                        ref.child(nodenames.CONTEST_RESULTS).child(currentUser.getUid()).setValue(hash);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                startActivity(new Intent(questions.this,contestEnd.class));
            }
        });
    }

}