package com.duykhanh.storeapp.model.data.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.duykhanh.storeapp.daos.DatabaseHelper;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.duykhanh.storeapp.presenter.order.PaymentContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHandle implements PaymentContract.Handle {
    final String TAG = this.getClass().toString();

    SQLiteDatabase database;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;

    public PaymentHandle(PaymentContract.View iView) {
        database = new DatabaseHelper((Context) iView).getWritableDatabase();
        sharedPreferences = ((Context) iView).getSharedPreferences("User", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override //Lấy thông tin người dùng đang đăng nhập từ Firebase
    public void getCurrentUser(OnGetCurrentUserListener listener) {
        String userId = sharedPreferences.getString("UserId", "");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    listener.onGetCurrentUserFinished(user);
                } catch (Exception e) {
                    listener.onGetCurrentUserFailure(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onGetCurrentUserFailure(databaseError.toException());
            }
        });
    }

    @Override //Update thông tin người dùng
    public void updateUserInfo(OnUpdateUserInfoListener listener, User user) {
        Log.d(TAG, "updateUserInfo: " + user.toString());
        try {
            String userId = sharedPreferences.getString("UserId", "");
            databaseReference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: ");
                    listener.onUpdateUserInfoFinished();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                }
            });
        } catch (Exception e) {
            listener.onUpdateUserInfoFailure(e);
        }
    }

    @Override // Lây danh sách mặt hàng trong giỏ hàng từ SQLite
    public void getCartItems(OnGetCartItemsListener listener) {
        Log.d(TAG, "getCartItems: ");
        try {
            List<CartItem> cartItems = new ArrayList<>();
            String truyvan = "SELECT * FROM " + DatabaseHelper.TABLE_CART;
            Cursor cursor = database.rawQuery(truyvan, null);
            if (cursor.moveToFirst()) {
                do {
                    int cartid = cursor.getInt(0);
                    String productid = cursor.getString(1);
                    String name = cursor.getString(2);
                    int quantity = cursor.getInt(3);
                    long price = cursor.getLong(4);
                    long total = cursor.getLong(5);
                    byte[] image = cursor.getBlob(6);
                    CartItem cartItem = new CartItem(productid, name, price, quantity, total, image);
                    cartItems.add(cartItem);
                }
                while (cursor.moveToNext());
                Log.d(TAG, "getCartItems: " + cartItems.size());
                listener.onGetCartItemsFinished(cartItems);
            }
        } catch (Exception e) {
            listener.onGetCartItemsFailure(e);
        }
    }

    @Override
    public void getOrderDetails(OnGetOrderDetailsListener listener, Order order) {


        try {
            List<OrderDetail> orderDetails = new ArrayList<>();
            String truyvan = "SELECT * FROM " + DatabaseHelper.TABLE_CART;
            Cursor cursor = database.rawQuery(truyvan, null);
            if (cursor.moveToFirst()) {
                do {
                    String productid = cursor.getString(1);
                    int quantity = cursor.getInt(3);
                    OrderDetail orderDetail = new OrderDetail("", productid, quantity);
                    orderDetails.add(orderDetail);
                }
                while (cursor.moveToNext());
                listener.onGetOrderDetailsFinished(order, orderDetails);
            }
        } catch (Exception e) {
            listener.onGetOrderDetailFailure(e);
        }
    }

    @Override //Gửi đơn hàng
    public void postOrder(OnPostOrderListener listener, Order order, List<OrderDetail> orderDetails) {
        DataClient apiService = ApiUtils.getProductList();
        order.setIdu(sharedPreferences.getString("UserId", ""));
        Call<Order> call = apiService.postOrder(order);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
                String orderId = response.body().getIdo();
                Log.d(TAG, "onResponse: " + orderId);
                for (OrderDetail orderDetail : orderDetails) {
                    orderDetail.setOrderId(orderId);
                }
                    listener.onPostOrderFinished(orderDetails);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                listener.onPostOrderFailure(t);
            }
        });
    }

    @Override
    public void postOrderDetail(OnPostOrderDetailListener listener, List<OrderDetail> orderDetails) {

//        DataClient apiService = ApiUtils.getProductList();
//        Call<Order> call = apiService.postOrderDetail(orderDetail);

        for (OrderDetail orderDetail : orderDetails) {
            DataClient apiService = ApiUtils.getProductList();
            Call<OrderDetail> call = apiService.postOrderDetail(orderDetail);
            call.enqueue(new Callback<OrderDetail>() {
                @Override
                public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "onResponse: " + response.code());
                        return;
                    }
                    listener.onPostOrderDetailFinished(orderDetails);
//                    Log.d(TAG, "onResponse: posted" + orderDetail.toString());
                }

                @Override
                public void onFailure(Call<OrderDetail> call, Throwable t) {
                    listener.onPostOrderDetailFailure(t);
                }
            });
        }
    }

    @Override //Xóa giỏ hàng
    public void deleteCarts(OnDeleteCartsListener listener, List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            String cartProductId = orderDetail.getProductId();
            try {
                database.delete(DatabaseHelper.TABLE_CART, DatabaseHelper.TABLE_CART_IDP + "=?", new String[]{cartProductId});
                listener.onDeleteCartsFinished();
            } catch (Exception e) {
                listener.onDeleteCartsFailure(e);
            }
        }
    }
}
