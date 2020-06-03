package com.mc.customizes.bottombars;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bon.interfaces.Optional;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItemBottomBar extends LinearLayout {
    private static final String TAG = ItemBottomBar.class.getSimpleName();

    @BindView(R.id.ivImage)
    AppCompatImageView ivImage;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tv_count_notifycation)
    TextView tvCountNotifycation;

    // drawable
    Drawable drawableIconNormal, drawableIconActive;

    Unbinder unbinder;

    public ItemBottomBar(Context context) {
        super(context);
        initView(context, null);
    }

    public ItemBottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ItemBottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemBottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_bar_item, this);
        unbinder = ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemBottomBar);

        // active
        boolean isActive = typedArray.getBoolean(R.styleable.ItemBottomBar_bottomBarActive, false);

        //badge notifycation
        boolean isShowBadge = typedArray.getBoolean(R.styleable.ItemBottomBar_isShowBadge, false);
        setBadgeActive(isShowBadge);

        // icon
        drawableIconNormal = typedArray.getDrawable(R.styleable.ItemBottomBar_bottomBarIconNormal);
        drawableIconActive = typedArray.getDrawable(R.styleable.ItemBottomBar_bottomBarIconActive);

        // set active mode
        setActiveMode(isActive);

        // recycle
        typedArray.recycle();
    }

    public View getImageView() {
        return ivImage;
    }

    /**
     * @param isActive
     */
    public void setActiveMode(boolean isActive) {
        vLine.setVisibility(isActive ? VISIBLE : INVISIBLE);
        ivImage.setImageDrawable(isActive ? drawableIconActive : drawableIconNormal);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);
    }

    public void setBadgeActive(boolean isShow) {
        tvCountNotifycation.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    public void setCountNotifycation(int count) {
        tvCountNotifycation.setText(String.valueOf(count));
    }
}
