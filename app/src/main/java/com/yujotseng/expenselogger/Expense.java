package com.yujotseng.expenselogger;

public class Expense {

    private int id;
    private String name;
//    private double amount;
//    private String category;
//    private String date;
//    private String time;
//    private String note;

    //public Expense(String name, double amount, String category, String date, String time, String note) {
    public Expense(String name) {
        this.name = name;
//        this.amount = amount;
//        this.category = category;
//        this.date = date;
//        this.time = time;
//        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getNote() {
//        return note;
//    }
//
//    public void setNote(String note) {
//        this.note = note;
//    }
}
