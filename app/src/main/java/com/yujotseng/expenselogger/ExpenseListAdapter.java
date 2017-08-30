package com.yujotseng.expenselogger;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ExpenseListAdapter extends CursorAdapter {

    private static final String TAG = "ExpenseListAdapter";

    Context context;
    int resource;

    public ExpenseListAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // Inflate and return new view
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_adapter_layout, viewGroup, false);
        return view;
    }

    // Bind data to view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Fields in inflated view to populate
        TextView expenseName = (TextView) view.findViewById(R.id.expenseName);
        TextView expenseAmount = (TextView) view.findViewById(R.id.expenseAmount);
        TextView expenseDate = (TextView) view.findViewById(R.id.expenseDate);

        // Get properties from cursor
        int nameIndex = cursor.getColumnIndex("_name");
        String name = cursor.getString(nameIndex);
        int dateIndex = cursor.getColumnIndex("_date");
        String date = cursor.getString(dateIndex);
        int amountIndex = cursor.getColumnIndex("_amount");
        long amountInCents = cursor.getLong(amountIndex);
        double amountModified = (double) amountInCents / 100;
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
//        numberFormat.setMinimumFractionDigits(2);
//        numberFormat.setMaximumFractionDigits(2);
        String amount = numberFormat.format(amountModified);

        // Set fields with properties
        expenseName.setText(name);
        expenseAmount.setText(amount);
        expenseDate.setText(date);

        // Alternate background colors
//        if (cursor.getPosition()%2==1) {
//            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOdd));
//        } else {
//            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorEven));
//        }
    }
}
