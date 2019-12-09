package com.duykhanh.storeapp.presenter.order;

import android.content.Context;

import com.duykhanh.storeapp.model.CartItem;

import java.util.List;

public interface CartContract {

    interface Handle {
        void getCartItems(OnGetCartItemsListener listener);// Lấy danh sách hàng trong giỏ hàng

        void increaseQuantity(OnIncreaseQuantityListener listener, String cartProductId);// Tăng số lượng

        void decreaseQuantity(OnDecreaseQuantityListener listener, String cartProductId);// Giảm số lượng

        void deleteCartItem(OnDeleteCartItemListener listener, String cartProductId);// Xóa hàng trong giỏ hàng

        void getCurrentUser(OnGetCurrentUserListener listener);

        interface OnGetCartItemsListener {
            void onGetCartItemsFinished(List<CartItem> cartItems);

            void onGetCartItemsFailure(Throwable throwable);
        }

        interface OnIncreaseQuantityListener {
            void onIncreaseQuantityFinished();

            void onIncreaseQuantityFailure(Throwable throwable);
        }

        interface OnDecreaseQuantityListener {
            void onDecreaseQuantityFinished();

            void onDecreaseQuantityFailure(Throwable throwable);
        }

        interface OnDeleteCartItemListener {
            void onDeleteCartItemFinished();

            void onDeleteCartItemFailure(Throwable throwable);
        }

        interface OnGetCurrentUserListener {
            void onGetCurrentUserFinished(String userId);

            void onGetCurrentUserFailure(Throwable throwable);
        }
    }

    interface View {

        void setCartItemsToCartRv(List<CartItem> cartItems);// Đưa danh sách mặt hàng trong giỏ hàng vào Recycler View

        void onCartItemsResponseFailure(Throwable throwable);// Gọi giỏ hàng không thành công thì hiển thị lỗi ra màn hình

        void requestCurrentUserComplete(String userId);//Hành động sau khi kiểm tra người dùng (đi tiếp hay dừng lại)

        Context getContext();// Handle cần Context =.="

        void showProgress();

        void hideProgress();

    }

    interface Presenter {
        void requestCartItems();// Yêu cầu lấy danh sách Cart

        void requestIncreaseQuantity(String cartProductId);// Yêu cầu tăng số lượng

        void requestDecreaseQuantity(String cartProductId);// Yêu cầu giảm số lượng

        void requestDeleteCartItem(String cartProductId);// Yêu cầu xóa món hàng

        void requestCurrentUser();// Check trạng thái đăng nhập rồi chuyển qua thanh toán

        void onDestroy();
    }

}

