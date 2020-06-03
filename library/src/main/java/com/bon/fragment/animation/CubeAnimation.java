package com.bon.fragment.animation;

import android.support.annotation.IntDef;
import android.view.animation.Transformation;

import com.bon.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 3D Cube Animation
 * Created by Dang on 5/27/2016.
 */
public class CubeAnimation extends ViewPropertyAnimation {
    private static final String TAG = CubeAnimation.class.getSimpleName();

    @IntDef({UP, DOWN, LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Direction {
    }

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    @Direction
    protected int mDirection;
    protected boolean mEnter;

    /**
     * Create new Animation.
     *
     * @param direction Direction of animation
     * @param enter     true for Enter / false for Exit
     * @param duration  Duration of Animation
     * @return
     */
    public static CubeAnimation create(@Direction int direction, boolean enter, long duration) {
        try {
            switch (direction) {
                case UP:
                case DOWN:
                    return new VerticalCubeAnimation(direction, enter, duration);
                case LEFT:
                case RIGHT:
                    return new HorizontalCubeAnimation(direction, enter, duration);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    private CubeAnimation(@Direction int direction, boolean enter, long duration) {
        try {
            mDirection = direction;
            mEnter = enter;
            setDuration(duration);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private static class VerticalCubeAnimation extends CubeAnimation {
        private VerticalCubeAnimation(@Direction int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            try {
                mPivotX = width * 0.5f;
                mPivotY = (mEnter == (mDirection == UP)) ? 0.0f : height;
                mCameraZ = -height * 0.015f;
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            try {
                float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
                if (mDirection == DOWN) value *= -1.0f;
                mRotationX = value * 90.0f;
                mTranslationY = -value * mHeight;

                super.applyTransformation(interpolatedTime, t);
                applyTransformation(t);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }

    private static class HorizontalCubeAnimation extends CubeAnimation {
        private HorizontalCubeAnimation(@Direction int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            try {
                mPivotX = (mEnter == (mDirection == LEFT)) ? 0.0f : width;
                mPivotY = height * 0.5f;
                mCameraZ = -width * 0.015f;
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            try {
                float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
                if (mDirection == RIGHT) value *= -1.0f;
                mRotationY = -value * 90.0f;
                mTranslationX = -value * mWidth;

                super.applyTransformation(interpolatedTime, t);
                applyTransformation(t);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }
}