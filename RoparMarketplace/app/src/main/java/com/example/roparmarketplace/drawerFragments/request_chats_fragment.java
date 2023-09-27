package com.example.roparmarketplace.drawerFragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roparmarketplace.R;
import com.example.roparmarketplace.changePassword;
import com.example.roparmarketplace.utility_classes.nodenames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class request_chats_fragment extends Fragment {

    private Button btnSave;
    private TextInputEditText etName,etPhone;
    private ImageView ivProfile;
    private FirebaseUser firebaseuser ;
    private DatabaseReference databaseReference;
    private StorageReference filestorage;
    private Uri localfileuri ,serverfileuri;
    private FirebaseAuth fireBaseAuth;
    private TextView btnChangePassword;
    private ProgressBar pb;
    public request_chats_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_request_chats_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         etName = view.findViewById(R.id.etName3);
         etPhone = view.findViewById(R.id.etPhone3);
         btnSave = view.findViewById(R.id.btnSave);

        filestorage = FirebaseStorage.getInstance().getReference();
        ivProfile = view.findViewById(R.id.imageView3);
        btnChangePassword = view.findViewById(R.id.forgotPassword);

        fireBaseAuth = FirebaseAuth.getInstance();
        firebaseuser = fireBaseAuth.getCurrentUser();
        pb = view.findViewById(R.id.progressBarProfileImage);

        if (firebaseuser != null) {
            etName.setText(firebaseuser.getDisplayName());
                    FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(firebaseuser.getUid()).child(nodenames.PHONE).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String phone = snapshot.getValue().toString();
                            etPhone.setText(phone);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            serverfileuri = firebaseuser.getPhotoUrl();

            if (serverfileuri != null) {
                Glide.with(this)
                        .load(serverfileuri).placeholder(R.drawable.profile_pic)
                        .error(R.drawable.profile_pic)
                        .into(ivProfile);

            }
        }
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(v);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSaveClick(v);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChangePassword(v);
            }
        });

    }

    public void changeImage(View view) {

        if (serverfileuri == null) {
            pickImage();
        } else {


            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_picture, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    int id = menuItem.getItemId();
                    if (id == R.id.mnuChangePic) {
                        pickImage();
                    } else if (id == R.id.mnuRemovePic) {
                        removePhoto();
                    }
                    return false;
                }
            });
            popupMenu.show();


        }
    }


    public void btnSaveClick(View view)
    {
        if(etName.getText().toString().trim().isEmpty())
        {
            etName.setError(getString(R.string.enter_name));

        }
        else if(etPhone.getText().length()<10)
        {
            etPhone.setError("Enter valid phone number");
        }
        else if(etName.getText().toString().trim().length()<5)
        {
            etName.setError("Enter full name");
        }
        else
        {
            if(localfileuri!=null)
            {
                updateNameandPhoto();
            }
            else
            {
                updateOnlyName();
            }
        }
    }



    private void pickImage()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},102);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==102)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            }
            else
            {
                Toast.makeText(getContext(), R.string.permission_required, Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101) {
            if (resultCode == RESULT_OK)
            {
                localfileuri = data.getData();

                ivProfile.setImageURI(localfileuri);
            }
        }
    }




    private void removePhoto()
    {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(etName.getText().toString().trim())
                .setPhotoUri(null)
                .build();


        firebaseuser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String userID = firebaseuser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS);



                    databaseReference.child(userID).child(nodenames.PHOTO).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getContext(), "Photo Removed Successfully", Toast.LENGTH_SHORT).show();
                            ivProfile.setImageResource(R.drawable.profile_pic);


                        }

                    });

                }
                else {
                    Toast.makeText(getContext(), R.string.failed_to_update_profile, Toast.LENGTH_SHORT).show();
                }
            }




        });
    }



    private void updateNameandPhoto() {
        String strFileName = firebaseuser.getUid() + ".jpg";
        final StorageReference fileRef = filestorage.child("images/" + strFileName);
        pb.setVisibility(View.VISIBLE);
        ivProfile.setVisibility(View.GONE);
        fileRef.putFile(localfileuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            serverfileuri = uri;
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(etName.getText().toString().trim())
                                    .setPhotoUri(serverfileuri)
                                    .build();

                            firebaseuser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String userID = firebaseuser.getUid();
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS);




                                        databaseReference.child(userID).child(nodenames.NAME).setValue(etName.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    databaseReference.child(userID).child(nodenames.PHOTO).setValue(serverfileuri.getPath()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(userID).child(nodenames.PHONE).setValue(etPhone.getText().toString().trim());
                                                            pb.setVisibility(View.GONE);
                                                            ivProfile.setVisibility(View.VISIBLE);

                                                            Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                }




                                            }
                                        });

                                    } else {
                                        Toast.makeText(getContext(), R.string.failed_to_update_profile, Toast.LENGTH_SHORT).show();
                                        pb.setVisibility(View.GONE);
                                        ivProfile.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }

                    });
                }
            }
        });
    }




    public void updateOnlyName ()
    {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(etName.getText().toString().trim())
                .build();
        firebaseuser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String userID = firebaseuser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(nodenames.USERS);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(nodenames.NAME, etName.getText().toString().trim());


                    databaseReference.child(userID).child(nodenames.NAME).setValue(etName.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference().child(nodenames.USERS).child(userID).child(nodenames.PHONE).setValue(etPhone.getText().toString().trim());
                                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                } else {
                    Toast.makeText(getContext(), R.string.failed_to_update_profile, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void btnChangePassword(View view)
    {
        startActivity(new Intent(getContext() , changePassword.class));
    }
}
