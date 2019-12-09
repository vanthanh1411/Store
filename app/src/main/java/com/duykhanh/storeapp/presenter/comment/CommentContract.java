package com.duykhanh.storeapp.presenter.comment;

import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Product;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Duy Khánh on 12/2/2019.
 */
public interface CommentContract {

    interface Handle {
        interface OnFinishedListener {
            void onFinished();

            void onFailed();

            void onFailure(Throwable t);
        }

        interface OnFinishedListenerCommentMore{
            void onFinishedComment(List<Comment> commentList);

            void onFailedComment();

            void onFailureComment(Throwable t);
        }

        void onPostCommentProduct(OnFinishedListener finishedListener, List<MultipartBody.Part> parts, Map<String, RequestBody> map);

        void onGetCommentProductById(OnFinishedListenerCommentMore finishedListenerCommentMore, String id_product);
    }

    interface Presenter{
        void requestDataFormServer(List<MultipartBody.Part> parts,Map<String, RequestBody> map);

        void requestDataFormServerComment(String idProduct);
    }

    interface View{

        void sendDataRecyclerViewComment(List<Comment> commentList);

        void onFinished();

        void onFailed();

        void onFailure(Throwable t);

        void onFailedComment();

        void onFailureComment (Throwable t);
    }
}
