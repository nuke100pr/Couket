package com.example.roparmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class userProfile extends AppCompatActivity {

    private ImageView ivProfile;
    private Button btn ;
    private TextView username ,contests_won,money_earned,coupons_sold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ivProfile = findViewById(R.id.ivUserProfile);
        username = findViewById(R.id.userProfile_name);
        contests_won = findViewById(R.id.userProfile_contestWon);
        money_earned = findViewById(R.id.userProfile_moneyEarned);
        coupons_sold = findViewById(R.id.userProfile_couponsSold);
        btn = findViewById(R.id.userProfile_like);

        Intent i = getIntent();
        String userId = i.getStringExtra("userId");
        String photo = userId +".jpg";
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();


        ref.child("images").child(photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(userProfile.this)
                        .load(uri).placeholder(R.drawable.loading)
                        .error(R.drawable.profile_pic)
                        .into(ivProfile);
            }
        });

        ref2.child(nodenames.USERS).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child(nodenames.NAME).getValue().toString();
                String contestsWon = snapshot.child(nodenames.CONTESTS_TAKEN).getValue()!=null?snapshot.child(nodenames.CONTESTS_TAKEN).getValue().toString():"0";
                String couponsSold = snapshot.child(nodenames.COUPON_SOLD).getValue()!=null?snapshot.child(nodenames.COUPON_SOLD).getValue().toString():"0";
                String moneyEarned = snapshot.child(nodenames.MONEY_EARNED).getValue()!=null?snapshot.child(nodenames.MONEY_EARNED).getValue().toString():"0";

                username.setText(userName);
                contests_won.setText(contestsWon);
                coupons_sold.setText(couponsSold);
                money_earned.setText(moneyEarned);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(userProfile.this, "Liked", Toast.LENGTH_SHORT).show();
                ref2.child(nodenames.USERS).child(userId).child(nodenames.PROFILE_VIEWS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int vt = snapshot.getValue()!=null?Integer.parseInt(snapshot.getValue().toString()):0;
                        vt++;
                        ref2.child(nodenames.USERS).child(userId).child(nodenames.PROFILE_VIEWS).setValue(vt);
                        v.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
        });


    }

}