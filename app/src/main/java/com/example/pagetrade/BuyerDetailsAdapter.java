package com.example.pagetrade;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BuyerDetailsAdapter extends RecyclerView.Adapter<BuyerDetailsAdapter.ViewHolder>{
    Context context;
    List<User> users;
    private String selectedTitle;

    public BuyerDetailsAdapter(Context context, List<User> users, String selectedTitle) {
        this.context = context;
        this.users = users;
        this.selectedTitle = selectedTitle;
    }

    @NonNull
    @Override
    public BuyerDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_details, parent, false);
        return new BuyerDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerDetailsAdapter.ViewHolder holder, int position) {
        User userList = users.get(position);
        if (userList != null ) {
            Log.d("FirebaseData", "name: " + userList.getName());
            Log.d("FirebaseData", "PhoneNo: " + userList.getPhone());
            Log.d("FirebaseData", "email: " + userList.getEmail());
            Log.d("FirebaseData", "address: " + userList.getAddress());

            holder.nameTv.setText(userList.getName() != null ? userList.getName() : "");
            holder.phoneTv.setText(userList.getPhone() != null ? userList.getPhone() : "");
            holder.mailTv.setText(userList.getEmail() != null ? userList.getEmail() : "");
            holder.addressTv.setText(userList.getAddress() != null ? userList.getAddress() : "");

            String imageUri = userList.getProfileImg();
            if (imageUri != null) {
                Log.d("FirebaseData", "Image: " + userList.getProfileImg());
                // Use Picasso to load the image into the ImageView
                Picasso.get().load(imageUri).into(holder.pic);
            }
            holder.phoneTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check for CALL_PHONE permission before initiating the call
                    if (checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
                        // Permission is granted, initiate the call
                        String phoneNumber = userList.getPhone().trim();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        context.startActivity(intent);
                    } else {
                        // Permission is not granted, request it from the user
                        requestPermissions((Activity) context, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                    }
                }
            });

            holder.mailTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sellerEmail = userList.getEmail();

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
    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv, phoneTv, mailTv, addressTv;
        private ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
            mailTv = itemView.findViewById(R.id.mailTv);
            addressTv = itemView.findViewById(R.id.addressTv);
            pic = itemView.findViewById(R.id.imgRvId);
        }
    }
}
