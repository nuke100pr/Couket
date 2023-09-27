package com.example.roparmarketplace;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.mainActivity.MainActivity;
import com.example.roparmarketplace.selectFriendClasses.select_friend_activity;
import com.example.roparmarketplace.utility_classes.Constants;
import com.example.roparmarketplace.utility_classes.Extras;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.example.roparmarketplace.utility_classes.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chat_activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivSend, ivAttachment, ivProfile;
    private EditText etMessage;
    private TextView tvUserName ,tvUserStatus,encrypted;
    private DatabaseReference mRootRef;

    private FirebaseAuth firebaseAuth;
    private String currentUserId, chatUserId;

    private RecyclerView rvMessages;
    private SwipeRefreshLayout srlMessages;
    private Messages_Adapter messagesAdapter;
    private List<Messages_Model> messagesList;

    private int currentPage = 1;
    private static final int RECORD_PER_PAGE = 30;

    private DatabaseReference databaseReferenceMessages;
    private ChildEventListener childEventListener;

    private LinearLayout llProgress;
    private String userName, photoName;
    private Toolbar toolbar;

    public static Boolean isChatOpen;
    private AppBarLayout appBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbar_drawer2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

       if (actionBar != null) {
            actionBar.setTitle("");
            ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_action_bar, null);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            actionBar.setElevation(0);
            actionBar.setCustomView(actionBarLayout);
            actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        }


        ivProfile = findViewById(R.id.ivProfile766);
        tvUserName = findViewById(R.id.tvUserName);
        ivSend = findViewById(R.id.ivSend);
        ivAttachment = findViewById(R.id.ivAttachment);
        etMessage = findViewById(R.id.etMessage);
        tvUserStatus = findViewById(R.id.tvUserStatus);
        ivSend.setOnClickListener(this);
        ivAttachment.setOnClickListener(this);
        encrypted = findViewById(R.id.encrypted);

        llProgress = findViewById(R.id.llProgress44);

        firebaseAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        if (getIntent().hasExtra(Extras.USER_KEY)) {
            chatUserId = getIntent().getStringExtra(Extras.USER_KEY);
        }
        if (getIntent().hasExtra(Extras.USER_NAME)) {
            userName = getIntent().getStringExtra(Extras.USER_NAME);
        }
        if (getIntent().hasExtra(Extras.PHOTO_NAME)) {
            photoName = getIntent().getStringExtra(Extras.PHOTO_NAME);
        }



       tvUserName.setText(userName);
        String phto = chatUserId+".jpg";
        if (!photoName.equals("")) {
            StorageReference photoRef = FirebaseStorage.getInstance().getReference().child(Constants.IMAGES_FOLDER).child(phto);

            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(chat_activity.this)
                            .load(uri)
                            .placeholder(R.drawable.profile_pic)
                            .error(R.drawable.profile_pic)
                            .into(ivProfile);
                }
            });
        }
        rvMessages = findViewById(R.id.rvMessages);
        srlMessages = findViewById(R.id.srlMessages);

        messagesList = new ArrayList<>();
        messagesAdapter = new Messages_Adapter(this, messagesList);

        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(messagesAdapter);

        mRootRef.child(nodenames.CHATS).child(currentUserId).child(chatUserId).child(nodenames.UNREAD_COUNT).setValue(0);



        loadMessages();
        rvMessages.scrollToPosition(messagesList.size() - 1);

        srlMessages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage++;
                loadMessages();
            }
        });



        if(getIntent().hasExtra(Extras.MESSAGE)&&getIntent().hasExtra(Extras.MESSAGE_TYPE)&&getIntent().hasExtra(Extras.MESSAGE_ID))
        {
            String message =getIntent().getStringExtra(Extras.MESSAGE);
            String messageId =getIntent().getStringExtra(Extras.MESSAGE_ID);
            String messageType =getIntent().getStringExtra(Extras.MESSAGE_TYPE);

            DatabaseReference messageRef = mRootRef.child(nodenames.MESSAGES).child(currentUserId).child(chatUserId).push();
            String newMessageId = messageRef.getKey();
            sendMessage(message, messageType, newMessageId);
        }

        DatabaseReference databaseReferenceUsers = mRootRef.child(nodenames.USERS).child(chatUserId);
        databaseReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status ="";
                if(snapshot.child(nodenames.ONLINE).getValue()!=null)
                {
                    status = snapshot.child(nodenames.ONLINE).getValue().toString();
                }
                if(status.equals("true"))
                {
                   tvUserStatus.setText(Constants.STATUS_ONLINE);
                }
                else
                {
                   tvUserStatus.setText(Constants.STATUS_OFFLINE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference currentUserRef = mRootRef.child(nodenames.CHATS).child(currentUserId).child(chatUserId);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().matches(""))
                {
                    currentUserRef.child(nodenames.TYPING).setValue(Constants.TYPING_STOPPED);
                }
                else
                {
                    currentUserRef.child(nodenames.TYPING).setValue(Constants.TYPING_STARTED);
                }
            }
        });

        DatabaseReference chatUserRef = mRootRef.child(nodenames.CHATS).child(chatUserId).child(currentUserId);
        chatUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(nodenames.TYPING).getValue()!=null)
                {
                    String typingStatus = snapshot.child(nodenames.TYPING).getValue().toString();
                    if(typingStatus.equals(Constants.TYPING_STARTED))
                    {
                      tvUserStatus.setText(Constants.STATUS_TYPING);
                    }
                    else
                    {
                      tvUserStatus.setText(Constants.STATUS_ONLINE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        appBarLayout = findViewById(R.id.appBarLayout);
        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(chat_activity.this,userProfile.class);
                i.putExtra("userId",chatUserId);
                startActivity(i);
            }
        });

        encrypted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encrypted();
            }
        });

    }

    private void sendMessage(String msg, String msgType, String pushId) {
        try {





            if (!msg.equals("")) {
                HashMap messageApp = new HashMap();
                messageApp.put(nodenames.MESSAGE_ID, pushId);
                messageApp.put(nodenames.MESSAGE, msg);
                messageApp.put(nodenames.MESSAGE_TYPE, msgType);
                messageApp.put(nodenames.MESSAGE_FROM, currentUserId);
                messageApp.put(nodenames.MESSAGE_TIME, ServerValue.TIMESTAMP);

                String currentUserRef = nodenames.MESSAGES + "/" + currentUserId + "/" + chatUserId;
                String chatUserRef = nodenames.MESSAGES + "/" + chatUserId + "/" + currentUserId;

                HashMap messageUserMap = new HashMap();
                messageUserMap.put(currentUserRef + "/" + pushId, messageApp);
                messageUserMap.put(chatUserRef + "/" + pushId, messageApp);

                etMessage.setText("");

                mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                        if (error != null)
                        {
                            Toast.makeText(chat_activity.this, getString(R.string.failed_to_send_message, error.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(chat_activity.this, R.string.message_sent_successfully, Toast.LENGTH_SHORT).show();
                            String title="New message";
                            FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(chatUserId).child(nodenames.IS_CHATTING).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue().equals("false"))
                                    {
                                        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(chatUserId).child(nodenames.NOTIFICATION_TRUTH_CHATS).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Boolean tru2 = snapshot.getValue()!=null?(boolean)snapshot.getValue():false;
                                                if(tru2)
                                                {
                                                    util.sendNotification(chat_activity.this,title,msg,chatUserId);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            String lastMessage= !title.equals("New message")?title:msg;
                            util.updateChatDetails(chat_activity.this,currentUserId,chatUserId,lastMessage);
                        }
                    }
                });

            }

        } catch (Exception ex) {
            Toast.makeText(chat_activity.this, getString(R.string.failed_to_send_message, ex.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMessages() {
        messagesList.clear();
        databaseReferenceMessages = mRootRef.child(nodenames.MESSAGES).child(currentUserId).child(chatUserId);

        Query messageQuery = databaseReferenceMessages.limitToLast(currentPage * RECORD_PER_PAGE);

        if (childEventListener != null) {
            messageQuery.removeEventListener(childEventListener);
        }
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Messages_Model message = snapshot.getValue(Messages_Model.class);


                messagesList.add(message);
                messagesAdapter.notifyDataSetChanged();
                rvMessages.scrollToPosition(messagesList.size() - 1);
                srlMessages.setRefreshing(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                loadMessages();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                srlMessages.setRefreshing(false);
            }
        };

        messageQuery.addChildEventListener(childEventListener);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivSend:
                if (util.connectionAvailable(this)) {
                    DatabaseReference userMessagePush = mRootRef.child(nodenames.MESSAGES).child(currentUserId).child(chatUserId).push();
                    String pushId = userMessagePush.getKey();
                    sendMessage(etMessage.getText().toString().trim(), Constants.MESSAGE_TYPE_TEXT, pushId);
                } else {
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteMessage(String messageId) {
        DatabaseReference databaseReference = mRootRef.child(nodenames.MESSAGES)
                .child(currentUserId).child(chatUserId).child(messageId);


        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    DatabaseReference databaseReference2 = mRootRef.child(nodenames.MESSAGES)
                            .child(chatUserId).child(currentUserId).child(messageId);

                    databaseReference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(chat_activity.this, R.string.message_deleted_successfully, Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(chat_activity.this, getString(R.string.failed_to_delete_message, task.getException()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(chat_activity.this, getString(R.string.failed_to_delete_message, task.getException()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        mRootRef.child(nodenames.CHATS).child(currentUserId).child(chatUserId).child(nodenames.UNREAD_COUNT).setValue(0);
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUserId).child(nodenames.IS_CHATTING).setValue("true");

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(currentUserId).child(nodenames.IS_CHATTING).setValue("false");

    }
    public void encrypted()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(chat_activity.this);
        builder.setMessage("Messages are Encrypted and Dencrypted using unique key.Not even Couket can see your messages.");
        builder.setTitle("Encryption");
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}









