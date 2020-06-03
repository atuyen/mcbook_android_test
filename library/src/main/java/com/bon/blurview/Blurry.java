package com.bon.blurview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bon.blurview.internal.Blur;
import com.bon.blurview.internal.BlurFactor;
import com.bon.blurview.internal.BlurTask;
import com.bon.blurview.internal.Helper;

/**
 * Created by Dang on 7/8/2016.
 */
public class Blurry {
    private static final String TAG = Blurry.class.getSimpleName();

    public static Composer with(Context context) {
        return new Composer(context);
    }

    public static void delete(ViewGroup target) {
        View view = target.findViewWithTag(TAG);
        if (view != null) {
            target.removeView(view);
        }
    }

    public static class Composer {
        private View blurredView;
        private Context context;
        private BlurFactor factor;
        private boolean async;
        private boolean animate;
        private int duration = 300;
        private ImageComposer.ImageComposerListener listener;

        public Composer(Context context) {
            this.context = context;
            this.blurredView = new View(context);
            this.blurredView.setTag(TAG);
            this.factor = new BlurFactor();
        }

        public Composer radius(int radius) {
            this.factor.radius = radius;
            return this;
        }

        public Composer sampling(int sampling) {
            this.factor.sampling = sampling;
            return this;
        }

        public Composer color(int color) {
            this.factor.color = color;
            return this;
        }

        public Composer async() {
            this.async = true;
            return this;
        }

        public Composer async(ImageComposer.ImageComposerListener listener) {
            this.async = true;
            this.listener = listener;
            return this;
        }

        public Composer animate() {
            this.animate = true;
            return this;
        }

        public Composer animate(int duration) {
            this.animate = true;
            this.duration = duration;
            return this;
        }

        public ImageComposer capture(View capture) {
            return new ImageComposer(context, capture, factor, async, listener);
        }

        public void onto(final ViewGroup target) {
            try {
                this.factor.width = target.getMeasuredWidth();
                this.factor.height = target.getMeasuredHeight();

                if (this.async) {
                    BlurTask task = new BlurTask(target, this.factor, new BlurTask.Callback() {
                        @Override
                        public void done(BitmapDrawable drawable) {
                            addView(target, drawable);
                        }
                    });
                    task.execute();
                } else {
                    Drawable drawable = new BitmapDrawable(context.getResources(), Blur.of(target, this.factor));
                    addView(target, drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void addView(ViewGroup target, Drawable drawable) {
            try {
                Helper.setBackground(this.blurredView, drawable);
                target.addView(this.blurredView);

                if (this.animate) {
                    Helper.animate(this.blurredView, this.duration);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ImageComposer {
        private Context context;
        private View capture;
        private BlurFactor factor;
        private boolean async;
        private ImageComposerListener listener;

        public ImageComposer(Context context, View capture, BlurFactor factor, boolean async,
                             ImageComposerListener listener) {
            this.context = context;
            this.capture = capture;
            this.factor = factor;
            this.async = async;
            this.listener = listener;
        }

        public void into(final ImageView target) {
            try {
                this.factor.width = capture.getMeasuredWidth();
                this. factor.height = capture.getMeasuredHeight();

                if (async) {
                    BlurTask task = new BlurTask(capture, factor, new BlurTask.Callback() {
                        @Override
                        public void done(BitmapDrawable drawable) {
                            try {
                                if (listener == null) {
                                    target.setImageDrawable(drawable);
                                } else {
                                    listener.onImageReady(drawable);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    task.execute();
                } else {
                    Drawable drawable = new BitmapDrawable(this.context.getResources(), Blur.of(this.capture, this.factor));
                    target.setImageDrawable(drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public interface ImageComposerListener {
            void onImageReady(BitmapDrawable drawable);
        }
    }
}
