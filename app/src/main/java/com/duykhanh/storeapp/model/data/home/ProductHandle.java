package com.duykhanh.storeapp.model.data.home;

import android.util.Log;

import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.ProductResponse;
import com.duykhanh.storeapp.model.SlideHome;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;
import com.duykhanh.storeapp.presenter.home.ProductListContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Duy Khánh on 11/6/2019.
 * Lớp này xử lý yêu cầu giữa client với server và nhận dữ liệu trả về.
 * Có thể xem nó được tách ra từ Presenter để thuận tiện cho việc xử lý dữ liệu
 */
public class ProductHandle implements ProductListContract.Handle {

    private final String TAG = "ProductHandle";

    // Hàm xử lý dữ liệu từ server
    @Override
    public void getProductList(OnFinishedListener onFinishedListener, int pageNo) {
        DataClient apiService = ApiUtils.getProductList();
        /*
        * Gửi yêu cầu trả về 1 danh sách dữ liệu (List<Product>)
        */
        Call<List<Product>> call = apiService.getDataProduct(pageNo);
        call.enqueue(new Callback<List<Product>>() {
            // Khi nhận được phản hồi
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                // Thành công gửi dữ liệu dưới dáng danh sách sản phẩm
                if(response.isSuccessful()) {
                    List<Product> products = response.body();
                    if(response.body().size() == 0){
                        onFinishedListener.onFinishedLoadMore();
                        return;
                    }
                    Log.d(TAG, "onResponse: " + response.body().size());
                    // Gửi dữ liệu cho presenter
                    onFinishedListener.onFinished(products);

                }
            }
            // Lỗi khi đang giao tiếp với server
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Gửi đi thông báo lỗi cho presenter
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void getProductView(OnFinishedListenerView onFinishedListenerView, int pageView) {
        DataClient apiService = ApiUtils.getProductList();
        /*
         * Gửi yêu cầu trả về 1 danh sách dữ liệu (List<Product>)
         */
        Call<List<Product>> call = apiService.getProductView(pageView);
        call.enqueue(new Callback<List<Product>>() {
            // Khi nhận được phản hồi
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                // Thành công gửi dữ liệu dưới dạng danh sách sản phẩm
                if(response.isSuccessful()) {
                    List<Product> productView= response.body();
                    Log.d(TAG, "onResponse: " + response.body().size());
                    // Gửi dữ liệu cho presenter
                    onFinishedListenerView.onFinishedView(productView);
                }
            }
            // Lỗi khi đang giao tiếp với server
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Gửi đi thông báo lỗi cho presenter
                onFinishedListenerView.onFailureView(t);
            }
        });
    }

    @Override
    public void getProductBuy(OnFinishedListenerBuy onFinishedListenerBuy, int pageBuy) {
        DataClient apiService = ApiUtils.getProductList();
        /*
         * Gửi yêu cầu trả về 1 danh sách dữ liệu (List<ProductResponse>)
         */
        Call<List<ProductResponse>> call = apiService.getProductBuy(pageBuy);
        call.enqueue(new Callback<List<ProductResponse>>() {
            // Khi nhận được phản hồi
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                // Thành công gửi dữ liệu dưới dạng danh sách sản phẩm
                if(response.isSuccessful()) {
                    List<ProductResponse> productResponse = response.body();
                    List<Product> arrayProductList = new ArrayList<>();
                    try{
                        for (int i = 0; i < productResponse.size(); i++) {
                            List<Product> productList = productResponse.get(i).getProducts();
                            arrayProductList.add(productList.get(0));
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    // Gửi dữ liệu cho presenter
                    onFinishedListenerBuy.onFinishedBuy(arrayProductList);
                }
            }
            // Lỗi khi đang giao tiếp với server
            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                // Gửi đi thông báo lỗi cho presenter
                onFinishedListenerBuy.onFailureBuy(t);
            }
        });
    }

    @Override
    public void getUtiliti(OnFinishedUtiliti finishedUtiliti) {
        DataClient apiService = ApiUtils.getProductList();
        /*
         * Gửi yêu cầu trả về 1 danh sách dữ liệu (List<ProductResponse>)
         */
        Call<List<SlideHome>> call = apiService.getUtiliti();
        call.enqueue(new Callback<List<SlideHome>>() {
            // Khi nhận được phản hồi
            @Override
            public void onResponse(Call<List<SlideHome>> call, Response<List<SlideHome>> response) {
                // Thành công gửi dữ liệu dưới dạng danh sách sản phẩm
                if(response.isSuccessful()) {
                    List<SlideHome> productResponse = response.body();
                    // Gửi dữ liệu cho presenter
                    finishedUtiliti.onFinishedUtiliti(productResponse);
                }
            }
            // Lỗi khi đang giao tiếp với server
            @Override
            public void onFailure(Call<List<SlideHome>> call, Throwable t) {
                // Gửi đi thông báo lỗi cho presenter
                finishedUtiliti.onFaild();
            }
        });
    }
}
