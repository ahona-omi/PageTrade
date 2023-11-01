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

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddedBookAdapter extends RecyclerView.Adapter<AddedBookAdapter.ViewHolder> implements Filterable {
    Context context;
    public List<BookItemModel> bookItemModelList , filterListI;
    private boolean isAddedBookActivity;
    private String selectedTitle;
    DatabaseReference mRef;
    private FilterBookItemAdd filterCatI;
    public AddedBookAdapter(DatabaseReference mRef,Context context, List<BookItemModel> bookItemModelList, boolean isAddedBookActivity) {
        this.context = context;
        this.bookItemModelList = bookItemModelList;
        this.filterListI = bookItemModelList;
        this.isAddedBookActivity = isAddedBookActivity;
        this.mRef = mRef;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookItemModel bookItemModel = bookItemModelList.get(position);
        if (bookItemModel != null) {
            Log.d("FirebaseData", "bookName: " + bookItemModel.getBookName());
            Log.d("FirebaseData", "author: " + bookItemModel.getAuthor());
            Log.d("FirebaseData", "price: " + bookItemModel.getPrice());

            holder.bookName.setText(bookItemModel.getBookName() != null ? bookItemModel.getBookName() : "");
            holder.author.setText(bookItemModel.getAuthor() != null ? bookItemModel.getAuthor() : "");
            holder.price.setText(bookItemModel.getPrice() != null ? bookItemModel.getPrice() : "");

            String imageUri = bookItemModel.getImage();
            if (imageUri != null) {
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
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Debug", "Delete button clicked");
                    deleteBookItem(bookItemModel);
                }
            });

            holder.cartBtn.setVisibility(View.INVISIBLE);
            if (isAddedBookActivity) {
                holder.deleteBtn.setVisibility(View.VISIBLE);

            } else {
                holder.deleteBtn.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void deleteBookItem(BookItemModel bookItemModel) {
        if (bookItemModel != null) {
            Log.d("Debug", "Deleting book item: " + bookItemModel.getBookName());
            String bookId = bookItemModel.getBookId();

            // Check if the bookId is not null or empty
            if (bookId != null && !bookId.isEmpty()) {
                mRef.child(bookId).removeValue()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Book item deleted successfully
                                Log.d("FirebaseData", "Book item deleted successfully");
                                Toast.makeText(context, bookItemModel.getBookName()+" is deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                // An error occurred while deleting the book item
                                Log.e("FirebaseData", "Error deleting book item: " + task.getException().getMessage());
                                Toast.makeText(context, bookItemModel.getBookName()+" is failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public String getSelectedTitle() {
        return selectedTitle;
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
            filterCatI= new FilterBookItemAdd(filterListI,this);
        }
        return filterCatI;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView bookName,author,price;
        ImageButton deleteBtn, cartBtn;
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
