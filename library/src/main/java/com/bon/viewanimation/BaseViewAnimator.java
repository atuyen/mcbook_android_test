/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 daimajia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bon.viewanimation;

import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.view.ViewHelper;

public abstract class BaseViewAnimator {
    public static final long DURATION = 1000;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private long mDuration = DURATION;

    protected abstract void prepare(View target);

    /**
     * @param target
     * @return
     */
    public BaseViewAnimator setTarget(View target) {
        reset(target);
        prepare(target);
        return this;
    }

    /**
     * start animation
     */
    public void animate() {
        start();
    }

    /**
     * reset the view to default status
     *
     * @param target
     */
    public void reset(View target) {
        ViewHelper.setAlpha(target, 1);
        ViewHelper.setScaleX(target, 1);
        ViewHelper.setScaleY(target, 1);
        ViewHelper.setTranslationX(target, 0);
        ViewHelper.setTranslationY(target, 0);
        ViewHelper.setRotation(target, 0);
        ViewHelper.setRotationY(target, 0);
        ViewHelper.setRotationX(target, 0);
        ViewHelper.setPivotX(target, target.getMeasuredWidth() / 2.0f);
        ViewHelper.setPivotY(target, target.getMeasuredHeight() / 2.0f);
    }

    /**
     * start to animate
     */
    public void start() {
        try {
            mAnimatorSet.setDuration(mDuration);
            mAnimatorSet.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param duration
     * @return
     */
    public BaseViewAnimator setDuration(long duration) {
        mDuration = duration;
        return this;
    }

    /**
     * @param delay
     * @return
     */
    public BaseViewAnimator setStartDelay(long delay) {
        getAnimatorAgent().setStartDelay(delay);
        return this;
    }

    public long getStartDelay() {
        return mAnimatorSet.getStartDelay();
    }

    /**
     * @param animatorListener
     * @return
     */
    public BaseViewAnimator addAnimatorListener(AnimatorListener animatorListener) {
        mAnimatorSet.addListener(animatorListener);
        return this;
    }

    /**
     * cancel animation
     */
    public void cancel() {
        mAnimatorSet.cancel();
    }

    /**
     * check animation state
     *
     * @return
     */
    public boolean isRunning() {
        return mAnimatorSet.isRunning();
    }

    public boolean isStarted() {
        return mAnimatorSet.isStarted();
    }

    /**
     * @param animatorListener
     */
    public void removeAnimatorListener(AnimatorListener animatorListener) {
        mAnimatorSet.removeListener(animatorListener);
    }

    public void removeAllListener() {
        mAnimatorSet.removeAllListeners();
    }

    /**
     * @param interpolator
     * @return
     */
    public BaseViewAnimator setInterpolator(Interpolator interpolator) {
        mAnimatorSet.setInterpolator(interpolator);
        return this;
    }

    public long getDuration() {
        return mDuration;
    }

    public AnimatorSet getAnimatorAgent() {
        return mAnimatorSet;
    }
}
