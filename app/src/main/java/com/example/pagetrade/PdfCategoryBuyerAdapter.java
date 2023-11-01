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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PdfCategoryBuyerAdapter  extends RecyclerView.Adapter<PdfCategoryBuyerAdapter.ViewHolder> implements Filterable {
    Context context;
    public List<BookCategoryModel> bookCategoryModelList, filterList;
    private String selectedTitle;
    private FilterCatPdfBuyer filterCat;
    public PdfCategoryBuyerAdapter(Context context, List<BookCategoryModel> bookCategoryModelList) {
        this.context = context;
        this.bookCategoryModelList = bookCategoryModelList;
        this.filterList = bookCategoryModelList;
    }

    @NonNull
    @Override
    public PdfCategoryBuyerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.designrow_rv,parent,false);

        return new PdfCategoryBuyerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfCategoryBuyerAdapter.ViewHolder holder, int position) {
        BookCategoryModel bookCategoryModel = bookCategoryModelList.get(position);
        if (bookCategoryModel != null) {
            Log.d("FirebaseData", "Title: " + bookCategoryModel.getTitle());
            holder.title.setText(bookCategoryModel.getTitle() != null ? bookCategoryModel.getTitle() : "");
            //holder.cartBtn.setVisibility(View.INVISIBLE);
            String imageUri = bookCategoryModel.getImage();
            if (imageUri != null) {
                Log.d("FirebaseData", "Image: " + bookCategoryModel.getImage());
                // Use Picasso to load the image into the ImageView
                Picasso.get().load(imageUri).into(holder.imageView);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTitle = bookCategoryModel.getTitle();
                    openItemDetails(bookCategoryModel); // Pass the category title
                }
            });
            holder.deleteBtn.setVisibility(View.INVISIBLE); // Hide ImageButton
        }
    }
    public String getSelectedTitle() {
        return selectedTitle;
    }
    private void openItemDetails(BookCategoryModel bookCategoryModel) {
        // Start a new activity to display details about the selected item
        Intent intent = new Intent(context, PdfItemBuyerActivity.class);
        intent.putExtra("title", bookCategoryModel.getTitle());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return bookCategoryModelList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterCat==null) {
            filterCat= new FilterCatPdfBuyer(filterList,this);
        }
        return filterCat;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ImageButton deleteBtn,cartBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imgRvId);
            title= itemView.findViewById(R.id.titleTvId);
            deleteBtn= itemView.findViewById(R.id.deleteBtn);
            cartBtn= itemView.findViewById(R.id.cartBtn);
        }
    }
}
