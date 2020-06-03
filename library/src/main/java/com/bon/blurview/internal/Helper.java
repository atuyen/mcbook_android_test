package com.bon.blurview.internal;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by Dang on 7/8/2016.
 */
@SuppressWarnings("ALL")
public class Helper {
    public static void setBackground(View view, Drawable drawable) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasZero(int... args) {
        try {
            for (int num : args) {
                if (num == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void animate(View view, int duration) {
        try {
            AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
            alpha.setDuration(duration);
            view.startAnimation(alpha);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
