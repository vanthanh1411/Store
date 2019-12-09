package com.duykhanh.storeapp.view.userpage.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersHandle implements OrdersContract.Handle {
    final String TAG = this.getClass().toString();

    SharedPreferences sharedPreferences;

    public OrdersHandle(OrdersContract.View iView) {
        sharedPreferences = ((Context) iView).getSharedPreferences("User", Context.MODE_PRIVATE);
    }

    @Override
    public void getUserId(OnGetUserIdListener listener) {
        String userId = sharedPreferences.getString("UserId", "");
        if (userId.equals("")) {
            Log.d(TAG, "getUserId: empty");
            return;
        }
        listener.onGetCurrentUserFinished(userId);
    }

    @Override
    public void getOrders(OnGetOrdersListener listener, String userId, int orderStatus) {
        DataClient apiService = ApiUtils.getProductList();
        Call<List<Order>> call = apiService.getOrders(userId, orderStatus);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.code());
                    return;
                }
                List<Order> orders = response.body();
                listener.onGetOrdersFinished(orders);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                listener.onGetOrderFailure(t);
            }
        });
    }
}
