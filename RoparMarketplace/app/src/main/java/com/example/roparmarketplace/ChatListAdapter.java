package com.example.roparmarketplace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.chatTabs.requestsAndChats;
import com.example.roparmarketplace.utility_classes.Constants;
import com.example.roparmarketplace.utility_classes.Extras;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.example.roparmarketplace.utility_classes.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context context;
    private List<ChatListModel> chatListModelList;
    private String userUid;

    public ChatListAdapter(Context context, List<ChatListModel> chatListModelList) {
        this.context = context;
        this.chatListModelList = chatListModelList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout,parent,false);

        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {


        ChatListModel chatListModel =chatListModelList.get(position);
        holder.tvFullName.setText(chatListModel.getUserName());

        StorageReference fileRef = FirebaseStorage.getInstance().getReference();
        String photo = chatListModel.getUserId()+".jpg";
        if(!chatListModel.getPhotoName().toString().equals("")) {
            fileRef.child("images").child(photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .placeholder(R.drawable.profile_pic)
                            .error(R.drawable.profile_pic)
                            .into(holder.ivProfile);
                }
            });
        }
       /* StorageReference mountainsRef = fileRef.child(chatListModel.getPhotoName());
        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.profile_pic)
                        .error(R.drawable.profile_pic)
                        .into(holder.ivProfile);
            }
        });*/

        String lastMessage = chatListModel.getLastMessage();
        lastMessage =lastMessage.length()>30?lastMessage.substring(0,30):lastMessage;
        holder.tvLastMessage.setText(lastMessage);

        String lastMessageTime = chatListModel.getLastMessageTime();
        if(lastMessageTime==null)
        {
           lastMessageTime="";
        }
        if(!TextUtils.isEmpty(lastMessageTime))
        {
            holder.tvLastMessageTime.setText(util.getTimeAgo(Long.parseLong(lastMessageTime)));
        }
        if(!chatListModel.getUnreadCount().equals("0"))
        {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(chatListModel.getUnreadCount());
        }
        else
        {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }

        holder.llChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chat_activity.class);
                intent.putExtra(Extras.USER_KEY,chatListModel.getUserId());
                intent.putExtra(Extras.USER_NAME,chatListModel.getUserName());
                intent.putExtra(Extras.PHOTO_NAME,chatListModel.getPhotoName());


                context.startActivity(intent);
            }
        });

        holder.llChatList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                userUid = chatListModel.getUserId();
                deleteChat(v);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatListModelList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llChatList;
        private TextView tvFullName ,tvLastMessage ,tvLastMessageTime,tvUnreadCount;
        private ImageView ivProfile;


        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            llChatList = itemView.findViewById(R.id.llChatList);
            tvFullName = itemView.findViewById(R.id.tvFullName655);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
            tvUnreadCount = itemView.findViewById(R.id.tvUnreadCount);
            ivProfile = itemView.findViewById(R.id.ivProfile655);
        }
    }

    public void deleteChat(View v)
    {


        PopupMenu popupMenu = new PopupMenu(context,v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_chat,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.delete_chat)
                {
                    DatabaseReference databaseReferenceRequests = FirebaseDatabase.getInstance().getReference().child(nodenames.FRIEND_REQUESTS);
                    DatabaseReference databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child(nodenames.CHATS);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String cuid = currentUser.getUid();
                    String ouid = userUid;

                    FirebaseDatabase.getInstance().getReference().child(nodenames.MESSAGES).child(cuid).child(ouid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child(nodenames.MESSAGES).child(ouid).child(cuid).removeValue();



                    databaseReferenceChats.child(cuid).child(ouid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful())
                                  {
                                      databaseReferenceChats.child(ouid).child(cuid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if(task.isSuccessful())
                                              {
                                                  ((requestsAndChats)context).finish();
                                                  context.startActivity(new Intent(context,requestsAndChats.class));
                                              }
                                          }
                                      });
                                  }
                              }
                          });
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
