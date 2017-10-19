package com.yujotseng.expenselogger;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// x: month, y: amount

public class BarChartFragment extends Fragment {
    private static final String TAG = "BarChartFragment";

    private View view;
    private DatabaseHandler databaseHandler;
    private BarChart barChart;
    private Cursor expenseCursor;
    private ArrayList<String> months;
    private double totalYearAmount;
    private TextView barChartTotalSpent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bar_chart_layout, container, false);

        // Instantiate database
        databaseHandler = DatabaseHandler.getInstance(getActivity());

        // Get UI
        final TextView barChartDate = view.findViewById(R.id.barChartDate);
        barChartTotalSpent = view.findViewById(R.id.barChartTotalSpent);
        ImageView prevYear = view.findViewById(R.id.prevYear);
        ImageView nextYear = view.findViewById(R.id.nextYear);
        barChart = (BarChart) view.findViewById(R.id.barChart);

        // Set UI
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String barChartDateString = simpleDateFormat.format(calendar.getTime());
        barChartDate.setText(barChartDateString);

        // Set image onClickListeners
        prevYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update UI
                calendar.add(Calendar.YEAR, -1);
                String yearString = simpleDateFormat.format(calendar.getTime());
                barChartDate.setText(yearString);

                // Update bar chart
                addData(calendar.get(Calendar.YEAR));
                setTotalSpent();
            }
        });

        nextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update UI
                calendar.add(Calendar.YEAR, 1);
                String yearString = simpleDateFormat.format(calendar.getTime());
                barChartDate.setText(yearString);

                // Update bar chart
                addData(calendar.get(Calendar.YEAR));
                setTotalSpent();
            }
        });

        addData(calendar.get(Calendar.YEAR));

        // Set UI
        setTotalSpent();

        // Set bar chart UI
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);       // Remove legend

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(12);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));

        barChart.getAxisRight().setEnabled(false);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setGranularity(1);
        yAxis.setGranularityEnabled(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    private void addData(int year) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        totalYearAmount = 0;

        for (int i=1; i<=12; i++) {
            // Get total expense amount of each month and add to barEntries
            expenseCursor = databaseHandler.getExpense(i, year);
            double totalMonthAmount = 0;
            if (expenseCursor.moveToFirst()) {
                do {
                    // Get cursor properties for each expense
                    int amountIndex = expenseCursor.getColumnIndex("_amount");
                    long amountInCents = expenseCursor.getLong(amountIndex);
                    double amountModified = (double) amountInCents / 100;
                    totalMonthAmount += amountModified;
                } while (expenseCursor.moveToNext());
            }
            totalYearAmount += totalMonthAmount;
            barEntries.add(new BarEntry(i-1, (float) totalMonthAmount));
        }
        expenseCursor.close();
        databaseHandler.close();

        months = new ArrayList<>();
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        BarDataSet barDataSet = new BarDataSet(barEntries, "Expense");
        barDataSet.setColor(ContextCompat.getColor(getActivity(), R.color.colorChartGreen));
        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new CurrencyFormatter());
        barData.setValueTextSize(10);
        barChart.setData(barData);
        barChart.highlightValues(null);
        barChart.invalidate();
    }

    private void setTotalSpent() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String totalYearAmountFormatted = numberFormat.format(totalYearAmount);
        String totalSpentString = "Total spent: " + totalYearAmountFormatted;
        Spannable spannable = new SpannableString(totalSpentString);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), ("Total spent: ").length(),
                ("Total spent: " + totalYearAmountFormatted).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        barChartTotalSpent.setText(spannable, TextView.BufferType.SPANNABLE);
    }
}
