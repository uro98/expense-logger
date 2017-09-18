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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class PieChartFragment extends Fragment {
    private static final String TAG = "PieChartFragment";

    private View view;
    private PieChart pieChart;
    private DatabaseHandler databaseHandler;
    private ArrayList<PieChartEntry> pieChartEntryArrayList;
    private NumberFormat numberFormat;
    private double totalMonthAmount;

    // todo: overlap, (category on click list)

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pie_chart_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity());

        // Get UI
        final TextView pieChartDate = view.findViewById(R.id.pieChartDate);
        ImageView prevMonth = view.findViewById(R.id.prevMonth);
        ImageView nextMonth = view.findViewById(R.id.nextMonth);
        pieChart = (PieChart) view.findViewById(R.id.pieChart);

        // Set UI
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        String pieChartDateString = simpleDateFormat.format(calendar.getTime());
        pieChartDate.setText(pieChartDateString);

        // Set image onClickListeners
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update UI
                calendar.add(Calendar.MONTH, -1);
                String monthName = simpleDateFormat.format(calendar.getTime());
                pieChartDate.setText(monthName);

                // Update pie chart
                getPieChartData(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                setCenterText();
                addData();
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update UI
                calendar.add(Calendar.MONTH, 1);
                String monthName = simpleDateFormat.format(calendar.getTime());
                pieChartDate.setText(monthName);

                // Update pie chart
                getPieChartData(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                setCenterText();
                addData();
            }
        });

        getPieChartData(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        // Set pie chart UI
        setCenterText();
        pieChart.setCenterTextSize(15);
        pieChart.setHoleRadius(42);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setEntryLabelColor(ContextCompat.getColor(getActivity(), R.color.colorDark));
        pieChart.setExtraOffsets(5, 0, 5, 0);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);       // Remove legend
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setWordWrapEnabled(true);

        addData();

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void getPieChartData(int month, int year) {
        // Get categories of the month, initialize the amount to 0 and put in ArrayList
        Cursor categoryCursor = databaseHandler.getCategoriesOfMonth(month, year);
        pieChartEntryArrayList = new ArrayList<>();
        if (categoryCursor.moveToFirst()) {
            do {
                int categoryIndex = categoryCursor.getColumnIndex("_category");
                String categoryName = categoryCursor.getString(categoryIndex);
                PieChartEntry pieChartEntry = new PieChartEntry(categoryName, 0, 0);
                pieChartEntryArrayList.add(pieChartEntry);
            } while (categoryCursor.moveToNext());
        }
        categoryCursor.close();

        // Get expenses of the month, sort their amounts into category amounts
        Cursor expenseCursor = databaseHandler.getExpense(month, year);
        totalMonthAmount = 0;
        if (expenseCursor.moveToFirst()) {
            do {
                // Get cursor properties for each expense
                int categoryIndex = expenseCursor.getColumnIndex("_category");
                String categoryName = expenseCursor.getString(categoryIndex);

                int amountIndex = expenseCursor.getColumnIndex("_amount");
                long amountInCents = expenseCursor.getLong(amountIndex);
                double amountModified = (double) amountInCents / 100;

                // If expense belongs in a category, add expense amount to category amount
                for (int i=0; i<pieChartEntryArrayList.size(); i++) {
                    if (categoryName.equals(pieChartEntryArrayList.get(i).getCategory())) {
                        totalMonthAmount += amountModified;
                        double updatedCategoryAmount = pieChartEntryArrayList.get(i).getAmount() + amountModified;
                        pieChartEntryArrayList.get(i).setAmount(updatedCategoryAmount);
                        break;
                    }
                }
            } while (expenseCursor.moveToNext());
        }
        expenseCursor.close();
        databaseHandler.close();

        // Update category percentages
        for (PieChartEntry pieChartEntry : pieChartEntryArrayList) {
            float percentage = (float) (pieChartEntry.getAmount() / totalMonthAmount * 100);
            pieChartEntry.setPercentage(percentage);
        }
    }

    private void setCenterText() {
        numberFormat = NumberFormat.getCurrencyInstance();
        String totalMonthAmountFormatted = numberFormat.format(totalMonthAmount);
        String totalSpentString = "Total spent: " + totalMonthAmountFormatted;
        Spannable spannable = new SpannableString(totalSpentString);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), ("Total spent: ").length(),
                ("Total spent: " + totalMonthAmountFormatted).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        pieChart.setCenterText(spannable);
    }

    private void addData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        // Sort pieChartEntryArrayList to order pieChartEntries from largest amount spent in the category to smallest
        Collections.sort(pieChartEntryArrayList);

        for (PieChartEntry pieChartEntry : pieChartEntryArrayList) {
            pieEntries.add(new PieEntry(pieChartEntry.getPercentage(), pieChartEntry.getCategory() + " (" + numberFormat.format(pieChartEntry.getAmount()) + ")"));
        }

        // Pie chart colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Monthly Expense Category Breakdown");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setColors(colors);
//        pieDataSet.setValueLinePart1OffsetPercentage(0);
//        pieDataSet.setValueLinePart1Length(0f);
//        pieDataSet.setValueLinePart2Length(0f);
//        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}
