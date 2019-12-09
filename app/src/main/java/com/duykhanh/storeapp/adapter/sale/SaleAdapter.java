package com.duykhanh.storeapp.adapter.sale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.homepage.salepage.SaleActivity;

import java.util.List;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {

    SaleActivity context;
    List<Product> productList;
    Formater formater;

    public SaleAdapter(SaleActivity context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        formater = new Formater();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sale_product,parent,false);
        return new SaleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product products = productList.get(position);
        String url = null;
        try {
            url = formater.formatImageLink(products.getImg().get(0));
        }
        catch (Exception e){
            Log.d("Error", "onBindViewHolder: " + e);
        }
        Glide.with(context)
                .load(url)
                .into(holder.img_sale_product);
        holder.txt_name_product_sale.setText("" + products.getNameproduct());
        holder.txt_price_product_sale.setText("" + Formater.formatMoney(products.getPrice()) + "đ");
        holder.ratingbarPointProductSale.setRating(products.getPoint());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onItemClickSale(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_sale_product;
        TextView txt_name_product_sale;
        RatingBar ratingbarPointProductSale;
        TextView txt_price_product_sale;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_sale_product = itemView.findViewById(R.id.img_sale_product);
            txt_name_product_sale = itemView.findViewById(R.id.txt_name_product_sale);
            ratingbarPointProductSale = itemView.findViewById(R.id.ratingbarPointProductSale);
            txt_price_product_sale = itemView.findViewById(R.id.txt_price_product_sale);
        }
    }
}
