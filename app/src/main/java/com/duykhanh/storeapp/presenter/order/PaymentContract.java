package com.duykhanh.storeapp.presenter.order;

import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.model.User;

import java.util.List;

public interface PaymentContract {

    interface Handle {
        void getCurrentUser(OnGetCurrentUserListener listener); //(1)Kiểm tra trạng thái đăng nhập

        void updateUserInfo(OnUpdateUserInfoListener listener, User user); //(2) Cập nhật thông tin người dùng (địa chỉ và số điện thoại);

        void getCartItems(OnGetCartItemsListener listener); //Lấy thông tin giỏ hàng trong SQLite

        void getOrderDetails(OnGetOrderDetailsListener listener, Order order); //Lấy danh sách hóa đơn chi tiết dựa trên thông tin giỏ hàng

        void postOrder(OnPostOrderListener listener, Order order, List<OrderDetail> orderDetails); //(1)Gửi đơn hàng lên sv

        void postOrderDetail(OnPostOrderDetailListener listener, List<OrderDetail> orderDetails); //(2)Gửi đơn hàng chi tiết lên sv sau (1)

        void deleteCarts(OnDeleteCartsListener listener, List<OrderDetail> orderDetails); //Xóa thông tin

        interface OnGetCurrentUserListener {
            void onGetCurrentUserFinished(User user);

            void onGetCurrentUserFailure(Throwable throwable);
        }

        interface OnUpdateUserInfoListener {
            void onUpdateUserInfoFinished();

            void onUpdateUserInfoFailure(Throwable throwable);
        }

        interface OnGetCartItemsListener {
            void onGetCartItemsFinished(List<CartItem> cartItems);

            void onGetCartItemsFailure(Throwable throwable);
        }

        interface OnGetOrderDetailsListener {
            void onGetOrderDetailsFinished(Order order, List<OrderDetail> orderDetails);

            void onGetOrderDetailFailure(Throwable throwable);

        }

        interface OnPostOrderListener {
            void onPostOrderFinished(List<OrderDetail> orderDetails);

            void onPostOrderFailure(Throwable throwable);
        }

        interface OnPostOrderDetailListener {
            void onPostOrderDetailFinished(List<OrderDetail> orderDetails);

            void onPostOrderDetailFailure(Throwable throwable);
        }

        interface OnDeleteCartsListener {
            void onDeleteCartsFinished();

            void onDeleteCartsFailure(Throwable throwable);
        }
    }

    interface View {
        void requestCurrentUserComplete(User user);

        void requestCartItemsComplete(List<CartItem> cartItems);

        void requestPayedFailure(Throwable throwable);

        void onPayed();

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void requestCurrentUser();

        void requestUpdateUserInfo(User user);

        void requestCartItemsFromSql();

        void requestOrderDetailsFromSql(Order order);

        void requestPay(Order order, List<OrderDetail> orderDetails);

        void onDestroy();
    }
}
