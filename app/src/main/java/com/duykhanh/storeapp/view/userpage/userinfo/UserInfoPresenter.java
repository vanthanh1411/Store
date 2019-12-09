package com.duykhanh.storeapp.view.userpage.userinfo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.duykhanh.storeapp.model.User;

public class UserInfoPresenter implements UserInfoContract.Presenter,
        UserInfoContract.Handle.OnGetCurrentUserListener,
UserInfoContract.Handle.OnChangeUserInfoListener{
    final String TAG = this.getClass().toString();

    UserInfoContract.View iView;
    UserInfoContract.Handle iHandle;

    public UserInfoPresenter(UserInfoContract.View iView) {
        this.iView = iView;
        iHandle = new UserInfoHandle(iView);
    }

    @Override //Lấy thông tin người dùng đang đăng nhập
    public void requestCurrentUser() {
        if (iView != null){
            iView.showProgress(1);
        }
        iHandle.getCurrentUser(this);
    }

    @Override //Lấy thông tin người dùng đang đăng nhập thành công
    public void onGetCurrenUserFinished(User user, Bitmap infoImageBitmap) {
        if (iView != null){
            iView.hideProgress(1);
            iView.requestCurrenUserSuccess(user, infoImageBitmap);
        }
    }

    @Override //Yêu cầu cập nhật thông tin người dùng
    public void requestChangeInfo(User user, Uri imageUri) {
        if (iView != null){
            iView.showProgress(2);
        }
        iHandle.changeUserInfo(this, user, imageUri);
    }

    @Override //Thay đổi thông tin người dùng thành công
    public void onChangeUserInfoFinished() {
        if (iView != null){
            iView.hideProgress(2);
            iView.changeUserInfoSuccess();
        }
    }

    @Override //Lấy thông tin người dùng đang đăng nhập thất bại
    public void onGetCurrntUserFailure(Throwable throwable) {
        Log.e(TAG, "onGetCurrntUserFailure: ", throwable);
    }

    @Override //Cập nhật thông tin người dùng thất bại.
    public void onChangeUserInfoFailure(Throwable throwable) {
        Log.e(TAG, "onChangeUserInfoFailure: ", throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
