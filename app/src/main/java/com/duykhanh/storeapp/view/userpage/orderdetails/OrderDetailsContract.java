package com.duykhanh.storeapp.view.userpage.orderdetails;

import com.duykhanh.storeapp.model.OrderDetail;

import java.util.List;

public interface OrderDetailsContract {

    interface Handle {
        void getOrderDetails(OnGetOrderDetailsListener listener, String orderId);

        interface OnGetOrderDetailsListener {
            void onGetOrderDetailsFinished(List<OrderDetail> orderDetails);

            void onGetOrderDetailsFailure(Throwable throwable);
        }
    }

    interface View {
        void requestOrderDetailSuccess(List<OrderDetail> orderDetails);

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void requestOrderDetails(String orderId);

        void onDestroy();
    }

}
