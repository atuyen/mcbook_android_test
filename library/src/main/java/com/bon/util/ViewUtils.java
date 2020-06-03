package com.bon.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.bon.logger.Logger;

public class ViewUtils {
    private static final String TAG = ViewUtils.class.getSimpleName();

    // alpha
    private static AlphaAnimation alpha = null;

    /**
     * invalidate view
     *
     * @param view
     */
    public static void invalidate(View view) {
        try {
            view.requestLayout();
            view.postInvalidate();
            view.refreshDrawableState();
            view.forceLayout();

            // If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    invalidate(innerView);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * set opacity for view
     *
     * @param view
     */
    public static void setOpacity(View view) {
        try {
            if (alpha != null) alpha.cancel();

            if (view.isEnabled()) {
                alpha = new AlphaAnimation(1.0F, 1.0F);
                // Make animation instant
                alpha.setDuration(0);
                // Tell it to persist after the
                alpha.setFillAfter(true);
                // animation
                view.startAnimation(alpha);
            } else {
                alpha = new AlphaAnimation(0.5F, 0.5F);
                // Make animation
                alpha.setDuration(System.currentTimeMillis());
                // Tell it to persist after the
                alpha.setFillAfter(false);
                // animation
                view.startAnimation(alpha);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
