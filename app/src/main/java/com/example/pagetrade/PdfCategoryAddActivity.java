package com.example.pagetrade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PdfCategoryAddActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton;
    EditText editText;
    Button add, cancel;
    ProgressDialog progressDialog;
    private static final int GALLERY_CODE = 1;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_category);

        imageButton = findViewById(R.id.imageBtn);
        editText = findViewById(R.id.titleET);
        add = findViewById(R.id.addBtn);
        cancel = findViewById(R.id.cancelBtn);
        progressDialog = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("PdfCategory");
        mStorage = FirebaseStorage.getInstance();

        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_CODE);
        });

        cancel.setOnClickListener(view -> {
            startActivity(new Intent(PdfCategoryAddActivity.this, PdfActivity.class));
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdfCategory();
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
    private void uploadPdfCategory() {
        String title = editText.getText().toString().trim();
        if (!(title.isEmpty() || imageUri == null)) {
            progressDialog.setTitle("Uploading Pdf Category...");
            progressDialog.show();

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            StorageReference filePath = mStorage.getReference().child("imagePost").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL
                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                downloadUrl.addOnCompleteListener(task -> {
                    String downloadUri = task.getResult().toString();

                    // Create a new category in the Firebase Realtime Database
                    DatabaseReference newCat = mRef.push();
                    newCat.child("title").setValue(title);
                    newCat.child("image").setValue(downloadUri);
                    newCat.child("userid").setValue(userId); // Add the userid here

                    progressDialog.dismiss();
                    Toast.makeText(PdfCategoryAddActivity.this, "Pdf Category added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PdfCategoryAddActivity.this, PdfActivity.class));
                });
            });
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
