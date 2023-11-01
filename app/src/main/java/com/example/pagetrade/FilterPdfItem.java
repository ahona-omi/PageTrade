package com.example.pagetrade;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterPdfItem extends Filter {
    List<PdfItemModel> filterList;
    PdfItemAdapter itemAdapter;

    public FilterPdfItem(List<PdfItemModel> filterList, PdfItemAdapter itemAdapter) {
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
            List<PdfItemModel> filteredModels= new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){         //valodated
                if(filterList.get(i).getPdfName().toUpperCase().contains(charSequence)){
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
        itemAdapter.pdfItemModelList= (List<PdfItemModel>)filterResults.values;
        itemAdapter.notifyDataSetChanged();
    }
}
