package com.yujotseng.expenselogger;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";

    private View view;
    private DatabaseHandler databaseHandler;
    private ListView allExpenseListView;
    private HomeFragment.PassDataListener callback;
    private ArrayList<Expense> expenseArrayList;

    // Top expenses, can search note (if search term matches category name, filter by category)

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity());

        // Get UI
        allExpenseListView = (ListView) view.findViewById(R.id.allExpenseListView);
        allExpenseListView.setEmptyView(view.findViewById(R.id.allEmptyListView));

        populateListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void populateListView() {
        // Get expenses and add them to expenseArrayList
        Cursor cursor = databaseHandler.getExpense();
        expenseArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
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

                Expense expense = new Expense(category, amountInCents, "9/19/2017", note);
                expenseArrayList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        databaseHandler.close();

        // Sort expenseArrayList to order expenses from largest amount to smallest
        Collections.sort(expenseArrayList);

        // Create and set ListAdapter
        HoldExpenseListAdapter holdExpenseListAdapter = new HoldExpenseListAdapter(getActivity(), R.layout.expense_list_adapter_layout, expenseArrayList);
        allExpenseListView.setAdapter(holdExpenseListAdapter);

        // Add onItemClickListener to ListView
        allExpenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callback.passID(l, true);
            }
        });
    }
}
