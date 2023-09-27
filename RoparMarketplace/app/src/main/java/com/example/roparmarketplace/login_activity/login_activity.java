package com.example.roparmarketplace.login_activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roparmarketplace.mainActivity.MainActivity;
import com.example.roparmarketplace.R;
import com.example.roparmarketplace.resetPassword;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.example.roparmarketplace.utility_classes.util;
import com.example.roparmarketplace.forgotPassword.forgot_password;
import com.example.roparmarketplace.signupActivity.signup_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class login_activity extends AppCompatActivity {

    private TextView signup_button ,login_email,login_password;
    private Button login_button,forgot_password_button;
    private String email , password;
    private ProgressBar progressBarLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        signup_button = findViewById(R.id.signup_new);
        login_button = findViewById(R.id.login_button);
        forgot_password_button = findViewById(R.id.forgot_password_button);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        progressBarLogin = findViewById(R.id.progressBarLogin);

    }

    public void tvSignUpClick(View v)
    {
        startActivity(new Intent(this,signup_activity.class));
    }

    public void btnLoginClick(View v)
    {
         email = login_email.getText().toString().trim();
         password = login_password.getText().toString().trim();

         if(email.isEmpty())
         {
             login_email.setError("Enter email");
         }
         else if(password.isEmpty())
         {
             login_password.setError("Enter password");
         }
         else
         {
               if(util.connectionAvailable(this))
               {
                   progressBarLogin.setVisibility(View.VISIBLE);
                   FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                   firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           progressBarLogin.setVisibility(View.GONE);
                           if(task.isSuccessful())
                           {
                               FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                                   if (!TextUtils.isEmpty(token)) {
                                       util.updateDeviceToken(login_activity.this,token);
                                       Log.d(TAG, "token successful : " + token);
                                   } else{
                                       Log.w(TAG, "token should not be null...");
                                   }
                               });

                               startActivity(new Intent(login_activity.this, MainActivity.class));
                               finishAffinity();
                           }
                           else
                           {
                               Toast.makeText(login_activity.this, R.string.failed_to_login, Toast.LENGTH_LONG).show();
                           }
                       }
                   });


               }
               else
               {
                   Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
               }
         }
    }

    public void resetPasswordClick(View v)
    {
        startActivity(new Intent(this, resetPassword.class));
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            FirebaseDatabase.getInstance().getReference().child(nodenames.OTHERS).child(nodenames.VERSION).child("version").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.getValue().toString().equals(nodenames.VERSION_NO))
                    {
                        /*Toast.makeText(login_activity.this, "PLZ DOWNLOAD THE LATEST VERSION", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        AlertDialog.Builder builder = new AlertDialog.Builder(login_activity.this);
                        builder.setMessage("INSTALL LATEST VERSION");
                        builder.setTitle("OLD VERSION DETECTED?");
                        builder.setCancelable(false);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();*/
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                if (!TextUtils.isEmpty(token)) {
                    util.updateDeviceToken(this,token);
                    Log.d(TAG, "token successful : " + token);
                } else{
                    Log.w(TAG, "token should not be null...");
                }
            });
            startActivity(new Intent(login_activity.this,MainActivity.class));
            finishAffinity();
        }

    }
}