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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PdfItemDetailsActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    List<PdfItemModel> pdfItemModelList;
    Toolbar toolbar;
    PdfDetailsAdapter adapter;
    private RecyclerView recyclerView;
    String selectedTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_item_details);
        toolbar = findViewById(R.id.toolbar);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("PdfItem");
        mStorage = FirebaseStorage.getInstance();
        selectedTitle = getIntent().getStringExtra("pdfName");
        int position = getIntent().getIntExtra("position", -1);

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pdfItemModelList= new ArrayList<>();
        adapter = new PdfDetailsAdapter(PdfItemDetailsActivity.this, pdfItemModelList, false, selectedTitle, true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(selectedTitle);

        //Query query = mRef.orderByChild("bookName").equalTo(selectedTitle);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PdfItemModel pdfItemModel = snapshot.getValue(PdfItemModel.class);
                if (pdfItemModel != null && pdfItemModel.getPdfName().equals(selectedTitle)) {
                    pdfItemModel.setPdfUrl(snapshot.child("pdfFile").getValue(String.class)); // Set the PDF URL
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
    }
}