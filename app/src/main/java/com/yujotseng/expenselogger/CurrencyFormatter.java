package com.yujotseng.expenselogger;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.NumberFormat;

public class CurrencyFormatter implements IValueFormatter {
    private static final String TAG = "CurrencyFormatter";

    private NumberFormat numberFormat;

    public CurrencyFormatter() {
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return numberFormat.format(value);
    }
}
