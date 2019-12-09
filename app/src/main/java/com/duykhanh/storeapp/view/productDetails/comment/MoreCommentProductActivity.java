package com.duykhanh.storeapp.view.productDetails.comment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.comment.CommentsAdapter;
import com.duykhanh.storeapp.adapter.comment.MoreCommentAdapter;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.presenter.comment.CommentContract;
import com.duykhanh.storeapp.presenter.comment.CommentPresenter;
import com.duykhanh.storeapp.presenter.productdetail.ProductDetailContract;

import java.util.ArrayList;
import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_COMMENT_PRODUCT;
import static com.duykhanh.storeapp.utils.Constants.KEY_READ_ALL_COMMENT;

public class MoreCommentProductActivity extends AppCompatActivity  implements CommentContract.View,View.OnClickListener {

    private static final String TAG = MoreCommentProductActivity.class.getSimpleName();

    View layout_toolbar_comment;
    ImageView img_back_comment;
    TextView txtCountProduct;
    RecyclerView rcl_comment_product_more;
    ProgressBar pb_load_comment_product;

    String id_product;

    List<Comment> comments;
    MoreCommentAdapter commentsAdapter;
    LinearLayoutManager manager;

    CommentContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_comment_product);

        initUI();
        initCommon();
        registerListener();
    }

    private void registerListener() {
        img_back_comment.setOnClickListener(this);
    }

    private void initCommon() {
        Intent iProduct = getIntent();

        id_product =  iProduct.getStringExtra(KEY_READ_ALL_COMMENT);

        presenter = new CommentPresenter(this);

        if(id_product != null) {
            presenter.requestDataFormServerComment(id_product);
        }

        comments = new ArrayList<>();
        commentsAdapter = new MoreCommentAdapter(MoreCommentProductActivity.this,comments,R.layout.item_comment_more);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcl_comment_product_more.setLayoutManager(manager);
        rcl_comment_product_more.setItemAnimator(new DefaultItemAnimator());
        rcl_comment_product_more.setAdapter(commentsAdapter);

    }

    //TODO Ánh xạ giao diện
    private void initUI() {
        layout_toolbar_comment = findViewById(R.id.layout_toolbar_comment);

        img_back_comment = layout_toolbar_comment.findViewById(R.id.img_back_comment);
        txtCountProduct = layout_toolbar_comment.findViewById(R.id.txtCountProduct);

        rcl_comment_product_more = findViewById(R.id.rcl_comment_product_more);
        pb_load_comment_product = findViewById(R.id.pb_load_comment_product);
    }

    @Override
    public void sendDataRecyclerViewComment(List<Comment> commentList) {
        comments.addAll(commentList);
        pb_load_comment_product.setVisibility(View.GONE);
        Log.d("GGGGG", "sendDataRecyclerViewComment: "+commentList.get(0).getIdp());
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinished() {
        // Not code
    }

    @Override
    public void onFailed() {
        // Not code
    }

    @Override
    public void onFailure(Throwable t) {
        // Not code
    }

    @Override
    public void onFailedComment() {
        pb_load_comment_product.setVisibility(View.GONE);
        Toast.makeText(this, "Get comment faild", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureComment(Throwable t) {
        pb_load_comment_product.setVisibility(View.GONE);
        Log.e(TAG, "onFailureComment: ",t );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back_comment:
                finish();
                break;
        }
    }
}
