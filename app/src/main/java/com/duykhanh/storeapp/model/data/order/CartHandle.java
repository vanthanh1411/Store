package com.duykhanh.storeapp.model.data.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.duykhanh.storeapp.daos.DatabaseHelper;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;
import com.duykhanh.storeapp.presenter.order.CartContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartHandle implements CartContract.Handle {
    final String TAG = this.getClass().toString();

    SQLiteDatabase database;

    SharedPreferences sharedPreferences;

    public CartHandle(CartContract.View iView) {
        database = new DatabaseHelper(iView.getContext()).getWritableDatabase();
        sharedPreferences = iView.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
    }

    @Override
    public void getCartItems(OnGetCartItemsListener listener) {
        List<CartItem> cartItems = new ArrayList<>();

        String truyvan = "SELECT * FROM " + DatabaseHelper.TABLE_CART;
        Cursor cursor = database.rawQuery(truyvan, null);
        if (cursor.getCount() != 0) {
            //Lượt đầu: Refresh tồn kho trên SQLite
            if (cursor.moveToFirst()) {
                do {
                    int cartid = cursor.getInt(0);
                    String productid = cursor.getString(1);
                    //Xử lý tồn kho
                    if (productid != null || !productid.equals("")) {
                        getStorageByProductId(productid);
                    }
                } while (cursor.moveToNext());
            }
            cursor.moveToFirst();
            //Lượt 2 lấy về danh sách cart item đã được cập nhật
            do {
                int cartid = cursor.getInt(0);
                String productid = cursor.getString(1);
                String name = cursor.getString(2);
                int quantity = cursor.getInt(3);
                long price = cursor.getLong(4);
                long total = cursor.getLong(5);
                byte[] image = cursor.getBlob(6);
                if (total > 0){
                    CartItem cartItem = new CartItem(productid, name, price, quantity, total, image);
                    cartItems.add(cartItem);
                }
                else {
                    database.delete(DatabaseHelper.TABLE_CART, DatabaseHelper.TABLE_CART_IDP + "=?", new String[]{productid});
                }
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        listener.onGetCartItemsFinished(cartItems);
    }

    @Override
    public void increaseQuantity(OnIncreaseQuantityListener listener, String cartProductId) {
        Log.d(TAG, "increaseQuantity: ");
        try {
            database.execSQL("update " + DatabaseHelper.TABLE_CART + " set " + DatabaseHelper.TABLE_CART_QUANTITY + "=" + DatabaseHelper.TABLE_CART_QUANTITY + " +1 where " + DatabaseHelper.TABLE_CART_IDP + "=" + "'" + cartProductId + "'");
            listener.onIncreaseQuantityFinished();
        } catch (Exception e) {
            listener.onIncreaseQuantityFailure(e);
        }
    }

    @Override
    public void decreaseQuantity(OnDecreaseQuantityListener listener, String cartProductId) {
        Log.d(TAG, "decreaseQuantity: ");
        try {
            database.execSQL("update " + DatabaseHelper.TABLE_CART + " set " + DatabaseHelper.TABLE_CART_QUANTITY + "=" + DatabaseHelper.TABLE_CART_QUANTITY + " -1 where " + DatabaseHelper.TABLE_CART_IDP + "=" + "'" + cartProductId + "'");
            listener.onDecreaseQuantityFinished();
        } catch (Exception e) {
            listener.onDecreaseQuantityFailure(e);
        }
    }

    @Override
    public void deleteCartItem(OnDeleteCartItemListener listener, String cartProductId) {
        Log.d(TAG, "deleteCartItem: ");
        try {
            database.delete(DatabaseHelper.TABLE_CART, DatabaseHelper.TABLE_CART_IDP + "=?", new String[]{cartProductId});
            listener.onDeleteCartItemFinished();
        } catch (Exception e) {
            listener.onDeleteCartItemFailure(e);
        }
    }

    @Override
    public void getCurrentUser(OnGetCurrentUserListener listener) {
        try {
            String userId = sharedPreferences.getString("UserId", "");
            listener.onGetCurrentUserFinished(userId);
        } catch (Exception e) {
            listener.onGetCurrentUserFailure(e);
        }
    }

    private void getStorageByProductId(String productId) {
        DataClient apiService = ApiUtils.getProductList();

        Call<Product> call = apiService.getProductDetail(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    //Tìm sản phẩm
                    String selectQuerry = "select " + DatabaseHelper.TABLE_CART_ID + " from " + DatabaseHelper.TABLE_CART + " where " + DatabaseHelper.TABLE_CART_IDP + " = " + "'" + productId + "'";

                    String cartItemId = "";

                    Cursor cursor = database.rawQuery(selectQuerry, null);
                    if (cursor.moveToFirst()) {
                        do {
                            cartItemId = cursor.getString(0);
                        }
                        while (cursor.moveToNext());
                    }
                    if (cartItemId.equals("")) {
                        //Không có
                        return;
                    } else {
                        //Có
                        try {
                            //Cập nhật tồn kho
                            Log.d(TAG, "onResponse: refresh" + "update " + DatabaseHelper.TABLE_CART + " set " + DatabaseHelper.TABLE_CART_STORAGE + " = " + product.getQuantity() + " where " + DatabaseHelper.TABLE_CART_IDP + "=" + "'" + productId + "'");
                            database.execSQL("update " + DatabaseHelper.TABLE_CART + " set " + DatabaseHelper.TABLE_CART_STORAGE + " = " + product.getQuantity() + " where " + DatabaseHelper.TABLE_CART_IDP + "=" + "'" + productId + "'");
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: refresh storage", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
            }
        });
    }
}
