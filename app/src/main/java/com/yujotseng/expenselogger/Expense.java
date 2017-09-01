package com.yujotseng.expenselogger;

public class Expense {

    private long id;
    private String category;
    private long amount;
    private String date;
    private String note;

    public Expense(String category, long amount, String date, String note) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
