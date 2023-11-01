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

public class BookItemActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    List<BookItemModel> bookItemModelList;
    Toolbar toolbar;
    Button add;
    EditText search;
    BookItemAdapter adapter;
    private RecyclerView recyclerView;
    String selectedTitle, selectedCategoryKey;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item);
        toolbar = findViewById(R.id.toolbar);
        add = findViewById(R.id.addBookBtn);
        search=findViewById(R.id.srcEt);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        String email= firebaseUser.getEmail();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView ev= findViewById(R.id.subTitleTv);
        ev.setText(email);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("BookItem");
        mStorage = FirebaseStorage.getInstance();
        selectedTitle = getIntent().getStringExtra("title");
        selectedCategoryKey = getIntent().getStringExtra("catKey");

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookItemModelList= new ArrayList<>();
        adapter = new BookItemAdapter(BookItemActivity.this, bookItemModelList, false, selectedTitle, selectedCategoryKey,false);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider))); // Set your divider drawable here
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(selectedTitle);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookItemModel bookItemModel = snapshot.getValue(BookItemModel.class);
                if (bookItemModel != null && bookItemModel.getCatKey().equals(selectedTitle)) {
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
        add.setOnClickListener(view -> {
            Toast.makeText(this, "Add new Book!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BookItemActivity.this, BookItemAddActivity.class);
            intent.putExtra("categoryId", selectedTitle);
            startActivity(intent);
        });
    }
}