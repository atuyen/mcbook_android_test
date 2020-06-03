package com.mc.customizes.menus;

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

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItemMenu extends LinearLayout {
    private static final String TAG = ItemMenu.class.getSimpleName();

    @BindView(R.id.ivImage)
    AppCompatImageView ivImage;
    @BindView(R.id.tvContent)
    ExtTextView tvContent;

    Unbinder unbinder;

    public ItemMenu(Context context) {
        super(context);
        initView(context, null);
    }

    public ItemMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ItemMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, this);
        unbinder = ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemMenu);

        Drawable drawable = typedArray.getDrawable(R.styleable.ItemMenu_menuItemIcon);
        Optional.from(drawable).doIfPresent(icon -> ivImage.setImageDrawable(icon));

        String content = typedArray.getString(R.styleable.ItemMenu_menuItemText);
        tvContent.setText(content);

        // recycle
        typedArray.recycle();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);
    }
}
