package com.yujotseng.expenselogger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * piechart: category + percentage ((category expense list) list with amount below), can pick %/$
 * barchart: time + amount
 * list top expenses
 */

public class AnalysisFragment extends Fragment {
    private static final String TAG = "AnalysisFragment";

    private View view;
    private GridView analysisGridView;
    private Fragment pieChartFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.analysis_layout, container, false);

        // Get UI
        analysisGridView = view.findViewById(R.id.analysisGridView);

        // Set up gridView arrays
        String[] analysisNames = {"Monthly expense category breakdown", "Bar chart", "List"};
        int[] drawables = {R.drawable.ic_pie_chart_black_24dp, R.drawable.ic_assessment_black_24dp, R.drawable.ic_format_list_bulleted_black_24dp};

        // Create and set ListAdapter
        AnalysisGridAdapter analysisGridAdapter = new AnalysisGridAdapter(getActivity(), analysisNames, drawables);
        analysisGridView.setAdapter(analysisGridAdapter);

        // Add onItemClickListener to ListView
        analysisGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        pieChartFragment = getFragmentManager().findFragmentByTag("PieChartFragment");
                        if (pieChartFragment == null) {
                            pieChartFragment = new PieChartFragment();
                        }
                        switchFragment(pieChartFragment);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
