package com.dkhs.portfolio.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class ImageLoaderUtils {


    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // String filePath=context.getCacheDir().getPath();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void setRoundedImage(String url, ImageView new_phone1) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
        loader.displayImage(url, new_phone1, options);
    }

    public static void setImage(String url, ImageView new_phone1) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        loader.displayImage(url, new_phone1, options);

    }

    public static void setRoundImageByPx(String url, ImageView new_phone1, int roundPx) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(roundPx))
                .build();
        loader.displayImage(url, new_phone1, options);

    }

    public static void setImagDefault(String url, ImageView new_phone1) {
        new_phone1.setVisibility(View.VISIBLE);
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(R.drawable.ic_img_failure)
                .showImageOnLoading(R.drawable.ic_img_thumbnail)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        loader.displayImage(url, new_phone1, options);

    }

    public static void setImage(String url, ImageView new_phone1, ImageLoadingListener loadingListener) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        loader.displayImage(url, new_phone1, options, loadingListener);

    }

    public static void loadImage(String url) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(600))
                .build();
        loader.loadImage(url, options, null);

    }

    public static void loadImage(String url, ImageLoadingListener imageLoadingListener) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(600))
                .build();

        loader.loadImage(url, options, imageLoadingListener);

    }

    public static void setRoundedImage(String url, ImageView new_phone1, @DrawableRes int loadingDrawableId, @DrawableRes int failedDrawableId) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(loadingDrawableId)
                .showImageOnFail(failedDrawableId)
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
        loader.displayImage(url, new_phone1, options);
    }


    public static void setHeanderImage(String url, ImageView new_phone1) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(300))
                .showImageForEmptyUri(R.drawable.ic_user_head)
                .showImageOnFail(R.drawable.ic_user_head)
                .showImageOnLoading(R.drawable.ic_user_head)
                .showImageForEmptyUri(R.drawable.ic_user_head)
                .build();
        loader.displayImage(url, new_phone1, options);
    }


}
