package com.example.pagetrade;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.example.pagetrade.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageActivity;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyerProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private CircleImageView profileIv;
    private EditText name, address, phone;
    private FloatingActionButton saveBtn;
    private static final int IMAGE_REQ_CODE= 100;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private StorageReference storageProfileR;
    private static final int GalleryPick=1;
    private Uri imgUri;
    private String myUri="";
    long timestamp;
    private String email;
    private String curUserId;
    private ProgressDialog progressDialog;
    private StorageTask uploadTask;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        databaseRef= FirebaseDatabase.getInstance().getReference();
        storageProfileR = FirebaseStorage.getInstance().getReference().child("profilePic");
        FirebaseUser firebaseUser= mAuth.getCurrentUser();
        email= firebaseUser.getEmail();
        timestamp = System.currentTimeMillis();

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        profileIv=findViewById(R.id.profileIv);
        name=findViewById(R.id.nameEt);
        address=findViewById(R.id.addressEt);
        phone=findViewById(R.id.phoneEt);
        saveBtn=findViewById(R.id.saveBtn);
        progressDialog = new ProgressDialog(BuyerProfileActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        curUserId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        RetrieveUserInfo();
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });
    }

    private void RetrieveUserInfo() {
        databaseRef.child("Users").child(curUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        if((datasnapshot.exists()) && (datasnapshot.hasChild("name") && (datasnapshot.hasChild("image")))){
                            String retrieveUserName=datasnapshot.child("name").getValue().toString();
                            String retrieveUserPhone=datasnapshot.child("PhoneNo").getValue().toString();
                            String retrieveUserAd=datasnapshot.child("address").getValue().toString();
                            String retrieveProfileImage = datasnapshot.child("profileImg").getValue().toString();
                            Picasso.get().load(retrieveProfileImage).into(profileIv);
                            name.setText(retrieveUserName);
                            phone.setText(retrieveUserPhone);
                            address.setText(retrieveUserAd);
                            myUri=retrieveProfileImage;
                        }
                        else if((datasnapshot.exists()) && (datasnapshot.hasChild("name"))){
                            String retrieveUserName=datasnapshot.child("name").getValue().toString();
                            String retrieveUserPhone=datasnapshot.child("PhoneNo").getValue().toString();
                            String retrieveUserAd=datasnapshot.child("address").getValue().toString();
                            String retrieveProfileImage=datasnapshot.child("profileImg").getValue().toString();
                            name.setText(retrieveUserName);
                            phone.setText(retrieveUserPhone);
                            address.setText(retrieveUserAd);
                        }
                        else{
                            name.setVisibility(View.VISIBLE);
                            Toast.makeText(BuyerProfileActivity.this, "Please set & update your profile information", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void updateProfile() {
        String setName = name.getText().toString();
        String setAddress = address.getText().toString();
        String setPhone = phone.getText().toString();

        if (TextUtils.isEmpty(setName) && TextUtils.isEmpty(setAddress) && TextUtils.isEmpty(setPhone)) {
            Toast.makeText(BuyerProfileActivity.this, "Please enter at least one information", Toast.LENGTH_SHORT).show();
        } else {
            // Create a HashMap to store the updated profile information
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("name", setName);
            profileMap.put("address", setAddress);
            profileMap.put("PhoneNo", setPhone);

            // Update the user's information in the Firebase Realtime Database
            DatabaseReference userRef = databaseRef.child("Users").child(curUserId);
            userRef.updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // If the update is successful, you can also update the user's information in the current FirebaseUser
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(setName)
                                        .build();
                                currentUser.updateProfile(profileUpdates);

                                Toast.makeText(BuyerProfileActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(BuyerProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == GalleryPick) {
                progressDialog.setMessage("Uploading profile photo...");
                progressDialog.show();
                imgUri = data.getData();
                if (imgUri != null) {
                    StorageReference filePath = storageProfileR.child(curUserId + ".jpg");

                    try {
                        filePath.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(BuyerProfileActivity.this, "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final String downloadUrl = uri.toString();
                                            myUri = downloadUrl;
                                            Log.d("ProfileImage", "Download URL: " + downloadUrl);

                                            Picasso.get().load(downloadUrl).into(profileIv);

                                            HashMap<String, Object> profileMap = new HashMap<>();
                                            profileMap.put("uid", curUserId);
                                            profileMap.put("email", email);
                                            profileMap.put("name", name);
                                            profileMap.put("address", address);
                                            profileMap.put("PhoneNo", phone);
                                            profileMap.put("timestamp", timestamp);

                                            if (!TextUtils.isEmpty(myUri)) {
                                                profileMap.put("profileImg", myUri);
                                            }

                                            databaseRef.child("Users").child(curUserId).child("profileImg")
                                                    .setValue(downloadUrl)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(BuyerProfileActivity.this, "Image saved in Database Successfully", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            } else {
                                                                String message = task.getException().toString();
                                                                Toast.makeText(BuyerProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(BuyerProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(BuyerProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(BuyerProfileActivity.this, "Image Uri is null", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id ==R.id.nav_home){
            Intent cartIntent = new Intent(BuyerProfileActivity.this, MainBuyerActivity.class);
            startActivity(cartIntent);
        }  else if(id ==R.id.nav_cart){
            Intent cartIntent = new Intent(BuyerProfileActivity.this, CartActivity.class);
            startActivity(cartIntent);
        } else if(id==R.id.nav_addbook){
            Intent viewOrderIntent = new Intent(BuyerProfileActivity.this, AddedBookItemActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_vieworder){
            Intent viewOrderIntent = new Intent(BuyerProfileActivity.this, VieworderBuyerActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent viewOrderIntent = new Intent(BuyerProfileActivity.this, LoginActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_share){
            Intent viewOrderIntent = new Intent(BuyerProfileActivity.this, ShareBuyerActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_rate){
            Intent viewOrderIntent = new Intent(BuyerProfileActivity.this, RateusBuyerActivity.class);
            startActivity(viewOrderIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        FirebaseAuth.getInstance().signOut();
    }

}
