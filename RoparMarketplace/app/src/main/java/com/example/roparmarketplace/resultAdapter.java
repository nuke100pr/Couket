package com.example.roparmarketplace;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class resultAdapter extends RecyclerView.Adapter<resultAdapter.resultViewHolder> {

    private Context context;
    private List<resultModel> resultModelList;

    public resultAdapter(Context context, List<resultModel> resultModelList) {
        this.context = context;
        this.resultModelList = resultModelList;
    }

    @NonNull
    @Override
    public resultAdapter.resultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_layout,parent,false);
        return new resultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull resultAdapter.resultViewHolder holder, int position) {

        resultModel resultModel = resultModelList.get(position);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String img = resultModel.getPhoto();

        FirebaseStorage.getInstance().getReference().child("images").child(img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri).placeholder(R.drawable.profile_pic)
                        .error(R.drawable.profile_pic)
                        .into(holder.resultPhoto);
            }
        });

        holder.name.setText(resultModel.getName());
        holder.score.setText(resultModel.getScore());
//        holder.time.setText(resultModel.getTime());


    }

    @Override
    public int getItemCount() {
        return resultModelList.size();
    }

    public class resultViewHolder extends RecyclerView.ViewHolder {
        private ImageView resultPhoto;
        private TextView name ,score,time;
        public resultViewHolder(@NonNull View itemView) {
            super(itemView);

            resultPhoto = itemView.findViewById(R.id.resultPhoto);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
            time = itemView.findViewById(R.id.scoreTime);
        }
    }
}
