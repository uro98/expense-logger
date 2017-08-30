package com.yujotseng.expenselogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "expense.db";

    public static final String TABLE_EXPENSE = "expense";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "_name";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_NOTE = "_note";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_EXPENSE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_DATE + " TEXT NOT NULL, " +
            COLUMN_NOTE + " TEXT" +
            ");";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(sqLiteDatabase);
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, expense.getName());
        contentValues.put(COLUMN_DATE, expense.getDate());
        contentValues.put(COLUMN_NOTE, expense.getNote());
        sqLiteDatabase.insert(TABLE_EXPENSE, null, contentValues);
    }

    public void deleteExpense(long _id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_EXPENSE, COLUMN_ID + " = " + _id, null);
    }

    public int updateExpense(long _id, String name, String date, String note) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_NOTE, note);

        int i = sqLiteDatabase.update(TABLE_EXPENSE, contentValues, COLUMN_ID + " = " + _id, null);
        return i;
    }

    // For ListView
    public Cursor getExpense(String date) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DATE
        };

        String selection = COLUMN_DATE + " = '" + date + "'";

        Cursor cursor = sqLiteDatabase.query(TABLE_EXPENSE, projection, selection, null, null, null, null);

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
                COLUMN_NAME,
                COLUMN_DATE,
                COLUMN_NOTE
        };

        String selection = COLUMN_ID + " = " + _id;

        Cursor cursor = sqLiteDatabase.query(TABLE_EXPENSE, projection, selection, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }
}