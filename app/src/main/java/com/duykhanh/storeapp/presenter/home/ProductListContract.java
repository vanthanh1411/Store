package com.duykhanh.storeapp.presenter.home;

import android.graphics.Movie;

import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.ProductResponse;
import com.duykhanh.storeapp.model.SlideHome;

import java.util.List;

/**
 * Created by Duy Khánh on 11/5/2019.
 */
public interface ProductListContract {

    // interface cho ProductHandle
    interface Handle {

        interface OnFinishedListener {
            void onFinished(List<Product> productArrayList);

            void onFinishedLoadMore();

            void onFailure(Throwable t);
        }

        interface OnFinishedListenerView {
            void onFinishedView(List<Product> viewProductArrayList);

            void onFailureView(Throwable t);
        }

        interface OnFinishedListenerBuy {
            void onFinishedBuy(List<Product> buyProductArrayList);

            void onFaild();

            void onFailureBuy(Throwable t);
        }

        interface OnFinishedListenderGetCount{
            void onFinished(int countProduct);
            void onFaild();
        }

        interface OnFinishedUtiliti{
            void onFinishedUtiliti(List<SlideHome> slideHomeList);
            void onFaild();
        }

        interface OnCountProductCart{
            void getCountProductCart(OnFinishedListenderGetCount handleCount);
        }

        void getProductList(OnFinishedListener onFinishedListener, int pageNo);

        void getProductView(OnFinishedListenerView onFinishedListenerView, int pageView);

        void getProductBuy(OnFinishedListenerBuy onFinishedListenerBuy, int pageBuy);

        void getUtiliti(OnFinishedUtiliti finishedUtiliti);
    }


    // interface cho HomeFragment
    interface View {
        void showProgress();

        void hideProgress();

        void showSizeCart();

        void hideSizeCart();

        void sendDataToRecyclerView(List<Product> productArrayList);

        void sendDataToHorizontalView(List<Product> viewProductArrayList);

        void sendDataToHorizontalBuy(List<Product> buyProductArrayList);

        void sendDataToSlideShowHome(List<SlideHome> slideHomeList);

        void sendCountProduct(int countProduct);

        void onResponseFailure(Throwable throwable);

    }

    // interface cho preseenter
    interface Presenter {

        void getMoreData(int pageNo);

        void getMoreDataView(int pageView);

        void requestDataFromServer();

        void requestDataFromServerView();

        void requestDatatFromServerBuy();

        void requestDataCountFormDB();

        void requestFromDataSlideHome();
    }
}
