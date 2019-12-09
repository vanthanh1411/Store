package com.duykhanh.storeapp.adapter.comment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.productDetails.comment.MoreCommentProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Duy Khánh on 12/7/2019.
 */
public class MoreCommentAdapter extends RecyclerView.Adapter<MoreCommentAdapter.ViewHolder> {
    final String TAG = this.getClass().toString();

    Formater formater;

    MoreCommentProductActivity context;
    List<Comment> comments;
    int resource;

    public MoreCommentAdapter(MoreCommentProductActivity context, List<Comment> comments, int resource) {
        this.context = context;
        this.comments = comments;
        this.resource = resource;
    }

    @NonNull
    @Override
    public MoreCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        MoreCommentAdapter.ViewHolder viewHolder = new MoreCommentAdapter.ViewHolder(view);
        View currentFocus = (context).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoreCommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        formater = new Formater();
        holder.tvCommentTitle.setText(comment.getTitle());
        holder.tvCommentContent.setText(comment.getContent());
        holder.rbCommentRate.setRating(comment.getPoint());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(comment.getIdu()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                holder.tvCommentUser.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.tvCommentConfirm.setText("Đã mua hàng");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        Date d = null;
        if (comment.getDate() != null) {
            try {
                d = input.parse(comment.getDate());
                String formatted = output.format(d);

                holder.tvCommentDate.setText("" + formatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rcl_image_comment_show.setLayoutManager(layoutManager);
        MoreImageCommentAdapter adapter = new MoreImageCommentAdapter(comment.getImgc(), context);
        holder.rcl_image_comment_show.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentTitle, tvCommentContent, tvCommentUser, tvCommentConfirm, tvCommentDate;
        RatingBar rbCommentRate;
        RecyclerView rcl_image_comment_show;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCommentTitle = itemView.findViewById(R.id.tvCommentTitleMore);
            tvCommentContent = itemView.findViewById(R.id.tvCommentContentMore);
            tvCommentUser = itemView.findViewById(R.id.tvCommentUserMore);
            tvCommentConfirm = itemView.findViewById(R.id.tvCommentConfirmMore);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDateMore);
            rbCommentRate = itemView.findViewById(R.id.rbCommentRateMore);
            rcl_image_comment_show = itemView.findViewById(R.id.rcl_image_comment_showMore);

        }
    }
}
