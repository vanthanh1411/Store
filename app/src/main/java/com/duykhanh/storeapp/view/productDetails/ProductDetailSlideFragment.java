package com.duykhanh.storeapp.view.productDetails;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.utils.Formater;

import static com.duykhanh.storeapp.utils.Constants.*;

public class ProductDetailSlideFragment extends Fragment {

    Formater formater;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_product_detail_slide, container, false);
        Bundle bundle = getArguments();
        String linkimg = bundle.getString("link");
        ImageView imgContainerSlider = view.findViewById(R.id.imgContainerSlider);
        formater = new Formater();
        setImg(imgContainerSlider, linkimg);

        return view;
    }

    private void setImg(final ImageView img, String linkhinh) {

        Glide.with(getContext())
                .load(formater.formatImageLink(linkhinh))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("Glide", "onLoadFailed: " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(img);

    }
}
