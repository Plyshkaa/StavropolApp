package com.example.stavropolplacesapp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.stavropolplacesapp.R

object ImageUtils {
    
    private val glideOptions = RequestOptions()
        .placeholder(R.drawable.ic_places_static_image)
        .error(R.drawable.ic_places_static_image)
        .centerCrop()
    
    fun loadImage(context: Context, imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .apply(glideOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
    
    fun loadImageWithCustomPlaceholder(
        context: Context, 
        imageUrl: String, 
        imageView: ImageView, 
        placeholderResId: Int
    ) {
        Glide.with(context)
            .load(imageUrl)
            .apply(glideOptions.placeholder(placeholderResId).error(placeholderResId))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
    
    fun preloadImage(context: Context, imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .preload()
    }
    
    fun clearImageCache(context: Context) {
        Glide.get(context).clearMemory()
        Thread {
            Glide.get(context).clearDiskCache()
        }.start()
    }
}
