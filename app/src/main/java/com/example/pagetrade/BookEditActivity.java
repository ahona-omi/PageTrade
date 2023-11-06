package com.example.pagetrade;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BookEditActivity extends AppCompatActivity {
    EditText bPrice, bSummary, sName, sPhone, sMail;
    TextView bName, author, category;
    ImageView bPic;
    private Button saveBtn;
    private DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        mRef = FirebaseDatabase.getInstance().getReference().child("BookItem");

        bPrice = findViewById(R.id.bookPriceTextView);
        bSummary=findViewById(R.id.bookDetailsTextView);
        sName=findViewById(R.id.sellerNameTextView);
        sPhone=findViewById(R.id.sellerPhoneTv);
        sMail=findViewById(R.id.sellerEmailTextView);
        bName=findViewById(R.id.bookNameTextView);
       author= findViewById(R.id.authorTextView);
        category=findViewById(R.id.catTextView);
       bPic=findViewById(R.id.imgRvId);
        saveBtn=findViewById(R.id.saveBtn);

//        BookItemModel bookItemModel = getIntent().getParcelableExtra("bookItemModel");
//
//        if (bookItemModel != null) {
//            // Populate the EditText fields with the book information
//            bName.setText(bookItemModel.getBookName());
//            author.setText(bookItemModel.getAuthor());
//            category.setText(bookItemModel.getCatKey());
//            bPrice.setText(bookItemModel.getPrice());
//            bSummary.setText(bookItemModel.getDescription());
//            sName.setText(bookItemModel.getSellerName());
//            sPhone.setText(bookItemModel.getSellerPhone());
//            sMail.setText(bookItemModel.getSellerMail());
//            String imageUri = bookItemModel.getImage();
//            if (imageUri != null) {
//                Log.d("FirebaseData", "Image: " + bookItemModel.getImage());
//                // Use Picasso to load the image into the ImageView
//                Picasso.get().load(imageUri).into(bPic);
//            }
//        }
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Update and save the book information
//                updateBookInformation(bookItemModel);
//            }
//        });
    }

    private void updateBookInformation(BookItemModel bookItemModel) {
        String editedSname = sName.getText().toString();
        String editedSphone = sPhone.getText().toString();
        String editedSmail = sMail.getText().toString();
        String editedDescription = bSummary.getText().toString();
        String editedPrice = bPrice.getText().toString();
        String bookN = bName.getText().toString();
        String writer = author.getText().toString();
        String bookCat = bPrice.getText().toString();

        // Update the book item model with edited information
        bookItemModel.setSellerName(editedSname);
        bookItemModel.setSellerPhone(editedSphone);
        bookItemModel.setSellerMail(editedSmail);
        bookItemModel.setDescription(editedDescription);
        bookItemModel.setPrice(editedPrice);
        bookItemModel.setBookName(bookN);
        bookItemModel.setAuthor(writer);
        bookItemModel.setCatKey(bookCat);

        // Update the book information in Firebase using the DatabaseReference
        mRef.child(bookItemModel.getBookId()).setValue(bookItemModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Book item updated successfully
                        Toast.makeText(BookEditActivity.this, "Book information updated", Toast.LENGTH_SHORT).show();
                        finish(); // Close the EditBookActivity
                    } else {
                        // Handle error
                        Toast.makeText(BookEditActivity.this, "Failed to update book information", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }
