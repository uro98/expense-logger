package com.yujotseng.expenselogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
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
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, expense.getName());
        sqLiteDatabase.insert(TABLE_EXPENSE, null, contentValues);
    }

    public void deleteExpense(long _id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_EXPENSE, COLUMN_ID + " = " + _id, null);
    }

    public int updateExpense(long _id, String name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);

        int i = sqLiteDatabase.update(TABLE_EXPENSE, contentValues, COLUMN_ID + " = " + _id, null);
        return i;
    }

    public Cursor getExpense(String name) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME
        };

        String selection = COLUMN_NAME + " = '" + name + "'";

        Cursor cursor = sqLiteDatabase.query(TABLE_EXPENSE, projection, selection, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getExpense(long _id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME
        };

        String selection = COLUMN_ID + " = " + _id;

        Cursor cursor = sqLiteDatabase.query(TABLE_EXPENSE, projection, selection, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }
}