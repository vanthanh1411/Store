package com.duykhanh.storeapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.userpage.orders.OrdersActivity;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    final String TAG = this.getClass().toString();
    OrdersActivity context;
    int resource;
    List<Order> orders;
    Formater formater;

    public OrdersAdapter(OrdersActivity context, int resource, List<Order> orders) {
        this.context = context;
        this.resource = resource;
        this.orders = orders;
        formater = new Formater();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvOrderDate.setText("Ngày đặt hàng: " + formater.formatDate(order.getDate()));
        holder.tvOrderId.setText("Mã đơn hàng: " + order.getIdo());
        holder.tvOrderStatus.setText("Trạng thái: " + formater.statusIntToString(order.getStatus()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onOrderItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderCustomer, tvOrderTotal, tvOrderAddress, tvOrderPhone, tvOrderDate, tvOrderStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
