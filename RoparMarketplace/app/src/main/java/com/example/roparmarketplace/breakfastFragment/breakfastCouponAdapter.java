package com.example.roparmarketplace.breakfastFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roparmarketplace.chatTabs.requestsAndChats;
import com.example.roparmarketplace.utility_classes.Constants;
import com.example.roparmarketplace.R;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.example.roparmarketplace.tab_fragments.tabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.List;

public class breakfastCouponAdapter extends RecyclerView.Adapter<breakfastCouponAdapter.breakfastViewHolder> {



    public breakfastCouponAdapter(Context context, List<breakfastCouponModel> breakfastCouponModelList) {
        this.context = context;
        this.breakfastCouponModelList = breakfastCouponModelList;
    }

    private Context context;

    private List<breakfastCouponModel> breakfastCouponModelList;
    @NonNull
    @Override
    public breakfastCouponAdapter.breakfastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.breakfast_display_layout,parent,false);
        return new breakfastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull breakfastCouponAdapter.breakfastViewHolder holder, int position) {
           breakfastCouponModel breakfastCouponModel =  breakfastCouponModelList.get(position);
           holder.coupon_type.setText(breakfastCouponModel.getCoupon_Type());
           holder.coupon_date.setText(breakfastCouponModel.getCoupon_date());
           holder.coupon_price.setText(breakfastCouponModel.getCoupon_price());
           holder.seller_name.setText(breakfastCouponModel.getSeller_name());



           if(breakfastCouponModel.getCoupon_Type().equals("Kanaka-breakfast"))
           {
               if(Integer.parseInt(breakfastCouponModel.getCoupon_price())<40) {
                   holder.discount.setText(((40 - Integer.parseInt(breakfastCouponModel.getCoupon_price()))*100)/40+"% OFF");
               }
           }
           else if(breakfastCouponModel.getCoupon_Type().equals("Kanaka-lunch"))
           {
               if(Integer.parseInt(breakfastCouponModel.getCoupon_price())<60) {
                   holder.discount.setText(((60 - Integer.parseInt(breakfastCouponModel.getCoupon_price()))*100)/60+"% OFF");
               }
           }
           else if(breakfastCouponModel.getCoupon_Type().equals("Kanaka-dinner"))
           {
               if(Integer.parseInt(breakfastCouponModel.getCoupon_price())<70) {
                   holder.discount.setText( ((70 - Integer.parseInt(breakfastCouponModel.getCoupon_price()))*100)/70+"% OFF");
               }
           }
           else if(breakfastCouponModel.getCoupon_Type().equals("Bhopal-breakfast"))
           {
               if(Integer.parseInt(breakfastCouponModel.getCoupon_price())<40) {
                   holder.discount.setText(((40 - Integer.parseInt(breakfastCouponModel.getCoupon_price()))*100)/40+"% OFF");
               }
           }
           else if(breakfastCouponModel.getCoupon_Type().equals("Bhopal-lunch"))
           {
               if(Integer.parseInt(breakfastCouponModel.getCoupon_price())<60) {
                   holder.discount.setText(((60 - Integer.parseInt(breakfastCouponModel.getCoupon_price()))*100)/60+"% OFF");
               }
           }
           else
           {
               if(Integer.parseInt(breakfastCouponModel.getCoupon_price())<70) {
                   holder.discount.setText(((70 - Integer.parseInt(breakfastCouponModel.getCoupon_price()))*100)/70+"% OFF");

               }
           }

          // if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED)
          // {
               if(breakfastCouponModel.getCall()) {

                       holder.call.setVisibility(View.VISIBLE);
                       holder.call.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                   String phoneNumber = breakfastCouponModel.getSeller_phone();
                                   Intent i = new Intent(Intent.ACTION_CALL);
                                   i.setData(Uri.parse("tel:" + phoneNumber));
                                   context.startActivity(i);
                               } else {
                                   ActivityCompat.requestPermissions((tabs) context, new String[]{Manifest.permission.CALL_PHONE}, 100);

                               }
                           }
                       });

               }
               else
               {
                   holder.call.setVisibility(View.GONE);
               }
               DatabaseReference databaseReferenceRequests = FirebaseDatabase.getInstance().getReference().child(nodenames.FRIEND_REQUESTS);
               DatabaseReference databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child(nodenames.CHATS);
               FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
               String cuid = currentUser.getUid();
               String ouid = breakfastCouponModel.getSeller_id();
               holder.chat.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                   databaseReferenceChats.child(cuid).child(ouid).child(nodenames.TIME_STAMP).setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful())
                           {
                               databaseReferenceChats.child(ouid).child(cuid).child(nodenames.TIME_STAMP).setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                          if(task.isSuccessful())
                                          {
                                              databaseReferenceRequests.child(cuid).child(ouid).child(nodenames.REQUEST_TYPE).setValue(Constants.REQUEST_STATUS_ACCEPTED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                      if(task.isSuccessful())
                                                      {
                                                          databaseReferenceRequests.child(ouid).child(cuid).child(nodenames.REQUEST_TYPE).setValue(Constants.REQUEST_STATUS_ACCEPTED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                 if(task.isSuccessful())
                                                                 {
                                                                     context.startActivity(new Intent(context,requestsAndChats.class));
                                                                 }
                                                              }
                                                          });
                                                      }
                                                  }
                                              });
                                          }
                                   }
                               });
                           }
                       }
                   });
                   }
               });
    }


    @Override
    public int getItemCount() {
        return breakfastCouponModelList.size();
    }

    public class breakfastViewHolder extends RecyclerView.ViewHolder {
        private TextView coupon_type,coupon_date,coupon_price,seller_name,discount;
        private Button chat,call;
        public breakfastViewHolder(@NonNull View itemView) {
            super(itemView);

            coupon_type = itemView.findViewById(R.id.coupon_type_breakfast2);
            coupon_date = itemView.findViewById(R.id.coupon_date_breakfast2);
            coupon_price = itemView.findViewById(R.id.coupon_price_breakfast2);
            seller_name = itemView.findViewById(R.id.coupon_seller_breakfast2);
            call = itemView.findViewById(R.id.call_breakfast);
            chat = itemView.findViewById(R.id.chat_breakfast);
            discount = itemView.findViewById(R.id.discount);
        }
    }


}
