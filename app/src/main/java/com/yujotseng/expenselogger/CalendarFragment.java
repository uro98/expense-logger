package com.yujotseng.expenselogger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Yu Jo on 8/26/2017.
 */

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_layout, container, false);

        return view;
    }
}
