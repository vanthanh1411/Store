package com.duykhanh.storeapp.adapter.buyviewproduct;

import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.duykhanh.storeapp.view.homepage.buythemostpage.BuyMostActivity;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;

import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_RELEASE_TO;

/**
 * Created by Duy Khánh on 12/4/2019.
 */
public class BuyProductMoreAdapter extends RecyclerView.Adapter<BuyProductMoreAdapter.ViewHolder>{
    private BuyMostActivity context;
    private List<Product> productList;
    //Format string
    Formater formater;

    public BuyProductMoreAdapter(BuyMostActivity context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        formater = new Formater();
    }

    @NonNull
    @Override
    public BuyProductMoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buy_product_more, parent, false);
        return new BuyProductMoreAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyProductMoreAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        String nameProduct = formater.formatNameProductViewMore(product.getNameproduct());
        holder.txtNameProduct.setText(nameProduct);
        holder.txtPriceProduct.setText(Formater.formatMoney(product.getPrice()) + "đ");
        try {
            holder.txtView.setText("Lượt xem : " + product.getView());
        }catch (Exception e){
            Log.i("ERR", "err: " + e);
        }
//        holder.ratingbarPointProduct.setRating(product.getPoint());
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
                        holder.pb_load_image.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb_load_image.setVisibility(View.GONE);
                        return false;
                    }
                }).apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(holder.img_product);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, ProductDetailActivity.class);
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
        TextView txtNameProduct, txtPriceProduct,txtView;
        RatingBar ratingbarPointProduct;
        ImageView img_product;
        ConstraintLayout ctl_view_product;
        ProgressBar pb_load_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.txt_name_product);
            txtPriceProduct = itemView.findViewById(R.id.txt_price_product);
            txtView = itemView.findViewById(R.id.txt_view);
            ratingbarPointProduct = itemView.findViewById(R.id.ratingbarPointProductView);
            img_product = itemView.findViewById(R.id.img_comment);
            ctl_view_product = itemView.findViewById(R.id.ctl_view_product_more);
            pb_load_image = itemView.findViewById(R.id.pb_load_buy_product);
        }
    }
}
