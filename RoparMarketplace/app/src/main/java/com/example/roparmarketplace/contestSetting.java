package com.example.roparmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class contestSetting extends AppCompatActivity {

    private TextInputEditText correctOption,questionNo,photo;
    private Button btnPut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_setting);

        correctOption =findViewById(R.id.contest1);
        questionNo =findViewById(R.id.contest2);
        photo =findViewById(R.id.contest3);
        btnPut = findViewById(R.id.btnPutContest);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        btnPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref2 = ref.child(nodenames.CONTEST_VALUES).push();
                String newsId = ref2.getKey();

                HashMap hashMap = new HashMap();
                hashMap.put(nodenames.QUESTION_NO,questionNo.getText().toString().trim());
                hashMap.put(nodenames.QUESTION_PHOTO,photo.getText().toString().trim());
                hashMap.put(nodenames.CORRECT_OPTION, correctOption.getText().toString().trim());


                ref.child(nodenames.CONTEST_VALUES).child(newsId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(contestSetting.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}