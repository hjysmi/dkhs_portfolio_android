package com.dkhs.portfolio.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


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

    public static void setImage(String url, ImageView new_phone1) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
        loader.displayImage(url, new_phone1, options);
    }
    public static void setImage(String url, ImageView new_phone1,@DrawableRes int failedDrawableId) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .showImageOnFail(failedDrawableId)
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
        loader.displayImage(url, new_phone1, options);
    }


    public static void setHeanderImage(String url, ImageView new_phone1) {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(300))
                .showImageForEmptyUri(R.drawable.ic_user_head)
                .build();
        loader.displayImage(url, new_phone1, options);
    }


}
