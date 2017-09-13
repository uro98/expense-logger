package com.yujotseng.expenselogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;

public class DatabaseHandler extends SQLiteOpenHelper {

    // todo: check memory leak, store date as integers, fragment navigation/stack, icons,
    // todo: amount in/under calendar, category management
    // todo: analysis, settings(budget)

    // Database info
    private static final int DATABASE_VERSION = 16;
    private static final String DATABASE_NAME = "expense.db";

    // Tables
    public static final String TABLE_EXPENSE = "expense";
    public static final String TABLE_CATEGORY = "category";

    // Expense table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AMOUNT = "_amount";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_NOTE = "_note";

    // Common columns
    public static final String COLUMN_CATEGORY = "_category";

    // Create expense table statement
    private static final String CREATE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY + " TEXT NOT NULL, " +
            COLUMN_AMOUNT + " INTEGER NOT NULL, " +
            COLUMN_DATE + " TEXT NOT NULL, " +
            COLUMN_NOTE + " TEXT" +
            ");";

    // Create category table statement
    private static final String CREATE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" +
            COLUMN_CATEGORY + " TEXT PRIMARY KEY NOT NULL " +
            ");";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EXPENSE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY);
        addDefaultCategories(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(sqLiteDatabase);
    }

    // Expense table CRUD
    public void addExpense(Expense expense) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, expense.getCategory());
        contentValues.put(COLUMN_AMOUNT, expense.getAmount());
        contentValues.put(COLUMN_DATE, expense.getDate());
        contentValues.put(COLUMN_NOTE, expense.getNote());
        sqLiteDatabase.insert(TABLE_EXPENSE, null, contentValues);
    }

    public void deleteExpense(long _id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String selection = COLUMN_ID + " Like ?";
        String[] selectionArgs = { Long.toString(_id) };
        sqLiteDatabase.delete(TABLE_EXPENSE, selection, selectionArgs);
    }

    public int updateExpense(long _id, String category, long amount, String date, String note) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, category);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_NOTE, note);

        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { Long.toString(_id) };

        int i = sqLiteDatabase.update(TABLE_EXPENSE, contentValues, selection, selectionArgs);
        return i;
    }

    // For expenseListView
    public Cursor getExpense(String date) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_CATEGORY,
                COLUMN_NOTE,
                COLUMN_AMOUNT
        };

        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = { date };

        Cursor cursor = sqLiteDatabase.query(TABLE_EXPENSE, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    // For expense detail
    public Cursor getExpense(long _id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_CATEGORY,
                COLUMN_AMOUNT,
                COLUMN_DATE,
                COLUMN_NOTE
        };

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { Long.toString(_id) };

        Cursor cursor = sqLiteDatabase.query(TABLE_EXPENSE, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    // For expense category pie chart
    public Cursor getCategoriesOfMonth(int month, int year) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_CATEGORY
        };

        String selection = COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = { "%" + month + "/" + year };

        Cursor cursor = sqLiteDatabase.query(true, TABLE_EXPENSE, projection, selection, selectionArgs, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return  cursor;
    }

    // For expense category pie chart
    public Cursor getExpense(int month, int year) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_CATEGORY,
                COLUMN_AMOUNT
        };

        String selection = COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = { "%" + month + "/" + year };

        Cursor cursor = sqLiteDatabase.query(TABLE_EXPENSE, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return  cursor;
    }

    // Category table CRUD
    public void addCategory(String categoryName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, categoryName);
        sqLiteDatabase.insert(TABLE_CATEGORY, null, contentValues);
    }

    public void deleteCategory(String categoryName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String selection = COLUMN_CATEGORY + " LIKE ?";
        String[] selectionArgs = { categoryName };
        sqLiteDatabase.delete(TABLE_CATEGORY, selection, selectionArgs);
    }

    public void updateCategory(String oldCategoryName, String newCategoryName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, newCategoryName);

        String selection = COLUMN_CATEGORY + " LIKE ?";
        String[] selectionArgs = { oldCategoryName };

        sqLiteDatabase.update(TABLE_CATEGORY, contentValues, selection, selectionArgs);
        sqLiteDatabase.update(TABLE_EXPENSE, contentValues, selection, selectionArgs);      // Also update expenses' categoryName
    }

    // For categoryListView
    public Cursor getCategory() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_CATEGORY
        };

        Cursor cursor = sqLiteDatabase.query(TABLE_CATEGORY, projection, null, null, null, null, COLUMN_CATEGORY + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public void addDefaultCategories(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        String[] defaultCategories = {"Food", "Clothing", "Household", "Transport", "Education", "Entertainment"};
        for(String defaultCategory : defaultCategories) {
            contentValues.put(COLUMN_CATEGORY, defaultCategory);
            sqLiteDatabase.insert(TABLE_CATEGORY, null, contentValues);
        }
    }
}