package com.duykhanh.storeapp.presenter.sale;

import com.duykhanh.storeapp.model.SlideSale;

import java.util.List;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public interface SaleContract {

    interface Handle{
        interface onFinishedListener{
            void onFinished(List<SlideSale> slideSaleList);
            void onFaild();
        }

        interface OnFinishedListenderGetCount {
            void onFinished(int countProduct);

            void onFaild();
        }

        interface OnCountProductCart {
            void getCountProductCart(SaleContract.Handle.OnFinishedListenderGetCount handleCount);
        }

        void onGetDataFormServer(onFinishedListener callback,String idSale);

    }

    interface View{
        void sendDataSlale(List<SlideSale> slideSales);

        void sendCountProduct(int countProduct);

        void showSizeCart();

        void hiddenSizeCart();
    }

    interface Presenter{
        void requestFormServer(String idSale);

        void requestDataCountFormDB();
    }
}
