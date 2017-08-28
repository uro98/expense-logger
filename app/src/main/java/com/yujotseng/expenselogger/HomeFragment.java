package com.yujotseng.expenselogger;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private View view;
    private Button newEntryButton;
    private Fragment fragment;
    private DatabaseHandler databaseHandler;
    private ListView expenseListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        newEntryButton = (Button) view.findViewById(R.id.newEntryButton);

        fragment = getFragmentManager().findFragmentByTag("NewEntryFragment");

        if(fragment == null) {
            fragment = new NewEntryFragment();
        }

        expenseListView = (ListView) view.findViewById(R.id.expenseListView);

        newEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.commit();
            }
        });

        populateListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    public void populateListView() {
        // Get expense and append to ArrayList
        Cursor cursor = databaseHandler.getExpense();
        ArrayList<String> expenseList = new ArrayList<>();
        while(cursor.moveToNext()) {
            // Get value from column 1 and add to List
            expenseList.add(cursor.getString(1));
        }
        // Create and set List Adapter
        ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, expenseList);
        expenseListView.setAdapter(listAdapter);

        // Add onItemClickListener to ListView
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
            }
        });
    }
}
