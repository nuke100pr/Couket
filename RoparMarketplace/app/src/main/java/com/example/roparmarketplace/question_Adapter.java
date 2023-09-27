package com.example.roparmarketplace;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.List;

public class question_Adapter extends RecyclerView.Adapter<question_Adapter.questionViewHolder> {

    private Context context;
    private List<question_Model> question_modelList;
    private RadioGroup radioGroup;

    public question_Adapter(Context context, List<question_Model> question_modelList) {
        this.context = context;
        this.question_modelList = question_modelList;
    }


    @NonNull
    @Override
    public question_Adapter.questionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_layout,parent,false);
        return new questionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull question_Adapter.questionViewHolder holder, int position) {
      question_Model question_model = question_modelList.get(position);
      holder.question_no.setText(question_model.getQuestionNo());
      String photoUrl = question_model.getPhotoName();

        FirebaseStorage.getInstance().getReference().child("contest").child(photoUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri).placeholder(R.drawable.img)
                        .error(R.drawable.img)
                        .into(holder.ivQuestion);
            }
        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(nodenames.INDIVIDUAL);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().trim().equals(""))
                {
                    holder.radioGroup.clearCheck();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.option1:
                        if(question_model.getCorrectOption().equals("A"))
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(true);
                        }
                        else
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(false);
                        }
                        break;
                    case R.id.option2:
                        if(question_model.getCorrectOption().equals("B"))
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(true);
                        }
                        else
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(false);
                        }
                        break;
                    case R.id.option3:
                        if(question_model.getCorrectOption().equals("C"))
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(true);
                        }
                        else
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(false);
                        }
                        break;
                    case R.id.option4:
                        if(question_model.getCorrectOption().equals("D"))
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(true);
                        }
                        else
                        {
                            ref.child(currentUser.getUid()).child(question_model.getQuestionNo()).setValue(false);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return question_modelList.size();
    }

    public class questionViewHolder extends RecyclerView.ViewHolder {

        private TextView question_no;
        private ImageView ivQuestion;
        private RadioGroup radioGroup;

        public questionViewHolder(@NonNull View itemView) {
            super(itemView);
            question_no = itemView.findViewById(R.id.question_no);
            ivQuestion = itemView.findViewById(R.id.ivQuestion);
            radioGroup =itemView.findViewById(R.id.radioGroup);



        }
    }
}
