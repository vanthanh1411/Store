package com.duykhanh.storeapp.presenter.search;

import android.content.Context;

import com.duykhanh.storeapp.model.Product;

import java.util.List;

public interface SearchContract {

    interface Handle {

        void getSuggestWords(OnGetSuggestWordsListener listener); //Lấy danh sách gợi ý

        void getProductByKey(OnGetProductByKeyListener listener, String searchingKey, int pageNo);//Lấy product từ Id


        interface OnGetProductByKeyListener {
            void onGetProductByKeyFinished(List<Product> products);

            void onGetProductByKeyFailure(Throwable throwable);
        }

        interface OnGetSuggestWordsListener {
            void onGetSuggestWordsFinished(List<String> suggestWords); //Lấy danh sách gợi ý thành công

            void onGetSuggestWordsFailure(Throwable throwable);//Lấy danh sách gợi ý thất bại
        }
    }

    interface View {

        void requestSearchComplete(List<Product> products);

        void requestSearchFailure(Throwable throwable);

        Context getContext();

        void showSuggestWords(List<String> suggestWords);

        void hideSuggestWords();

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void requestSuggestWords();

        void requestSearch(String searchingKey);

        void requestMoreData(String searchingKey, int pageNo);

        void onDestroy();
    }

}
