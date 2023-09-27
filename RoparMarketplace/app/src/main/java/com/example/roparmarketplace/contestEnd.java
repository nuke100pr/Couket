package com.example.roparmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class contestEnd extends AppCompatActivity {

    private TextView score,time;
    private Button viewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_end);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        score = findViewById(R.id.score);
        time = findViewById(R.id.scoreTime);
        viewResult = findViewById(R.id.btnResult);


        FirebaseDatabase.getInstance().getReference().child(nodenames.CONTEST_RESULTS).child(currentUser.getUid()).child(nodenames.SCORE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                score.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(contestEnd.this,contestResult.class));
            }
        });


    }
}