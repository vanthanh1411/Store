package com.duykhanh.storeapp.view.userpage.orders;

import com.duykhanh.storeapp.model.Order;

import java.util.List;

public interface OrdersContract {

    interface Handle {

        void getUserId(OnGetUserIdListener listener);//Lấy thông tin User

        void getOrders(OnGetOrdersListener listener, String userId, int orderStatus);//Lấy list Order

        interface OnGetUserIdListener {//Hoàn thành lấy thông tin User

            void onGetCurrentUserFinished(String userId);//Thành công

            void onGetCurrentUserFailure(Throwable throwable);//Thất bại
        }

        interface OnGetOrdersListener {//Hoàn thành lấy list Order

            void onGetOrdersFinished(List<Order> orders);//Thành công

            void onGetOrderFailure(Throwable throwable);//Thất bại
        }
    }

    interface View {

        void requestGetUserIdSuccess(String userId);//Hoàn thành yêu cầu lấy User Id

        void requestGetOrdersSuccess(List<Order> orders);//Hoàn thành yêu cầu lấy Orders

        void showProgress();

        void hideProgress();
    }

    interface Presenter {

        void requestGetUserId();//Yêu cầu lấy thông tin User

        void requestGetOrders(String userId, int orderStatus);//yêu cầu lấy list Order

        void onDestroy();
    }

}
