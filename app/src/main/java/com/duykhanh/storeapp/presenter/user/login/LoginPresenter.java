package com.duykhanh.storeapp.presenter.user.login;

import com.duykhanh.storeapp.model.data.user.login.LoginHandle;

public class LoginPresenter implements LoginContract.Presenter,
        LoginContract.Handle.OnLoggingInListener,
        LoginContract.Handle.OnGetTokenIdListener,
        LoginContract.Handle.OnPutTokenIdToUserListener,
        LoginContract.Handle.OnStoreUserIdListener {

    LoginContract.View iView;
    LoginContract.Handle iHandle;

    public LoginPresenter(LoginContract.View iView) {
        this.iView = iView;
        iHandle = new LoginHandle(iView);
    }

    //    Gửi yêu cầu đăng nhập
    @Override
    public void requestLogIn(String email, String password) {
        if (iView != null) {
            iView.showProgress();
        }
        iHandle.loggingIn(this, email, password);
    }

    @Override
    public void onLoggingInFinished(String userId) {
//        Lấy Token
        iHandle.getTokenId(this, userId);
        iHandle.storeUserId(this, userId);
    }

    @Override
    public void onStoreUserIdFinished() {

    }

    @Override
    public void onGetTokenIdFinished(String userId, String tokenId) {
//        Cập nhật Token cho User
        iHandle.putTokenIdToUser(this, userId, tokenId);
    }

    @Override
    public void onPutTokenIdToUserFinished() {
        if (iView != null) {
            iView.hideProgress();
            iView.requestLogInFinished();
        }
    }

    @Override
    public void onLoggingInFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
            iView.requestLogInFailure(throwable);
        }
    }

    @Override
    public void onGetTokenIdFailure(Throwable throwable) {
        iView.requestLogInFailure(throwable);
    }

    @Override
    public void onPutTokenIdToUserFailure(Throwable throwable) {
        if (iView != null) {
            iView.hideProgress();
        }
    }

    @Override
    public void onStoreUserIdFailure() {

    }

    @Override
    public void onDestroyed() {
        iView = null;
    }
}
