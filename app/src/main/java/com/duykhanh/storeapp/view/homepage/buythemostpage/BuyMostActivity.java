package com.duykhanh.storeapp.view.homepage.buythemostpage;

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
import com.duykhanh.storeapp.adapter.buyviewproduct.BuyProductMoreAdapter;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.SlideHome;
import com.duykhanh.storeapp.presenter.home.HomePresenter;
import com.duykhanh.storeapp.presenter.home.ProductListContract;
import com.duykhanh.storeapp.view.order.OrderActivity;

import java.util.ArrayList;
import java.util.List;

public class BuyMostActivity extends AppCompatActivity implements View.OnClickListener, ProductListContract.View {

    View icl_toolbar_buy_product;
    ImageButton imgbtnSizeShop, img_back_buy_product;
    TextView txt_size_cart, txtTitleViewProduct;
    RecyclerView rcl_buy_product;
    ProgressBar pb_load_product;

    List<Product> productList;

    LinearLayoutManager linearLayoutManager;

    BuyProductMoreAdapter adapter;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private ProductListContract.Presenter presenter;
    private int pageBuy = 0;
    private int previousTotal = 0; // Tổng số item khi yêu cầu dữ liệu trên server
    private boolean loading = true; // Trạng thái load dữ liệu
    private int visibleThreshold = 4;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_most);

        initUI();
        registerListener();
        initRecyclerView();
        setListener();
    }

    private void registerListener() {
        img_back_buy_product.setOnClickListener(this);
        imgbtnSizeShop.setOnClickListener(this);
    }

    private void initRecyclerView() {
        txtTitleViewProduct.setText("Lượt mua nhiều nhất");

        presenter = new HomePresenter(this, this);
        productList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcl_buy_product.setLayoutManager(linearLayoutManager);
        rcl_buy_product.setItemAnimator(new DefaultItemAnimator());
        adapter = new BuyProductMoreAdapter(this, productList);
        rcl_buy_product.setAdapter(adapter);

        presenter.requestDatatFromServerBuy();
        presenter.requestDataCountFormDB();
    }

    //TODO: Khởi tạo giao diện cần thiết

    private void initUI() {
        icl_toolbar_buy_product = findViewById(R.id.icl_toolbar_buy_product);
        rcl_buy_product = findViewById(R.id.rcl_buy_product_all);
        pb_load_product = findViewById(R.id.pb_load_buy_product);

        img_back_buy_product = icl_toolbar_buy_product.findViewById(R.id.img_back_view_product);
        imgbtnSizeShop = icl_toolbar_buy_product.findViewById(R.id.imgbtnSizeShop);
        txt_size_cart = icl_toolbar_buy_product.findViewById(R.id.txtSizeShoppingHome);
        txtTitleViewProduct = icl_toolbar_buy_product.findViewById(R.id.txtTitleViewProduct);
    }

    private void setListener() {
        rcl_buy_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();// Số lượng item đang hiển thị trên màn hình
                totalItemCount = linearLayoutManager.getItemCount();// Tổng item đang có trên view
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();// Vị trí item hiển thị đầu tiên kho scroll view

                if (loading) {
                    // Nếu tổng item lớn hơn tổng số item trước đó thì gán nó cho biến previousTotal
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    presenter.getMoreDataView(pageBuy);
                    loading = true;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_view_product:
                finish();
                break;
            case R.id.imgbtnSizeShop:
                Intent iBuyProduct = new Intent(BuyMostActivity.this, OrderActivity.class);
                startActivity(iBuyProduct);
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showSizeCart() {

    }

    @Override
    public void hideSizeCart() {

    }

    @Override
    public void sendDataToRecyclerView(List<Product> productArrayList) {

    }

    @Override
    public void sendDataToHorizontalView(List<Product> viewProductArrayList) {

    }

    @Override
    public void sendDataToHorizontalBuy(List<Product> buyProductArrayList) {
        productList.addAll(buyProductArrayList);
        adapter.notifyDataSetChanged();

        pageBuy++;
    }

    @Override
    public void sendDataToSlideShowHome(List<SlideHome> slideHomeList) {
        //Not code
    }

    @Override
    public void sendCountProduct(int countProduct) {
        if (countProduct != 0) {
            txt_size_cart.setVisibility(View.VISIBLE);
        } else {
            txt_size_cart.setVisibility(View.GONE);
        }
        txt_size_cart.setText("" + countProduct);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }
}
