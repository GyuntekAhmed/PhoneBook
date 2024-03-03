package com.example.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRST_NAME = "firstname";
    public static final String COLUMN_LAST_NAME = "lastname";
    public static final String COLUMN_PHONE_NUMBER = "phonenumber";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_FIRST_NAME + " TEXT, " +
            COLUMN_LAST_NAME + " TEXT, " +
            COLUMN_PHONE_NUMBER + " TEXT)";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addContact(String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor searchContact(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_PHONE_NUMBER};
        String selection = COLUMN_FIRST_NAME + " LIKE ? OR " + COLUMN_LAST_NAME + " LIKE ? OR " + COLUMN_PHONE_NUMBER + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%"};
        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    public void deleteContact(int contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "_id = ?", new String[]{String.valueOf(contactId)});
        db.close();
    }

    public long getLastInsertedContactId() {
        SQLiteDatabase db = this.getReadableDatabase();
        long lastInsertedId = -1;

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        if (cursor != null && cursor.moveToFirst()) {
            lastInsertedId = cursor.getLong(0);
            cursor.close();
        }

        return lastInsertedId;
    }
}
