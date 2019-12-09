package com.duykhanh.storeapp.presenter.user.register;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

public interface RegisterContract {

    interface Handle {
        void doRegister(OnDoRegisterListener listener, String email, String password);

        void createUser(OnCreateUserListener listener, FirebaseUser firebaseUser);

        void autoLogOut();

        interface OnDoRegisterListener {
            void onDoRegisterFinished(FirebaseUser user);

            void onDoRegisterFailure(Throwable throwable);
        }

        interface OnCreateUserListener {
            void onCreateUserFinished();

            void onCreateUserFailure(Throwable throwable);
        }
    }

    interface View {

        void requestRegisterComplete();

        void requestRegisterFailure(Throwable throwable);


        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void requestRegister(String email, String password);

        void onDestroy();
    }

}
