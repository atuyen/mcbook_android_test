package com.bon.fragment.transformer;

import android.graphics.Matrix;

/**
 * Created by Dang on 5/27/2016.
 */
@SuppressWarnings("ALL")
public interface AnimationTransformer {
    void onInitialize(int width, int height, int parentWidth, int parentHeight);

    Matrix getTransformationMatrix(float interpolatedTime);

    float getTransformationAlpha();
}
