package com.duykhanh.storeapp.view.userpage.orderdetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.OrderDetailsAdapter;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.duykhanh.storeapp.utils.Constants.KEY_RELEASE_TO;

public class OrderDetailsActivity extends AppCompatActivity implements OrderDetailsContract.View, Serializable, OrderDetailsItemClickListener {
    final String TAG = this.getClass().toString();
    Order order;

    List<OrderDetail> orderDetails;

    Toolbar toolbar;
    TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderAddress, tvTotal;
    RecyclerView rvOrderDetails;

    LinearLayoutManager layoutManager;
    OrderDetailsAdapter adapter;
    OrderDetailsPresenter presenter;
    Formater formater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        //Lấy Order Id từ Intent
        Intent intent = getIntent();
        order = intent.getParcelableExtra("Order");
        if (order == null) {
            Toast.makeText(this, "Oops, có lỗi xảy ra", Toast.LENGTH_LONG).show();
            return;
        }
        //
        initView();
        initComponent();
        settingToolbar();
        settingRecyclerView();
        bindOrderToView();
        presenter.requestOrderDetails(order.getIdo());

    }

    @Override
    public void requestOrderDetailSuccess(List<OrderDetail> orderDetailss) {
        orderDetails.clear();
        orderDetails.addAll(orderDetailss);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void bindOrderToView() {
        Log.d(TAG, "bindOrderToView: " + order.toString());
        tvOrderId.setText(Html.fromHtml("<b>Mã đơn hàng:</b> " + order.getIdo()));
        tvOrderDate.setText(Html.fromHtml("<b>Ngày đặt hàng:</b> " + Formater.formatDate(order.getDate())));
        tvOrderStatus.setText(Html.fromHtml("<b>Trạng thái:</b> " + Formater.statusIntToString(order.getStatus())));
        tvOrderAddress.setText(order.getAddress());
        tvTotal.setText(Formater.formatMoney((int) order.getTotal()));
    }

    @Override
    public void onItemClickListener(int position) {
        Intent detailIntent = new Intent(this, ProductDetailActivity.class);
        detailIntent.putExtra(KEY_RELEASE_TO, orderDetails.get(position).getProductId());
        startActivity(detailIntent);
    }

    private void settingRecyclerView() {
        rvOrderDetails.setLayoutManager(layoutManager);
        rvOrderDetails.setAdapter(adapter);
    }

    private void settingToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initComponent() {
        formater = new Formater();
        presenter = new OrderDetailsPresenter(this);
        orderDetails = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        adapter = new OrderDetailsAdapter(this, R.layout.item_orderdetails, orderDetails);
    }

    private void initView() {
        toolbar = findViewById(R.id.tbOrderDetails);
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        tvOrderId = findViewById(R.id.tvOrderDetailOId);
        tvOrderDate = findViewById(R.id.tvOrderDetailODate);
        tvOrderStatus = findViewById(R.id.tvOrderDetailOStatus);
        tvOrderAddress = findViewById(R.id.tvOrderDetailOAddress);
        tvTotal = findViewById(R.id.tvOrderDetailOTotal);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
