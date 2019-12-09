package com.duykhanh.storeapp.adapter.homes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.duykhanh.storeapp.R;

import com.duykhanh.storeapp.model.SlideHome;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.homepage.salepage.SaleActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SlideshowAdapter extends SliderViewAdapter<SlideshowAdapter.SliderAdapterVH> {

    private Context context;
    private List<SlideHome> slideHomeList;
    private Formater formater;

    public SlideshowAdapter(Context context,List<SlideHome> slideHomeList) {
        this.context = context;
        this.slideHomeList = slideHomeList;
        formater = new Formater();
    }

    @Override
    public SlideshowAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slide_show_category, null);
        return new SlideshowAdapter.SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SlideshowAdapter.SliderAdapterVH viewHolder, int position) {
        SlideHome slideHome = slideHomeList.get(position);
        String url = null;
        try {
            url = formater.formatImageLink(slideHome.getImg());
        }
        catch (Exception e){
            Log.d("Error", "onBindViewHolder: " + e);
        }
        switch (position){
            case 0:
                Glide.with(viewHolder.itemView)
                        .load(url)
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                Glide.with(viewHolder.itemView)
                        .load(url)
                        .into(viewHolder.imageViewBackground);
                break;
            case 2:
                Glide.with(viewHolder.itemView)
                        .load(url)
                        .into(viewHolder.imageViewBackground);
                break;
            default:
                Glide.with(viewHolder.itemView)
                        .load(url)
                        .into(viewHolder.imageViewBackground);
                break;
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iSale = new Intent(context, SaleActivity.class);
                iSale.putExtra("SALE",slideHome.getId());
                iSale.putExtra("NAME_SALE",slideHome.getName());
                context.startActivity(iSale);
            }
        });
    }

    @Override
    public int getCount() {
        return slideHomeList.size();
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
