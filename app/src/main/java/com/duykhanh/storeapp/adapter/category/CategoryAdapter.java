package com.duykhanh.storeapp.adapter.category;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.Category;
import com.duykhanh.storeapp.utils.Formater;

import java.util.List;

/**
 * Created by Duy Khánh on 11/25/2019.
 */
public class CategoryAdapter extends BaseAdapter {
    List<Category> categoryList;
    Context context;
    Formater formater;
    private static LayoutInflater inflater = null;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        formater = new Formater();
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class Holder {
        TextView txtNameCategory;
        ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();

        View grid;

        grid = inflater.inflate(R.layout.item_grid_category,null);
        holder.txtNameCategory = grid.findViewById(R.id.txtNameCategory);
        holder.imageView = grid.findViewById(R.id.imgCategory);
        holder.txtNameCategory.setText(categoryList.get(i).getName());

        String url = formater.formatImageLink(categoryList.get(i).getImg());
        Log.d("IMAGE", "getView: " + categoryList.get(0).getImg());

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
                .into(holder.imageView);
        return grid;
    }
}
