package com.duykhanh.storeapp.presenter.order;

import android.util.Log;

import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.model.data.order.PaymentHandle;

import java.util.List;

public class PaymentPresenter implements PaymentContract.Presenter,
        PaymentContract.Handle.OnGetCartItemsListener,
        PaymentContract.Handle.OnPostOrderListener,
        PaymentContract.Handle.OnPostOrderDetailListener,
        PaymentContract.Handle.OnGetOrderDetailsListener,
        PaymentContract.Handle.OnDeleteCartsListener,
        PaymentContract.Handle.OnGetCurrentUserListener,
        PaymentContract.Handle.OnUpdateUserInfoListener {
    final String TAG = this.getClass().toString();

    PaymentContract.View iView;
    PaymentContract.Handle iHandle;

    public PaymentPresenter(PaymentContract.View iView) {
        this.iView = iView;
        iHandle = new PaymentHandle(iView);
    }

    @Override //Yêu cầu trạng thái đăng nhập
    public void requestCurrentUser() {
        iHandle.getCurrentUser(this);
    }

    @Override //Hoàn thành yêu cầu trạng thái đăng nhập
    public void onGetCurrentUserFinished(User user) {
        iView.requestCurrentUserComplete(user);
    }

    @Override //Gửi yêu cầu update thông tin người dùng
    public void requestUpdateUserInfo(User user) {
        iHandle.updateUserInfo(this, user);
    }

    @Override //Yêu cầu update hoàn thành
    public void onUpdateUserInfoFinished() {
        Log.d(TAG, "onUpdateUserInfoFinished: ");
        //Lấy lại thông tin người dùng
        iHandle.getCurrentUser(this);
    }

    @Override //Gửi yêu cầu lấy danh sách sản phẩm trong giỏ hàng từ SQLite
    public void requestCartItemsFromSql() {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getCartItems(this);
    }

    @Override //Lấy danh sách mặt hàng trong giỏ hàng thành công.
    public void onGetCartItemsFinished(List<CartItem> cartItems) {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestCartItemsComplete(cartItems);
    }

    @Override //Yêu cầu lấu danh sách Order Detail
    public void requestOrderDetailsFromSql(Order order) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getOrderDetails(this, order);
    }

    @Override //Lấy danh sách OrderDetail hoàn thành
    public void onGetOrderDetailsFinished(Order order, List<OrderDetail> orderDetails) {
        iHandle.postOrder(this, order, orderDetails);
    }


    //    Request POST Order và Listener
    @Override
    public void requestPay(Order order, List<OrderDetail> orderDetails) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.postOrder(this, order, orderDetails);
    }

    @Override
    public void onPostOrderFinished(List<OrderDetail> orderDetails) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.postOrderDetail(this, orderDetails);
    }

    @Override
    public void onPostOrderFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestPayedFailure(throwable);
    }

    @Override
    public void onGetOrderDetailFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestPayedFailure(throwable);
    }

    //    POST Orders Detail Listener

    @Override
    public void onPostOrderDetailFinished(List<OrderDetail> orderDetails) {
        Log.d(TAG, "onPostOrderDetailFinished: ");
        iHandle.deleteCarts(this, orderDetails);
    }

    @Override //Hoàn thành xóa Cart
    public void onDeleteCartsFinished() {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.onPayed();
    }

    @Override
    public void onGetCartItemsFailure(Throwable throwable) {
        iView.requestPayedFailure(throwable);
    }

    @Override
    public void onPostOrderDetailFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
            iView.requestPayedFailure(throwable);
        }
    }

    @Override
    public void onGetCurrentUserFailure(Throwable throwable) {
        if (iView != null) {
            iView.requestPayedFailure(throwable);
        }
    }

    @Override//Xóa Cart thất bại
    public void onDeleteCartsFailure(Throwable throwable) {
        if (iView != null) {
            iView.requestPayedFailure(throwable);
        }
    }

    @Override
    public void onUpdateUserInfoFailure(Throwable throwable) {
        if (iView != null) {
            iView.requestPayedFailure(throwable);
        }
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
