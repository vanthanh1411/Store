package com.duykhanh.storeapp.adapter.searproduct;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.view.search.SearchFragment;

import java.util.List;

public class SuggestWordsAdapter extends RecyclerView.Adapter<SuggestWordsAdapter.ViewHolder> {
    SearchFragment context;
    int resource;
    List<String> suggestWords;

    public SuggestWordsAdapter(SearchFragment context, int resource, List<String> suggestWords) {
        this.context = context;
        this.resource = resource;
        this.suggestWords = suggestWords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String suggestWord = suggestWords.get(position);
        holder.tvSuggestWord.setText(suggestWord);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onSuggestWordClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestWords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSuggestWord;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSuggestWord = (TextView) itemView.findViewById(R.id.tvSuggestWord);
        }
    }
}
