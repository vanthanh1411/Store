package com.duykhanh.storeapp.presenter.productdetail;

import android.util.Log;

import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.model.data.productdetail.ProductDetailHandle;

import java.util.List;

public class ProductDetailPresenter implements ProductDetailContract.Presenter,
        ProductDetailContract.Handle.OnGetProductDetailListener,
        ProductDetailContract.Handle.OnGetCommentByIdpListener,
        ProductDetailContract.Handle.OnCreateCartItemListener,
        ProductDetailContract.Handle.OnGetCartCounterListener,
        ProductDetailContract.Handle.onGetInfomationUser {
    final String TAG = this.getClass().toString();

    ProductDetailContract.Handle iHanlde;
    ProductDetailContract.View iView;

    public ProductDetailPresenter(ProductDetailContract.View iView) {
        this.iView = iView;
        iHanlde = new ProductDetailHandle(iView);
    }

    @Override
    public void requestProductFromServer(String productId) {
        Log.d(TAG, "requestProductFromServer: ");
        if (iView != null) {
            iView.showProgress();
        }
        iHanlde.getProductDetail(this, productId);
    }

    @Override
    public void requestCommentsFromServer(String productId) {
        iHanlde.getCommentByIdp(this, productId);
    }

    @Override
    public void requestCartCounter() {
        Log.d(TAG, "requestCartCounter: ");
        iHanlde.getCartCounter(this);
    }

    @Override
    public void requestInfomationUser() {
        iHanlde.getInfomationUser(this);
    }


    @Override
    public void addCartItem(CartItem cartItem) {
        iHanlde.createCartItem(this, cartItem);
    }

    @Override
    public void requestIncreaseView(String productId) {
        iHanlde.increaseProductView(productId);
    }

    @Override
    public void onGetProductDetailFinished(Product product) {
        Log.d(TAG, "onGetProductDetailFinished: " + product.toString());
        if (iView != null) {
            iView.hideProgress();
        }
        iView.setDataToView(product);

    }

    @Override
    public void onGetCommentByIdpFinished(List<Comment> comments) {
        iView.setCommentsToRecyclerView(comments);
    }

    @Override
    public void onCreateCartItemFinished() {
        iHanlde.getCartCounter(this);
    }

    @Override
    public void onGetCartCounterFinished(int sumQuantity) {
        iView.setCartItemCounter(sumQuantity);
    }

    @Override
    public void onGetProductDetailFailure(Throwable throwable) {
        iView.onResponseFailure(throwable);
        if (iView != null) {
            iView.hideProgress();
        }
    }

    @Override
    public void onGetCommentByIdpFailure(Throwable throwable) {
        iView.onCommentsResponseFailure(throwable);
    }

    @Override
    public void onCreateCartItemFailure(Throwable throwable) {
        iView.onCartItemCountResponseFailure(throwable);
    }

    @Override
    public void onFinished(List<User> userList) {
        if (iView != null) {
            iView.sendInfomationUser(userList);
        }
    }

    @Override
    public void onFaild() {
        iView.onFaild();
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
