package com.yujotseng.expenselogger;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.NumberFormat;

public class ExpenseListAdapter extends CursorAdapter {

    private static final String TAG = "ExpenseListAdapter";

    public ExpenseListAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // Inflate and return new view
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense_list_adapter_layout, viewGroup, false);
        return view;
    }

    // Bind data to view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Fields in inflated view to populate
        TextView expenseCategory = (TextView) view.findViewById(R.id.expenseCategory);
        TextView expenseNote = (TextView) view.findViewById(R.id.expenseNote);
        TextView expenseAmount = (TextView) view.findViewById(R.id.expenseAmount);

        // Get properties from cursor
        int categoryIndex = cursor.getColumnIndex("_category");
        String category = cursor.getString(categoryIndex);
        int noteIndex = cursor.getColumnIndex("_note");
        String note;
        if (cursor.isNull(noteIndex)) {
            note = "";
        } else {
            note = cursor.getString(noteIndex);
        }
        int amountIndex = cursor.getColumnIndex("_amount");
        long amountInCents = cursor.getLong(amountIndex);
        double amountModified = (double) amountInCents / 100;
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String amount = numberFormat.format(amountModified);

        // Set fields with properties
        expenseCategory.setText(category);
        expenseNote.setText(note);
        expenseAmount.setText(amount);

        // Alternate background colors
//        if (cursor.getPosition()%2==1) {
//            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOdd));
//        } else {
//            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorEven));
//        }
    }
}
