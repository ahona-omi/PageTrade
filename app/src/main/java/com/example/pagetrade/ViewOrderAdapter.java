package com.example.pagetrade;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolder>{
    private List<CartModel> orderList;
    DatabaseReference mRef;
    Context context;
    boolean isSeller;

    public ViewOrderAdapter(DatabaseReference mRef, Context context, List<CartModel> orderList, boolean isSeller) {
        this.orderList = orderList;
        this.mRef = mRef;
        this.context = context;
        this.isSeller = isSeller;
    }

    @NonNull
    @Override
    public ViewOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vieworder, parent, false);
        return new ViewOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOrderAdapter.ViewHolder holder, int position) {
        CartModel cartItem = orderList.get(position);
        String imageUrl = cartItem.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.bookPic);
        }
        holder.bookNameTv.setText(cartItem.getBookName());
        holder.authorTv.setText(cartItem.getAuthor());
        holder.priceTv.setText(String.valueOf(cartItem.getPrice()));
        holder.amountTv.setText(String.valueOf(cartItem.getNumber()));

        if (isSeller) {
            holder.order.setVisibility(View.VISIBLE);
            holder.bookPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBuyerDetails(cartItem.getBuyerId());
                }
            });
        } else {
            holder.order.setVisibility(View.INVISIBLE);
            holder.bookPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openItemDetails(cartItem.getBookName(), cartItem.getType());
                }
            });
        }
        if (cartItem.getType().equals("PDF")) {
            holder.order.setText("Purchased PDF");
        } else{
            holder.order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.order.setText("Delivered Book");
                }
            });
        }
    }

    private void openBuyerDetails(String buyerId) {
        Intent intent = new Intent(context, BuyerDetailsActivity.class);
        intent.putExtra("uid", buyerId);
        context.startActivity(intent);
    }

    private void openItemDetails(String name, String type) {
        if (type.equals("Book")) {
            Intent intent = new Intent(context, BookItemDetailsActivity.class);
            intent.putExtra("bookName", name);
            context.startActivity(intent);
        } else if(type.equals("PDF")) { // Corrected the comparison here
            Intent intent = new Intent(context, PdfItemDetailsActivity.class);
            intent.putExtra("pdfName", name);
            context.startActivity(intent);
        }
    }
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookNameTv, authorTv,priceTv,amountTv;
        ImageView bookPic;
        Button order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookPic= itemView.findViewById(R.id.book);
            bookNameTv= itemView.findViewById(R.id.bookNameCart);
            authorTv= itemView.findViewById(R.id.writer);
            priceTv= itemView.findViewById(R.id.priceBook);
            amountTv= itemView.findViewById(R.id.amount);
            order= itemView.findViewById(R.id.orderBtn);
        }
    }
}
