package com.example.pagetrade;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PdfItemBuyerActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    List<PdfItemModel> pdfItemModelList;
    Toolbar toolbar;
    EditText search;
    PdfItemAdapter adapter;
    private RecyclerView recyclerView;
    String selectedTitle, selectedCategoryKey;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_item_buyer);

        toolbar = findViewById(R.id.toolbar);
        search=findViewById(R.id.srcEt);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        String email= firebaseUser.getEmail();
        TextView ev= findViewById(R.id.subTitleTv);
        ev.setText(email);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("PdfItem");
        mStorage = FirebaseStorage.getInstance();
        selectedTitle = getIntent().getStringExtra("title");
        selectedCategoryKey = getIntent().getStringExtra("catKey");

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pdfItemModelList= new ArrayList<>();
        adapter = new PdfItemAdapter(PdfItemBuyerActivity.this, pdfItemModelList, false, selectedTitle, selectedCategoryKey,true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider))); // Set your divider drawable here
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(selectedTitle);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PdfItemModel pdfItemModel = snapshot.getValue(PdfItemModel.class);
                if (pdfItemModel != null && pdfItemModel.getCatKey().equals(selectedTitle)) {
                    pdfItemModelList.add(pdfItemModel);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    adapter.getFilter().filter(charSequence);
                } catch (Exception e) {}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}