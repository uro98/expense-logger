package com.yujotseng.expenselogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expense.db";

    public static final String TABLE_EXPENSE = "expense";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "_name";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_EXPENSE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT NOT NULL" +
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
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, expense.getName());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_EXPENSE, null, contentValues);
    }

    public void deleteExpense(Expense expense) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_EXPENSE +
        " WHERE " + COLUMN_ID + " = " + expense.getId() + ";");
    }

    public Cursor getExpense() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSE;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return cursor;
    }
}
