package com.duykhanh.storeapp.view.homepage.viewproductpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.viewproduct.ViewProductMoreAdapter;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.SlideHome;
import com.duykhanh.storeapp.presenter.home.HomePresenter;
import com.duykhanh.storeapp.presenter.home.ProductListContract;
import com.duykhanh.storeapp.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity implements ProductListContract.View, View.OnClickListener {

    View icl_toolbar_view_product;
    ImageButton imgbtnSizeShop, img_back_view_product;
    TextView txt_size_cart;
    RecyclerView rcl_view_product;
    ProgressBar pb_load_product;

    ViewProductMoreAdapter adapter;

    List<Product> productList;

    LinearLayoutManager linearLayoutManager;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private ProductListContract.Presenter presenter;
    private int pageView = 0;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        initUI();
        registerListener();
        initRecyclerView();
        setListener();

    }

    private void initUI() {
        icl_toolbar_view_product = findViewById(R.id.icl_toolbar_view_product);
        img_back_view_product = icl_toolbar_view_product.findViewById(R.id.img_back_view_product);
        imgbtnSizeShop = icl_toolbar_view_product.findViewById(R.id.imgbtnSizeShop);
        txt_size_cart = icl_toolbar_view_product.findViewById(R.id.txtSizeShoppingHome);

        pb_load_product = findViewById(R.id.pb_load_view_product);
        rcl_view_product = findViewById(R.id.rcl_view_product_more);
    }

    private void registerListener() {
        img_back_view_product.setOnClickListener(this);
        imgbtnSizeShop.setOnClickListener(this);
    }

    private void initRecyclerView() {
        presenter = new HomePresenter(this, this);
        productList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcl_view_product.setLayoutManager(linearLayoutManager);
        rcl_view_product.setItemAnimator(new DefaultItemAnimator());
        adapter = new ViewProductMoreAdapter(this, productList);
        rcl_view_product.setAdapter(adapter);

        presenter.requestDataFromServerView();
        presenter.requestDataCountFormDB();
    }

    private void setListener() {
        // Xử lý sự kiện phân trang danh sách lượt xem
        rcl_view_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading) {
                    pageView++;
                    presenter.getMoreDataView(pageView);
                    loading = true;
                }
            }
        });
    }

    @Override
    public void showProgress() {
        pb_load_product.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pb_load_product.setVisibility(View.GONE);
    }

    @Override
    public void showSizeCart() {
        txt_size_cart.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSizeCart() {
        txt_size_cart.setVisibility(View.GONE);
    }


    @Override
    public void sendDataToRecyclerView(List<Product> productArrayList) {
        // not code
    }

    @Override
    public void sendDataToHorizontalView(List<Product> viewProductArrayList) {
        productList.addAll(viewProductArrayList);
        adapter.notifyDataSetChanged();

        pageView++;
    }

    @Override
    public void sendDataToHorizontalBuy(List<Product> buyProductArrayList) {
        // Not code
    }

    @Override
    public void sendDataToSlideShowHome(List<SlideHome> slideHomeList) {
        //Not code
    }

    @Override
    public void sendCountProduct(int countProduct) {
        txt_size_cart.setText("" + countProduct);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_view_product:
                finish();
                break;
            case R.id.imgbtnSizeShop:
                Intent iViewProduct = new Intent(ViewProductActivity.this, MainActivity.class);
                setResult(RESULT_OK, iViewProduct);
                finish();
                break;
        }
    }
}
