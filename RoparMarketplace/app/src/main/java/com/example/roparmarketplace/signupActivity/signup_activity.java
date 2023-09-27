package com.example.roparmarketplace.signupActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roparmarketplace.R;
import com.example.roparmarketplace.login_activity.login_activity;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup_activity extends AppCompatActivity {

    private TextView signup_email ,signup_name,signup_password,signup_confirm_password,signup_phone;
    private Button signup_button;
    private String email ,name ,password ,confirm_password,phone;
    private ProgressBar progressBarSignup;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

            signup_email = findViewById(R.id.signup_email);
            signup_name = findViewById(R.id.signup_name);
            signup_password =findViewById(R.id.signup_password);
            signup_confirm_password = findViewById(R.id.signup_confirm_password);
            signup_button = findViewById(R.id.signup_button);
            progressBarSignup = findViewById(R.id.progressBarSignup);
            signup_phone = findViewById(R.id.signup_phone);
    }

    public void updateName()
    {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(signup_name.getText().toString().trim())
                .build();
        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String userId = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(nodenames.NAME, signup_name.getText().toString().trim());
                    hashMap.put(nodenames.EMAIL, signup_email.getText().toString().trim());
                    hashMap.put(nodenames.PHONE, signup_phone.getText().toString().trim());
                    hashMap.put(nodenames.ONLINE, "true");
                    hashMap.put(nodenames.STATUS, "");
                    hashMap.put(nodenames.PHOTO,"");
                    hashMap.put(nodenames.COUPON_SOLD,"0");
                    hashMap.put(nodenames.CONTESTS_TAKEN,"0");
                    hashMap.put(nodenames.MONEY_EARNED,"0");
                    hashMap.put(nodenames.PROFILE_VIEWS,"0");
                    hashMap.put(nodenames.COUPONS_PUT,"0");
                    hashMap.put(nodenames.IS_CHATTING,"true");
                    hashMap.put(nodenames.NOTIFICATION_TRUTH_COUPONS,"true");
                    hashMap.put(nodenames.NOTIFICATION_TRUTH_CHATS,"true");


                    databaseReference.child(userId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(userId).child(nodenames.NOTIFICATION_TRUTH_COUPONS).setValue(true);
                                FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(userId).child(nodenames.NOTIFICATION_TRUTH_CHATS).setValue(true);
                                Toast.makeText(signup_activity.this, R.string.user_created_successfully, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup_activity.this, login_activity.class));
                            } else {
                                Toast.makeText(signup_activity.this, R.string.failed_to_update_user_details, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else
                {
                    Toast.makeText(signup_activity.this, R.string.failed_to_update_user_details, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void btnSignUpClick(View v)
    {
         name = signup_name.getText().toString().trim();
         email = signup_email.getText().toString().trim();
         password = signup_password.getText().toString().trim();
         confirm_password = signup_confirm_password.getText().toString().trim();
         phone = signup_phone.getText().toString().trim();


         if(name.isEmpty())
         {
             signup_name.setError("Enter name");

         }
         else if(email.isEmpty())
         {
             signup_email.setError("Enter email");
         }
         else if(password.isEmpty())
         {
             signup_password.setError("Enter password");
         }
         else if(password.length()<6)
         {
             signup_password.setError("Minimum length of password is 6");
         }
         else if(confirm_password.isEmpty())
         {
             signup_confirm_password.setError("Confirm password");
         }
         else if(!password.equals(confirm_password))
         {
             signup_confirm_password.setError("Password and Confirm Password not equal");
         }
         else if(phone.length()<10)
         {
             signup_phone.setError("Phone number not valid");
         }
         else
         {

             progressBarSignup.setVisibility(View.VISIBLE);
             FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

             firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                         firebaseUser = firebaseAuth.getCurrentUser();
                         progressBarSignup.setVisibility(View.GONE);
                         updateName();
                     }
                     else
                     {
                         progressBarSignup.setVisibility(View.GONE);
                         Toast.makeText(signup_activity.this, getString(R.string.failed_to_signup,task.getException()), Toast.LENGTH_LONG).show();
                     }
                 }
             });

         }
    }

}