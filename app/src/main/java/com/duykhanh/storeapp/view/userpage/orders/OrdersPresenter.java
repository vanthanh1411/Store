package com.duykhanh.storeapp.view.userpage.orders;

import android.util.Log;

import com.duykhanh.storeapp.model.Order;

import java.util.List;

public class OrdersPresenter implements OrdersContract.Presenter, OrdersContract.Handle.OnGetOrdersListener,
        OrdersContract.Handle.OnGetUserIdListener {
    final String TAG = this.getClass().toString();

    OrdersContract.View iView;
    OrdersContract.Handle iHandle;

    public OrdersPresenter(OrdersContract.View iView) {
        this.iView = iView;
        iHandle = new OrdersHandle(iView);
    }

    @Override//Yêu cầu lấy thông tin User
    public void requestGetUserId() {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getUserId(this);
    }

    @Override//Lấy thông tin người dùng thành công
    public void onGetCurrentUserFinished(String userId) {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestGetUserIdSuccess(userId);
    }

    @Override//Yêu cầu lấy list Order
    public void requestGetOrders(String userId, int orderStatus) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getOrders(this, userId, orderStatus);
    }

    @Override//Lấy list Order thành công
    public void onGetOrdersFinished(List<Order> orders) {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestGetOrdersSuccess(orders);
    }

    @Override//Lấy Id User thất bại
    public void onGetCurrentUserFailure(Throwable throwable) {
        if (iView != null) {
            iView.showProgress();
        }
        Log.e(TAG, "onGetCurrentUserFailure: ", throwable);
    }

    @Override//Lấy list Order thất bại
    public void onGetOrderFailure(Throwable throwable) {
        if (iView != null) {
            iView.showProgress();
        }
        Log.e(TAG, "onGetOrderFailure: ", throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
