package com.example.pagetrade;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pagetrade.databinding.ActivityPdfBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PdfActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    private RecyclerView recyclerView;
    PdfCategoryAdapter adapter;
    List<BookCategoryModel> modelList;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        String email= firebaseUser.getEmail();
        TextView ev= findViewById(R.id.subTitleTv);
        ev.setText(email);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        search=findViewById(R.id.srcEt);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("PdfCategory");
        mStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelList= new ArrayList<BookCategoryModel>();
        adapter=new PdfCategoryAdapter(PdfActivity.this,modelList,false);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider))); // Set your divider drawable here
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button seeBk = findViewById(R.id.bookButton);
        Button pdf = findViewById(R.id.pdf);
        Button addCategoryBtn = findViewById(R.id.addCategoryBtn);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookCategoryModel bookCategoryModel= snapshot.getValue(BookCategoryModel.class);
                modelList.add(bookCategoryModel);
                adapter.notifyDataSetChanged();
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
        addCategoryBtn.setOnClickListener(view -> {
            // Handle all book button click
            Toast.makeText(this, "Add new Pdf Category!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PdfActivity.this, PdfCategoryAddActivity.class);
            startActivity(intent);
        });
        seeBk.setOnClickListener(view -> {
            // Handle all book button click
            Toast.makeText(this, "See Books", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PdfActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

         if(id==R.id.nav_vieworder){
            Intent viewOrderIntent = new Intent(PdfActivity.this, VieworderActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_profile){
            Intent viewOrderIntent = new Intent(PdfActivity.this, ProfileActivity.class);
            startActivity(viewOrderIntent);
         } else if(id==R.id.nav_addbook){
             Intent viewOrderIntent = new Intent(PdfActivity.this, AddedBookItemActivity.class);
             startActivity(viewOrderIntent);
        } else if(id==R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent viewOrderIntent = new Intent(PdfActivity.this, LoginActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_share){
            Intent viewOrderIntent = new Intent(PdfActivity.this, ShareActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_rate){
            Intent viewOrderIntent = new Intent(PdfActivity.this, RateusActivity.class);
            startActivity(viewOrderIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
