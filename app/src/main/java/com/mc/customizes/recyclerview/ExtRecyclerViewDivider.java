package com.mc.customizes.recyclerview;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ExtRecyclerViewDivider extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int height;

    public ExtRecyclerViewDivider() {
    }

    public ExtRecyclerViewDivider(Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + (height == 0 ? mDivider.getIntrinsicHeight() : height);

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * @param drawable
     */
    public void setDivider(Drawable drawable) {
        this.mDivider = drawable;
    }

    /**
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
