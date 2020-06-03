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

import com.bon.viewanimation.easing.back.BackEaseIn;
import com.bon.viewanimation.easing.back.BackEaseInOut;
import com.bon.viewanimation.easing.back.BackEaseOut;
import com.bon.viewanimation.easing.bounce.BounceEaseIn;
import com.bon.viewanimation.easing.bounce.BounceEaseInOut;
import com.bon.viewanimation.easing.bounce.BounceEaseOut;
import com.bon.viewanimation.easing.circ.CircEaseIn;
import com.bon.viewanimation.easing.circ.CircEaseInOut;
import com.bon.viewanimation.easing.circ.CircEaseOut;
import com.bon.viewanimation.easing.cubic.CubicEaseIn;
import com.bon.viewanimation.easing.cubic.CubicEaseInOut;
import com.bon.viewanimation.easing.cubic.CubicEaseOut;
import com.bon.viewanimation.easing.elastic.ElasticEaseIn;
import com.bon.viewanimation.easing.elastic.ElasticEaseOut;
import com.bon.viewanimation.easing.expo.ExpoEaseIn;
import com.bon.viewanimation.easing.expo.ExpoEaseInOut;
import com.bon.viewanimation.easing.expo.ExpoEaseOut;
import com.bon.viewanimation.easing.quad.QuadEaseIn;
import com.bon.viewanimation.easing.quad.QuadEaseInOut;
import com.bon.viewanimation.easing.quad.QuadEaseOut;
import com.bon.viewanimation.easing.quint.QuintEaseIn;
import com.bon.viewanimation.easing.quint.QuintEaseInOut;
import com.bon.viewanimation.easing.quint.QuintEaseOut;
import com.bon.viewanimation.easing.sine.SineEaseIn;
import com.bon.viewanimation.easing.sine.SineEaseInOut;
import com.bon.viewanimation.easing.sine.SineEaseOut;
import com.bon.viewanimation.easing.linear.Linear;

public enum  Skill {

    BackEaseIn(BackEaseIn.class),
    BackEaseOut(BackEaseOut.class),
    BackEaseInOut(BackEaseInOut.class),

    BounceEaseIn(BounceEaseIn.class),
    BounceEaseOut(BounceEaseOut.class),
    BounceEaseInOut(BounceEaseInOut.class),

    CircEaseIn(CircEaseIn.class),
    CircEaseOut(CircEaseOut.class),
    CircEaseInOut(CircEaseInOut.class),

    CubicEaseIn(CubicEaseIn.class),
    CubicEaseOut(CubicEaseOut.class),
    CubicEaseInOut(CubicEaseInOut.class),

    ElasticEaseIn(ElasticEaseIn.class),
    ElasticEaseOut(ElasticEaseOut.class),

    ExpoEaseIn(ExpoEaseIn.class),
    ExpoEaseOut(ExpoEaseOut.class),
    ExpoEaseInOut(ExpoEaseInOut.class),

    QuadEaseIn(QuadEaseIn.class),
    QuadEaseOut(QuadEaseOut.class),
    QuadEaseInOut(QuadEaseInOut.class),

    QuintEaseIn(QuintEaseIn.class),
    QuintEaseOut(QuintEaseOut.class),
    QuintEaseInOut(QuintEaseInOut.class),

    SineEaseIn(SineEaseIn.class),
    SineEaseOut(SineEaseOut.class),
    SineEaseInOut(SineEaseInOut.class),

    Linear(Linear.class);

    private Class easingMethod;

    Skill(Class clazz) {
        easingMethod = clazz;
    }

    public BaseEasingMethod getMethod(float duration) {
        try {
            return (BaseEasingMethod)easingMethod.getConstructor(float.class).newInstance(duration);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Can not init easingMethod instance");
        }
    }
}
