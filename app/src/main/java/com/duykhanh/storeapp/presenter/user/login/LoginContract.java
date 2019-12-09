package com.duykhanh.storeapp.presenter.user.login;

import android.content.Context;

public interface LoginContract {

    interface Handle {
        void loggingIn(OnLoggingInListener listener, String email, String password);

        void getTokenId(OnGetTokenIdListener listener, String userId);

        void putTokenIdToUser(OnPutTokenIdToUserListener listener, String userId, String tokenId);

        void storeUserId(OnStoreUserIdListener listener, String userId);

        interface OnLoggingInListener {
            void onLoggingInFinished(String userId);

            void onLoggingInFailure(Throwable throwable);
        }

        interface OnGetTokenIdListener {
            void onGetTokenIdFinished(String userId, String tokenId);

            void onGetTokenIdFailure(Throwable throwable);
        }

        interface OnPutTokenIdToUserListener {
            void onPutTokenIdToUserFinished();

            void onPutTokenIdToUserFailure(Throwable throwable);
        }

        interface OnStoreUserIdListener {
            void onStoreUserIdFinished();

            void onStoreUserIdFailure();
        }
    }

    interface View {
        void requestLogInFinished();

        void requestLogInFailure(Throwable throwable);

        Context getContext();

        void showProgress();

        void hideProgress();
    }


    interface Presenter {
        void requestLogIn(String email, String password);

        void onDestroyed();
    }
}
