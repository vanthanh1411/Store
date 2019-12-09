package com.duykhanh.storeapp.view.order.payment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.BuyingProductAdapter;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.presenter.order.PaymentContract;
import com.duykhanh.storeapp.presenter.order.PaymentPresenter;
import com.duykhanh.storeapp.utils.Formater;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, PaymentContract.View {
    final String TAG = this.getClass().toString();
    int total;
    List<CartItem> cartItems;
    User user;
    float pay;

    RecyclerView rvBuyingProducts;
    TextView tvName, tvAddress, tvPhone, tvTotalPay;
    EditText etNewName, etNewAddress, etNewPhone;
    ProgressBar pbLoading;
    Button btnChangeAddress,
            btnPay;
    AlertDialog alertDialog;

    PaymentPresenter presenter;
    LinearLayoutManager layoutManager;
    BuyingProductAdapter adapter;

    Formater formater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        initComponent();
        settingRecyclerView();
        total = 0;
        //Lấy thông tin người dùng
        presenter.requestCurrentUser();
        //Lấy thông tin giỏ hàng
        presenter.requestCartItemsFromSql();

        btnChangeAddress.setOnClickListener(this);
        btnPay.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override //Hoàn thành lấy trạng thái đăng nhập
    public void requestCurrentUserComplete(User userr) {
        user = userr;
        //Đưa tên, địa chỉ, SĐT vào view
        if (user.getName().trim().equals("")) { //Tên
            tvName.setText(Html.fromHtml("<b>Tên người nhận:</b> (Chưa có)"));
        } else {
            tvName.setText(Html.fromHtml("<b>Tên người nhận: </b> " + user.getName()));
        }
        //
        if (user.getAddress().trim().equals("")) { //Địa chỉ
            tvAddress.setText(Html.fromHtml("<b>Địa chỉ: </b>(Chưa có)"));
        } else {
            tvAddress.setText(Html.fromHtml("<b>Địa chỉ: </b> " + user.getAddress()));
        }
        //
        if (user.getPhone().trim().equals("")) { //SĐT
            tvPhone.setText(Html.fromHtml("<b>SĐT: </b>(Chưa có)"));
        } else {
            tvPhone.setText(Html.fromHtml("<b>SĐT: </b>" + user.getPhone()));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override //Hoàn thành yêu cầu lấy thông tin giỏ hàng
    public void requestCartItemsComplete(List<CartItem> cartItemss) {
        //Danh sách hàng sẽ mua
        cartItems.clear();
        cartItems.addAll(cartItemss);
        adapter.notifyDataSetChanged();
        //Tính toán chi phí
        for (CartItem cartItem : cartItems) {
            total += (cartItem.getQuantity() * cartItem.getPrice());
        }
        pay = total;
        Log.d(TAG, "requestCartItemsComplete: pay" + pay);
        //Hiển thị
        tvTotalPay.setText("Thành tiền: " + formater.formatMoney((int) pay) + " vnđ");
    }

    @Override
    public void onPayed() {
        Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void requestPayedFailure(Throwable throwable) {
        Log.e(TAG, "requestPayedFailure: ", throwable);
        Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPay: //Nút mua hàng
                if (user.getAddress() != null && user.getPhone() != null && user.getName() != null) {
                    if (user.getName().trim().equals("") || user.getAddress().trim().equals("") || user.getPhone().trim().equals("")) {
                        Toast.makeText(this, "Vui lòng kiểm tra lại Tên người nhận\nĐịa Chỉ hoặc Số Điện thoại!", Toast.LENGTH_SHORT).show();
                    } else {
                        Order order = new Order(Calendar.getInstance().getTime(), "StringUID", user.getAddress(), user.getPhone(), pay);
                        //Yêu cầu tạo danh sách Order Detail
                        presenter.requestOrderDetailsFromSql(order);
                    }
                } else {
                    Toast.makeText(this, "Đang tải thông tin người dùng\nVui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnChangePhoneAndAddress: //Nút hiện form thay đổi phone và số điện thoại
                if (user.getAddress() != null && user.getPhone() != null && user.getName() != null) {
                    showDialogChangeInfo();

                } else {
                    Toast.makeText(this, "Đang tải thông tin người dùng\nVui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showDialogChangeInfo() { // Custom Dialog thay đổi thông tin
        //Tạo, khai báo Layout và View cho Dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_form_changeaddressandphone, null);
        etNewName = dialog.findViewById(R.id.etNewName);
        etNewAddress = dialog.findViewById(R.id.etNewAddress);
        etNewPhone = dialog.findViewById(R.id.etNewPhone);
        etNewName.setText(user.getName());
        etNewAddress.setText(user.getAddress());
        etNewPhone.setText(user.getPhone());
        //Tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialog)
                .setPositiveButton("Thay đổi", null)
                .setNegativeButton("Hủy bỏ", null);
        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etNewName.getText().toString();
                String address = etNewAddress.getText().toString();
                String phone = etNewPhone.getText().toString();
                if (address.equals("") || phone.equals("")) {
                    Toast.makeText(PaymentActivity.this, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    user.setName(name);
                    user.setAddress(address);
                    user.setPhone(phone);
                    presenter.requestUpdateUserInfo(user);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void settingRecyclerView() {
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvBuyingProducts.setLayoutManager(layoutManager);
        rvBuyingProducts.setAdapter(adapter);
    }

    private void initComponent() {
        //Formater
        formater = new Formater();
        //User
        user = new User();
        //Presenter
        presenter = new PaymentPresenter(PaymentActivity.this);
        //RecyclerView
        cartItems = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        adapter = new BuyingProductAdapter(R.layout.item_payment, this, cartItems);
    }

    private void initView() {
        tvName = findViewById(R.id.tvPUserName);
        tvAddress = findViewById(R.id.tvPUserAddress);
        tvPhone = findViewById(R.id.tvPUserPhone);
        btnChangeAddress = findViewById(R.id.btnChangePhoneAndAddress);
        rvBuyingProducts = findViewById(R.id.rvBuyingProducts);
        tvTotalPay = findViewById(R.id.tvTotalPay);
        pbLoading = findViewById(R.id.pbPayLoading);
        btnPay = findViewById(R.id.btnPay);

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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
