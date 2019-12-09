package com.duykhanh.storeapp.presenter.sale;

import android.content.Context;

import com.duykhanh.storeapp.model.SlideSale;
import com.duykhanh.storeapp.model.data.sale.CountProductSaleHandle;
import com.duykhanh.storeapp.model.data.sale.SaleHandle;

import java.util.List;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public class SalePresenter implements SaleContract.Presenter,
        SaleContract.Handle.onFinishedListener,
        SaleContract.Handle.OnFinishedListenderGetCount{

    SaleContract.Handle handle;
    SaleContract.View view;
    SaleContract.Handle.OnCountProductCart handleCount;

    public SalePresenter(SaleContract.View view, Context context) {
        this.view = view;
        this.handle = new SaleHandle();
        this.handleCount = new CountProductSaleHandle(context);
    }

    @Override
    public void onFinished(List<SlideSale> slideSaleList) {
        view.sendDataSlale(slideSaleList);
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

    }

    @Override
    public void requestFormServer(String idSale) {
        handle.onGetDataFormServer(this,idSale);
    }

    @Override
    public void requestDataCountFormDB() {
        handleCount.getCountProductCart(this);
    }
}
