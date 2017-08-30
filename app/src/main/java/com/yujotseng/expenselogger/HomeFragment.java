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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private View view;
    private Button newEntryButton;
    private Fragment fragment;
    private DatabaseHandler databaseHandler;
    private ListView expenseListView;
    private PassDataListener callback;
    private TextView date;
    private Calendar calendar;

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

        // Get calendar
        calendar = Calendar.getInstance();

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        // Get UI
        date = (TextView) view.findViewById(R.id.date);
        newEntryButton = (Button) view.findViewById(R.id.newEntryButton);
        expenseListView = (ListView) view.findViewById(R.id.expenseListView);
        expenseListView.setEmptyView(view.findViewById(R.id.emptyListView));

        // Set UI
        date.setText(getTodayDate() + "\n" + getTodayDayOfWeek());

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

        Log.d(TAG, "onCreateView: " + getTodayDate());
        populateListView(getTodayDate());

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void populateListView(String date) {
        // Get expense and append to ArrayList
        Cursor cursor = databaseHandler.getExpense(date);

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

    private String getTodayDate() {
        // Get current year, month and day
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        month++;    // MONTH starts from 0
        return day + "/" + month + "/" + year;
    }

    private String getTodayDayOfWeek() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        return simpleDateFormat.format(calendar.getTime());
    }
}
