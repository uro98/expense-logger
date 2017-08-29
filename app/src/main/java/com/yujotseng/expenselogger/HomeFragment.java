package com.yujotseng.expenselogger;

import android.content.Context;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private View view;
    private Button newEntryButton;
    private Fragment fragment;
    private DatabaseHandler databaseHandler;
    private ListView expenseListView;
    private PassDataListener callback;

    public interface PassDataListener {
        public void passData(long id, boolean toView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure host activity implements OnListItemSelectedListener interface, otherwise throw exception
        try {
            callback = (PassDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnListItemSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        // Get UI
        newEntryButton = (Button) view.findViewById(R.id.newEntryButton);
        expenseListView = (ListView) view.findViewById(R.id.expenseListView);

        // Get NewEntryFragment
        fragment = getFragmentManager().findFragmentByTag("NewEntryFragment");
        if(fragment == null) {
            fragment = new NewEntryFragment();
        }

        // Set button onClickListener
        newEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to NewEntryFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.commit();
            }
        });

        populateListView("apple");

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void populateListView(String name) {
        // Get expense and append to ArrayList
        Cursor cursor = databaseHandler.getExpense(name);

        // Create and set List Adapter
        ExpenseListAdapter expenseListAdapter = new ExpenseListAdapter(getActivity(), cursor);
        expenseListView.setAdapter(expenseListAdapter);

        // Add onItemClickListener to ListView
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callback.passData(l, true);

//                Cursor item = (Cursor) reminderCursorAdapter.getItem(position);
//                Log.d("Clicked item field", " "+ item.getColumn(your column index));
            }
        });
    }
}
