package com.duykhanh.storeapp.presenter.user;

import android.content.Context;
import android.graphics.Bitmap;

import com.duykhanh.storeapp.model.User;

public interface UserContract {

    interface Handle {
        void getCurrentUser(OnGetCurrentUserListener listener);//Lấy thông tin người dùng dựa trên User Id

        void logOut(OnLogOutListener listener);//Đăng xuất

        void removeUserId(OnRemoveUserIdListener listener);//Xóa User Id

        interface OnGetCurrentUserListener {//Hoàn thành lấy User

            void onGetCurrentUserFinished(User user, Bitmap bmImage);//Thành công

            void onGetCurrentUserFailure(Throwable throwable);//Thất bại
        }

        interface OnLogOutListener {//Hoàn thành đăng xuất

            void onLogoutFinished();//Thành công

            void onLogOutFailure(Throwable throwable);//Thất bại
        }

        interface OnRemoveUserIdListener {//Hoàn thành xóa User Id

            void onRemoveUserIdFinished();//Thành công

            void onRemoveUserIdFailure(Throwable throwable);//Thất bại
        }
    }

    interface View {
        void requestCurrentUserSuccess(User user, Bitmap bmImage); //Yêu cầu lấy User thành công

        void requestUserFailure(Throwable throwable);//Yêu cầu lấy User thất bại

        void requestLogOutSuccess();//Yêu cầu đăng xuất thành công

        void requestLogOutFailure(Throwable throwable);//Yêu cầu đăng xuất thất bại

        Context getContext();

        void showLoadingUser();

        void hideLoadingUser();

        void showLoginRequire();

        void hideLoginRequire();
    }

    interface Presenter {
        void requestGetCurrentUser();

        void requestLogOut();

        void onDestroy();
    }
}
