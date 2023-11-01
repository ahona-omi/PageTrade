package com.example.pagetrade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pagetrade.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference cartRef;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    String currentUserId;
    List<CartModel> cartItemList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        String email= firebaseUser.getEmail();
        TextView ev= findViewById(R.id.subTitleTv);
        ev.setText(email);
        currentUserId = firebaseUser.getUid();

        cartRef = FirebaseDatabase.getInstance().getReference().child("Carts");

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartRef,CartActivity.this,cartItemList);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider))); // Set your divider drawable here
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(cartAdapter);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fetchCartItemsFromFirebase();
    }

    private void fetchCartItemsFromFirebase() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartItemList.clear(); // Clear the existing list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartModel cartItem = snapshot.getValue(CartModel.class);
                    if (cartItem != null && cartItem.getBuyerId().equals(currentUserId)) {
                        cartItemList.add(cartItem);
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });
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
            Intent cartIntent = new Intent(CartActivity.this, MainBuyerActivity.class);
            startActivity(cartIntent);
        } else if(id==R.id.nav_vieworder){
            Intent viewOrderIntent = new Intent(CartActivity.this, VieworderBuyerActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_profile){
            Intent viewOrderIntent = new Intent(CartActivity.this, BuyerProfileActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent viewOrderIntent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_share){
            Intent viewOrderIntent = new Intent(CartActivity.this, ShareBuyerActivity.class);
            startActivity(viewOrderIntent);
        } else if(id==R.id.nav_rate){
            Intent viewOrderIntent = new Intent(CartActivity.this, RateusBuyerActivity.class);
            startActivity(viewOrderIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
