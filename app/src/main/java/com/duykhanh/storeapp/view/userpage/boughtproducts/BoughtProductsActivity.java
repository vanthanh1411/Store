package com.duykhanh.storeapp.view.userpage.boughtproducts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.OrderDetail;

import java.util.List;

public class BoughtProductsActivity extends AppCompatActivity implements BoughtProductsContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_products);
    }

    @Override
    public void requestBoughtProductsSuccess(List<OrderDetail> orderDetails) {
        
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
