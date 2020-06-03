package com.bon.fragment.animation;

import android.support.annotation.IntDef;
import android.view.animation.Transformation;

import com.bon.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 3D Push/Pull Animation
 * Created by Dang on 5/27/2016.
 */
public class PushPullAnimation extends ViewPropertyAnimation {
    private static final String TAG = PushPullAnimation.class.getSimpleName();

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
    public static PushPullAnimation create(@Direction int direction, boolean enter, long duration) {
        try {
            switch (direction) {
                case UP:
                case DOWN:
                    return new VerticalPushPullAnimation(direction, enter, duration);
                case LEFT:
                case RIGHT:
                    return new HorizontalPushPullAnimation(direction, enter, duration);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    private PushPullAnimation(@Direction int direction, boolean enter, long duration) {
        try {
            mDirection = direction;
            mEnter = enter;
            setDuration(duration);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private static class VerticalPushPullAnimation extends PushPullAnimation {
        private VerticalPushPullAnimation(@Direction int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            try {
                mPivotX = width * 0.5f;
                mPivotY = (mEnter == (mDirection == DOWN)) ? 0.0f : height;
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            try {
                float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
                if (mDirection == UP) value *= -1.0f;
                mRotationX = value * 90.0f;
                mAlpha = mEnter ? interpolatedTime : (1.0f - interpolatedTime);

                super.applyTransformation(interpolatedTime, t);
                applyTransformation(t);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }

    private static class HorizontalPushPullAnimation extends PushPullAnimation {
        private HorizontalPushPullAnimation(@Direction int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            try {
                mPivotX = (mEnter == (mDirection == RIGHT)) ? 0.0f : width;
                mPivotY = height * 0.5f;
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            try {
                float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
                if (mDirection == LEFT) value *= -1.0f;
                mRotationY = -value * 90.0f;
                mAlpha = mEnter ? interpolatedTime : (1.0f - interpolatedTime);

                super.applyTransformation(interpolatedTime, t);
                applyTransformation(t);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }
}
