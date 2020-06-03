package com.bon.customview.recycleview.animator.animators;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;

import com.bon.logger.Logger;

public class ScaleInLeftAnimator extends BaseItemAnimator {
    private static final String TAG = ScaleInLeftAnimator.class.getSimpleName();

    public ScaleInLeftAnimator() {
    }

    public ScaleInLeftAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    protected void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {
        try {
            ViewCompat.setPivotX(holder.itemView, 0);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        try {
            ViewCompat.animate(holder.itemView)
                    .scaleX(0)
                    .scaleY(0)
                    .setDuration(getRemoveDuration())
                    .setInterpolator(mInterpolator)
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
            ViewCompat.setPivotX(holder.itemView, 0);
            ViewCompat.setScaleX(holder.itemView, 0);
            ViewCompat.setScaleY(holder.itemView, 0);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        try {
            ViewCompat.animate(holder.itemView)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(getAddDuration())
                    .setInterpolator(mInterpolator)
                    .setListener(new DefaultAddVpaListener(holder))
                    .setStartDelay(getAddDelay(holder))
                    .start();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
