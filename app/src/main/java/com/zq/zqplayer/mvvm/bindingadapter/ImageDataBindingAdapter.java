package com.zq.zqplayer.mvvm.bindingadapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zq.zqplayer.R;
import com.zq.zqplayer.wiget.GlideRoundTransform;

import androidx.databinding.BindingAdapter;

public class ImageDataBindingAdapter {

    @BindingAdapter("app:imageUrl")
    public static void setImage(ImageView imageView, String url){
        RequestOptions requestOptions= new RequestOptions()
                .placeholder(R.drawable.ic_picture_default_bg)
                .error(R.drawable.ic_picture_default_bg)
                .transform(new GlideRoundTransform(5));
        Glide.with(imageView.getContext())
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }
}