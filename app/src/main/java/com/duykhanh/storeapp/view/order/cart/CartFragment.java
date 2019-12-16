package com.duykhanh.storeapp.view.order.cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.cart.CartAdapter;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.presenter.order.CartContract;
import com.duykhanh.storeapp.presenter.order.CartPresenter;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.MainActivity;
import com.duykhanh.storeapp.view.order.payment.PaymentActivity;
import com.duykhanh.storeapp.view.userpage.account.AccountActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment implements CartContract.View, OnCartItemClickListener, View.OnClickListener {
    final String TAG = this.getClass().toString();
    int total;
    List<CartItem> cartItems;
    int orderNo = 0;

    CartAdapter cartAdapter;
    LinearLayoutManager layoutManager;

    Toolbar tbCart;
    View view;
    LinearLayout llctnCartProductRequire;
    RecyclerView rvCart;
    ProgressBar pbLoading;
    TextView tvTotal;
    Button btnPay;
    Button btnToProducts;

    CartPresenter presenter;

    Formater formater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView();
        initComponent();
        settingCartRecyclerView();

        //Xác định chuyển sang Cart trong OrderActivity
        Bundle args = getArguments();
        if (args != null){
            orderNo = args.getInt("CartInOrderActivity", 0);
        }

        btnPay.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        presenter.requestCartItems();
        //Hiện nút Back
        Log.d(TAG, "onResume: cartNo" + orderNo);
        if (orderNo != 1) {
            tbCart.setVisibility(View.VISIBLE);
            settingToolbar();
        } else {
            tbCart.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCartItemsToCartRv(List<CartItem> cartItemss) {
        Log.d(TAG, "setCartItemsToCartRv: ");
        cartItems.clear();
        total = 0;
        cartItems.addAll(cartItemss);
        for (CartItem cartItem : cartItems) {
        }
        cartAdapter.notifyDataSetChanged();
        for (CartItem cartItem : cartItems) {
            total += (cartItem.getQuantity() * cartItem.getPrice());
        }
        tvTotal.setText(formater.formatMoney(total) + " đ");
        if (total == 0) {
            llctnCartProductRequire.setVisibility(View.VISIBLE);
            btnToProducts.setOnClickListener(this);
            btnPay.setEnabled(false);
            btnPay.setBackgroundColor(getContext().getColor(R.color.colorGrey));
        }
    }

    @Override
    public void onCartItemClick(int position) {
        CartItem cartItem = cartItems.get(position);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        CartItem cartItem = cartItems.get(position);
        presenter.requestDeleteCartItem(cartItem.getProductid());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onIncreaseButtonClick(int position) {
        CartItem cartItem = cartItems.get(position);
        presenter.requestIncreaseQuantity(cartItem.getProductid());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDecreaseButtonClick(int position) {
        CartItem cartItem = cartItems.get(position);
        presenter.requestDecreaseQuantity(cartItem.getProductid());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartItemsResponseFailure(Throwable throwable) {
        Log.e(TAG, "onCartItemsResponseFailure: ", throwable);
        Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnToPay:
                presenter.requestCurrentUser(); //Kiểm tra trạng thái đăng nhập
                break;
            case R.id.btnToProducts:
                Log.d(TAG, "onClick: " + getContext().toString());
                Intent i = new Intent(getContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
    }

    @Override //Kiểm tra hoàn tất
    public void requestCurrentUserComplete(String userId) {
        if (userId.equals("")) { //Chưa đăng nhập
            showDialog();// Hiển thị dialog yêu cầu đăng nhập
        } else { //Đã đăng nhập
            startActivity(new Intent(getContext(), PaymentActivity.class));
        }
    }

    private void showDialog() {
        Log.d(TAG, "showDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn hiện chưa đăng nhập!\nNhấn đồng ý chuyển đến màn hình đăng nhập.")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getContext(), AccountActivity.class));
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    private void settingCartRecyclerView() {
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvCart.setLayoutManager(layoutManager);
        rvCart.setAdapter(cartAdapter);
    }

    private void initComponent() {
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItems, R.layout.item_cartitem);
        presenter = new CartPresenter(this);
        layoutManager = new LinearLayoutManager(getContext());
        formater = new Formater();
    }

    private void initView() {
        tbCart = view.findViewById(R.id.tbCart);
        llctnCartProductRequire = view.findViewById(R.id.llctnCartProductRequire);
        rvCart = view.findViewById(R.id.rcl_cart);
        pbLoading = view.findViewById(R.id.pbCartsLoading);
        tvTotal = view.findViewById(R.id.txt_sumMoneyCart);
        btnPay = view.findViewById(R.id.btnToPay);
        btnToProducts = view.findViewById(R.id.btnToProducts);
    }

    private void settingToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbCart);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }


    @Override
    public void showProgress() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
