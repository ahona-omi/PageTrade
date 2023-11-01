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

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;
public class AddedPdfAdapter extends RecyclerView.Adapter<AddedPdfAdapter.ViewHolder>  implements Filterable {
    Context context;
    public List<PdfItemModel> pdfItemModelList,filterListI;
    private boolean isAddedBookActivity;
    private String selectedTitle;
    DatabaseReference mRef;
    private FilterPdfItemAdd filterCatI;
    public AddedPdfAdapter(DatabaseReference mRef,Context context, List<PdfItemModel> pdfItemModelList, boolean isAddedBookActivity) {
        this.context = context;
        this.pdfItemModelList = pdfItemModelList;
        this.filterListI = pdfItemModelList;
        this.isAddedBookActivity = isAddedBookActivity;
        this.mRef = mRef;
    }

    @NonNull
    @Override
    public AddedPdfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card, parent, false);
        return new AddedPdfAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedPdfAdapter.ViewHolder holder, int position) {
        PdfItemModel pdfItemModel = pdfItemModelList.get(position);
        if (pdfItemModel != null) {
            Log.d("FirebaseData", "bookName: " + pdfItemModel.getPdfName());
            Log.d("FirebaseData", "author: " + pdfItemModel.getAuthor());
            Log.d("FirebaseData", "price: " + pdfItemModel.getPrice());

            holder.bookName.setText(pdfItemModel.getPdfName() != null ? pdfItemModel.getPdfName() : "");
            holder.author.setText(pdfItemModel.getAuthor() != null ? pdfItemModel.getAuthor() : "");
            holder.price.setText(pdfItemModel.getPrice() != null ? pdfItemModel.getPrice() : "");

            String imageUri = pdfItemModel.getImage();
            if (imageUri != null) {
                Log.d("FirebaseData", "Image: " + pdfItemModel.getImage());
                // Use Picasso to load the image into the ImageView
                Picasso.get().load(imageUri).into(holder.imageView);
            }
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Debug", "Delete button clicked");
                    deleteBookItem(pdfItemModel);
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openItemDetails(pdfItemModel.getPdfName());
                }
            });
            holder.cartBtn.setVisibility(View.INVISIBLE);
            if (isAddedBookActivity) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.deleteBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void deleteBookItem(PdfItemModel pdfItemModel) {
        if (pdfItemModel != null) {
            Log.d("Debug", "Deleting book item: " + pdfItemModel.getPdfName());
            String bookId = pdfItemModel.getPdfId();

            // Check if the bookId is not null or empty
            if (bookId != null && !bookId.isEmpty()) {
                mRef.child(bookId).removeValue()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                Log.d("FirebaseData", "PDF deleted successfully");
                                Toast.makeText(context, pdfItemModel.getPdfName()+" is deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                // An error occurred while deleting the book item
                                Log.e("FirebaseData", "Error deleting PDF: " + task.getException().getMessage());
                                Toast.makeText(context, pdfItemModel.getPdfName()+" is failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public String getSelectedTitle() {
        return selectedTitle;
    }

    private void openItemDetails(String pdfName) {
        // Start a new activity to display details about the selected item
        Intent intent = new Intent(context, PdfItemDetailsActivity.class);
        intent.putExtra("pdfName", pdfName);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return pdfItemModelList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterCatI==null) {
            filterCatI= new FilterPdfItemAdd(filterListI,this);
        }
        return filterCatI;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView bookName,author,price;
        ImageButton deleteBtn, cartBtn;
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
