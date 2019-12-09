package com.duykhanh.storeapp.view.userpage.orderdetails;

import android.util.Log;

import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsHandle implements OrderDetailsContract.Handle {
    final String TAG = this.getClass().toString();

    @Override
    public void getOrderDetails(OnGetOrderDetailsListener listener, String orderId) {
        DataClient apiService = ApiUtils.getProductList();
        Call<List<OrderDetail>> call = apiService.getOrderDetails(orderId);
        call.enqueue(new Callback<List<OrderDetail>>() {
            @Override
            public void onResponse(Call<List<OrderDetail>> call, Response<List<OrderDetail>> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
                List<OrderDetail> orderDetails = response.body();
                listener.onGetOrderDetailsFinished(orderDetails);
            }

            @Override
            public void onFailure(Call<List<OrderDetail>> call, Throwable t) {
                listener.onGetOrderDetailsFailure(t);
            }
        });
    }
}
