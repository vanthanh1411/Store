package com.duykhanh.storeapp.model.data.comment;
import android.util.Log;

import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.ResponseComment;
import com.duykhanh.storeapp.network.ApiUtils;
import com.duykhanh.storeapp.network.DataClient;
import com.duykhanh.storeapp.presenter.comment.CommentContract;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Duy Khánh on 12/2/2019.
 */
public class CommentHandle implements CommentContract.Handle {


    @Override
    public void onPostCommentProduct(CommentContract.Handle.OnFinishedListener finishedListener, List<MultipartBody.Part> parts, Map<String, RequestBody> map) {
        DataClient apiService = ApiUtils.getProductList();
        Call<ResponseComment> call = apiService.postComment(parts,map);
        call.enqueue(new Callback<ResponseComment>() {
            @Override
            public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {
                if (response.isSuccessful()) {
                    ResponseComment comment = response.body();
                    finishedListener.onFinished();
                }
                else{
                    finishedListener.onFailed();
                }
            }

            // Lỗi khi đang giao tiếp với server
            @Override
            public void onFailure(Call<ResponseComment> call, Throwable t) {
                finishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void onGetCommentProductById(OnFinishedListenerCommentMore finishedListenerCommentMore, String id_product) {
        DataClient apiService = ApiUtils.getProductList();

        Call<List<Comment>> call = apiService.getCommentByIdp(id_product);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {

                } else {
                    finishedListenerCommentMore.onFailedComment();
                }
                List<Comment> comments = response.body();
                Log.d("AAAAAA", "onResponse: " + comments.size());
                finishedListenerCommentMore.onFinishedComment(comments);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                finishedListenerCommentMore.onFailureComment(t);
                Log.d("DEBUG", "onFailure: " + t);
            }
        });
    }


}
