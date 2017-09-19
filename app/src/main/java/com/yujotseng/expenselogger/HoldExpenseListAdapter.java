package com.yujotseng.expenselogger;

// ViewHolder snippet from https://developer.android.com/training/improving-layouts/smooth-scrolling.html

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class HoldExpenseListAdapter extends ArrayAdapter<Expense> {
    private static final String TAG = "HoldExpenseListAdapter";

    private Context context;
    private int resource;

    private static class ViewHolder {
        TextView expenseCategory;
        TextView expenseNote;
        TextView expenseAmount;
    }

    public HoldExpenseListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Expense> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get expense properties
        String category = getItem(position).getCategory();
        String note = getItem(position).getNote();
        long amountInCents =getItem(position).getAmount();
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
}
