package com.duykhanh.storeapp.presenter.category;

import com.duykhanh.storeapp.model.Product;

import java.util.List;

/**
 * Created by Duy Khánh on 11/26/2019.
 */
public interface CategoryProductListContract {
    // interface cho ProductHandle
    interface Handle {
        interface OnFinishedListener {
            void onFinished(List<Product> productList);

            void onFaild();

            void onFailure(Throwable t);
        }

        interface OnFinishedListenderGetCount {
            void onFinished(int countProduct);

            void onFaild();
        }

        interface OnFinishedListenerHighPrice {
            void onFinishedHighPrice(List<Product> productList);

            void onFaildHighPrice();

            void onFailure(Throwable t);
        }

        interface OnFinishedListenerLowPrice {
            void onFinishedLowPrice(List<Product> productList);

            void onFaildLowPrice();

            void onFailure(Throwable t);
        }

        interface OnCountProductCart {
            void getCountProductCart(CategoryProductListContract.Handle.OnFinishedListenderGetCount handleCount);
        }

        void getProductListCategory(CategoryProductListContract.Handle.OnFinishedListener onFinishedListener, String id_category, int pageCategory);

        void getProductListHightPriceProduct(CategoryProductListContract.Handle.OnFinishedListenerHighPrice onFinishedListenerHighPrice, String id_category, int pageHight);

        void getProductListLowPriceProduct(CategoryProductListContract.Handle.OnFinishedListenerLowPrice onFinishedListenerLowPrice, String id_category, int pageLow);
    }


    // interface cho HomeFragment
    interface View {

        void senDataToRecyclerView(List<Product> productList);

        void sendDataToRecyclerViewHigh(List<Product> productHight);

        void sendDataToRecyclerViewLow(List<Product> productView);

        void sendCountProduct(int countProduct);

        void onResponseFailure(Throwable throwable);

        void hiddenProgressBar();

        void showSizeCart();

        void hiddenSizeCart();

    }

    // interface cho preseenter
    interface Presenter {
        void requestDataFromServer(String id_category, int pageCategory);

        void getMoreListCategory(String id_category, int pageCategory);

        void requestDataHighPriceFromServer(String id_category, int pageHight);

        void requestDataLowPriceFromServer(String id_categoy, int pagelow);

        void getMoreListHightPriceProduct(String id_category, int pageHigh);

        void getMoreListLowPriceProduct(String id_category, int pageLow);

        void requestDataCountFormDB();
    }
}
