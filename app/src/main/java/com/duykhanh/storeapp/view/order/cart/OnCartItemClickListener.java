package com.duykhanh.storeapp.view.order.cart;

public interface OnCartItemClickListener {

    void onCartItemClick(int position);

    void onDeleteButtonClick(int position);

    void onIncreaseButtonClick(int position);

    void onDecreaseButtonClick(int position);
}
