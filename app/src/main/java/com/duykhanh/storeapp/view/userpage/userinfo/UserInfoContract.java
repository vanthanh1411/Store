package com.duykhanh.storeapp.view.userpage.userinfo;

import android.graphics.Bitmap;
import android.net.Uri;

import com.duykhanh.storeapp.model.User;

public interface UserInfoContract {

    interface Handle {
        void getCurrentUser(OnGetCurrentUserListener listener);

        void changeUserInfo(OnChangeUserInfoListener listener, User user, Uri imageUri);

        interface OnGetCurrentUserListener {
            void onGetCurrenUserFinished(User user, Bitmap infoImageBitmap);

            void onGetCurrntUserFailure(Throwable throwable);
        }

        interface  OnChangeUserInfoListener{
            void onChangeUserInfoFinished();

            void onChangeUserInfoFailure(Throwable throwable);
        }
    }

    interface View {
        void requestCurrenUserSuccess(User user, Bitmap infoImageBitmap);

        void changeUserInfoSuccess();

        void showProgress(int index);

        void hideProgress(int index);
    }

    interface Presenter {
        void requestCurrentUser();

        void requestChangeInfo(User user, Uri imageUri);

        void onDestroy();
    }

}
