package com.duykhanh.storeapp.presenter.search;

import android.util.Log;

import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.data.search.SearchHandle;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter,
        SearchContract.Handle.OnGetProductByKeyListener,
        SearchContract.Handle.OnGetSuggestWordsListener {
    final String TAG = this.getClass().toString();

    SearchContract.Handle iHandle;
    SearchContract.View iView;

    public SearchPresenter(SearchContract.View iView) {
        this.iView = iView;
        iHandle = new SearchHandle(iView);
    }

    @Override//Yêu cầu lấy list gợi ý
    public void requestSuggestWords() {
        if (iView != null) {
            iHandle.getSuggestWords(this);
        }
    }

    @Override//Lấy danh sách gợi ý thành công
    public void onGetSuggestWordsFinished(List<String> suggestWords) {
        if (iView != null){
            iView.showSuggestWords(suggestWords);//Hiển thị danh sách gợi ý
        }
    }

    @Override//Yêu cầu tìm kiếm
    public void requestSearch(String searchingKey) {
        if (iView != null) {
            iView.hideSuggestWords();
            iView.showProgress();
        }
        iHandle.getProductByKey(this, searchingKey, 1);
    }

    @Override//Lấy danh sách sản phẩm theo từ khóa
    public void onGetProductByKeyFinished(List<Product> products) {
        if (iView != null) {
            iView.hideProgress();
            iView.requestSearchComplete(products);
        }
    }

    @Override
    public void requestMoreData(String searchingKey, int pageNo) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.getProductByKey(this, searchingKey, pageNo);
    }

    @Override
    public void onGetProductByKeyFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
            iView.requestSearchFailure(throwable);
        }
    }

    @Override//Lấy danh sách gợi ý thất bại
    public void onGetSuggestWordsFailure(Throwable throwable) {
        Log.e(TAG, "onGetSuggestWordsFailure: ", throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
