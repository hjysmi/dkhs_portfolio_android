package com.dkhs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void setRoundedImage(String url, ImageView new_phone1) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
        loader.displayImage(url, new_phone1, options);
    }

    public static void setImage(String url, ImageView new_phone1, ImageLoadingListener loadingListener) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .displayer(new SimpleBitmapDisplayer())
                .build();
        loader.displayImage(url, new_phone1, options,loadingListener);

    }

    public static void setRoundedImage(String url, ImageView new_phone1, @DrawableRes int loadingDrawableId, @DrawableRes int failedDrawableId) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .showImageOnLoading(loadingDrawableId)
                .showImageOnFail(failedDrawableId)
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
        loader.displayImage(url, new_phone1, options);
    }





}
