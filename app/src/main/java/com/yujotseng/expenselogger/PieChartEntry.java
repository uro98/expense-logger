package com.yujotseng.expenselogger;

import android.support.annotation.NonNull;

public class PieChartEntry implements Comparable<PieChartEntry> {

    private String category;
    private double amount;
    private float percentage;

    public PieChartEntry(String category, double amount, float percentage) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }



    @Override
    public int compareTo(@NonNull PieChartEntry pieChartEntry) {
        return 0;
    }
}
