package com.duykhanh.storeapp.view.userpage;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.presenter.user.UserContract;
import com.duykhanh.storeapp.presenter.user.UserPresenter;
import com.duykhanh.storeapp.view.userpage.account.AccountActivity;
import com.duykhanh.storeapp.view.userpage.orders.OrdersActivity;
import com.duykhanh.storeapp.view.userpage.userinfo.UserInfoActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements UserContract.View, View.OnClickListener {
    final String TAG = this.getClass().toString();
    boolean isLoggedIn = false;

    CardView cvUserCard;
    RelativeLayout rlLoadingUser;
    LinearLayout llLoginRequire;
    View view;
    Button btnToLogIn, btnLogOut;
    TextView tvUserName, tvUserEmail,
            tvOrderStatus0, tvOrderStatus1, tvOrderStatus2, tvOrderStatus3, tvOrderStatus4, tvHotLine;
    ImageView ivUserImage;

    UserPresenter presenter;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        initComponent(view);
        settingOrderStatus();//Thêm icon trước TextView

        llLoginRequire.setOnClickListener(this);
        cvUserCard.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        tvOrderStatus0.setOnClickListener(this);
        tvOrderStatus1.setOnClickListener(this);
        tvOrderStatus2.setOnClickListener(this);
        tvOrderStatus3.setOnClickListener(this);
        tvOrderStatus4.setOnClickListener(this);
        tvHotLine.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.requestGetCurrentUser();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void requestCurrentUserSuccess(User user, Bitmap bmImage) {
        //Email
        tvUserEmail.setText(user.getEmail());
        //Name
        if (user.getName().equals("")) {
            tvUserName.setText("(Unknown)");
        } else {
            tvUserName.setText(user.getName());
        }
        //Image
        if (user.getPhoto().equals("")) {
            ivUserImage.setImageResource(R.drawable.unknown);
        } else {
            ivUserImage.setImageBitmap(bmImage);
        }
    }

    @Override
    public void requestLogOutSuccess() {
        isLoggedIn = false;
        presenter.requestGetCurrentUser();
    }

    @Override
    public void requestUserFailure(Throwable throwable) {
        Log.e(TAG, "requestUserFailure: ", throwable);
    }

    @Override
    public void requestLogOutFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Đăng xuất thất bại!", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "requestLogOutFailure: ", throwable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLoginRequire:
                startActivity(new Intent(getContext(), AccountActivity.class));
                break;
            case R.id.btnLogOut:
                presenter.requestLogOut();
                break;
            case R.id.rlctnUserCart:
                startActivity(new Intent(getContext(), UserInfoActivity.class));
                break;
            case R.id.tvOrderStatus0:
                if (!isLoggedIn) {
                    Toast.makeText(getContext(), "Yêu cầu đăng nhập!!!", Toast.LENGTH_SHORT).show();
                } else
                    goOrder(0);
                break;
            case R.id.tvOrderStatus1:
                if (!isLoggedIn) {
                    Toast.makeText(getContext(), "Yêu cầu đăng nhập!!!", Toast.LENGTH_SHORT).show();
                } else
                    goOrder(1);
                break;
            case R.id.tvOrderStatus2:
                if (!isLoggedIn) {
                    Toast.makeText(getContext(), "Yêu cầu đăng nhập!!!", Toast.LENGTH_SHORT).show();
                } else
                    goOrder(2);
                break;
            case R.id.tvOrderStatus3:
                if (!isLoggedIn) {
                    Toast.makeText(getContext(), "Yêu cầu đăng nhập!!!", Toast.LENGTH_SHORT).show();
                } else
                    goOrder(3);
                break;
            case R.id.tvOrderStatus4:
                if (!isLoggedIn) {
                    Toast.makeText(getContext(), "Yêu cầu đăng nhập!!!", Toast.LENGTH_SHORT).show();
                } else
                    goOrder(4);
                break;
            case R.id.tvHotLine:
                String phone = tvHotLine.getText().toString().substring(10);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;
        }
    }

    private void goOrder(int orderStatus) {
        Intent intent = new Intent(getContext(), OrdersActivity.class);
        intent.putExtra("orderStatus", orderStatus);
        startActivity(intent);
    }

    private void settingOrderStatus() {
        float density = getResources().getDisplayMetrics().density;
        int drawableWidth = Math.round(30 * density);
        int drawableHeight = Math.round(30 * density);

        Drawable orderRight = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_keyboard_arrow_right_black_24dp);
        orderRight.setBounds(0, 0, drawableWidth, drawableHeight);

        Drawable orderDrawable1 = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_orderstep1);
        orderDrawable1.setBounds(0, 0, drawableWidth, drawableHeight);
        tvOrderStatus0.setCompoundDrawables(orderDrawable1, null, orderRight, null);
        tvOrderStatus0.setCompoundDrawablePadding(25);

        Drawable orderDrawable2 = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_orderstep2);
        orderDrawable2.setBounds(0, 0, drawableWidth, drawableHeight);
        tvOrderStatus1.setCompoundDrawables(orderDrawable2, null, orderRight, null);
        tvOrderStatus1.setCompoundDrawablePadding(25);

        Drawable orderDrawable3 = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_orderstep3);
        orderDrawable3.setBounds(0, 0, drawableWidth, drawableHeight);
        tvOrderStatus2.setCompoundDrawables(orderDrawable3, null, orderRight, null);
        tvOrderStatus2.setCompoundDrawablePadding(25);

        Drawable orderDrawable4 = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_orderstep4);
        orderDrawable4.setBounds(0, 0, drawableWidth, drawableHeight);
        tvOrderStatus3.setCompoundDrawables(orderDrawable4, null, orderRight, null);
        tvOrderStatus3.setCompoundDrawablePadding(25);

        Drawable orderDrawable5 = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_orderstep5);
        orderDrawable5.setBounds(0, 0, drawableWidth, drawableHeight);
        tvOrderStatus4.setCompoundDrawables(orderDrawable5, null, orderRight, null);
        tvOrderStatus4.setCompoundDrawablePadding(25);

    }

    private void initComponent(View view) {
        presenter = new UserPresenter(this);
    }

    private void initView() {
        rlLoadingUser = view.findViewById(R.id.rlLoadingUser);
        llLoginRequire = view.findViewById(R.id.llLoginRequire);
        cvUserCard = view.findViewById(R.id.rlctnUserCart);
        btnToLogIn = view.findViewById(R.id.btnToLogIn);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        ivUserImage = view.findViewById(R.id.ivUserImage);
        tvOrderStatus0 = view.findViewById(R.id.tvOrderStatus0);
        tvOrderStatus1 = view.findViewById(R.id.tvOrderStatus1);
        tvOrderStatus2 = view.findViewById(R.id.tvOrderStatus2);
        tvOrderStatus3 = view.findViewById(R.id.tvOrderStatus3);
        tvOrderStatus4 = view.findViewById(R.id.tvOrderStatus4);
        tvHotLine = view.findViewById(R.id.tvHotLine);
    }

    @Override
    public void showLoadingUser() {
        rlLoadingUser.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingUser() {
        rlLoadingUser.setVisibility(View.GONE);
    }

    @Override
    public void showLoginRequire() {
        llLoginRequire.setVisibility(View.VISIBLE);
        btnLogOut.setVisibility(View.GONE);
    }

    @Override
    public void hideLoginRequire() {
        isLoggedIn = true;
        llLoginRequire.setVisibility(View.GONE);
        btnLogOut.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}