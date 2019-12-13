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
        ProductDetailContract.Handle.OnGetInfomationUser,
        ProductDetailContract.Handle.OnGetCurrentUserListener,
        ProductDetailContract.Handle.OnGetRelatedProductsListener {
    final String TAG = this.getClass().toString();

    ProductDetailContract.Handle iHandle;
    ProductDetailContract.View iView;

    public ProductDetailPresenter(ProductDetailContract.View iView) {
        this.iView = iView;
        iHandle = new ProductDetailHandle(iView);
    }

    @Override
    public void requestCurrentUser() {
        iHandle.getCurrentUser(this);
    }

    @Override
    public void requestProductFromServer(String productId) {
        Log.d(TAG, "requestProductFromServer: ");
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getProductDetail(this, productId);
    }

    @Override
    public void requestCommentsFromServer(String productId) {
        iHandle.getCommentByIdp(this, productId);
    }

    @Override
    public void requestCartCounter() {
        Log.d(TAG, "requestCartCounter: ");
        iHandle.getCartCounter(this);
    }

    @Override
    public void requestInfomationUser() {
        iHandle.getInfomationUser(this);
    }


    @Override
    public void addCartItem(CartItem cartItem) {
        iHandle.createCartItem(this, cartItem);
    }

    @Override
    public void requestIncreaseView(String productId) {
        iHandle.increaseProductView(productId);
    }

    @Override//Yêu cầu lấy danh sách những sản phẩm liên quan
    public void requestRelatedProducts(String categoryId) {
        if (iView != null) {
            iHandle.getRelatedProducts(this, categoryId);
        }
    }

    @Override//Yêu cầu lấy danh sách sản phẩm liên quan thành công
    public void onGetRelatedProductsFinished(List<Product> products) {
        iView.requestRelatedProductsSuccess(products);
    }

    @Override
    public void onGetProductDetailFinished(Product product) {
        Log.d(TAG, "onGetProductDetailFinished: " + product.toString());
        if (iView != null) {
            iView.hideProgress();
            iView.setDataToView(product);
        }

    }

    @Override
    public void onGetCommentByIdpFinished(List<Comment> comments) {
        iView.setCommentsToRecyclerView(comments);
    }

    @Override
    public void onCreateCartItemFinished() {
        iHandle.getCartCounter(this);
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

    @Override
    public void onGetCurrentUserFinished(String userId) {
        if (iView != null) {
            iView.requestCurrentUserComplete(userId);
        }
    }

    @Override
    public void onGetCurrentUserFailure(Throwable throwable) {
        Log.e(TAG, "onGetCurrentUserFailure: ", throwable);
    }

    @Override//Lấy danh sách sản phẩm liên quan thất bại
    public void onGetRelatedProductsFailure(Throwable throwable) {
        Log.e(TAG, "onGetRelatedProductsFailure: ", throwable);
    }
}
