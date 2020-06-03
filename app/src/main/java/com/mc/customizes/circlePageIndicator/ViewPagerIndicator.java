package com.mc.customizes.circlePageIndicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mc.books.R;

public class ViewPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {
    private Context mContext = null;
    private ViewPager mPager;
    @DrawableRes
    private int mSelectedDrawable = -1;
    @DrawableRes
    private int mDeselectedDrawable = -1;
    @Dimension
    private int mIndicatorSpacing = 5;
    private int mAnimationDuration = 150;
    private float mAnimScaleMultiplier = 1.5F;
    private boolean mAnimate = false;

    private DataSetObserver mDatasetObserver = new DataSetObserver() {
        public void onChanged() {
            super.onChanged();
            ViewPagerIndicator.this.initializeIndicatorBar(ViewPagerIndicator.this.mPager.getAdapter().getCount());
        }
    };

    public void setPager(ViewPager pager) {
        if (this.mPager != null) {
            this.mPager.removeOnPageChangeListener(this);
            this.mPager.removeOnAdapterChangeListener(this);
            this.mPager = null;
        }

        this.mPager = pager;
        this.initializeIndicatorBar(this.mPager.getAdapter().getCount());
        this.mPager.addOnPageChangeListener(this);
        this.mPager.addOnAdapterChangeListener(this);
        this.mPager.getAdapter().registerDataSetObserver(this.mDatasetObserver);
    }

    public ViewPagerIndicator(Context context) {
        super(context);
        this.initializeViews(context, (AttributeSet) null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initializeViews(context, attrs);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
            this.mSelectedDrawable = a.getResourceId(R.styleable.ViewPagerIndicator_selectedDrawable, -1);
            this.mDeselectedDrawable = a.getResourceId(R.styleable.ViewPagerIndicator_deselectedDrawable, -1);
            this.mIndicatorSpacing = (int) a.getDimension(R.styleable.ViewPagerIndicator_indicatorSpacing, 5.0F);
            this.mAnimate = a.getBoolean(R.styleable.ViewPagerIndicator_enableAnimation, false);
            this.mAnimationDuration = a.getInteger(R.styleable.ViewPagerIndicator_animationDurationPager, 150);
            this.mAnimScaleMultiplier = a.getFloat(R.styleable.ViewPagerIndicator_animationScale, 1.5F);
            a.recycle();
        }

    }

    @ColorInt
    private int getThemeColor(@NonNull Context context, @AttrRes int attributeColor) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, value, true);
        return value.data;
    }

    private void initializeIndicatorBar(int num) {
        this.removeAllViewsInLayout();
        for (int i = 0; i < num; ++i) {
            ImageView img = new ImageView(this.mContext);
            img.setTag(i);
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.setMargins(this.mIndicatorSpacing / 2, 0, this.mIndicatorSpacing / 2, 0);
            lp.gravity = 17;
            this.addView(img, lp);
        }

        this.setSelectedIndicator(this.mPager.getCurrentItem());
    }

    @SuppressLint("ResourceType")
    private void setSelectedIndicator(int selected) {
        int num = this.getChildCount();

        for (int i = 0; i < num; ++i) {
            ImageView img = (ImageView) this.getChildAt(i);
            if (this.mAnimate) {
                img.clearAnimation();
//                img.animate().scaleX(1.0F).scaleY(1.0F).setDuration((long) this.mAnimationDuration).start();
            }

            img.clearColorFilter();
            if (this.mDeselectedDrawable != -1) {
                img.setImageResource(this.mDeselectedDrawable);
            } else if (this.mSelectedDrawable != -1) {
                img.setImageResource(this.mSelectedDrawable);
            } else {
                img.setImageResource(R.drawable.circle_drawable);
            }
        }

        ImageView selectedView = (ImageView) this.getChildAt(selected);
        if (this.mAnimate) {
//            selectedView.animate().scaleX(this.mAnimScaleMultiplier).scaleY(this.mAnimScaleMultiplier).setDuration((long) this.mAnimationDuration).start();
        }

        if (this.mSelectedDrawable != -1) {
            selectedView.clearColorFilter();
            selectedView.setImageResource(this.mSelectedDrawable);
        } else {
        }

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        this.setSelectedIndicator(position);
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(this.mDatasetObserver);
        }

        if (newAdapter != null) {
            this.initializeIndicatorBar(newAdapter.getCount());
            newAdapter.registerDataSetObserver(this.mDatasetObserver);
        }

    }
}

