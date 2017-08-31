package com.yujotseng.expenselogger;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";

    private View view;
    private CalendarView calendarView;
    private HomeFragment.PassDataListener callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar_layout, container, false);

        calendarView = view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++; // since MONTH starts from 0
                String date = day + "/" + month + "/" + year;
                callback.PassDate(date, year, month, day, true);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure host activity implements PassDateListener interface, otherwise throw exception
        try {
            callback = (HomeFragment.PassDataListener) context;
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
}
