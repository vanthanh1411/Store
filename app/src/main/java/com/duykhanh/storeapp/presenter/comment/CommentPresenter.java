package com.duykhanh.storeapp.presenter.comment;

import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.data.comment.CommentHandle;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Duy Khánh on 12/2/2019.
 */
public class CommentPresenter implements CommentContract.Presenter,
        CommentContract.Handle.OnFinishedListener,
        CommentContract.Handle.OnFinishedListenerCommentMore{

    CommentContract.View view;
    CommentContract.Handle handle;

    public CommentPresenter(CommentContract.View view) {
        this.view = view;
        handle = new CommentHandle();
    }

    @Override
    public void onFinished() {
        view.onFinished();
    }

    @Override
    public void onFinishedComment(List<Comment> commentList) {
        view.sendDataRecyclerViewComment(commentList);
    }

    @Override
    public void onFailed() {
        view.onFailed();
    }

    @Override
    public void onFailure(Throwable t) {
        view.onFailure(t);
    }


    @Override
    public void requestDataFormServer(List<MultipartBody.Part> parts, Map<String, RequestBody> map) {
        handle.onPostCommentProduct(this,parts,map);
    }

    @Override
    public void requestDataFormServerComment(String idProduct) {
        handle.onGetCommentProductById(this,idProduct);
    }

    @Override
    public void onFailedComment() {
        view.onFailedComment();
    }

    @Override
    public void onFailureComment(Throwable t) {
        view.onFailureComment(t);
    }
}
