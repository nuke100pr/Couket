package com.example.roparmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class newsSetting extends AppCompatActivity {
    private TextInputEditText title,content,photo;
    private Button btnPut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_setting);
        title =findViewById(R.id.news1);
        content =findViewById(R.id.news2);
        photo =findViewById(R.id.news3);
        btnPut = findViewById(R.id.btnPut);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        btnPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref2 = ref.child(nodenames.NEWS_VALUES).push();
                String newsId = ref2.getKey();

                HashMap hashMap = new HashMap();
                hashMap.put(nodenames.NEWS_CONTENT,content.getText().toString().trim());
                hashMap.put(nodenames.NEWS_TITLE,title.getText().toString().trim());

                hashMap.put(nodenames.NEWS_TIME, ServerValue.TIMESTAMP);

               /* String s = "news/1.jpg";
                final String[] url = new String[1];
                final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(s);
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                         url[0] = uri.toString();

                    }
                });*/
                hashMap.put(nodenames.NEWS_PHOTO,photo.getText().toString().trim());

                ref.child(nodenames.NEWS_VALUES).child(newsId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(newsSetting.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}