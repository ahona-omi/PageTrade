package com.example.pagetrade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartModel> cartItemList;
    DatabaseReference mRef, orderRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Context context;
    public CartAdapter(DatabaseReference mRef,Context context,List<CartModel> cartItemList) {
        this.context = context;
        this.mRef = mRef;
        this.cartItemList = cartItemList;
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartModel cartItem = cartItemList.get(position);
        String imageUrl = cartItem.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.bookPic);
        }
        holder.bookNameTv.setText(cartItem.getBookName());
        holder.authorTv.setText(cartItem.getAuthor());
        holder.priceTv.setText(String.valueOf(cartItem.getPrice()));
        holder.amountTv.setText(String.valueOf(cartItem.getNumber()));

        holder.delete.setOnClickListener(view -> {
            // Remove the item from Firebase
            String itemKey = cartItem.getKey();
            mRef.child(itemKey).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the item from cartItemList
                        cartItemList.remove(position);

                        // Notify the adapter about the item removal
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItemList.size());

                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error deleting item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        holder.incremet.setOnClickListener(view -> {
            String itemKey = cartItem.getKey();
            incrementAmount(itemKey, position);
        });
        holder.decrement.setOnClickListener(view -> {
            String itemKey = cartItem.getKey();
            decrementAmount(itemKey, position);
        });

        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemKey = cartItem.getKey();
                orderItem(cartItem);
                //removePdf(itemKey,position);
                if (cartItem.getType().equals("PDF")) {
                    // If the item is a PDF, go to PaymentActivity
                    Intent intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("cartItemKey", cartItem.getKey());
                    intent.putExtra("bookName", cartItem.getBookName());
                    intent.putExtra("price", cartItem.getPrice());
                    intent.putExtra("mail", cartItem.getMail());
                    intent.putExtra("position", position);
                    context.startActivity(intent);

                }
                else {
                    // If it's not a PDF, add the item to orders and remove it from the cart
                    //orderItem(cartItem);
                    mRef.child(itemKey).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                // Remove the item from cartItemList
                                cartItemList.remove(position);

                                // Notify the adapter about the item removal
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, cartItemList.size());

                                Toast.makeText(context, "Item ordered successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Error ordering item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        holder.bookPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openItemDetails(cartItem.getBookName(), cartItem.getType());
            }
        });
    }
    private void removePdf(String itemKey, int position) {
        // Remove the item from Firebase
        mRef.child(itemKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Remove the item from cartItemList
                    cartItemList.remove(position);

                    // Notify the adapter about the item removal
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartItemList.size());

                    Toast.makeText(context, "PDF item purchased and removed from cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error removing PDF item from cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void openItemDetails(String name, String type) {
        if (type.equals("Book")) {
            Intent intent = new Intent(context, BookItemDetailsActivity.class);
            intent.putExtra("bookName", name);
            context.startActivity(intent);
        } else if(type.equals("PDF")) { // Corrected the comparison here
            Intent intent = new Intent(context, PdfItemDetailsActivity2.class);
            intent.putExtra("pdfName", name);
            context.startActivity(intent);
        }
    }

    private void orderItem(CartModel cartItem) {
        String currentUserId = firebaseUser.getUid() ;
        String type= cartItem.getType();
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        CartModel orderItem = new CartModel();
        orderItem.setBookId(cartItem.getBookId());
        orderItem.setImage(cartItem.getImage());
        orderItem.setBookName(cartItem.getBookName());
        orderItem.setAuthor(cartItem.getAuthor());
        orderItem.setPrice(cartItem.getPrice());
        orderItem.setNumber(cartItem.getNumber());
        orderItem.setBuyerId(currentUserId);
        orderItem.setSellerId(cartItem.getSellerId());
        orderItem.setMail(cartItem.getMail());
        orderItem.setType(type);

        DatabaseReference newItemRef = orderRef.push();
        String cartItemKey = newItemRef.getKey();

        orderItem.setKey(cartItemKey);

        // Set the cart item with the generated unique key
        newItemRef.setValue(orderItem);
        Toast.makeText(context, "Order Confirmed", Toast.LENGTH_SHORT).show();
    }

    private void incrementAmount(String itemKey, int position) {
        int currentAmount = Integer.parseInt(cartItemList.get(position).getNumber());
        String curr = cartItemList.get(position).getPrice();
        String curr_price = "" , signn = "";
        for(int i = 0 ; i < curr.length() ; i++){
            if((curr.charAt(i) >= '0' && curr.charAt(i) <= '9' || curr.charAt(i) == '.')){
                curr_price += curr.charAt(i);
            }else{
                signn += curr.charAt(i);
            }
        }
        float current_price = Float.parseFloat(curr_price);
        float per_price = current_price / (float)currentAmount;
        int newAmount = currentAmount + 1;
        float newPrice = (float)newAmount * per_price;
        String.format("%.2f", newPrice);
        String me = signn + String.valueOf(newPrice);

        // Update the amount in Firebase
        mRef.child(itemKey).child("number").setValue(String.valueOf(newAmount))
                .addOnSuccessListener(aVoid -> {
                    // Update the item's amount locally
                    cartItemList.get(position).setNumber(String.valueOf(newAmount));
                    notifyItemChanged(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error incrementing amount: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        mRef.child(itemKey).child("price").setValue(me)
                .addOnSuccessListener(aVoid -> {
                    // Update the item's amount locally
                    cartItemList.get(position).setPrice(me);
                    notifyItemChanged(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error incrementing price: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void decrementAmount(String itemKey, int position) {
        int currentAmount = Integer.parseInt(cartItemList.get(position).getNumber());
        String curr = cartItemList.get(position).getPrice();
        String curr_price = "" , signn = "";
        for(int i = 0 ; i < curr.length() ; i++){
            if((curr.charAt(i) >= '0' && curr.charAt(i) <= '9' || curr.charAt(i) == '.')){
                curr_price += curr.charAt(i);
            }else{
                signn += curr.charAt(i);
            }
        }
        float current_price = Float.parseFloat(curr_price);
        float per_price = current_price / (float)currentAmount;
        // Ensure the amount is greater than 1 before decrementing
        if (currentAmount > 1) {
            int newAmount = currentAmount - 1;
            float newPrice = (float)newAmount * per_price;
            String.format("%.2f", newPrice);
            String me = signn + String.valueOf(newPrice);
            // Update the amount in Firebase
            mRef.child(itemKey).child("number").setValue(String.valueOf(newAmount))
                    .addOnSuccessListener(aVoid -> {
                        // Update the item's amount locally
                        cartItemList.get(position).setNumber(String.valueOf(newAmount));
                        notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error decrementing number: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            mRef.child(itemKey).child("price").setValue(me)
                    .addOnSuccessListener(aVoid -> {
                        // Update the item's amount locally
                        cartItemList.get(position).setPrice(me);
                        notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error decrementing price: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Minimum amount reached", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookNameTv, authorTv,priceTv,amountTv;
        ImageView incremet, decrement, bookPic;
        Button order;
        ImageButton delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookPic= itemView.findViewById(R.id.book);
            bookNameTv= itemView.findViewById(R.id.bookNameCart);
            authorTv= itemView.findViewById(R.id.writer);
            priceTv= itemView.findViewById(R.id.priceBook);
            amountTv= itemView.findViewById(R.id.amount);
            incremet= itemView.findViewById(R.id.plus);
            decrement= itemView.findViewById(R.id.minus);
            order= itemView.findViewById(R.id.orderBtn);
            delete= itemView.findViewById(R.id.remove);
        }
    }
}
