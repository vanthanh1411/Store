package com.duykhanh.storeapp.adapter.comment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    final String TAG = this.getClass().toString();

    Formater formater;

    Context context;
    List<Comment> comments;
    int resource;

    public CommentsAdapter(Context context, List<Comment> comments, int resource) {
        this.context = context;
        this.comments = comments;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        ImageCommentAdapter adapter = new ImageCommentAdapter(comment.getImgc(), context);
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

            tvCommentTitle = itemView.findViewById(R.id.tvCommentTitle);
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
            tvCommentUser = itemView.findViewById(R.id.tvCommentUser);
            tvCommentConfirm = itemView.findViewById(R.id.tvCommentConfirm);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
            rbCommentRate = itemView.findViewById(R.id.rbCommentRate);
            rcl_image_comment_show = itemView.findViewById(R.id.rcl_image_comment_show);

        }
    }
}
