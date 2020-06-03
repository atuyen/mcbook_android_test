package com.bon.viewanimation.specials.out;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.bon.viewanimation.BaseViewAnimator;
import com.bon.viewanimation.easing.Glider;
import com.bon.viewanimation.easing.Skill;

public class TakingOffAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        try {
            getAnimatorAgent().playTogether(
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleX", 1f, 1.5f)),
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleY", 1f, 1.5f)),
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "alpha", 1, 0))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
