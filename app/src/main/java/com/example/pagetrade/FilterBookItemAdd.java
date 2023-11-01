package com.example.pagetrade;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterBookItemAdd extends Filter {
    List<BookItemModel> filterList;
    AddedBookAdapter itemAdapter;

    public FilterBookItemAdd(List<BookItemModel> filterList, AddedBookAdapter itemAdapter) {
        this.filterList = filterList;
        this.itemAdapter = itemAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        //value shouldn't be null/empty
        if( charSequence != null && charSequence.length() > 0){
            //to avoid case sensitivity, change to upper
            charSequence= charSequence.toString().toUpperCase();
            List<BookItemModel> filteredModels= new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){         //valodated
                if(filterList.get(i).getBookName().toUpperCase().contains(charSequence)){
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
        itemAdapter.bookItemModelList= (List<BookItemModel>)filterResults.values;
        itemAdapter.notifyDataSetChanged();
    }
}
