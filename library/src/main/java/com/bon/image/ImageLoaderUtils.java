package com.bon.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.bon.logger.Logger;

/**
 * Created by Dang on 10/20/2015.
 */
@SuppressWarnings("ALL")
public class ImageLoaderUtils {
    private static final String TAG = ImageLoaderUtils.class.getSimpleName();

    /**
     * get display option
     *
     * @param drawableId
     * @return
     */
    public static DisplayImageOptions getDisplayImageOption(int drawableId) {
        try {
            return new DisplayImageOptions.Builder()
                    .showImageOnLoading(drawableId) // resource or drawable
                    .showImageForEmptyUri(drawableId) // resource or drawable
                    .showImageOnFail(drawableId) // resource or drawable
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .build();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * init image loader
     *
     * @param context
     * @param memoryCacheSize(MB)
     * @param diskCacheSize(MB)
     */
    public static void initImageLoader(Context context, int memoryCacheSize, int diskCacheSize) {
        try {
            initImageLoader(context, memoryCacheSize, diskCacheSize, null);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param memoryCacheSize
     * @param diskCacheSize
     * @param baseImageDownloader
     */
    public static void initImageLoader(Context context, int memoryCacheSize, int diskCacheSize, BaseImageDownloader baseImageDownloader) {
        try {
            // Load image cache
            DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading(true).cacheInMemory(true)
                    .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.ARGB_8888).build();

            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(imageOptions)
                    .threadPoolSize(3).threadPriority(Thread.MIN_PRIORITY)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCacheSize(memoryCacheSize * 1024 * 1024)
                    .memoryCache(new LruMemoryCache(memoryCacheSize * 1024 * 1024))
                    .diskCacheSize(diskCacheSize * 1024 * 1024);
            if (baseImageDownloader != null) builder.imageDownloader(baseImageDownloader);

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(builder.build());
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * clear cache by url
     *
     * @param url
     */
    public static void clearCacheByUrl(String url) {
        try {
            MemoryCacheUtils.removeFromCache(url, ImageLoader.getInstance().getMemoryCache());
            DiskCacheUtils.removeFromCache(url, ImageLoader.getInstance().getDiskCache());
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * get link image from drawable
     *
     * @param drawableId
     * @return
     */
    public static String getUrlFromDrawable(int drawableId) {
        try {
            return "drawable://" + drawableId;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * display image view
     *
     * @param imageUri
     * @param imageView
     * @param drawableId
     */
    public static void displayImage(String imageUri, ImageView imageView, int drawableId) {
        try {
            ImageLoader.getInstance().displayImage(imageUri, imageView, ImageLoaderUtils.getDisplayImageOption(drawableId));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param imageUri
     * @param imageView
     */
    public static void displayImage(String imageUri, ImageView imageView) {
        try {
            ImageLoader.getInstance().displayImage(imageUri, imageView);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param imageUri
     * @param imageView
     * @param imageLoadingListener
     */
    public static void displayImage(String imageUri, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        try {
            ImageLoader.getInstance().displayImage(imageUri, imageView, imageLoadingListener);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param imageUri
     * @param imageView
     * @param imageOptions
     * @param imageLoadingListener
     */
    public static void displayImage(String imageUri, ImageView imageView, DisplayImageOptions imageOptions, ImageLoadingListener imageLoadingListener) {
        try {
            ImageLoader.getInstance().displayImage(imageUri, imageView, imageOptions, imageLoadingListener);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
