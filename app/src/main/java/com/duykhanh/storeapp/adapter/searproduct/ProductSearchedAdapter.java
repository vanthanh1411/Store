package com.duykhanh.storeapp.adapter.searproduct;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.search.SearchFragment;

import java.util.List;

public class ProductSearchedAdapter extends RecyclerView.Adapter<ProductSearchedAdapter.ViewHolder> {
    final String TAG = this.getClass().toString();
    Formater formater;

    SearchFragment context;
    List<Product> products;
    int resource;

    public ProductSearchedAdapter(SearchFragment context, List<Product> products, int resource) {
        this.context = context;
        this.products = products;
        this.resource = resource;
        formater = new Formater();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        Glide.with(context)
                .load(formater.formatImageLink(product.getImg().get(0)))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pbLoadingImage.setVisibility(View.GONE);
                        Log.d("Glide", "onLoadFailed: " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pbLoadingImage.setVisibility(View.GONE);
                        return false;
                    }
                }).apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(holder.ivSProductImage);
        holder.tvProductName.setText(product.getNameproduct());
        holder.tvProductPrice.setText(formater.formatMoney(product.getPrice()) + " đ");
        holder.rbRating.setRating(product.getPoint());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onSearchedItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSProductImage;
        TextView tvProductName, tvProductPrice;
        RatingBar rbRating;

        ProgressBar pbLoadingImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pbLoadingImage = itemView.findViewById(R.id.pbSLoadingImage);
            ivSProductImage = itemView.findViewById(R.id.ivSProductImage);
            tvProductName = itemView.findViewById(R.id.tvSProductName);
            tvProductPrice = itemView.findViewById(R.id.tvSProductPrice);
            rbRating = itemView.findViewById(R.id.rbSProductRating);
        }
    }
}
