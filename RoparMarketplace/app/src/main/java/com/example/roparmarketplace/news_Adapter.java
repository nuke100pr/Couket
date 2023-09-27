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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class news_Adapter extends RecyclerView.Adapter<news_Adapter.newsViewHolder> {


    private Context context;
    private List<news_Model> news_modelList;

    public news_Adapter(Context context, List<news_Model> news_modelList) {
        this.context = context;
        this.news_modelList = news_modelList;
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_layout,parent,false);
        return new newsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsViewHolder holder, int position) {
       news_Model news_model = news_modelList.get(position);
       holder.news_title.setText(news_model.getNewsTitle());
       SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
       String sus = sfd.format(Long.parseLong(news_model.getNewsTime()));
       holder.news_time.setText(sus);
       holder.news_content.setText(news_model.getNewsContent());
       String photoUrl = news_model.getPhotoName();
        FirebaseStorage.getInstance().getReference().child("news").child(photoUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
           public void onSuccess(Uri uri) {
               Glide.with(context)
                       .load(uri).placeholder(R.drawable.loading)
                       .error(R.drawable.loading)
                       .into(holder.news_photo);
           }
       });



    }

    @Override
    public int getItemCount() {
        return news_modelList.size();
    }

    public class newsViewHolder extends RecyclerView.ViewHolder {

        private TextView news_title ,news_content,news_time;
        private ImageView news_photo;
        public newsViewHolder(@NonNull View itemView) {
            super(itemView);

            news_title = itemView.findViewById(R.id.news_heading);
            news_content = itemView.findViewById(R.id.news_content);
            news_time = itemView.findViewById(R.id.news_time);
            news_photo= itemView.findViewById(R.id.ivNews);

        }
    }
}
