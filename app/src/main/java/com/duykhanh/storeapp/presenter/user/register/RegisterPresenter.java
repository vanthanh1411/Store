package com.duykhanh.storeapp.presenter.user.register;

import android.util.Log;

import com.duykhanh.storeapp.model.data.user.register.RegisterHandle;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPresenter implements RegisterContract.Presenter,
        RegisterContract.Handle.OnDoRegisterListener,
        RegisterContract.Handle.OnCreateUserListener {
    final String TAG = this.getClass().toString();

    RegisterContract.View iView;
    RegisterContract.Handle iHandle;

    public RegisterPresenter(RegisterContract.View iView) {
        this.iView = iView;
        iHandle = new RegisterHandle();
    }

    //    Gửi yêu cầu Đăng Ký
    @Override
    public void requestRegister(String email, String password) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.doRegister(this, email, password);
    }

    //    Lắng nghe sự kiện Đăng Ký
    @Override
    public void onDoRegisterFinished(FirebaseUser firebaseUser) {
        Log.d(TAG, "onDoRegisterFinished: " + firebaseUser.getEmail());

//        Tạo User mới
        iHandle.createUser(this, firebaseUser);
    }

    @Override
    public void onDoRegisterFailure(Throwable throwable) {
        Log.d(TAG, "onDoRegisterFailure: ");
        if (iView != null) {
            iView.hideProgress();
        }
        iView.requestRegisterFailure(throwable);
    }

    //    Lắng nghe sự kiện tạo User
    @Override
    public void onCreateUserFinished() {
        if (iView != null) {
            iView.hideProgress();
        }
        iHandle.autoLogOut();
        iView.requestRegisterComplete();
    }

    @Override
    public void onCreateUserFailure(Throwable throwable) {
        iView.requestRegisterFailure(throwable);
    }

    @Override
    public void onDestroy() {
        iView = null;
    }
}
