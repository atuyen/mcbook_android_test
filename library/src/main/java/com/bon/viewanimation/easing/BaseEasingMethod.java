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

package com.bon.viewanimation.easing;

import com.nineoldandroids.animation.TypeEvaluator;

import java.util.ArrayList;

public abstract class BaseEasingMethod implements TypeEvaluator<Number> {
    protected float mDuration;
    private ArrayList<EasingListener> mListeners = new ArrayList<EasingListener>();

    public interface EasingListener {
        /**
         * @param time
         * @param value
         * @param start
         * @param end
         * @param duration
         */
        public void on(float time, float value, float start, float end, float duration);
    }

    public BaseEasingMethod(float duration) {
        mDuration = duration;
    }

    public BaseEasingMethod addEasingListener(EasingListener easingListener) {
        mListeners.add(easingListener);
        return this;
    }

    public BaseEasingMethod addEasingListeners(EasingListener... easingListeners) {
        for (EasingListener easingListener : easingListeners) {
            mListeners.add(easingListener);
        }
        return this;
    }

    public BaseEasingMethod removeEasingListener(EasingListener easingListener) {
        mListeners.remove(easingListener);
        return this;
    }

    public BaseEasingMethod clearEasingListeners() {
        mListeners.clear();
        return this;
    }


    public BaseEasingMethod setDuration(float duration) {
        mDuration = duration;
        return this;
    }

    @Override
    public final Float evaluate(float fraction, Number startValue, Number endValue) {
        try {
            float time = mDuration * fraction;
            float start = startValue.floatValue();
            float end = endValue.floatValue() - startValue.floatValue();
            float duration = mDuration;
            float value = calculate(time, start, end, duration);

            for (EasingListener easingListener : mListeners) {
                easingListener.on(time, value, start, end, duration);
            }

            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0f;
    }

    /**
     * @param time
     * @param start
     * @param end
     * @param duration
     * @return
     */
    public abstract Float calculate(float time, float start, float end, float duration);
}
