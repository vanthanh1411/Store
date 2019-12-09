package com.duykhanh.storeapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.utils.Formater;

import java.util.List;

public class BuyingProductAdapter extends RecyclerView.Adapter<BuyingProductAdapter.ViewHolder> {
    final String TAG = this.getClass().toString();

    int resource;
    Context context;
    List<CartItem> cartItems;

    Formater formater;

    public BuyingProductAdapter(int resource, Context context, List<CartItem> cartItems) {
        this.resource = resource;
        this.context = context;
        this.cartItems = cartItems;
        formater = new Formater();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.tvName.setText(cartItem.getName());
        holder.tvQuantity.setText("Số lượng: " + cartItem.getQuantity());
        holder.tvPrice.setText("Giá: " + formater.formatMoney((int)(cartItem.getPrice())));
        holder.tvTotal.setText("Thành tiền: " + formater.formatMoney((int)(cartItem.getPrice() * cartItem.getQuantity())));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvPProductName);
            tvQuantity = itemView.findViewById(R.id.tvPProductQuantity);
            tvPrice = itemView.findViewById(R.id.tvPProductPrice);
            tvTotal = itemView.findViewById(R.id.tvPProductsTotal);
        }
    }
}
