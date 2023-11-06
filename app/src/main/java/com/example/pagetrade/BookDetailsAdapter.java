package com.example.pagetrade;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.startActivity;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookDetailsAdapter extends RecyclerView.Adapter<BookDetailsAdapter.ViewHolder>{
    Context context;
    List<BookItemModel> bookItemModelList;
    private boolean isAddedBookActivity;
    private String selectedTitle;

    public BookDetailsAdapter(Context context, List<BookItemModel> bookItemModelList, boolean isAddedBookActivity,String selectedTitle) {
        this.context = context;
        this.bookItemModelList = bookItemModelList;
        this.isAddedBookActivity = isAddedBookActivity;
        this.selectedTitle = selectedTitle;
    }

    @NonNull
    @Override
    public BookDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_details, parent, false);
        return new BookDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookDetailsAdapter.ViewHolder holder, int position) {
        BookItemModel bookItemModel = bookItemModelList.get(position);
        if (bookItemModel != null ) {
            Log.d("FirebaseData", "bookName: " + bookItemModel.getBookName());
            Log.d("FirebaseData", "author: " + bookItemModel.getAuthor());
            Log.d("FirebaseData", "price: " + bookItemModel.getPrice());
            Log.d("FirebaseData", "catKey: " + bookItemModel.getCatKey());
            Log.d("FirebaseData", "description: " + bookItemModel.getDescription());
            Log.d("FirebaseData", "sellerName: " + bookItemModel.getSellerName());
            Log.d("FirebaseData", "sellerPhone: " + bookItemModel.getSellerPhone());
            Log.d("FirebaseData", "sellerMail: " + bookItemModel.getSellerMail());

            holder.bookName.setText(bookItemModel.getBookName() != null ? bookItemModel.getBookName() : "");
            holder.author.setText(bookItemModel.getAuthor() != null ? bookItemModel.getAuthor() : "");
            holder.price.setText(bookItemModel.getPrice() != null ? bookItemModel.getPrice() : "");
            holder.cat.setText(bookItemModel.getCatKey() != null ? bookItemModel.getCatKey() : "");
            holder.desc.setText(bookItemModel.getDescription() != null ? bookItemModel.getDescription() : "");
            holder.sName.setText(bookItemModel.getSellerName() != null ? bookItemModel.getSellerName() : "");
            holder.sPhone.setText(bookItemModel.getSellerPhone() != null ? bookItemModel.getSellerPhone() : "");
            holder.sEmail.setText(bookItemModel.getSellerMail() != null ? bookItemModel.getSellerMail() : "");

            String imageUri = bookItemModel.getImage();
            if (imageUri != null) {
                Log.d("FirebaseData", "Image: " + bookItemModel.getImage());
                // Use Picasso to load the image into the ImageView
                Picasso.get().load(imageUri).into(holder.imageView);
            }
            holder.sPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check for CALL_PHONE permission before initiating the call
                    if (checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
                        // Permission is granted, initiate the call
                        String phoneNumber = bookItemModel.getSellerPhone().trim();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        context.startActivity(intent);
                    } else {
                        // Permission is not granted, request it from the user
                        requestPermissions((Activity) context, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                    }
                }
            });

            holder.sEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sellerEmail = bookItemModel.getSellerMail();

                    // Create an Intent to send an email
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + sellerEmail)); // Set the email address
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding the book"); // Set the email subject
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I am interested in your book."); // Set the email body

                    try {
                        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        // Handle the case where no email client is installed on the device
                        Toast.makeText(context, "No email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public String getSelectedTitle() {
        return selectedTitle;
    }

    private void close(BookItemModel bookItemModel) {
        // Start a new activity to display details about the selected book
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return bookItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView bookName,author,price,cat,desc,sName, sPhone, sEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imgRvId);
            bookName= itemView.findViewById(R.id.bookNameTextView);
            author= itemView.findViewById(R.id.authorTextView);
            price= itemView.findViewById(R.id.bookPriceTextView);
            cat= itemView.findViewById(R.id.catTextView);
            desc= itemView.findViewById(R.id.bookDetailsTextView);
            sName= itemView.findViewById(R.id.sellerNameTextView);
            sPhone= itemView.findViewById(R.id.sellerPhoneTextView);
            sEmail= itemView.findViewById(R.id.sellerEmailTextView);

        }
    }
}
