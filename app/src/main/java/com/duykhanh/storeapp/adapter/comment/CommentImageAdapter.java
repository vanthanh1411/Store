package com.duykhanh.storeapp.adapter.comment;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.view.productDetails.comment.CommentproductItemListener;

import java.util.List;

/**
 * Created by Duy Khánh on 12/1/2019.
 */
public class CommentImageAdapter extends RecyclerView.Adapter<CommentImageAdapter.ViewHolder>{

    List<Uri> imgCommentList;
    Context context;

    CommentproductItemListener callback;

    public CommentImageAdapter(List<Uri> imgCommentList, Context context,CommentproductItemListener callback) {
        this.imgCommentList = imgCommentList;
        this.context = context;
        this.callback = callback;
    }


    @NonNull
    @Override
    public CommentImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_comment, parent, false);
        return new CommentImageAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentImageAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(imgCommentList.get(position)).into(holder.imgComment);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onCliCkCommentProductitem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnDelete;
        ImageView imgComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.btn_delete_image);
            imgComment = itemView.findViewById(R.id.img_comment);
        }
    }
}
