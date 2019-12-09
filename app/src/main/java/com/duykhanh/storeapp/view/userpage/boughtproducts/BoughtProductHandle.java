package com.duykhanh.storeapp.view.userpage.boughtproducts;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoughtProductHandle implements BoughtProductsContract.Handle {
    final String TAG = this.getClass().toString();

    SharedPreferences sharedPreferences;

    public BoughtProductHandle(BoughtProductsContract.View iView) {
        sharedPreferences = ((Context) iView).getSharedPreferences("User", Context.MODE_PRIVATE);
    }

    @Override
    public void getBoughtProducts(OnGetBoughtProductsListener listener) {
        String userId = sharedPreferences.getString("UserId", "");

        DataClient apiService = ApiUtils.getProductList();
        Call<List<Order>> call = apiService.getOrders(userId, 4);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.code());
                    return;
                }
                List<Order> orders = response.body();
                List<OrderDetail> orderDetails = null;
                for (Order order : orders) {
                    String orderId = order.getIdo();
                    orderDetails.addAll(getOrderDetail(orderId));
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
            }
        });
    }

    private List<OrderDetail> getOrderDetail(String orderId) {
        final List<OrderDetail> orderDetails = null;
        DataClient apiService = ApiUtils.getProductList();
        Call<List<OrderDetail>> call = apiService.getOrderDetails(orderId);
        call.enqueue(new Callback<List<OrderDetail>>() {
            @Override
            public void onResponse(Call<List<OrderDetail>> call, Response<List<OrderDetail>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.code());
                    return;
                }
//                orderDetails = response.body();
            }

            @Override
            public void onFailure(Call<List<OrderDetail>> call, Throwable t) {
            }
        });
        return orderDetails;
    }
}
