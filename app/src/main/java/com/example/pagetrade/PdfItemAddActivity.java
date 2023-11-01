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
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class PdfItemAddActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton, pdfBtn;
    EditText etTitle, etAuthor, etPrice, etDescription, etSellerName,  etSellerPhn,  etSellerMail;
    Button add, cancel;
    ProgressDialog progressDialog;
    private static final int GALLERY_CODE = 1;
    private static final int PDF_PICKER_CODE = 2;
    private static final String TAG = "ADD_PDF_TAG";
    Uri imageUri = null, pdfUri =null;
    String selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_item_add);

        imageButton= findViewById(R.id.imageBtn);
        pdfBtn= findViewById(R.id.addPdfBtn);
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
        mRef = mDatabase.getReference().child("PdfItem");
        mStorage = FirebaseStorage.getInstance();

        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_CODE);
        });
        pdfBtn.setOnClickListener(view -> {
            Intent pdfPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pdfPickerIntent.setType("application/pdf");
            startActivityForResult(pdfPickerIntent, PDF_PICKER_CODE);
        });
        cancel.setOnClickListener(view -> {
            // Handle all book button click
            Toast.makeText(this, "Go to Pdf Category lists!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PdfItemAddActivity.this, PdfActivity.class);
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
        if (requestCode == PDF_PICKER_CODE && resultCode == RESULT_OK) {
            pdfUri = data.getData();
            pdfBtn.setImageURI(pdfUri);
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

        if (!(title.isEmpty() || author.isEmpty() || price.isEmpty() || sellerPhone.isEmpty() || sellerEmail.isEmpty() || imageUri == null || pdfUri == null)) {
            progressDialog.setTitle("Uploading PDF...");
            progressDialog.show();

            StorageReference pdfPath = mStorage.getReference().child("pdfs").child(pdfUri.getLastPathSegment());
            pdfPath.putFile(pdfUri).addOnSuccessListener(pdfTaskSnapshot -> {
                pdfPath.getDownloadUrl().addOnSuccessListener(pdfUriTask -> {
                    String pdfDownloadUri = pdfUriTask.toString();

                    StorageReference filePath = mStorage.getReference().child("imageBook").child(imageUri.getLastPathSegment());
                    filePath.putFile(imageUri).addOnSuccessListener(imageTaskSnapshot -> {
                        filePath.getDownloadUrl().addOnSuccessListener(imageUriTask -> {
                            String imageDownloadUri = imageUriTask.toString();

                            String pdfKey = mRef.push().getKey();

                            // Create a HashMap to store the book's information
                            HashMap<String, Object> pdfData = new HashMap<>();
                            pdfData.put("pdfId", pdfKey);
                            pdfData.put("catKey", categoryKey);
                            pdfData.put("pdfName", title);
                            pdfData.put("pdfFile", pdfDownloadUri);
                            pdfData.put("image", imageDownloadUri);
                            pdfData.put("author", author);
                            pdfData.put("price", price);
                            pdfData.put("description", description);
                            pdfData.put("sellerName", sellerName);
                            pdfData.put("sellerPhone", sellerPhone);
                            pdfData.put("sellerMail", sellerEmail);
                            pdfData.put("uid", userId);

                            // Add the book data to the "PdfItem" node under the pdfKey
                            mRef.child(pdfKey).setValue(pdfData);

                            progressDialog.dismiss();
                            Toast.makeText(PdfItemAddActivity.this, "PDF added successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PdfItemAddActivity.this, PdfActivity.class));
                        });
                    });
                });
            });
        } else {
            Toast.makeText(this, "Please fill title, author, price, seller phone, seller email, select an image, and a PDF file.", Toast.LENGTH_SHORT).show();
        }
    }
}
