package com.duykhanh.storeapp.adapter.buyviewproduct;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.duykhanh.storeapp.view.homepage.HomeFragment;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;

import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_RELEASE_TO;

/**
 * Created by Duy Khánh on 12/4/2019.
 */
public class BuyProductAdapter extends RecyclerView.Adapter<BuyProductAdapter.ViewHolder> {

    List<Product> productList;
    HomeFragment context;

    Formater formater;

    public BuyProductAdapter( HomeFragment context,List<Product> productList) {
        this.productList = productList;
        this.context = context;
        formater = new Formater();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buy_product, parent, false);
        return new BuyProductAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        String nameProduct = formater.formatNameProductView(product.getNameproduct());
        String priceProduct = formater.formatMoney(product.getPrice());
        holder.txtNameProduct.setText(nameProduct);
        holder.txtPriceProduct.setText(priceProduct + " đ");
        String url = null;
        try {
            url = formater.formatImageLink(product.getImg().get(0));
        } catch (Exception e) {
            Log.d("Error", "onBindViewHolder: " + e);
        }

        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context.getContext(), ProductDetailActivity.class);
                detailIntent.putExtra(KEY_RELEASE_TO, productList.get(position).getId());
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProduct, txtPriceProduct;
        RatingBar ratingbarPointProduct;
        ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.txtNameBuyProduct);
            txtPriceProduct = itemView.findViewById(R.id.txtPriceBuyProduct);
            ratingbarPointProduct = itemView.findViewById(R.id.ratingbarPointProduct);
            imgProduct = itemView.findViewById(R.id.img_buy_product);
        }
    }
}
