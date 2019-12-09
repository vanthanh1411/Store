package com.duykhanh.storeapp.presenter.category;

import android.content.Context;

import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.data.category.CategoryHandle;
import com.duykhanh.storeapp.model.data.category.CountProductCategoryHandle;

import java.util.List;

/**
 * Created by Duy Khánh on 11/26/2019.
 */
public class CategoryProductListPresenter implements CategoryProductListContract.Presenter,
        CategoryProductListContract.Handle.OnFinishedListener,
        CategoryProductListContract.Handle.OnFinishedListenderGetCount,
        CategoryProductListContract.Handle.OnFinishedListenerHighPrice,
        CategoryProductListContract.Handle.OnFinishedListenerLowPrice{

    CategoryProductListContract.View view;
    CategoryProductListContract.Handle handle;
    CategoryProductListContract.Handle.OnCountProductCart handleCount;

    public CategoryProductListPresenter(CategoryProductListContract.View view, Context context) {
        this.view = view;
        handle = new CategoryHandle();
        handleCount = new CountProductCategoryHandle(context);
    }

    @Override
    public void requestDataFromServer(String id_category, int pageCategory) {
        handle.getProductListCategory(this,id_category,1);
    }

    @Override
    public void getMoreListCategory(String id_category, int pageCategory) {
        handle.getProductListCategory(this,id_category,pageCategory);
    }

    @Override
    public void requestDataHighPriceFromServer(String id_category, int pageHigh) {
        handle.getProductListHightPriceProduct(this,id_category,1);
    }

    @Override
    public void requestDataLowPriceFromServer(String id_category, int pageLow) {
        handle.getProductListLowPriceProduct(this,id_category, 1);
    }

    @Override
    public void getMoreListHightPriceProduct(String id_category, int pageHigh) {
        handle.getProductListHightPriceProduct(this,id_category, pageHigh);
    }

    @Override
    public void getMoreListLowPriceProduct(String id_category, int pageLow) {
        handle.getProductListLowPriceProduct(this, id_category, pageLow);
    }

    @Override
    public void requestDataCountFormDB() {
        handleCount.getCountProductCart(this);
    }

    @Override
    public void onFinished(List<Product> productList) {
        if(view != null){
            view.hiddenProgressBar();
        }
        view.senDataToRecyclerView(productList);
    }

    @Override
    public void onFinishedHighPrice(List<Product> productList) {
        if(view != null){
            view.hiddenProgressBar();
        }
        view.sendDataToRecyclerViewHigh(productList);
    }

    @Override
    public void onFaildHighPrice() {
        if(view != null){
            view.hiddenProgressBar();
        }
    }

    @Override
    public void onFinishedLowPrice(List<Product> productList) {
        if(view != null){
            view.hiddenProgressBar();
        }
        view.sendDataToRecyclerViewLow(productList);
    }

    @Override
    public void onFaildLowPrice() {
        if(view != null){
            view.hiddenProgressBar();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(view != null){
            view.hiddenProgressBar();
        }
        view.onResponseFailure(t);
    }

    @Override
    public void onFinished(int countProduct) {
        if(countProduct != 0){
            if(view != null){
                view.showSizeCart();
            }
        }
        else {
            if(view != null) {
                view.hiddenSizeCart();
            }
        }
        view.sendCountProduct(countProduct);
    }

    @Override
    public void onFaild() {
        if(view != null){
            view.hiddenProgressBar();
        }
    }
}
