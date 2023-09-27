package com.example.roparmarketplace.notification_classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.roparmarketplace.R;
import com.example.roparmarketplace.chatTabs.requestsAndChats;
import com.example.roparmarketplace.login_activity.login_activity;
import com.example.roparmarketplace.tab_fragments.tabs;
import com.example.roparmarketplace.utility_classes.Constants;
import com.example.roparmarketplace.utility_classes.util;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.example.roparmarketplace.chat_activity;

public class ChatMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        util.updateDeviceToken(this ,token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {






            super.onMessageReceived(message);
            String title = message.getData().get(Constants.NOTIFICATION_TITLE);
            String Rmessage = message.getData().get(Constants.NOTIFICATION_MESSAGE);

            Intent intentChat;
            if(title.trim().equals("New message")) {
                intentChat = new Intent(this, requestsAndChats.class);
            }
            else
            {
                intentChat = new Intent(this, tabs.class);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentChat, PendingIntent.FLAG_IMMUTABLE);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(Constants.CHANNEL_DESC);
                notificationManager.createNotificationChannel(channel);
                notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID);

            } else {

                notificationBuilder = new NotificationCompat.Builder(this);
            }
            notificationBuilder.setSmallIcon(R.drawable.notification_logo5);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_secondary));
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setAutoCancel(true);
           /* Notification note = notificationBuilder.build();
            note.sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification2);*/

            //notificationBuilder.setSound(Uri.parse("android.resource://"+ getApplicationContext().getPackageName() + "/" + R.raw.notification2));
            notificationBuilder.setSound(defaultSoundUri);


            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setContentText(Rmessage);
            notificationManager.notify(999, notificationBuilder.build());
        }
    }

