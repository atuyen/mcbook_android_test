package com.bon.fragment.transformer;

import android.graphics.Camera;
import android.graphics.Matrix;

import com.bon.logger.Logger;

/**
 * Created by Dang on 5/27/2016.
 */
@SuppressWarnings("ALL")
public abstract class PropertyTransformer implements AnimationTransformer {
    private static final String TAG = PropertyTransformer.class.getSimpleName();

    protected final float mPivotX = 0.0f;
    protected final float mPivotY = 0.0f;
    protected final float mScaleX = 1.0f;
    protected final float mScaleY = 1.0f;
    protected final float mRotationX = 0.0f;
    protected final float mRotationY = 0.0f;
    protected final float mRotationZ = 0.0f;
    protected final float mTranslationX = 0.0f;
    protected final float mTranslationY = 0.0f;
    protected final float mTranslationZ = 0.0f;
    private final Camera mCamera = new Camera();
    private final Matrix mMatrix = new Matrix();
    private final float mFromAlpha = -1.0f;
    private final float mToAlpha = -1.0f;
    protected int mWidth = 0;
    protected int mHeight = 0;
    protected float mAlpha = 1.0f;

    @Override
    public void onInitialize(int width, int height, int parentWidth, int parentHeight) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public Matrix getTransformationMatrix(float interpolatedTime) {
        try {
            if (mFromAlpha >= 0 && mToAlpha >= 0) {
                mAlpha = mFromAlpha + (mToAlpha - mFromAlpha) * interpolatedTime;
            }
            return calcTransformationMatrix();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    @Override
    public float getTransformationAlpha() {
        return mAlpha;
    }

    private Matrix calcTransformationMatrix() {
        try {
            final Matrix m = mMatrix;
            m.reset();

            final float w = mWidth;
            final float h = mHeight;
            final float pX = mPivotX;
            final float pY = mPivotY;

            final float rX = mRotationX;
            final float rY = mRotationY;
            final float rZ = mRotationZ;
            final float tZ = mTranslationZ;

            if ((rX != 0) || (rY != 0) || (rZ != 0) || (tZ != 0)) {
                final Camera camera = mCamera;
                camera.save();
                camera.translate(0, 0, tZ);
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

            return m;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }
}