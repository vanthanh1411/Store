package com.duykhanh.storeapp.model.data.sale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duykhanh.storeapp.daos.DatabaseHelper;
import com.duykhanh.storeapp.presenter.home.ProductListContract;
import com.duykhanh.storeapp.presenter.sale.SaleContract;

/**
 * Created by Duy Khánh on 12/8/2019.
 */
public class CountProductSaleHandle implements SaleContract.Handle.OnCountProductCart {
    SQLiteDatabase database;

    public CountProductSaleHandle(Context context) {
        database = new DatabaseHelper(context).getWritableDatabase();
    }

    @Override
    public void getCountProductCart(SaleContract.Handle.OnFinishedListenderGetCount handleCount) {
        int countProduct = 0;
        String selectQuerry = "select " + DatabaseHelper.TABLE_CART_ID + " from " + DatabaseHelper.TABLE_CART;
        Cursor cursor = database.rawQuery(selectQuerry, null);
        if (cursor.moveToFirst()) {
            do {
                countProduct = cursor.getCount();

            }
            while (cursor.moveToNext());
        }
        handleCount.onFinished(countProduct);
    }
}
