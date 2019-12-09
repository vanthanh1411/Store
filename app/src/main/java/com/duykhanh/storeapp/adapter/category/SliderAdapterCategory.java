package com.duykhanh.storeapp.adapter.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.duykhanh.storeapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

/**
 * Created by Duy Khánh on 11/26/2019.
 */
public class SliderAdapterCategory extends SliderViewAdapter<SliderAdapterCategory.SliderAdapterVH> {

    private Context context;

    public SliderAdapterCategory(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterCategory.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slide_show_category, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterCategory.SliderAdapterVH viewHolder, int position) {
        switch (position){
            case 0:
                Glide.with(viewHolder.itemView)
                        .load("https://www.bedsndreams.com.au/media/catalog/category/Category_BANNER_03.jpg")
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                Glide.with(viewHolder.itemView)
                        .load("https://www.bedsndreams.com.au/media/catalog/category/Category_BANNER_04.jpg")
                        .into(viewHolder.imageViewBackground);
                break;
            case 2:
                Glide.with(viewHolder.itemView)
                        .load("https://www.bedsndreams.com.au/media/catalog/category/Category_BANNER_01.jpg")
                        .into(viewHolder.imageViewBackground);
                break;
            default:
                Glide.with(viewHolder.itemView)
                        .load("https://www.bedsndreams.com.au/media/catalog/category/Category_BANNER_02.jpg")
                        .into(viewHolder.imageViewBackground);
                break;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder{
        View itemView;
        ImageView imageViewBackground;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider_category);
            this.itemView = itemView;
        }
    }
}
