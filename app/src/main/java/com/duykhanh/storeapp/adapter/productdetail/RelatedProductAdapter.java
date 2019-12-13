package com.duykhanh.storeapp.adapter.productdetail;

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
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;

import java.util.List;

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.ViewHolder> {

    ProductDetailActivity context;
    int resource;
    List<Product> products;

    public RelatedProductAdapter(ProductDetailActivity context, int resource, List<Product> products) {
        this.context = context;
        this.resource = resource;
        this.products = products;
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
        Product product = products.get(position);

        Glide.with(context)
                .load(Formater.formatImageLink(product.getImg().get(0)))
                .into(holder.ivProductImage);
        holder.tvProductName.setText(product.getNameproduct());
        holder.tvProductPrice.setText(Formater.formatMoney(product.getPrice()) + " đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onRelatedProductItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivHoriProductImage);
            tvProductName = itemView.findViewById(R.id.tvHoriProductName);
            tvProductPrice = itemView.findViewById(R.id.tvHoriProductPrice);

        }
    }
}
