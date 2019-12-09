package com.duykhanh.storeapp.view.order;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.view.order.cart.CartFragment;

public class OrderActivity extends AppCompatActivity {
    final String TAG = this.getClass().toString();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initComponent();
        addPaymentFragment();
    }

    private void addPaymentFragment() {
        //Thêm Payment Fragment vào Activity
        fragmentTransaction.add(R.id.flOrderContainer, cartFragment);
        fragmentTransaction.commit();
    }

    private void initComponent() {
        //Payment Fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        cartFragment = new CartFragment();
    }
}
