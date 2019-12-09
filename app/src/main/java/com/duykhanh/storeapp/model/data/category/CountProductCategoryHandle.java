package com.duykhanh.storeapp.model.data.category;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.duykhanh.storeapp.daos.DatabaseHelper;
import com.duykhanh.storeapp.presenter.category.CategoryContract;
import com.duykhanh.storeapp.presenter.category.CategoryProductListContract;

/**
 * Created by Duy Khánh on 11/28/2019.
 */
public class CountProductCategoryHandle implements CategoryContract.Handle.OnCountProductCart,
        CategoryProductListContract.Handle.OnCountProductCart {
    SQLiteDatabase database;

    public CountProductCategoryHandle(Context context) {
        database = new DatabaseHelper(context).getWritableDatabase();
    }

    @Override
    public void getCountProductCart(CategoryContract.Handle.OnFinishedListenderGetCount handleCount) {
//        int countProduct = 0;
//        String selectQuerry = "select " + DatabaseHelper.TABLE_CART_ID + " from " + DatabaseHelper.TABLE_CART;
//        Cursor cursor = database.rawQuery(selectQuerry, null);
//        if (cursor.moveToFirst()) {
//            do {
//                countProduct = cursor.getCount();
//            }
//            while (cursor.moveToNext());
//        }
        int sumQuantity = 0;
        String selectQuerry = "select " + DatabaseHelper.TABLE_CART_QUANTITY + " from " + DatabaseHelper.TABLE_CART;
        Cursor cursor = database.rawQuery(selectQuerry, null);
        if (cursor.moveToFirst()) {
            do {
                int quantity = cursor.getInt(0);
                sumQuantity += quantity;
            }
            while (cursor.moveToNext());
        }
        handleCount.onFinished(sumQuantity);
    }

    @Override
    public void getCountProductCart(CategoryProductListContract.Handle.OnFinishedListenderGetCount handleCount) {
        int sumQuantity = 0;
        String selectQuerry = "select " + DatabaseHelper.TABLE_CART_QUANTITY + " from " + DatabaseHelper.TABLE_CART;
        Cursor cursor = database.rawQuery(selectQuerry, null);
        if (cursor.moveToFirst()) {
            do {
                int quantity = cursor.getInt(0);
                sumQuantity += quantity;
            }
            while (cursor.moveToNext());
        }
        handleCount.onFinished(sumQuantity);
    }
}
