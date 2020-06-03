package com.bon.customview.recycleview.animator.animators;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;

import com.bon.logger.Logger;

public class ScaleInTopAnimator extends BaseItemAnimator {
    private static final String TAG = ScaleInTopAnimator.class.getSimpleName();

    public ScaleInTopAnimator() {
    }

    public ScaleInTopAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    protected void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {
        try {
            // @TODO https://code.google.com/p/android/issues/detail?id=80863
            //        ViewCompat.setPivotY(holder.itemView, 0);
            holder.itemView.setPivotY(0);
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
            // @TODO https://code.google.com/p/android/issues/detail?id=80863
            //        ViewCompat.setPivotY(holder.itemView, 0);
            holder.itemView.setPivotY(0);
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
