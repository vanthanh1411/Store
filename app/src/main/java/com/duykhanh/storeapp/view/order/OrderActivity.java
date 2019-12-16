package com.duykhanh.storeapp.view.order;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.view.order.cart.CartFragment;

import java.util.Objects;

public class OrderActivity extends AppCompatActivity {
    final String TAG = this.getClass().toString();

    Toolbar tbOrderActivity;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        tbOrderActivity = findViewById(R.id.tbOrderActivity);
        settingToolbar();


        initComponent();
        addPaymentFragment();
    }

    private void addPaymentFragment() {
        //Thêm Payment Fragment vào Activity
        fragmentTransaction.add(R.id.flOrderContainer, cartFragment,"tag");
        fragmentTransaction.commit();
    }

    private void settingToolbar() {
        setSupportActionBar(tbOrderActivity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initComponent() {
        //Payment Fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        cartFragment = new CartFragment();
        Bundle args = new Bundle();
        args.putInt("CartInOrderActivity", getIntent().getExtras().getInt("CartInOrderActivity"));
        cartFragment.setArguments(args);
    }
}
