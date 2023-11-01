package com.example.pagetrade;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.startActivity;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class PdfDetailsAdapter extends RecyclerView.Adapter<PdfDetailsAdapter.ViewHolder>{
    Context context;
    List<PdfItemModel> pdfItemModelList;
    private boolean isAddedBookActivity;
    private boolean isPDF;
    private String selectedTitle;
    private FirebaseStorage mStorage;
    public PdfDetailsAdapter(Context context, List<PdfItemModel> pdfItemModelList, boolean isAddedBookActivity, String selectedTitle, boolean isPDF) {
        this.context = context;
        this.pdfItemModelList = pdfItemModelList;
        this.isPDF = isPDF;
        this.isAddedBookActivity = isAddedBookActivity;
        this.selectedTitle = selectedTitle;
        mStorage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public PdfDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_details, parent, false);
        return new PdfDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfDetailsAdapter.ViewHolder holder, int position) {
        PdfItemModel pdfItemModel = pdfItemModelList.get(position);

        if (pdfItemModel != null ) {
            Log.d("FirebaseData", "pdfName: " + pdfItemModel.getPdfName());
            Log.d("FirebaseData", "author: " + pdfItemModel.getAuthor());
            Log.d("FirebaseData", "price: " +pdfItemModel.getPrice());
            Log.d("FirebaseData", "catKey: " + pdfItemModel.getPrice());
            Log.d("FirebaseData", "description: " + pdfItemModel.getPrice());
            Log.d("FirebaseData", "sellerName: " + pdfItemModel.getPrice());
            Log.d("FirebaseData", "sellerPhone: " + pdfItemModel.getPrice());
            Log.d("FirebaseData", "sellerMail: " + pdfItemModel.getPrice());

            holder.bookName.setText(pdfItemModel.getPdfName() != null ? pdfItemModel.getPdfName() : "");
            holder.author.setText(pdfItemModel.getAuthor() != null ? pdfItemModel.getAuthor() : "");
            holder.price.setText(pdfItemModel.getPrice() != null ? pdfItemModel.getPrice() : "");
            holder.cat.setText(pdfItemModel.getCatKey() != null ? pdfItemModel.getCatKey() : "");
            holder.desc.setText(pdfItemModel.getDescription() != null ? pdfItemModel.getDescription() : "");
            holder.sName.setText(pdfItemModel.getSellerName() != null ? pdfItemModel.getSellerName() : "");
            holder.sPhone.setText(pdfItemModel.getSellerPhone() != null ? pdfItemModel.getSellerPhone() : "");
            holder.sEmail.setText(pdfItemModel.getSellerMail() != null ? pdfItemModel.getSellerMail() : "");
            //holder.pdfButton.setText(pdfItemModel.getPdf() != null ? pdfItemModel.getPdf() : "");

            String imageUri = pdfItemModel.getImage();

            if (imageUri != null) {
                Log.d("FirebaseData", "Image: " + pdfItemModel.getImage());
                // Use Picasso to load the image into the ImageView
                Picasso.get().load(imageUri).into(holder.imageView);
            }
            holder.sPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check for CALL_PHONE permission before initiating the call
                    if (checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
                        // Permission is granted, initiate the call
                        String phoneNumber = pdfItemModel.getSellerPhone().trim();
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
                    String sellerEmail = pdfItemModel.getSellerMail();

                    // Create an Intent to send an email
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + sellerEmail)); // Set the email address
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding the Pdf"); // Set the email subject
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I am interested in your Pdf."); // Set the email body

                    try {
                        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        // Handle the case where no email client is installed on the device
                        Toast.makeText(context, "No email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (isPDF) {
                holder.pdfButton.setVisibility(View.VISIBLE);
                holder.pdfButton.setOnClickListener(view -> {
                    String pdfUrl = pdfItemModel.getPdfUrl();
                    if (pdfUrl != null) {
                        openPdf(pdfUrl);
                    } else {
                        Toast.makeText(context, "PDF not available.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                holder.pdfButton.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void openPdf(String pdfUrl) {
        try {
            //Log.d("FirebaseData", "pdfFile: " + pdfUrl);
            Uri pdfUri = Uri.parse(pdfUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");

            //intent.setPackage("com.adobe.reader");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                // If no PDF viewer is installed, inform the user.
                Toast.makeText(context, "No PDF viewer installed.", Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException e) {
            // Handle exceptions, e.g., if there's no app to handle PDF files
            Toast.makeText(context, "Error opening PDF.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getSelectedTitle() {
        return selectedTitle;
    }
    private void close(PdfItemModel pdfItemModel) {
        // Start a new activity to display details about the selected book
        Intent intent = new Intent(context, PdfActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return pdfItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,pdf;
        TextView bookName,author,price,cat,desc,sName, sPhone, sEmail,pdfButton;
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
            pdfButton= itemView.findViewById(R.id.pdfTextView);
        }
    }
}
