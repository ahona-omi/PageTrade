package com.example.pagetrade;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.ViewHolder> implements Filterable {
    Context context;
    public List<BookItemModel> bookItemModelList, filterListI;
    private boolean isAddedBookActivity, isCart;
    String selectedTitle;
    private String selectedCategoryKey;
    private FilterBookItem filterCatI;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference cartRef;
    public BookItemAdapter(Context context, List<BookItemModel> bookItemModelList, boolean isAddedBookActivity,String selectedTitle, String selectedCategoryKey, boolean isCart) {
        this.context = context;
        this.bookItemModelList = bookItemModelList;
        this.filterListI = bookItemModelList;
        this.isAddedBookActivity = isAddedBookActivity;
        this.selectedTitle = selectedTitle;
        this.selectedCategoryKey = selectedCategoryKey;
        this.isCart = isCart;
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public BookItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card,parent,false);

        return new BookItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookItemAdapter.ViewHolder holder, int position) {
        BookItemModel bookItemModel = bookItemModelList.get(position);
        if (bookItemModel != null ) {
            Log.d("FirebaseData", "bookName: " + bookItemModel.getBookName());
            Log.d("FirebaseData", "author: " + bookItemModel.getAuthor());
            Log.d("FirebaseData", "price: " + bookItemModel.getPrice());

            holder.bookName.setText(bookItemModel.getBookName() != null ? bookItemModel.getBookName() : "");
            holder.author.setText(bookItemModel.getAuthor() != null ? bookItemModel.getAuthor() : "");
            holder.price.setText(bookItemModel.getPrice() != null ? bookItemModel.getPrice() : "");

            String imageUri = bookItemModel.getImage();
            if (imageUri != null && bookItemModel.getCatKey().equals(selectedTitle)) {
                Log.d("FirebaseData", "Image: " + bookItemModel.getImage());
                // Use Picasso to load the image into the ImageView
                Picasso.get().load(imageUri).into(holder.imageView);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openItemDetails(bookItemModel.getBookName());
                }
            });
            if (isCart) {
                holder.cartBtn.setVisibility(View.VISIBLE);
                holder.cartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartBookItem(bookItemModel);
                    }
                });
            } else {
                holder.cartBtn.setVisibility(View.INVISIBLE);
            }
            if (isAddedBookActivity) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.deleteBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void cartBookItem(BookItemModel bookItemModel) {
        String currentUserId = firebaseUser.getUid() ;
        cartRef = FirebaseDatabase.getInstance().getReference().child("Carts");

        CartModel cartItem = new CartModel();
        cartItem.setBookId(bookItemModel.getBookId());
        cartItem.setImage(bookItemModel.getImage());
        cartItem.setBookName(bookItemModel.getBookName());
        cartItem.setAuthor(bookItemModel.getAuthor());
        cartItem.setPrice(bookItemModel.getPrice());
        cartItem.setNumber("1");
        cartItem.setBuyerId(currentUserId);
        cartItem.setSellerId(bookItemModel.getUid());
        cartItem.setMail(bookItemModel.getSellerMail());
        cartItem.setType("Book");

        DatabaseReference newItemRef = cartRef.push();
        String cartItemKey = newItemRef.getKey();

        cartItem.setKey(cartItemKey);

        // Set the cart item with the generated unique key
        newItemRef.setValue(cartItem);
        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
    }

    private void openItemDetails(String bookName) {
        // Start a new activity to display details about the selected item
        Intent intent = new Intent(context, BookItemDetailsActivity.class);
        intent.putExtra("bookName", bookName);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return bookItemModelList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterCatI==null) {
            filterCatI= new FilterBookItem(filterListI,this);
        }
        return filterCatI;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView bookName,author,price;
        ImageButton deleteBtn,cartBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imgRvId);
            bookName= itemView.findViewById(R.id.titleTvId);
            author= itemView.findViewById(R.id.authorTvId);
            price= itemView.findViewById(R.id.priceTvId);
            deleteBtn= itemView.findViewById(R.id.deleteBtn);
            cartBtn= itemView.findViewById(R.id.cartBtn);
        }
    }
}
