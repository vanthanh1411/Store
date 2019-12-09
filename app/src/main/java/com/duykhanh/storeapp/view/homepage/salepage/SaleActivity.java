package com.duykhanh.storeapp.view.homepage.salepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.sale.SaleAdapter;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.SlideSale;
import com.duykhanh.storeapp.presenter.sale.SaleContract;
import com.duykhanh.storeapp.presenter.sale.SalePresenter;
import com.duykhanh.storeapp.view.order.OrderActivity;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_TEM_POSITION_SALE;

public class SaleActivity extends AppCompatActivity implements View.OnClickListener
        , SaleContract.View, SaleItemClickListener {

    View icl_toolbar_sale;
    ImageButton img_back_sale;
    TextView txtTitleSaleProduct,
            txtCountProduct;
    ImageButton btnShop;
    RecyclerView rcl_sale_product_more;
    ProgressBar pb_load_sale_product;

    String idSale;
    String titleSale;
    SaleContract.Presenter presenter;

    List<Product> products;

    SaleAdapter adapter;

    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        initUI();
        initData();
        initCommont();
        registerListener();
    }

    private void initData() {
        Intent intent = getIntent();
        idSale = intent.getStringExtra("SALE");
        titleSale = intent.getStringExtra("NAME_SALE");
    }

    private void registerListener() {
        img_back_sale.setOnClickListener(this);
        btnShop.setOnClickListener(this);
    }

    private void initCommont() {
        txtTitleSaleProduct.setText("" + titleSale);
        presenter = new SalePresenter(this, this);
        presenter.requestFormServer(idSale);
        presenter.requestDataCountFormDB();

        products = new ArrayList<>();
        adapter = new SaleAdapter(this, products);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcl_sale_product_more.setLayoutManager(layoutManager);
        rcl_sale_product_more.setItemAnimator(new DefaultItemAnimator());
        rcl_sale_product_more.setAdapter(adapter);
    }

    private void initUI() {
        icl_toolbar_sale = findViewById(R.id.icl_toolbar_sale);
        img_back_sale = icl_toolbar_sale.findViewById(R.id.img_back_sale);
        txtTitleSaleProduct = icl_toolbar_sale.findViewById(R.id.txtTitleSaleProduct);
        btnShop = icl_toolbar_sale.findViewById(R.id.btnShop);
        txtCountProduct = icl_toolbar_sale.findViewById(R.id.txtCountProduct);
        rcl_sale_product_more = findViewById(R.id.rcl_sale_product_more);
        pb_load_sale_product = findViewById(R.id.pb_load_sale_product);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_sale:
                finish();
                break;
            case R.id.btnShop:
                Intent iOders = new Intent(SaleActivity.this, OrderActivity.class);
                startActivity(iOders);
                break;
        }
    }

    @Override
    public void sendDataSlale(List<SlideSale> slideSales) {
        for (int i = 0; i < slideSales.size(); i++) {
            Product product = slideSales.get(i).getProductObjects();
            double saleOff = slideSales.get(i).getProducts().getPromotion();
            product.setPrice((int) (product.getPrice() - product.getPrice() * saleOff));
            products.add(product);
        }
        adapter.notifyDataSetChanged();
        pb_load_sale_product.setVisibility(View.GONE);
    }

    @Override
    public void sendCountProduct(int countProduct) {
        txtCountProduct.setText("" + countProduct);
    }

    @Override
    public void showSizeCart() {
        txtCountProduct.setVisibility(View.VISIBLE);
    }

    @Override
    public void hiddenSizeCart() {
        txtCountProduct.setVisibility(View.GONE);
    }

    @Override
    public void onItemClickSale(int position) {
        Intent detailIntent = new Intent(this, ProductDetailActivity.class);
        detailIntent.putExtra(KEY_TEM_POSITION_SALE, products.get(position).getId());
        startActivity(detailIntent);
    }
}
