package com.duykhanh.storeapp.view.userpage.orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.OrdersAdapter;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.view.userpage.orderdetails.OrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersActivity extends AppCompatActivity implements OrdersContract.View, OrdersItemClickListener {
    final String TAG = this.getClass().toString();
    String userId = "";
    List<Order> orders;

    Toolbar toolbar;
    RecyclerView rvOrders;

    OrdersAdapter ordersAdapter;
    OrdersPresenter presenter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        initView();
        initComponent();
        settingToolbar();
        settingRecyclerView();
        //Lấy số để hiển thị đúng list Order
        // 0 "Đơn hàng đang chờ đơn vị xử lý"
        // 1 "Đơn hàng đã được tiếp nhận
        // 2 "Đơn hàng đã được bàn giao cho đơn vị vận chuyển"
        // 3 "Đơn hàng đang được vận chuyển"
        // 4 "Đơn hàng đã vận chuyển thành công"
        Intent intent = getIntent();
        int orderStatus = intent.getIntExtra("orderStatus", -1);
        //Lấy thông tin User
        presenter.requestGetUserId();
        Log.d(TAG, "onCreate: " + "-UserId-" + userId + "-OrderStatus-" + orderStatus);
        if (!userId.equals("")) {
            presenter.requestGetOrders(userId, orderStatus);
        }
    }

    @Override //Hoàn thành yêu cầu lấy thông tin người dùng
    public void requestGetUserIdSuccess(String userId) {
        this.userId = userId;
    }

    @Override//Hoàn thành yêu cầu lấy list Order
    public void requestGetOrdersSuccess(List<Order> orderss) {
        orders.clear();
        orders.addAll(orderss);
        ordersAdapter.notifyDataSetChanged();
    }

    @Override//Chuyển màn hình list Order Detail đồng thời gửi Order Id qua luôn
    public void onOrderItemClick(int position) {
        Intent intent = new Intent(OrdersActivity.this, OrderDetailsActivity.class);
        intent.putExtra("Order", orders.get(position));
        startActivity(intent);
    }

    private void settingRecyclerView() {
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(ordersAdapter);
    }

    private void settingToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initComponent() {
        orders = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        ordersAdapter = new OrdersAdapter(this, R.layout.item_orders, orders);
        presenter = new OrdersPresenter(this);
    }

    private void initView() {
        rvOrders = findViewById(R.id.rvOrders);
        toolbar = findViewById(R.id.tbOrders);
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

    }
}
