package com.example.roparmarketplace.utility_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class common_methods {

    public static void onClickLogout(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?");
        builder.setTitle("Log Out?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    public void updateDrawerProfile(TextView drawer_profile_name, TextView drawer_profile_email)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(userId);
        String name = firebaseDatabase.child(nodenames.NAME).toString();
        String email = firebaseDatabase.child(nodenames.EMAIL).toString();

        drawer_profile_name.setText(name);
        drawer_profile_email.setText(email);
    }
}
