package com.duykhanh.storeapp.presenter.order;

import android.util.Log;

import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.data.order.CartHandle;

import java.util.List;

public class CartPresenter implements CartContract.Presenter, CartContract.Handle.OnGetCartItemsListener,
        CartContract.Handle.OnIncreaseQuantityListener,
        CartContract.Handle.OnDecreaseQuantityListener,
        CartContract.Handle.OnDeleteCartItemListener,
        CartContract.Handle.OnGetCurrentUserListener {

    final String TAG = this.getClass().toString();

    CartContract.View iView;
    CartContract.Handle iHandle;

    public CartPresenter(CartContract.View iView) {
        this.iView = iView;
        iHandle = new CartHandle(iView);
    }

    @Override
    public void requestCartItems() {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getCartItems(this);
    }

    @Override //Tăng số lượng
    public void requestIncreaseQuantity(String cartProductId) {
        Log.d(TAG, "requestIncreaseQuantity: ");
        iHandle.increaseQuantity(this, cartProductId);
    }

    @Override //Giảm số lượng
    public void requestDecreaseQuantity(String cartProductId) {
        Log.d(TAG, "requestDecreaseQuantity: ");
        iHandle.decreaseQuantity(this, cartProductId);

    }

    @Override //Xóa món hàng
    public void requestDeleteCartItem(String cartProductId) {
        Log.d(TAG, "requestDeleteCartItem: ");
        iHandle.deleteCartItem(this, cartProductId);
    }

    @Override //Gửi yêu cầu kiểm tra trạng thái đăng nhập
    public void requestCurrentUser() {
        iHandle.getCurrentUser(this);
    }

    @Override //Trả về User Id (Có thể "")
    public void onGetCurrentUserFinished(String userId) {
        iView.requestCurrentUserComplete(userId);
    }

    //Request finished các kiểu
    @Override
    public void onGetCartItemsFinished(List<CartItem> cartItems) {
        Log.d(TAG, "onGetCartItemsFinished: test");
        if (iView != null) {
            Log.d(TAG, "onGetCartItemsFinished: test");
            iView.hideProgress();
        }
        iView.setCartItemsToCartRv(cartItems);
    }

    @Override
    public void onIncreaseQuantityFinished() {
        Log.d(TAG, "onIncreaseQuantityFinished: ");
        iHandle.getCartItems(this);
    }

    @Override
    public void onDecreaseQuantityFinished() {
        Log.d(TAG, "onDecreaseQuantityFinished: ");
        iHandle.getCartItems(this);
    }

    @Override
    public void onDeleteCartItemFinished() {
        Log.d(TAG, "onDeleteCartItemFinished: ");
        iHandle.getCartItems(this);
    }

    //Request Failure các kiểu
    @Override
    public void onGetCartItemsFailure(Throwable throwable) {
        Log.e(TAG, "onGetCartItemsFailure: ", throwable);
        if (iView != null) {
            iView.hideProgress();
            Log.e(TAG, "onGetCartItemsFailure: ", throwable);
        }
        iView.onCartItemsResponseFailure(throwable);
    }

    @Override
    public void onIncreaseQuantityFailure(Throwable throwable) {
        Log.e(TAG, "onIncreaseQuantityFailure: ", throwable);
    }

    @Override
    public void onDecreaseQuantityFailure(Throwable throwable) {
        Log.e(TAG, "onDecreaseQuantityFailure: ", throwable);

    }

    @Override
    public void onDeleteCartItemFailure(Throwable throwable) {
        Log.e(TAG, "onDeleteCartItemFailure: ", throwable);
    }

    @Override
    public void onGetCurrentUserFailure(Throwable throwable) {
        iView.onCartItemsResponseFailure(throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
