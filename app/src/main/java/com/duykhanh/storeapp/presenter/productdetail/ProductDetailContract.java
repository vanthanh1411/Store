package com.duykhanh.storeapp.presenter.productdetail;

import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.User;

import java.util.List;

public interface ProductDetailContract {

    interface Handle {
        void getProductDetail(OnGetProductDetailListener listener, String productId);

        void getCommentByIdp(OnGetCommentByIdpListener listener, String productId);

        void createCartItem(OnCreateCartItemListener listener, CartItem cartItem);

        void getCartCounter(OnGetCartCounterListener listener);

        void increaseProductView(String productId);

        void getInfomationUser(onGetInfomationUser callback);

        // Instance ProductListConstract.Presenter
        void onGetProductCount();

        interface OnGetProductDetailListener {

            void onGetProductDetailFinished(Product product);

            void onGetProductDetailFailure(Throwable throwable);

        }

        interface OnGetCommentByIdpListener {
            void onGetCommentByIdpFinished(List<Comment> comment);

            void onGetCommentByIdpFailure(Throwable throwable);
        }

        interface OnCreateCartItemListener {
            void onCreateCartItemFinished();

            void onCreateCartItemFailure(Throwable throwable);
        }

        interface OnGetCartCounterListener {
            void onGetCartCounterFinished(int sumQuantity);
        }

        interface onGetInfomationUser{
            void onFinished(List<User> userList);
            void onFaild();
        }
    }

    interface View {
        //Set chi tiết sản phẩm
        void setDataToView(Product product);

        void onResponseFailure(Throwable throwable);

        //Set list Comment
        void setCommentsToRecyclerView(List<Comment> comments);

        void onCommentsResponseFailure(Throwable throwable);

        //Set số lượng hàng trong giỏ hàng
        void setCartItemCounter(int productQuantity);

        void onCartItemCountResponseFailure(Throwable throwable);

        // Set thôn tin user

        void sendInfomationUser(List<User> userList);

        void onFaild();

        void showProgress();

        void hideProgress();

    }

    interface Presenter {
        void requestProductFromServer(String productId);

        void requestCommentsFromServer(String productId);

        void requestCartCounter();

        void requestInfomationUser();

        void addCartItem(CartItem cartItem);

        void requestIncreaseView(String productId);

        void onDestroy();
    }

}
