package com.example.roparmarketplace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.login_activity.login_activity;
import com.example.roparmarketplace.mainActivity.MainActivity;
import com.example.roparmarketplace.utility_classes.Constants;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Messages_Adapter extends RecyclerView.Adapter<Messages_Adapter.MessageViewHolder>{

    private Context context;
    private List<Messages_Model> messageList;
    private FirebaseAuth firebaseAuth;

    private ActionMode actionMode;
    private ConstraintLayout selectedView;


    public Messages_Adapter(Context context, List<Messages_Model> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public Messages_Adapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.message_layout,parent,false);

        return new MessageViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull Messages_Adapter.MessageViewHolder holder, int position) {
           Messages_Model message = messageList.get(position);
           firebaseAuth =FirebaseAuth.getInstance();
           String currentUserId = firebaseAuth.getCurrentUser().getUid();
           String fromUserId= message.getMessageFrom();
           SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
           String dateTime = sfd.format(new Date(message.getMessageTime()));
           String [] splitString = dateTime.split(" ");
           String messageTime = splitString[1];


        if(fromUserId.equals(currentUserId))
        {
                holder.llSent.setVisibility(View.VISIBLE);
                holder.llSentImage.setVisibility(View.GONE);
                holder.llReceived.setVisibility(View.GONE);
                holder.llReceivedImage.setVisibility(View.GONE);
                holder.tvSentMessage.setText(message.getMessage());
                holder.tvSentMessageTime.setText(messageTime);
        }
        else
        {
                holder.llReceived.setVisibility(View.VISIBLE);
                holder.llReceivedImage.setVisibility(View.GONE);
                holder.llSent.setVisibility(View.GONE);
                holder.llSent.setVisibility(View.GONE);
                holder.tvReceivedMessage.setText(message.getMessage());
                holder.tvReceivedMessageTime.setText(messageTime);
        }
        holder.clMessage.setTag(R.id.TAG_MESSAGE,message.getMessage());
        holder.clMessage.setTag(R.id.TAG_MESSAGE_ID,message.getMessageId());


        holder.clMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedView = holder.clMessage;
               popup(v);
                return false;
            }
        });

      /*  holder.clMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedView = holder.clMessage;
                popup(v);
            }
        });*/



    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout llSent, llReceived, llSentImage, llReceivedImage;
        private TextView tvSentMessage, tvSentMessageTime, tvReceivedMessage, tvReceivedMessageTime, tvSentImageTime, tvReceivedImageTime;
        private ConstraintLayout clMessage;
        private ImageView ivSent, ivReceived;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            llSent = itemView.findViewById(R.id.llSent);
            llReceived = itemView.findViewById(R.id.llReceived);
            tvSentMessage = itemView.findViewById(R.id.tvSentMessage);
            tvSentMessageTime = itemView.findViewById(R.id.tvSentMessageTime);

            tvReceivedMessage = itemView.findViewById(R.id.tvReceivedMessage);
            tvReceivedMessageTime = itemView.findViewById(R.id.tvReceivedMessageTime);

            clMessage = itemView.findViewById(R.id.clMessage);

            llSentImage = itemView.findViewById(R.id.llSentImage);
            llReceivedImage = itemView.findViewById(R.id.llReceivedImage);
            ivSent = itemView.findViewById(R.id.ivSent);
            ivReceived = itemView.findViewById(R.id.ivReceived);

            tvSentImageTime = itemView.findViewById(R.id.tvSentImageTime);
            tvReceivedImageTime = itemView.findViewById(R.id.tvReceivedImageTime);



        }
    }

    public void popup(View v)
    {
        String selectedMessageId = selectedView.getTag(R.id.TAG_MESSAGE_ID).toString();


        PopupMenu popupMenu = new PopupMenu(context,v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.delete_message)
                {
                    ((chat_activity)context).deleteMessage(selectedMessageId);
                }
                return false;
            }
        });
        popupMenu.show();
    }


}


