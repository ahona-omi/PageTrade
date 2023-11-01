package com.example.pagetrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pagetrade.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {
    private ActivityCategoryAddBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private  String category="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        category= binding.categoryEt.getText().toString().trim();

        if(TextUtils.isEmpty(category)){
            Toast.makeText(this,"Please Enter Category...!",Toast.LENGTH_SHORT).show();
        } else{
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {
        progressDialog.setMessage("Adding Category...");
        progressDialog.show();
        //get timestamp
        long timestamp= System.currentTimeMillis();

        //setup info to add in firebase
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("id", ""+timestamp);
        hashMap.put("category", ""+category);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("uid", ""+firebaseAuth.getUid());

        //add to firebase
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void unused){
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this,"Category added successfully...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}