package com.duykhanh.storeapp.presenter.user;

import android.graphics.Bitmap;
import com.duykhanh.storeapp.model.data.user.UserHandle;

import com.duykhanh.storeapp.model.User;

public class UserPresenter implements UserContract.Presenter,
        UserContract.Handle.OnLogOutListener,
        UserContract.Handle.OnRemoveUserIdListener,
        UserContract.Handle.OnGetCurrentUserListener {

    UserContract.Handle iHandle;
    UserContract.View iView;

    public UserPresenter(UserContract.View iView) {
        this.iView = iView;
        iHandle = new UserHandle(iView);
    }

    //    Gửi yêu cầu lấy người dùng hiện tại
    @Override
    public void requestGetCurrentUser() {
        if (iView != null){
            iView.showLoadingUser();
        }
        iHandle.getCurrentUser(this);
    }

    //    Lắng nghe sự kiện lấy người dùng hiện tại
    @Override
    public void onGetCurrentUserFinished(User user, Bitmap bmImage) {
        if (iView != null) {
            iView.hideLoadingUser();
            if (user != null) {
                iView.requestCurrentUserSuccess(user, bmImage);
                iView.hideLoginRequire();
            } else {
                iView.showLoginRequire();
            }
        }
    }

    @Override//Gửi yêu cầu Đăng Xuất
    public void requestLogOut() {
        iHandle.logOut(this);
    }

    @Override //Đăng xuất thành công
    public void onLogoutFinished() {
        //Xóa User Id
        iHandle.removeUserId(this);
    }

    @Override//Xóa User ID thành công
    public void onRemoveUserIdFinished() {
        iView.requestLogOutSuccess();
    }

    @Override//Lấy User hiện tại thất bại
    public void onGetCurrentUserFailure(Throwable throwable) {
        iView.requestLogOutFailure(throwable);
    }

    @Override//Đăng xuất thất bại
    public void onLogOutFailure(Throwable throwable) {
        iView.requestLogOutFailure(throwable);
    }

    @Override//Xóa User Id thất bại
    public void onRemoveUserIdFailure(Throwable throwable) {
        iView.requestLogOutFailure(throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
