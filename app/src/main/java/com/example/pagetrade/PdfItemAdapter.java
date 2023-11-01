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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
public class PdfItemAdapter extends RecyclerView.Adapter<PdfItemAdapter.ViewHolder> implements Filterable {
    Context context;
    public List<PdfItemModel> pdfItemModelList, filterListI;
    private boolean isAddedBookActivity, isCart;
    String selectedTitle;
    private String selectedCategoryKey;
    private FilterPdfItem filterCatI;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference cartRef;
    public PdfItemAdapter(Context context, List<PdfItemModel> pdfItemModelList, boolean isAddedBookActivity, String selectedTitle, String selectedCategoryKey, boolean isCart) {
        this.context = context;
        this.pdfItemModelList = pdfItemModelList;
        this.filterListI = pdfItemModelList;
        this.isAddedBookActivity = isAddedBookActivity;
        this.isCart = isCart;
        this.selectedTitle = selectedTitle;
        this.selectedCategoryKey = selectedCategoryKey;
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public PdfItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card,parent,false);

        return new PdfItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfItemAdapter.ViewHolder holder, int position) {
        PdfItemModel pdfItemModel = pdfItemModelList.get(position);
        if (pdfItemModel != null ) {
            Log.d("FirebaseData", "pdfName: " + pdfItemModel.getPdfName());
            Log.d("FirebaseData", "author: " + pdfItemModel.getAuthor());
            Log.d("FirebaseData", "price: " + pdfItemModel.getPrice());

            holder.bookName.setText(pdfItemModel.getPdfName() != null ? pdfItemModel.getPdfName() : "");
            holder.author.setText(pdfItemModel.getAuthor() != null ? pdfItemModel.getAuthor() : "");
            holder.price.setText(pdfItemModel.getPrice() != null ? pdfItemModel.getPrice() : "");

            String imageUri = pdfItemModel.getImage();
            if (imageUri != null && pdfItemModel.getCatKey().equals(selectedTitle)) {
                Log.d("FirebaseData", "Image: " + pdfItemModel.getImage());
                // Use Picasso to load the image into the ImageView
                Picasso.get().load(imageUri).into(holder.imageView);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openItemDetails(pdfItemModel.getPdfName());
                }
            });
            if (isCart) {
                holder.cartBtn.setVisibility(View.VISIBLE);
                holder.cartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartBookItem(pdfItemModel);
                    }
                });
            } else {
                holder.cartBtn.setVisibility(View.INVISIBLE);
            }
            if (isAddedBookActivity) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.deleteBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void cartBookItem(PdfItemModel pdfItemModel) {
        String currentUserId = firebaseUser.getUid() ;
        cartRef = FirebaseDatabase.getInstance().getReference().child("Carts");

        CartModel cartItem = new CartModel();
        cartItem.setBookId(pdfItemModel.getPdfId());
        cartItem.setImage(pdfItemModel.getImage());
        cartItem.setBookName(pdfItemModel.getPdfName());
        cartItem.setAuthor(pdfItemModel.getAuthor());
        cartItem.setPrice(pdfItemModel.getPrice());
        cartItem.setNumber("1");
        cartItem.setBuyerId(currentUserId);
        cartItem.setSellerId(pdfItemModel.getUid());
        cartItem.setMail(pdfItemModel.getSellerMail());
        cartItem.setType("PDF");

        DatabaseReference newItemRef = cartRef.push();
        String cartItemKey = newItemRef.getKey();

        cartItem.setKey(cartItemKey);

        // Set the cart item with the generated unique key
        newItemRef.setValue(cartItem);
        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
    }

    private void openItemDetails(String pdfName) {
        // Start a new activity to display details about the selected item
        Intent intent = new Intent(context, PdfItemDetailsActivity2.class);
        intent.putExtra("pdfName", pdfName);
        context.startActivity(intent);
    }
    private void deletePdf(Context con, String pdfId, String pdfFile, String pdfName){

    }
    @Override
    public int getItemCount() {
        return pdfItemModelList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterCatI==null) {
            filterCatI= new FilterPdfItem(filterListI,this);
        }
        return filterCatI;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView bookName,author,price;
        ImageButton deleteBtn,cartBtn;
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
