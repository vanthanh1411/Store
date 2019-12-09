package com.duykhanh.storeapp.view.userpage.userinfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.User;

import java.util.Objects;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, UserInfoContract.View {
    final String TAG = this.getClass().toString();
    boolean imgIsChanged;
    Uri imageUri;

    User user;

    Toolbar toolbar;
    RelativeLayout rlInfoUser;
    ImageView ivUserImage;
    TextView tvHelloUser;
    EditText etName, etPhone, etAddress;
    Button btnChange;
    ProgressBar pbChangingInfo;
    RelativeLayout rlLoadingUserInfo;

    UserInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initComponent();
        settingToolbar();

        imgIsChanged = false;

        presenter.requestCurrentUser();

        rlInfoUser.setOnClickListener(this);
        btnChange.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override //Lấy thông tin người dùng đang đăng nhập thành công
    public void requestCurrenUserSuccess(User userr, Bitmap infoImageBitmap) {
        Log.d(TAG, "requestCurrenUserSuccess: ");
        user = userr;
        //Set dữ liệu người dùng vào View
        tvHelloUser.setText(Html.fromHtml("Xin chào, " + "<b><i>" + user.getEmail() + "<b><i>"));
        etName.setText(user.getName());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());
        if (infoImageBitmap != null) {
            ivUserImage.setImageBitmap(infoImageBitmap);
        }
    }

    @Override //Thay đổi thông tin người dùng thành công
    public void changeUserInfoSuccess() {
        Toast.makeText(this, "Thay đổi thông tin thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangeInfo: //Button xác nhận thay đổi thông tin người dùng
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();
                if (!(name.trim().length() > 0) || !(phone.trim().length() > 0) || !(address.trim().length() > 0)) {
                    Toast.makeText(this, "Vui lòng không bỏ trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.trim().length() != 10) {
                    Toast.makeText(this, "Số điện thoại phải gồm 10 số!", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.setName(name);
                user.setPhone(phone);
                user.setAddress(address);
                presenter.requestChangeInfo(user, imageUri);
                break;
            case R.id.rlInfoImage: //Chuyển hướng đến màn hình chọn ảnh
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(ivUserImage);
        }
    }

    private void settingToolbar() {
        setSupportActionBar(toolbar);
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
        presenter = new UserInfoPresenter(this);
        user = new User();
    }

    private void initView() {
        toolbar = findViewById(R.id.tbUserInfo);
        rlInfoUser = findViewById(R.id.rlInfoImage);
        ivUserImage = findViewById(R.id.ivInfoImage);
        tvHelloUser = findViewById(R.id.tvHelloUser);
        etName = findViewById(R.id.etInfoName);
        etPhone = findViewById(R.id.etInfoPhone);
        etAddress = findViewById(R.id.etInfoAddress);
        btnChange = findViewById(R.id.btnChangeInfo);
        pbChangingInfo = findViewById(R.id.pbChangingInfo);
        rlLoadingUserInfo = findViewById(R.id.rlLoadingUserInfo);
    }

    @Override
    public void showProgress(int index) {
        switch (index) {
            case 1:
                rlLoadingUserInfo.setVisibility(View.VISIBLE);
                break;
            case 2:
                pbChangingInfo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void hideProgress(int index) {
        switch (index) {
            case 1:
                rlLoadingUserInfo.setVisibility(View.GONE);
                break;
            case 2:
                pbChangingInfo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
