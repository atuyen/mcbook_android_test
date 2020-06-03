package com.bon.customview.recycleview.animator.internal;

import android.support.v4.view.ViewCompat;
import android.view.View;

import com.bon.logger.Logger;

public final class ViewHelper {
    private static final String TAG = ViewHelper.class.getSimpleName();

    public static void clear(View v) {
        try {
            ViewCompat.setAlpha(v, 1);
            ViewCompat.setScaleY(v, 1);
            ViewCompat.setScaleX(v, 1);
            ViewCompat.setTranslationY(v, 0);
            ViewCompat.setTranslationX(v, 0);
            ViewCompat.setRotation(v, 0);
            ViewCompat.setRotationY(v, 0);
            ViewCompat.setRotationX(v, 0);
            ViewCompat.setPivotY(v, v.getMeasuredHeight() / 2);
            ViewCompat.setPivotX(v, v.getMeasuredWidth() / 2);
            ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
