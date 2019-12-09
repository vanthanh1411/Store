package com.duykhanh.storeapp.model.data.sale;

import android.util.Log;

import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.SlideSale;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;
import com.duykhanh.storeapp.presenter.sale.SaleContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public class SaleHandle implements SaleContract.Handle {

    private static final String TAG = SaleHandle.class.getSimpleName();

    @Override
    public void onGetDataFormServer(onFinishedListener callback,String idSale) {
        DataClient apiService = ApiUtils.getProductList();

        Call<List<SlideSale>> call = apiService.getProductSale(idSale);
        call.enqueue(new Callback<List<SlideSale>>() {
            @Override
            public void onResponse(Call<List<SlideSale>> call, Response<List<SlideSale>> response) {
                if (response.isSuccessful()) {

                } else {
                    callback.onFaild();
                }
                List<SlideSale> slideSales = response.body();
                Log.d(TAG, "onResponse: " + slideSales.size());
                callback.onFinished(slideSales);
            }

            @Override
            public void onFailure(Call<List<SlideSale>> call, Throwable t) {
                callback.onFaild();
            }
        });
    }
}
