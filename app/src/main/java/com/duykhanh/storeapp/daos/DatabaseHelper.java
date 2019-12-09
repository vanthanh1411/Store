package com.duykhanh.storeapp.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    final String TAG = this.getClass().toString();

    public static String TABLE_CART = "cart";
    public static String TABLE_CART_ID = "id";
    public static String TABLE_CART_IDP = "idp";
    public static String TABLE_CART_NAME = "name";
    public static String TABLE_CART_IMAGE = "imgp";
    public static String TABLE_CART_QUANTITY = "quantity";
    public static String TABLE_CART_PRICE = "price";
    public static String TABLE_CART_STORAGE = "storage";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "SQLSizeCart", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table_cart = "CREATE TABLE " + TABLE_CART +
                " ( "
                + TABLE_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TABLE_CART_IDP + " TEXT, "
                + TABLE_CART_NAME + " TEXT, "
                + TABLE_CART_QUANTITY + " INTEGER, "
                + TABLE_CART_PRICE + " REAl, "
                + TABLE_CART_STORAGE + " REAl,"
                + TABLE_CART_IMAGE + " BLOB "
                + " );";
        Cursor c1 = db.rawQuery("PRAGMA journal_mode=off", null);
        c1.close();
        db.disableWriteAheadLogging();
        db.execSQL(table_cart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.w(TAG, "onOpen: ");
        db.disableWriteAheadLogging();
        Cursor c1 = db.rawQuery("PRAGMA journal_mode=delete", null);
        c1.close();
        super.onOpen(db);
    }
}
