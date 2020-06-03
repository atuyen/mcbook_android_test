package com.mc.customizes.gifts;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.listview.ExtListView;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;

public class ExpandableCardView extends LinearLayout {
    private String title;
    private String titleColor;

    private View innerView;
    private LinearLayout containerView;
    private ExtListView lvItems;

    private ImageView arrowBtn;
    private ImageView headerIcon;
    private ExtTextView textViewTitle;

    private TypedArray typedArray;
    private int innerViewRes;
    private Drawable iconDrawable;

    private CardView card;

    public static final int DEFAULT_ANIM_DURATION = 350;
    private long animDuration = DEFAULT_ANIM_DURATION;

    public final static int COLLAPSING = 0;
    public final static int EXPANDING = 1;

    private boolean isExpanded = false;
    private boolean isExpanding = false;
    private boolean isCollapsing = false;
    private boolean expandOnClick = false;

    private int previousHeight = 0;

    private boolean isUseListView = false;
    private OnExpandedListener listener;

    private OnClickListener defaultClickListener = v -> {
        if (isExpanded()) collapse();
        else expand();
    };

    public ExpandableCardView(Context context) {
        super(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttributes(context, attrs);
        initView(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttributes(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expandable_cardview, this);
        arrowBtn = findViewById(R.id.arrow);
        textViewTitle = findViewById(R.id.title);
        headerIcon = findViewById(R.id.icon);

        if (!TextUtils.isEmpty(title)) textViewTitle.setText(title);
        if (!TextUtils.isEmpty(titleColor))
            textViewTitle.setTextColor(Color.parseColor(titleColor));

        if (iconDrawable != null) {
            headerIcon.setVisibility(VISIBLE);
            headerIcon.setBackground(iconDrawable);
        }

        card = findViewById(R.id.card);

        setInnerView(innerViewRes);

        containerView = findViewById(R.id.viewContainer);
        lvItems = findViewById(R.id.lvItems);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(convertDpToPixels(getContext(), 4));
        }

        if (expandOnClick) {
            card.setOnClickListener(defaultClickListener);
            arrowBtn.setOnClickListener(defaultClickListener);
        }
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView);
        title = typedArray.getString(R.styleable.ExpandableCardView_title);
        titleColor = typedArray.getString(R.styleable.ExpandableCardView_titleColor);
        iconDrawable = typedArray.getDrawable(R.styleable.ExpandableCardView_icon);
        innerViewRes = typedArray.getResourceId(R.styleable.ExpandableCardView_inner_view, View.NO_ID);
        expandOnClick = typedArray.getBoolean(R.styleable.ExpandableCardView_expandOnClick, false);
        animDuration = typedArray.getInteger(R.styleable.ExpandableCardView_animationDuration, DEFAULT_ANIM_DURATION);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        arrowBtn = findViewById(R.id.arrow);
//        textViewTitle = findViewById(R.id.title);
//        headerIcon = findViewById(R.id.icon);
//
//        if (!TextUtils.isEmpty(title)) textViewTitle.setText(title);
//        if (!TextUtils.isEmpty(titleColor))
//            textViewTitle.setTextColor(Color.parseColor(titleColor));
//
//        if (iconDrawable != null) {
//            headerIcon.setVisibility(VISIBLE);
//            headerIcon.setBackground(iconDrawable);
//        }
//
//        card = findViewById(R.id.card);
//
//        setInnerView(innerViewRes);
//
//        containerView = findViewById(R.id.viewContainer);
//        lvItems = findViewById(R.id.lvItems);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setElevation(convertDpToPixels(getContext(), 4));
//        }
//
//        if (expandOnClick) {
//            card.setOnClickListener(defaultClickListener);
//            arrowBtn.setOnClickListener(defaultClickListener);
//        }
    }

    public static float convertDpToPixels(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void expand() {
        if (isUseListView) {
            animateViewsListView(EXPANDING);
        } else {
            final int initialHeight = card.getHeight();
            if (!isMoving()) {
                previousHeight = initialHeight;
            }

            card.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            int targetHeight = card.getMeasuredHeight();

            if (targetHeight - initialHeight != 0) {
                animateViews(initialHeight,
                        targetHeight - initialHeight,
                        EXPANDING);
            }
        }
    }

    public void collapse() {
        if (isUseListView) {
            animateViewsListView(COLLAPSING);
        } else {
            int initialHeight = card.getMeasuredHeight();

            if (initialHeight - previousHeight != 0) {
                animateViews(initialHeight,
                        initialHeight - previousHeight,
                        COLLAPSING);
            }
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void runAnimationArrow(final int animationType) {
        RotateAnimation arrowAnimation = animationType == EXPANDING ?
                new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f) :
                new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
        arrowAnimation.setFillAfter(true);
        arrowAnimation.setDuration(animDuration);
        arrowBtn.startAnimation(arrowAnimation);
    }

    private void animateViewsListView(final int animationType) {
        runAnimationArrow(animationType);

        isExpanding = animationType == EXPANDING;
        isCollapsing = animationType == COLLAPSING;
        isExpanded = animationType == EXPANDING;

        if (listener != null) {
            if (animationType == EXPANDING) {
                listener.onExpandChanged(card, true);
            } else {
                listener.onExpandChanged(card, false);
            }
        }
    }

    private void animateViews(final int initialHeight, final int distance, final int animationType) {
        Animation expandAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    //Setting isExpanding/isCollapsing to false
                    isExpanding = false;
                    isCollapsing = false;

                    if (listener != null) {
                        if (animationType == EXPANDING) {
                            listener.onExpandChanged(card, true);
                        } else {
                            listener.onExpandChanged(card, false);
                        }
                    }
                }

                card.getLayoutParams().height = animationType == EXPANDING ? (int) (initialHeight + (distance * interpolatedTime)) :
                        (int) (initialHeight - (distance * interpolatedTime));
                card.findViewById(R.id.viewContainer).requestLayout();

                containerView.getLayoutParams().height = animationType == EXPANDING ? (int) (initialHeight + (distance * interpolatedTime)) :
                        (int) (initialHeight - (distance * interpolatedTime));

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };


        expandAnimation.setDuration(animDuration);

        isExpanding = animationType == EXPANDING;
        isCollapsing = animationType == COLLAPSING;

        startAnimation(expandAnimation);
        Log.d("SO", "Started animation: " + (animationType == EXPANDING ? "Expanding" : "Collapsing"));
        runAnimationArrow(animationType);
        isExpanded = animationType == EXPANDING;
    }

    private boolean isExpanding() {
        return isExpanding;
    }

    private boolean isCollapsing() {
        return isCollapsing;
    }

    private boolean isMoving() {
        return isExpanding() || isCollapsing();
    }

    public void setOnExpandedListener(OnExpandedListener listener) {
        this.listener = listener;
    }

    public void removeOnExpandedListener() {
        this.listener = null;
    }

    public void setTitle(String title) {
        if (textViewTitle != null) textViewTitle.setText(title);
    }

    public void setTitle(int resId) {
        if (textViewTitle != null) textViewTitle.setText(resId);
    }

    public void setTitleColor(int color) {
        if (textViewTitle != null) textViewTitle.setTextColor(color);
    }

    public void setSingleLine() {
        if (textViewTitle != null){
            textViewTitle.setMaxLines(1);
            textViewTitle.setSingleLine(true);
        }
    }

    public void setTypeFace(int typeFace) {
        textViewTitle.setTypeface(null, typeFace);
    }

//    public void

    public void setIcon(Drawable drawable) {
        if (headerIcon != null) {
            headerIcon.setBackground(drawable);
            iconDrawable = drawable;
        }
    }

    public void setIcon(int resId) {
        if (headerIcon != null) {
            iconDrawable = ContextCompat.getDrawable(getContext(), resId);
            headerIcon.setBackground(iconDrawable);
        }
    }

    private void setInnerView(int resId) {
        ViewStub stub = findViewById(R.id.viewStub);
        stub.setLayoutResource(resId);
        innerView = stub.inflate();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (arrowBtn != null) arrowBtn.setOnClickListener(l);
        super.setOnClickListener(l);
    }

    public long getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    public LinearLayout getContainerView() {
        return containerView;
    }

    public ExtListView getLvItems() {
        return lvItems;
    }

    public void setUseListView(boolean useListView) {
        isUseListView = useListView;
    }

    /**
     * Interfaces
     */

    public interface OnExpandedListener {
        void onExpandChanged(View v, boolean isExpanded);
    }
}


