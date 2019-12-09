package com.duykhanh.storeapp.presenter.home;

import android.content.Context;

import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.SlideHome;
import com.duykhanh.storeapp.model.data.home.ProductHandle;
import com.duykhanh.storeapp.model.data.home.CountProductHomeHandle;

import java.util.List;

/**
 * Created by Duy Khánh on 11/6/2019.
 * Lớp trung gian giao giao tiếp giữu Handle và View
 */
public class HomePresenter implements ProductListContract.Presenter,
        ProductListContract.Handle.OnFinishedListener,
        ProductListContract.Handle.OnFinishedListenerView,
        ProductListContract.Handle.OnFinishedListenerBuy,
        ProductListContract.Handle.OnFinishedListenderGetCount,
        ProductListContract.Handle.OnFinishedUtiliti{

    // Presenter interface dùng cho view
    private ProductListContract.View productListView;

    // Presenter interface dùng cho Handle
    private ProductListContract.Handle handleProductList;

    private ProductListContract.Handle.OnCountProductCart handleCountProduct;

    /*
    * Hàm khởi tạo của HomePresenter với tham số là ProductListContract.View.
    * Tham số thể hiện dùng để giao tiếp giữu HomePresenter và HomeFragment
    */
    public HomePresenter(ProductListContract.View productListView, Context context) {
        this.productListView = productListView;
        handleProductList = new ProductHandle();
        handleCountProduct = new CountProductHomeHandle(context);
    }

    /*
    * Nhận dữ liệu từ class xử lý data ProductHandle.class và gửi dữ liệu cho view
    */
    @Override
    public void onFinished(List<Product> productArrayList) {
        productListView.sendDataToRecyclerView(productArrayList);
        if(productListView != null){
            productListView.hideProgress();
        }
    }

    @Override
    public void onFinishedLoadMore() {
        if(productListView != null){
            productListView.hideProgress();
        }
    }

    // Lỗi kết nối được gọi bởi ProductHandle.class và gửi cho view
    @Override
    public void onFailure(Throwable t) {
        // Gửi lỗi cho view
        productListView.onResponseFailure(t);
    }


    @Override
    public void getMoreData(int pageNo) {
        if(productListView != null){
            productListView.showProgress();
        }
        handleProductList.getProductList(this,pageNo);
    }

    @Override
    public void getMoreDataView(int pageView) {
        handleProductList.getProductView(this,pageView);
    }

    /*
    * Gửi yêu cầu từ view và được xử lý bởi ProductHandle.class
    */
    @Override
    public void requestDataFromServer() {
        if(productListView != null){
            productListView.showProgress();
        }
        handleProductList.getProductList(this,1);
    }

    @Override
    public void requestDataFromServerView() {
        if(productListView != null){
            productListView.showProgress();
        }
        handleProductList.getProductView(this,1);
    }

    @Override
    public void requestDatatFromServerBuy() {
        handleProductList.getProductBuy(this,0);
    }

    @Override
    public void requestDataCountFormDB() {
        handleCountProduct.getCountProductCart(this);
    }

    @Override
    public void requestFromDataSlideHome() {
        handleProductList.getUtiliti(this);
    }

    /*
    * hận dữ liệu từ class xử lý data ProductHandle.class và gửi dữ liệu cho view
    */
    @Override
    public void onFinishedView(List<Product> viewProductArrayList) {
        productListView.sendDataToHorizontalView(viewProductArrayList);
        if(productListView != null){
            productListView.hideProgress();
        }
    }

    @Override
    public void onFailureView(Throwable t) {
        if(productListView != null){
            productListView.hideProgress();
        }
        productListView.onResponseFailure(t);
    }

    @Override
    public void onFinished(int countProduct) {
        if(countProduct != 0){
            if(productListView != null){
                productListView.showSizeCart();
            }
        }
        else {
            if(productListView != null) {
                productListView.hideSizeCart();
            }
        }
        productListView.sendCountProduct(countProduct);
    }

    @Override
    public void onFinishedBuy(List<Product> buyProductArrayList) {
        productListView.sendDataToHorizontalBuy(buyProductArrayList);
    }

    @Override
    public void onFinishedUtiliti(List<SlideHome> slideHomeList) {
        productListView.sendDataToSlideShowHome(slideHomeList);
    }

    @Override
    public void onFaild() {

    }

    @Override
    public void onFailureBuy(Throwable t) {
        productListView.onResponseFailure(t);
    }
}
