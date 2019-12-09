package com.duykhanh.storeapp.model.data.productdetail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.duykhanh.storeapp.daos.DatabaseHelper;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;
import com.duykhanh.storeapp.presenter.productdetail.ProductDetailContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailHandle implements ProductDetailContract.Handle {
    final String TAG = this.getClass().toString();

    SQLiteDatabase database;

    public ProductDetailHandle(ProductDetailContract.View iView) {
        database = new DatabaseHelper((Context) iView).getWritableDatabase();
    }

    //Lấy thông tin chi tiết sản phẩm
    @Override
    public void getProductDetail(OnGetProductDetailListener listener, String productId) {
        Log.d(TAG, "getProductDetail: productid" + productId);
        DataClient apiService = ApiUtils.getProductList();

        Call<Product> call = apiService.getProductDetail(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
                Product product = response.body();
                Log.d(TAG, "onResponse: " + response.body());
                listener.onGetProductDetailFinished(product);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onGetProductDetailFailure(t);
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    @Override
    public void getCommentByIdp(OnGetCommentByIdpListener listener, String productId) {
        DataClient apiService = ApiUtils.getProductList();

        Call<List<Comment>> call = apiService.getCommentByIdp(productId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                } else {
                    Log.d(TAG, "onResponse: err");
                }
                List<Comment> comments = response.body();
                listener.onGetCommentByIdpFinished(comments);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                listener.onGetCommentByIdpFailure(t);
            }
        });
    }

    @Override
    public void createCartItem(OnCreateCartItemListener listener, CartItem cartItem) {
        Log.d(TAG, "insertCart: " + cartItem.toString());

        String selectQuerry = "select " + DatabaseHelper.TABLE_CART_ID + " from " + DatabaseHelper.TABLE_CART + " where " + DatabaseHelper.TABLE_CART_IDP + " = " + "'" + cartItem.getProductid() + "'";

        String cartItemId = "";

        Cursor cursor = database.rawQuery(selectQuerry, null);
        if (cursor.moveToFirst()) {
            do {
                cartItemId = cursor.getString(0);
            }
            while (cursor.moveToNext());
        }
        if (cartItemId.equals("")) {
            Log.d(TAG, "createCartItem: Add");
            //Thêm sản phẩm vào giỏ hàng
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.TABLE_CART_IDP, cartItem.getProductid());
            values.put(DatabaseHelper.TABLE_CART_NAME, cartItem.getName());
            values.put(DatabaseHelper.TABLE_CART_QUANTITY, 1);
            values.put(DatabaseHelper.TABLE_CART_PRICE, cartItem.getPrice());
            values.put(DatabaseHelper.TABLE_CART_STORAGE, cartItem.getStorage());
            values.put(DatabaseHelper.TABLE_CART_IMAGE, cartItem.getImage());
            try {
                database.insert(DatabaseHelper.TABLE_CART, null, values);
                listener.onCreateCartItemFinished();
            } catch (Exception e) {
                listener.onCreateCartItemFailure(e);
            }
        } else {
            try {
                database.execSQL("update " + DatabaseHelper.TABLE_CART + " set " + DatabaseHelper.TABLE_CART_QUANTITY + "=" + DatabaseHelper.TABLE_CART_QUANTITY + " +1 where " + DatabaseHelper.TABLE_CART_IDP + "=" + "'" + cartItem.getProductid() + "'");
                listener.onCreateCartItemFinished();
            } catch (Exception e) {
                listener.onCreateCartItemFailure(e);
            }
        }
    }

    @Override
    public void getCartCounter(OnGetCartCounterListener listener) {
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
        listener.onGetCartCounterFinished(sumQuantity);
    }

    @Override
    public void increaseProductView(String productId) {
        DataClient apiService = ApiUtils.getProductList();
        Call<Product> call = apiService.putViewProductUp(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: code" + response.code());
                    return;
                }
                Log.d(TAG, "onResponse: up view complete");
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public void getInfomationUser(onGetInfomationUser callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                callback.onFinished(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFaild();
            }
        });
    }

    @Override
    public void onGetProductCount() {
        int countProduct = 0;
        String selectQuerry = "select " + DatabaseHelper.TABLE_CART_ID + " from " + DatabaseHelper.TABLE_CART;
        Cursor cursor = database.rawQuery(selectQuerry, null);
        if (cursor.moveToFirst()) {
            do {
                countProduct = cursor.getCount();

            }
            while (cursor.moveToNext());
        }
        Log.d(TAG, "onGetProductCount: " + countProduct);
    }

}
