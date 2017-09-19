package com.yujotseng.expenselogger;

// ViewHolder snippet from https://developer.android.com/training/improving-layouts/smooth-scrolling.html

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class HoldExpenseListAdapter extends ArrayAdapter<Expense> implements Filterable {
    private static final String TAG = "HoldExpenseListAdapter";

    private Context context;
    private int resource;
    private ArrayList<Expense> expenseArrayList;
    private ArrayList<Expense> filteredExpenseArrayList;

    private static class ViewHolder {
        TextView expenseCategory;
        TextView expenseNote;
        TextView expenseAmount;
    }

    public HoldExpenseListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Expense> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        expenseArrayList = objects;
        filteredExpenseArrayList = objects;
    }

    @Override
    public int getCount() {
        return filteredExpenseArrayList.size();
    }

    @Nullable
    @Override
    public Expense getItem(int position) {
        return filteredExpenseArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredExpenseArrayList.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get expense properties
        String category = filteredExpenseArrayList.get(position).getCategory();
        String note = filteredExpenseArrayList.get(position).getNote();
        long amountInCents = filteredExpenseArrayList.get(position).getAmount();
        double amountModified = (double) amountInCents / 100;
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String amount = numberFormat.format(amountModified);

        ViewHolder viewHolder;

        // If the position hasn't been visited yet
        if (convertView == null) {
            // Make the view
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.expenseCategory = (TextView) convertView.findViewById(R.id.expenseCategory);
            viewHolder.expenseNote = (TextView) convertView.findViewById(R.id.expenseNote);
            viewHolder.expenseAmount = (TextView) convertView.findViewById(R.id.expenseAmount);
            convertView.setTag(viewHolder);
        } else {
            // Get the view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.expenseCategory.setText(category);
        viewHolder.expenseNote.setText(note);
        viewHolder.expenseAmount.setText(amount);

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                // If not searching anything, show full list
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.values = expenseArrayList;
                    filterResults.count = expenseArrayList.size();
                } else {
                    String searchTerm = charSequence.toString().toLowerCase();

                    ArrayList<Expense> filteredList = new ArrayList<>();
                    for (Expense expense : expenseArrayList) {
                        String note = expense.getNote();
                        // If expense note contains the search term, add it to the filtered list
                        if (note.toLowerCase().contains(searchTerm)) {
                            filteredList.add(expense);
                        }
                    }

                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredExpenseArrayList = (ArrayList<Expense>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
