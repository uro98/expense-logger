package com.yujotseng.expenselogger;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    public static final String DATE = "_date";
    public static final String YEAR = "_year";
    public static final String MONTH = "_month";
    public static final String DAY = "_day";

    private View view;
    private Button newEntryButton;
    private Fragment fragment;
    private DatabaseHandler databaseHandler;
    private ListView expenseListView;
    private PassDataListener callback;
    private TextView date;
    private TextView totalSpent;
    private Calendar calendar;
    private String calendarDate;
    private Cursor cursor;
    private Bundle bundle;
    private int year;
    private int month;
    private int day;

    public interface PassDataListener {
        public void passID(long id, boolean toView);
        public void PassDate(String date, int year, int month, int day, boolean toHome);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);

        // Get calendar
        calendar = Calendar.getInstance();

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity());

        // Get UI
        date = (TextView) view.findViewById(R.id.date);
        totalSpent = (TextView) view.findViewById(R.id.totalSpent);
        newEntryButton = (Button) view.findViewById(R.id.newEntryButton);
        expenseListView = (ListView) view.findViewById(R.id.expenseListView);
        expenseListView.setEmptyView(view.findViewById(R.id.emptyListView));

        // Get NewEntryFragment
        fragment = getFragmentManager().findFragmentByTag("NewEntryFragment");
        if(fragment == null) {
            fragment = new NewEntryFragment();
        }

        // Set button onClickListener
        newEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle != null) {
                    // If currently on a different day (came from calendar), NewEntryFragment should use this date
                    callback.PassDate(day + "/" + month + "/" + year, year, month, day, false);
                } else {
                    // Go to NewEntryFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bundle = getArguments();
        if (bundle != null) {
            calendarDate = bundle.getString(DATE);
            year = bundle.getInt(YEAR);
            month = bundle.getInt(MONTH);
            day = bundle.getInt(DAY);

            date.setText(calendarDate + "\n" + getDayFromDate(year, month, day));
            populateListView(calendarDate);
            String totalSpentString = "Total spent: " + getTotalSpent(day + "/" + month + "/" + year);
            Spannable spannable = new SpannableString(totalSpentString);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), ("Total spent: ").length(),
                    ("Total spent: " + getTotalSpent(day + "/" + month + "/" + year)).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            totalSpent.setText(spannable, TextView.BufferType.SPANNABLE);
        } else {
            date.setText(getTodayDate() + "\n" + getTodayDayOfWeek());
            populateListView(getTodayDate());
            String totalSpentString = "Total spent: " + getTotalSpent(getTodayDate());
            Spannable spannable = new SpannableString(totalSpentString);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), ("Total spent: ").length(),
                    ("Total spent: " + getTotalSpent(getTodayDate())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            totalSpent.setText(spannable, TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure host activity implements PassIDListener interface, otherwise throw exception
        try {
            callback = (PassDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnListItemSelectedListener");
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void populateListView(String date) {
        // Get expenses
        cursor = databaseHandler.getExpense(date);

        // Create and set ListAdapter
        ExpenseListAdapter expenseListAdapter = new ExpenseListAdapter(getActivity(), cursor);
        expenseListView.setAdapter(expenseListAdapter);

        // Add onItemClickListener to ListView
        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callback.passID(l, true);

//                Cursor item = (Cursor) reminderCursorAdapter.getItem(position);
//                Log.d("Clicked item field", " "+ item.getColumn(your column index));
            }
        });
    }

    public String getTodayDate() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
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

    private String getDayFromDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        month--;    // MONTH starts from 0
        cal.set(year, month, day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        return simpleDateFormat.format(cal.getTime());
    }

    private String getTotalSpent(String date) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        long totalSpentInCents = 0;
        String amount;
        cursor = databaseHandler.getExpense(date);
        if (cursor.moveToFirst()) {
            do {
                int amountIndex = cursor.getColumnIndex("_amount");
                long amountInCents = cursor.getLong(amountIndex);
                totalSpentInCents += amountInCents;
            } while (cursor.moveToNext());
            double amountModified = (double) totalSpentInCents / 100;
            amount = numberFormat.format(amountModified);
        } else {
            amount = numberFormat.format(totalSpentInCents);
        }
        cursor.close();
        return amount;
    }
}
