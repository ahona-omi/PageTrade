package com.example.pagetrade;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterCat extends Filter {
    List<BookCategoryModel> filterList;
    BookCategoryAdapter categoryAdapter;

    public FilterCat(List<BookCategoryModel> filterList, BookCategoryAdapter categoryAdapter) {
        this.filterList = filterList;
        this.categoryAdapter = categoryAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        //value shouldn't be null/empty
        if( charSequence != null && charSequence.length() > 0){
            //to avoid case sensitivity, change to upper
            charSequence= charSequence.toString().toUpperCase();
            List<BookCategoryModel> filteredModels= new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){         //valodated
                if(filterList.get(i).getTitle().toUpperCase().contains(charSequence)){
                    //add to filterList
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count= filteredModels.size();
            results.values= filteredModels;
        } else {
            results.count= filterList.size();
            results.values= filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        //apply filter change
        categoryAdapter.bookCategoryModelList= (List<BookCategoryModel>)filterResults.values;
        categoryAdapter.notifyDataSetChanged();
    }
}
