package com.duykhanh.storeapp.model.data.search;

import android.util.Log;

import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;
import com.duykhanh.storeapp.presenter.search.SearchContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchHandle implements SearchContract.Handle {
    final String TAG = this.getClass().toString();


    public SearchHandle(SearchContract.View iView) {
    }

    @Override
    public void getSuggestWords(OnGetSuggestWordsListener listener) {
        List<String> suggestWords = new ArrayList<>();
        suggestWords.add("gương");
        suggestWords.add("giường ngủ");
        suggestWords.add("kệ sách");
        suggestWords.add("tủ gỗ");
        suggestWords.add("bàn làm việc");
        suggestWords.add("trang trí");
        suggestWords.add("nội thất");
        suggestWords.add("bàn");

        listener.onGetSuggestWordsFinished(suggestWords);
    }

    @Override
    public void getProductByKey(OnGetProductByKeyListener listener, String searchingKey, int pageNo) {
        DataClient apiService = ApiUtils.getProductList();
        Call<List<Product>> call = apiService.getProductByKey(searchingKey, pageNo);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response.code());
                    return;
                }
                List<Product> products = response.body();
                listener.onGetProductByKeyFinished(products);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                listener.onGetProductByKeyFailure(t);
            }
        });

    }

}
