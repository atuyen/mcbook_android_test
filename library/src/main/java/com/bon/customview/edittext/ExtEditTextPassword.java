package com.bon.customview.edittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Dang Pham Phu on 2/3/2017.
 */

public class ExtEditTextPassword extends ExtEditText {
    private static final int EXTRA_TAP_AREA = 13;

    // drawable
    private Drawable drawableRight;
    private Drawable drawableRightVisible;
    private Drawable drawableRightHide;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;

    // action
    private int actionX, actionY;
    private boolean isHidePassword = true;

    // listener
    private DrawableClickListener clickListener;

    /**
     * @param context
     * @param attrs
     */
    public ExtEditTextPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExtEditTextPassword(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (left != null) this.drawableLeft = left;
        if (right != null) this.drawableRight = right;
        if (top != null) this.drawableTop = top;
        if (bottom != null) this.drawableBottom = bottom;
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.actionX = (int) event.getX();
            this.actionY = (int) event.getY();

            // onclick bottom
            if (this.drawableBottom != null && this.drawableBottom.getBounds().contains(actionX, actionY)) {
                this.clickListener.onClick(DrawableClickListener.DrawablePosition.BOTTOM);
                return super.onTouchEvent(event);
            }

            // onclick top
            if (this.drawableTop != null && this.drawableTop.getBounds().contains(actionX, actionY)) {
                this.clickListener.onClick(DrawableClickListener.DrawablePosition.TOP);
                return super.onTouchEvent(event);
            }

            // this works for left since container shares 0,0 origin with bounds
            if (this.drawableLeft != null) {
                bounds = this.drawableLeft.getBounds();
                int extraTapArea = (int) (EXTRA_TAP_AREA * getResources().getDisplayMetrics().density + 0.5);
                int x = actionX, y = actionY;

                if (!bounds.contains(actionX, actionY)) {
                    /** Gives the +20 area for tapping. */
                    x = actionX - extraTapArea;
                    y = actionY - extraTapArea;

                    if (x <= 0) x = actionX;
                    if (y <= 0) y = actionY;

                    /** Creates square from the smallest value */
                    if (x < y) y = x;
                }

                // onclick left
                if (bounds.contains(x, y) && clickListener != null) {
                    this.clickListener.onClick(DrawableClickListener.DrawablePosition.LEFT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
            }

            // this works for right since container shares 0,0 origin with bounds
            if (this.drawableRight != null) {
                bounds = this.drawableRight.getBounds();
                int extraTapArea = (int) (EXTRA_TAP_AREA * getResources().getDisplayMetrics().density + 0.5);

                /**
                 * IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                 * THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                 * CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWABLE
                 * BOUND. - this process help to increase the tap able area of
                 * the rectangle.
                 */
                int x = actionX + extraTapArea, y = actionY - extraTapArea;


                /**Since this is right drawable subtract the value of x from the width
                 * of view. so that width - tapped area will result in x co-ordinate in drawable bound.
                 */
                x = getWidth() - x;

                 /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if (x <= 0) x += extraTapArea;

                 /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extra tapping area value doesn't go into negative value.
                 */

                if (y <= 0) y = actionY;

                /**If drawable bounds contains the x and y points then move ahead.*/
                if (bounds.contains(x, y) && clickListener != null) {
                    this.clickListener.onClick(DrawableClickListener.DrawablePosition.RIGHT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    this.isHidePassword = isHidePassword ? false : true;
                    this.setCompoundDrawablesWithIntrinsicBounds(this.drawableLeft, this.drawableTop,
                            this.isHidePassword ? this.drawableRightVisible : this.drawableRightHide,
                            this.drawableBottom);
                    return false;
                }

                return super.onTouchEvent(event);
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        this.drawableRight = null;
        this.drawableBottom = null;
        this.drawableLeft = null;
        this.drawableTop = null;
        super.finalize();
    }

    /**
     * set click drawable listener
     *
     * @param listener
     */
    public void setDrawableClickListener(DrawableClickListener listener) {
        this.clickListener = listener;
    }

    public void setDrawableRightVisible(Drawable drawableRightVisible) {
        this.drawableRightVisible = drawableRightVisible;
    }

    public void setDrawableRightHide(Drawable drawableRightHide) {
        this.drawableRightHide = drawableRightHide;
    }

    public interface DrawableClickListener {
        enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}

        void onClick(DrawableClickListener.DrawablePosition target);
    }
}