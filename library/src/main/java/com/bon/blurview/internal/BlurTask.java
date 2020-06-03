package com.bon.blurview.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dang on 7/8/2016.
 */
public class BlurTask {
    public interface Callback {
        void done(BitmapDrawable drawable);
    }

    private Resources resources;
    private WeakReference<Context> weakReference;
    private BlurFactor blurFactor;
    private Bitmap bitmap;
    private Callback callback;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public BlurTask(View target, BlurFactor blurFactor, Callback callback) {
        try {
            target.setDrawingCacheEnabled(true);
            this.resources = target.getResources();
            this.blurFactor = blurFactor;
            this.callback = callback;

            target.destroyDrawingCache();
            target.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
            this.bitmap = target.getDrawingCache();
            this.weakReference = new WeakReference<>(target.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        try {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Context context = weakReference.get();
                        final BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, Blur.of(context, bitmap, blurFactor));
                        if (callback != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.done(bitmapDrawable);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}