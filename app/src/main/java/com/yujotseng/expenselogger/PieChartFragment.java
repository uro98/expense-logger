package com.yujotseng.expenselogger;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;

public class PieChartFragment extends Fragment {
    private static final String TAG = "PieChartFragment";

    private View view;
    private PieChart pieChart;
    private DatabaseHandler databaseHandler;

    // category pie chart of the month
    // x: category, y: percentage/amount

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pie_chart_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity());

        // Get UI
        pieChart = (PieChart) view.findViewById(R.id.pieChart);

        // Get categories of the month and put in ArrayList
        Cursor categoryCursor = databaseHandler.getCategoriesOfMonth(9, 2017);

        ArrayList<String> categoryArrayList = new ArrayList<>();
        if (categoryCursor.moveToFirst()) {
            do {
                int categoryIndex = categoryCursor.getColumnIndex("_category");
                String categoryName = categoryCursor.getString(categoryIndex);
                categoryArrayList.add(categoryName);
            } while (categoryCursor.moveToNext());
        }

        // Create array to hold amounts corresponding to categories
        Double[] categoryAmount = new Double[categoryArrayList.size()];

        // Get expenses of the month, sort their amounts into category amounts
        Cursor expenseCursor = databaseHandler.getExpense(9, 2017);
        if (expenseCursor.moveToFirst()) {
            do {
                // Get cursor properties for each expense
                int categoryIndex = categoryCursor.getColumnIndex("_category");
                String categoryName = categoryCursor.getString(categoryIndex);

                int amountIndex = expenseCursor.getColumnIndex("_amount");
                long amountInCents = expenseCursor.getLong(amountIndex);
                double amountModified = (double) amountInCents / 100;

                // If expense belongs in a category, add expense amount to category amount
                for (int i=0; i<categoryArrayList.size(); i++) {
                    if (categoryName.equals(categoryArrayList.get(i))) {
                        categoryAmount[i] += amountModified;
                        break;
                    }
                }
            } while (categoryCursor.moveToNext());
        }

        // Format category amounts and hold in String array
        String[] categoryAmountFormatted = new String[categoryAmount.length];
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        for (int i=0; i<categoryAmountFormatted.length; i++) {
            categoryAmountFormatted[i] = numberFormat.format(categoryAmount[i]);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }
}
