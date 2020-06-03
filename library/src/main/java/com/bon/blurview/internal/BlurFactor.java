package com.bon.blurview.internal;

import android.graphics.Color;

/**
 * Created by Dang on 7/8/2016.
 */
public class BlurFactor {
    public static final int DEFAULT_RADIUS = 25;
    public static final int DEFAULT_SAMPLING = 1;
    public static final int MIN_RADIUS =1;
    public static final int ONE =1;

    public int width;
    public int height;

    public int radius = DEFAULT_RADIUS;
    public int sampling = DEFAULT_SAMPLING;
    public int color = Color.TRANSPARENT;
}
