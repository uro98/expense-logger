package com.yujotseng.expenselogger;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.NumberFormat;

public class TwoDecimalFormatter implements IValueFormatter {
    private static final String TAG = "TwoDecimalFormatter";

    private NumberFormat numberFormat;

    public TwoDecimalFormatter() {
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return numberFormat.format(value);
    }
}
