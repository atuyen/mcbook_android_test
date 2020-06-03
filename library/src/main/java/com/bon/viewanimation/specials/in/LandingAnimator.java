package com.bon.viewanimation.specials.in;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.bon.viewanimation.BaseViewAnimator;
import com.bon.viewanimation.easing.Glider;
import com.bon.viewanimation.easing.Skill;

public class LandingAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        try {
            getAnimatorAgent().playTogether(
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleX", 1.5f, 1f)),
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleY", 1.5f, 1f)),
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "alpha", 0, 1f))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
