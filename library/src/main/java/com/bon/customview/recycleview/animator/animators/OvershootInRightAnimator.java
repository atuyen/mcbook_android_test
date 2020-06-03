package com.bon.customview.recycleview.animator.animators;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import com.bon.logger.Logger;

public class OvershootInRightAnimator extends BaseItemAnimator {
    private static final String TAG = OvershootInRightAnimator.class.getSimpleName();
    private final float mTension;

    public OvershootInRightAnimator() {
        mTension = 2.0f;
    }

    public OvershootInRightAnimator(float mTension) {
        this.mTension = mTension;
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        try {
            ViewCompat.animate(holder.itemView)
                    .translationX(holder.itemView.getRootView().getWidth())
                    .setDuration(getRemoveDuration())
                    .setListener(new DefaultRemoveVpaListener(holder))
                    .setStartDelay(getRemoveDelay(holder))
                    .start();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        try {
            ViewCompat.setTranslationX(holder.itemView, holder.itemView.getRootView().getWidth());
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        try {
            ViewCompat.animate(holder.itemView)
                    .translationX(0)
                    .setDuration(getAddDuration())
                    .setInterpolator(new OvershootInterpolator(mTension))
                    .setListener(new DefaultAddVpaListener(holder))
                    .setStartDelay(getAddDelay(holder))
                    .start();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
