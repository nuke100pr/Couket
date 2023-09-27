package com.example.roparmarketplace.myCoupons;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.chatTabs.requestsAndChats;
import com.example.roparmarketplace.modelClasses.my_coupons_Model;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class my_coupons_Adapter extends RecyclerView.Adapter<my_coupons_Adapter.my_coupons_ViewHolder> {


    private Context context;
    private List<my_coupons_Model> my_coupons_modelList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private final int[] count = {0};

    public my_coupons_Adapter(Context context, List<my_coupons_Model> my_coupons_modelList) {
        this.context = context;
        this.my_coupons_modelList = my_coupons_modelList;
    }

    @NonNull
    @Override
    public my_coupons_Adapter.my_coupons_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_coupons_layout,parent,false);

        return new my_coupons_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull my_coupons_Adapter.my_coupons_ViewHolder holder, int position) {


      my_coupons_Model my_coupons_model = my_coupons_modelList.get(position);
      String couponType = my_coupons_model.getCoupon_type();
      String couponDate = my_coupons_model.getCoupon_date();
      String couponPrice= my_coupons_model.getCoupon_price();
      String coupon_id = my_coupons_model.getUserId();

      holder.coupon_type.setText(couponType);
      holder.coupon_date.setText(couponDate);
      holder.coupon_price.setText(couponPrice);

      DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
      FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        mRootRef.child(nodenames.USERS).child(firebaseUser.getUid()).child(nodenames.COUPONS_PUT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count[0]=Integer.parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


      currentUser = FirebaseAuth.getInstance().getCurrentUser();
      databaseReference = FirebaseDatabase.getInstance().getReference().child(nodenames.REQUESTS).child(currentUser.getUid());
      holder.remove.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              count[0]-=1;
              mRootRef.child(nodenames.USERS).child(firebaseUser.getUid()).child(nodenames.COUPONS_PUT).setValue(count[0]);

             databaseReference.child(coupon_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful())
                     {
                         final int[] y = {0};
                         mRootRef.child(nodenames.USERS).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 y[0] =!snapshot.child(nodenames.COUPON_SOLD).getValue().toString().equals("")?Integer.parseInt(snapshot.child(nodenames.COUPON_SOLD).getValue().toString()):0;
                                 y[0] +=1;
                                 mRootRef.child(nodenames.USERS).child(firebaseUser.getUid()).child(nodenames.COUPON_SOLD).setValue(y[0]);
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });
                         v.setVisibility(View.GONE);
                         context.startActivity(new Intent(context, requestsAndChats.class));

                         Toast.makeText(context, R.string.coupon_removed_successfully, Toast.LENGTH_SHORT).show();
                         ((requestsAndChats)context).finish();



                     }
                     else
                     {
                         Toast.makeText(context,R.string.coupon_remove_failure, Toast.LENGTH_SHORT).show();
                     }
                 }

             });

          }
      });

    }

    @Override
    public int getItemCount() {
        return my_coupons_modelList.size();
    }

    public class my_coupons_ViewHolder extends RecyclerView.ViewHolder{

        private TextView coupon_type,coupon_price,coupon_date;
        private Button  sold , remove ;
        public my_coupons_ViewHolder(@NonNull View itemView) {
            super(itemView);

            coupon_type = itemView.findViewById(R.id.my_coupon_type2);
            coupon_date = itemView.findViewById(R.id.my_coupon_date2);
            coupon_price = itemView.findViewById(R.id.my_coupon_price2);
            remove=itemView.findViewById(R.id.remove_coupon_list);
        }
    }
}
