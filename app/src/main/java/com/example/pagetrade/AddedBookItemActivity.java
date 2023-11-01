package com.example.pagetrade;

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

import android.annotation.SuppressLint;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddedBookItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    FirebaseAuth mAuth;
    String currentUserUid;
    List<BookItemModel> bookItemModelList;
    Toolbar toolbar;
    AddedBookAdapter adapter;
    private RecyclerView recyclerView;
    EditText search;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_book_item);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        String email= firebaseUser.getEmail();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView ev= findViewById(R.id.subTitleTv);
        ev.setText(email);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        search=findViewById(R.id.srcEt);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("BookItem");
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookItemModelList= new ArrayList<>();
        adapter = new AddedBookAdapter(mRef,AddedBookItemActivity.this, bookItemModelList, true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);
        setSupportActionBar(toolbar);

        Query query = mRef.orderByChild("uid").equalTo(currentUserUid);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookItemModel bookItemModel = snapshot.getValue(BookItemModel.class);
                if (bookItemModel != null && bookItemModel.getUid().equals(currentUserUid)) {
                    bookItemModelList.add(bookItemModel);
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

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button seeBk = findViewById(R.id.bookButton);
        Button pdf = findViewById(R.id.pdf);

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
        pdf.setOnClickListener(view -> {
            // Handle all book button click
            Toast.makeText(this, "Show added PDFs", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddedBookItemActivity.this, AddedPdfActivity.class);
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

        if(id ==R.id.nav_home){
            Intent cartIntent = new Intent(AddedBookItemActivity.this, MainActivity.class);
            startActivity(cartIntent);
        } else if(id==R.id.nav_vieworder){
            Intent viewOrderIntent = new Intent(AddedBookItemActivity.this, VieworderActivity.class);
            startActivity(viewOrderIntent);
        }  else if(id==R.id.nav_profile){
            Intent viewOrderIntent = new Intent(AddedBookItemActivity.this, ProfileActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent viewOrderIntent = new Intent(AddedBookItemActivity.this, LoginActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_share){
            Intent viewOrderIntent = new Intent(AddedBookItemActivity.this, ShareActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_rate){
            Intent viewOrderIntent = new Intent(AddedBookItemActivity.this, RateusActivity.class);
            startActivity(viewOrderIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
