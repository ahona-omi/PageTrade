package com.example.pagetrade;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class BookItemAddActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton;
    EditText etTitle, etAuthor, etPrice, etDescription, etSellerName,  etSellerPhn,  etSellerMail;
    Button add, cancel;
    ProgressDialog progressDialog;
    private static final int GALLERY_CODE = 1;
    Uri imageUri = null;
    String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item_add);

        imageButton= findViewById(R.id.imageBtn);
        etTitle= findViewById(R.id.titleET);
        etAuthor= findViewById(R.id.authorET);
        etPrice= findViewById(R.id.priceET);
        etDescription= findViewById(R.id.detailsET);
        etSellerName= findViewById(R.id.sNameET);
        etSellerPhn= findViewById(R.id.sPhoneET);
        etSellerMail= findViewById(R.id.sEmailET);
        add= findViewById(R.id.addBtn);
        cancel= findViewById(R.id.cancelBtn);
        progressDialog = new ProgressDialog(this);

        selectedCategory = getIntent().getStringExtra("categoryId");

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("BookItem");
        mStorage = FirebaseStorage.getInstance();

        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_CODE);
        });

        cancel.setOnClickListener(view -> {
            // Handle all book button click
            Toast.makeText(this, "Go to Book Category List!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BookItemAddActivity.this, MainActivity.class);
            startActivity(intent);
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBookItem();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }
    }
    private void uploadBookItem() {
        String categoryKey = selectedCategory; // This should be the unique identifier of the selected category
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String sellerName = etSellerName.getText().toString().trim();
        String sellerPhone = etSellerPhn.getText().toString().trim();
        String sellerEmail = etSellerMail.getText().toString().trim();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (!(title.isEmpty() || author.isEmpty() || price.isEmpty() || sellerPhone.isEmpty() || sellerEmail.isEmpty() || imageUri == null)) {
            progressDialog.setTitle("Uploading Book...");
            progressDialog.show();

            StorageReference filePath = mStorage.getReference().child("imageBook").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL
                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                downloadUrl.addOnCompleteListener(task -> {
                    String downloadUri = task.getResult().toString();
                    String bookKey = mRef.push().getKey();

// Create a HashMap to store the book's information
                    HashMap<String, Object> bookData = new HashMap<>();
                    bookData.put("bookId", bookKey);
                    bookData.put("catKey", categoryKey);
                    bookData.put("bookName", title);
                    bookData.put("image", downloadUri);
                    bookData.put("author", author);
                    bookData.put("price", price);
                    bookData.put("description", description);
                    bookData.put("sellerName", sellerName);
                    bookData.put("sellerPhone", sellerPhone);
                    bookData.put("sellerMail", sellerEmail);
                    bookData.put("uid", userId);

// Add the book data to the "Books" node under the bookKey
                    mRef.child(bookKey).setValue(bookData);

                    progressDialog.dismiss();
                    Toast.makeText(BookItemAddActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookItemAddActivity.this, MainActivity.class));
                });
            });
        } else {
            Toast.makeText(this, "Please fill title,author, price, seller phone, seller email and image.", Toast.LENGTH_SHORT).show();
        }
    }
}