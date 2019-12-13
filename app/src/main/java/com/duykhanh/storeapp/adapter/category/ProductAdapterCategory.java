package com.duykhanh.storeapp.adapter.category;

import android.content.Intent;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.duykhanh.storeapp.view.categorypage.CategoryListProductActivity;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;

import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.*;

/**
 * Created by Duy Khánh on 11/26/2019.
 */
public class ProductAdapterCategory extends RecyclerView.Adapter<ProductAdapterCategory.ViewHolder> implements Filterable {
    private CategoryListProductActivity context;
    private List<Product> productList;
    private List<Product> originalProductList;


    Formater formater;

    public ProductAdapterCategory(CategoryListProductActivity context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.originalProductList = productList;
//        HomeFragment.setOnProductAdapterListener(this);
        formater = new Formater();
    }


    @NonNull
    @Override
    public ProductAdapterCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_category, parent, false);
        return new ProductAdapterCategory.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterCategory.ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.txtNameProduct.setText(product.getNameproduct());
        holder.txtPriceProduct.setText(Formater.formatMoney(product.getPrice()) + "đ");
        holder.ratingbarPointProduct.setRating(product.getPoint());

        String url = formater.formatImageLink(product.getImg().get(0));

        Log.d("URL", "onBindViewHolder: " + url);

        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pb_load_image.setVisibility(View.GONE);
                        Log.d("Glide", "onLoadFailed: " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb_load_image.setVisibility(View.GONE);
                        return false;
                    }
                }).apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iDetailProduct = new Intent(context, ProductDetailActivity.class);
                iDetailProduct.putExtra(KEY_ITEM_CATEGORY,product.getId());
                iDetailProduct.putExtra("KEY_START_CATEGORY",KEY_DATA_CATEGORY_TO_DETAIL_PRODUCT);
                context.startActivityForResult(iDetailProduct,KEY_START_DETAIL_PRODUCT);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("TAGGGG", "getItemCount: " + productList.size());
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Movie> filteredResults = null;
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList = (List<Product>) filterResults.values;
                ProductAdapterCategory.this.notifyDataSetChanged();
            }
        };
    }

//    @Override
//    public void loadMoreProduct(int count) {
//
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProduct, txtPriceProduct;
        RatingBar ratingbarPointProduct;
        ImageView imgProduct;
        RecyclerView recyclerProducts;
        CardView rltContainer;

        ProgressBar pb_load_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.txtNameProduct);
            txtPriceProduct = itemView.findViewById(R.id.txtPriceProduct);
            ratingbarPointProduct = itemView.findViewById(R.id.ratingbarPointProduct);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            recyclerProducts = itemView.findViewById(R.id.recyclerProducts);
            rltContainer = itemView.findViewById(R.id.rltContainer);
            pb_load_image = itemView.findViewById(R.id.pb_load_image);
        }
    }
}
