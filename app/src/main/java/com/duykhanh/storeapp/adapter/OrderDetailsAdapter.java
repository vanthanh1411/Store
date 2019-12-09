package com.duykhanh.storeapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.userpage.orderdetails.OrderDetailsActivity;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    final String TAG = this.getClass().toString();

    OrderDetailsActivity context;
    int resource;
    List<OrderDetail> orderDetails;

    public OrderDetailsAdapter(OrderDetailsActivity context, int resource, List<OrderDetail> orderDetails) {
        this.context = context;
        this.resource = resource;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetails.get(position);

        holder.tvProductName.setText(orderDetail.getProduct().getNameproduct());
        holder.tvProductQuantity.setText(orderDetail.getQuantity() + " x");
        holder.tvProductPrice.setText(Formater.formatMoney(orderDetail.getProduct().getPrice()) + " đ");
        Glide.with(context)
                .load(Formater.formatImageLink(orderDetail.getProduct().getImg().get(0)))
                        .into(holder.ivOrderDetailImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOrderDetailImg;
        TextView tvProductId, tvProductQuantity, tvProductName, tvProductPrice, tvProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivOrderDetailImg = itemView.findViewById(R.id.ivOrderDetailImg);
            tvProductQuantity = itemView.findViewById(R.id.tvOrderDetailPQuantity);
            tvProductName = itemView.findViewById(R.id.tvOrderDetailPName);
            tvProductPrice = itemView.findViewById(R.id.tvOrderDetailPPrice);
        }
    }
}
