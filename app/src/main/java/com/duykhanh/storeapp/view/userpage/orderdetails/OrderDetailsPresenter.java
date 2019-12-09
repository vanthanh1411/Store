package com.duykhanh.storeapp.view.userpage.orderdetails;

import android.util.Log;

import com.duykhanh.storeapp.model.OrderDetail;

import java.util.List;

public class OrderDetailsPresenter implements OrderDetailsContract.Presenter,
        OrderDetailsContract.Handle.OnGetOrderDetailsListener {
    final String TAG = this.getClass().toString();

    OrderDetailsContract.View iView;
    OrderDetailsContract.Handle iHandle;

    public OrderDetailsPresenter(OrderDetailsContract.View iView) {
        this.iView = iView;
        iHandle = new OrderDetailsHandle();
    }

    @Override//Yêu cầu lấy list Order Detail
    public void requestOrderDetails(String orderId) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getOrderDetails(this, orderId);
    }

    @Override//Lấy list Order Detail thành công
    public void onGetOrderDetailsFinished(List<OrderDetail> orderDetails) {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestOrderDetailSuccess(orderDetails);
    }

    @Override//Lấy list Order Detail thất bại
    public void onGetOrderDetailsFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
        }
        Log.e(TAG, "onGetOrderDetailsFailure: ", throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
