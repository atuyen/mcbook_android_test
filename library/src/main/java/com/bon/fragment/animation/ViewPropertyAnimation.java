package com.bon.fragment.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.bon.logger.Logger;

/**
 * Created by Dang on 5/27/2016.
 */
@SuppressWarnings("ALL")
public class ViewPropertyAnimation extends Animation {
    private static final String TAG = ViewPropertyAnimation.class.getSimpleName();

    protected final float mScaleX = 1.0f;
    protected final float mScaleY = 1.0f;
    protected final float mRotationZ = 0.0f;
    protected final float mCameraX = 0.0f;
    protected final float mCameraY = 0.0f;
    private final Camera mCamera = new Camera();
    protected int mWidth = 0;
    protected int mHeight = 0;
    protected float mAlpha = 1.0f;
    protected float mPivotX = 0.0f;
    protected float mPivotY = 0.0f;
    protected float mRotationX = 0.0f;
    protected float mRotationY = 0.0f;
    protected float mTranslationX = 0.0f;
    protected float mTranslationY = 0.0f;
    protected float mTranslationZ = 0.0f;
    protected float mCameraZ = -8.0f;

    private float mFromAlpha = -1.0f;
    private float mToAlpha = -1.0f;

    public ViewPropertyAnimation fading(@FloatRange(from = 0.0f, to = 1.0f) float fromAlpha, @FloatRange(from = 0.0f, to = 1.0f) float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        return this;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mWidth = width;
        mHeight = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        try {
            if (mFromAlpha >= 0 && mToAlpha >= 0) {
                mAlpha = mFromAlpha + (mToAlpha - mFromAlpha) * interpolatedTime;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    protected void applyTransformation(Transformation t) {
        try {
            final Matrix m = t.getMatrix();
            final float w = mWidth;
            final float h = mHeight;
            final float pX = mPivotX;
            final float pY = mPivotY;

            final float rX = mRotationX;
            final float rY = mRotationY;
            final float rZ = mRotationZ;

            if ((rX != 0) || (rY != 0) || (rZ != 0)) {
                final Camera camera = mCamera;
                camera.save();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    camera.setLocation(mCameraX, mCameraY, mCameraZ);
                }

                if (mTranslationZ != 0) {
                    camera.translate(0, 0, mTranslationZ);
                }

                camera.rotateX(rX);
                camera.rotateY(rY);
                camera.rotateZ(-rZ);
                camera.getMatrix(m);
                camera.restore();
                m.preTranslate(-pX, -pY);
                m.postTranslate(pX, pY);
            }

            final float sX = mScaleX;
            final float sY = mScaleY;
            if ((sX != 1.0f) || (sY != 1.0f)) {
                m.postScale(sX, sY);
                final float sPX = -(pX / w) * ((sX * w) - w);
                final float sPY = -(pY / h) * ((sY * h) - h);
                m.postTranslate(sPX, sPY);
            }

            m.postTranslate(mTranslationX, mTranslationY);
            t.setAlpha(mAlpha);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}