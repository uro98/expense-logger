package com.yujotseng.expenselogger;

public class Expense {

    private long id;
    private String name;
    private long amount;
//    private String category;
    private String date;
    private String note;

    //public Expense(String name, double amount, String category, String date, String note) {
    public Expense(String name, long amount, String date, String note) {
        this.name = name;
        this.amount = amount;
//        this.category = category;
        this.date = date;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
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
