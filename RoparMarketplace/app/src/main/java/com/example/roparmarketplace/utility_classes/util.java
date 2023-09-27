package com.example.roparmarketplace.utility_classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.roparmarketplace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class util {

    public static boolean connectionAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null && connectivityManager.getActiveNetworkInfo()!=null)
        {
            return connectivityManager.getActiveNetworkInfo().isAvailable() ;

        }
        else
        {
            return false;
        }
    }


    public static void updateDeviceToken(Context context ,String token) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReference = rootRef.child(nodenames.TOKENS).child(currentUser.getUid());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(nodenames.DEVICE_TOKEN, token);

            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (!task.isSuccessful()) {
                        Toast.makeText(context, R.string.failed_add_device_token, Toast.LENGTH_SHORT).show();
                    }

                }

            });

        }
    }

    public static void sendNotification(Context context ,String title,String message,String userId)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = rootRef.child(nodenames.TOKENS).child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(nodenames.DEVICE_TOKEN).getValue()!=null)
                {
                    String deviceToken = snapshot.child(nodenames.DEVICE_TOKEN).getValue().toString();
                    JSONObject notification = new JSONObject();
                    JSONObject notificationData = new JSONObject();

                    try {
                        notificationData.put(Constants.NOTIFICATION_TITLE,title);
                        notificationData.put(Constants.NOTIFICATION_MESSAGE,message);

                        notification.put(Constants.NOTIFICATION_T0,deviceToken);
                        notification.put(Constants.NOTIFICATION_DATA,notificationData);


                        String fcmApiUrl = "https://fcm.googleapis.com/fcm/send";
                        String contentType = "application/json";



                        com.android.volley.Response.Listener successListener = new com.android.volley.Response.Listener() {
                            @Override
                            public void onResponse(Object response) {

                               // Toast.makeText(context, R.string.notification_sent, Toast.LENGTH_SHORT).show();
                            }
                        };

                        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                              //  Toast.makeText(context, context.getString( R.string.failed_to_send_notification,error.getMessage()), Toast.LENGTH_SHORT).show();
                            }
                        };

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(fcmApiUrl,notification,successListener,errorListener)
                        {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("Authorization","key="+Constants.FIREBASE_KEY);
                                params.put("Sender","id="+Constants.SENDER_ID);
                                params.put("Content-Type",contentType);
                                return params;

                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(jsonObjectRequest);




                    } catch (JSONException e) {
                        Toast.makeText(context, context.getString( R.string.failed_to_send_notification,e.getMessage()), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, context.getString( R.string.failed_to_send_notification,error.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static void updateChatDetails(Context context ,String currentUserId ,String chatUserId,String lastMessage)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference chatRef = rootRef.child(nodenames.CHATS).child(chatUserId).child(currentUserId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentCount="0";
                if(snapshot.child(nodenames.UNREAD_COUNT).getValue()!=null)
                {
                    currentCount = snapshot.child(nodenames.UNREAD_COUNT).getValue().toString();

                }


                Map chatMap = new HashMap();
                chatMap.put(nodenames.TIME_STAMP, ServerValue.TIMESTAMP);
                chatMap.put(nodenames.UNREAD_COUNT,Integer.valueOf(currentCount)+1);
                chatMap.put(nodenames.LAST_MESSSAGE,lastMessage);
                chatMap.put(nodenames.LAST_MESSSAGE_TIME,ServerValue.TIMESTAMP);

                Map chatUserMap = new HashMap();
                chatUserMap.put(nodenames.CHATS+"/"+chatUserId+"/"+currentUserId,chatMap);

                rootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error!=null)
                        {
                            Toast.makeText(context, context.getString(R.string.something_went_wrong,error.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong,error.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

 /*   public static void sendNotification2(Context context ,String title,String message,String userId,String coupon_Type)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = rootRef.child(nodenames.TOKENS).child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(nodenames.DEVICE_TOKEN).getValue()!=null)
                {
                    String deviceToken = snapshot.child(nodenames.DEVICE_TOKEN).getValue().toString();
                    JSONObject notification = new JSONObject();
                    JSONObject notificationData = new JSONObject();
                    String fullTitle2[] = new String[1];
                    try {
                        FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(userId).child(nodenames.NAME).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fullTitle = title+" "+coupon_Type +"from"+ snapshot.getValue();
                                fullTitle2[0]=fullTitle+" ";
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        notificationData.put(Constants.NOTIFICATION_TITLE,fullTitle2);
                        notificationData.put(Constants.NOTIFICATION_MESSAGE,message);

                        notification.put(Constants.NOTIFICATION_T0,deviceToken);
                        notification.put(Constants.NOTIFICATION_DATA,notificationData);

                        String fcmApiUrl = "https://fcm.googleapis.com/fcm/send";
                        String contentType = "application/json";



                        com.android.volley.Response.Listener successListener = new com.android.volley.Response.Listener() {
                            @Override
                            public void onResponse(Object response) {

                               // Toast.makeText(context, R.string.notification_sent, Toast.LENGTH_SHORT).show();
                            }
                        };

                        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(context, context.getString( R.string.failed_to_send_notification,error.getMessage()), Toast.LENGTH_SHORT).show();
                            }
                        };

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(fcmApiUrl,notification,successListener,errorListener)
                        {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("Authorization","key="+Constants.FIREBASE_KEY);
                                params.put("Sender","id="+Constants.SENDER_ID);
                                params.put("Content-Type",contentType);
                                return params;

                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(jsonObjectRequest);




                    } catch (JSONException e) {
                        Toast.makeText(context, context.getString( R.string.failed_to_send_notification,e.getMessage()), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, context.getString( R.string.failed_to_send_notification,error.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });


    }*/
    public static String getTimeAgo(long time)
    {
        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60*SECOND_MILLIS;
        final int HOUR_MILLIS = 60*MINUTE_MILLIS;
        final int DAY_MILLIS = 24*HOUR_MILLIS;


        time *=1000;
        long now = System.currentTimeMillis();
        if(time>now)
        {
            return "";
        }
        final  long diff = now-time;
        if(diff<MINUTE_MILLIS)
        {
            return "just now";
        }
        else if(diff<2*MINUTE_MILLIS)
        {
            return "a minute ago";
        }
        else if(diff<59*MINUTE_MILLIS)
        {
            return diff/MINUTE_MILLIS+"minutes ago ";
        }
        else if(diff<90*MINUTE_MILLIS)
        {
            return "an hour ago";
        }
        else if(diff<24*HOUR_MILLIS)
        {
            return diff/HOUR_MILLIS+"hours ago";
        }
        else if(diff<48*HOUR_MILLIS)
        {
            return "yesterday";
        }
        else
        {
            return "long time ago";
        }
    }
}

