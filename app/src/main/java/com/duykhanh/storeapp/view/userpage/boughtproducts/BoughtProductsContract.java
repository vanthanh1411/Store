package com.duykhanh.storeapp.view.userpage.boughtproducts;

import com.duykhanh.storeapp.model.OrderDetail;

import java.util.List;

public interface BoughtProductsContract {

    interface Handle {
        void getBoughtProducts(OnGetBoughtProductsListener listener);

        interface OnGetBoughtProductsListener {
            void onGetBoughtProductsFinished(List<OrderDetail> orderDetails);

            void onGetBoughtProductsFailure(Throwable throwable);
        }
    }

    interface View {

        void requestBoughtProductsSuccess(List<OrderDetail> orderDetails);

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void requestBoughtProducts();

        void onDestroy();
    }
}
