package com.duykhanh.storeapp.adapter.comment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.duykhanh.storeapp.utils.Formater;

import java.util.List;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public class MoreImageCommentAdapter extends RecyclerView.Adapter<MoreImageCommentAdapter.ViewHolder> {
    private List<String> imageList;
    private Context context;
    private Formater formater;

    public MoreImageCommentAdapter(List<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
        formater = new Formater();
    }

    @NonNull
    @Override
    public MoreImageCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_image_comment_show_more, parent,false);
        return new MoreImageCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreImageCommentAdapter.ViewHolder holder, int position) {
        String url = null;
        try {
            url = formater.formatImageLink(imageList.get(position));
        }
        catch (Exception e){
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
                .into(holder.imgCommentShowMore);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCommentShowMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCommentShowMore = itemView.findViewById(R.id.imgCommentShowMore);
        }
    }
}

