package com.duykhanh.storeapp.view.userpage.boughtproducts;

import android.util.Log;

import com.duykhanh.storeapp.model.OrderDetail;

import java.util.List;

public class BoughtProductsPresenter implements BoughtProductsContract.Presenter,
        BoughtProductsContract.Handle.OnGetBoughtProductsListener {
    final String TAG = this.getClass().toString();

    BoughtProductsContract.View iView;
    BoughtProductsContract.Handle iHandle;

    public BoughtProductsPresenter(BoughtProductsContract.View iView) {
        this.iView = iView;
        iHandle = new BoughtProductHandle(iView);
    }

    @Override
    public void requestBoughtProducts() {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getBoughtProducts(this);
    }

    @Override
    public void onGetBoughtProductsFinished(List<OrderDetail> orderDetails) {
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestBoughtProductsSuccess(orderDetails);
    }

    @Override
    public void onGetBoughtProductsFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
        }
        Log.e(TAG, "onGetBoughtProductsFailure: ", throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
