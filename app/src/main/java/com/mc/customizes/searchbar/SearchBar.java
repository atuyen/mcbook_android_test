package com.mc.customizes.searchbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.interfaces.Optional;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java8.util.function.Consumer;

public class SearchBar extends LinearLayout {

    @BindView(R.id.imgLeft)
    ImageView imgLeft;
    @BindView(R.id.edtSearch)
    ExtEditText edtSearch;
    @BindView(R.id.imgRight)
    ImageView imgRight;
    @BindView(R.id.llSearchBar)
    LinearLayout linearLayout;
    Unbinder unbinder;

    public SearchBar(Context context) {
        super(context);
        initView(context, null);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_bar, this);
        unbinder = ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchBar);
        edtSearch.setText(typedArray.getString(R.styleable.SearchBar_searchBarContent));
        edtSearch.setHint(typedArray.getString(R.styleable.SearchBar_searchBarHint));

        Drawable drawableLeft = typedArray.getDrawable(R.styleable.SearchBar_searchBarIconLeft);
        Optional.from(drawableLeft).doIfPresent(icon -> {
            imgLeft.setImageDrawable(drawableLeft);
            imgLeft.setVisibility(VISIBLE);
        });

        Drawable bgDrawble = typedArray.getDrawable(R.styleable.SearchBar_bgColorSearchBar);
        Optional.from(bgDrawble).doIfPresent(bg -> {
            setColorBgSearchBar(bgDrawble);
        });

        int colorSearchHint = typedArray.getColor(R.styleable.SearchBar_colorSearchHint, getResources().getColor(R.color.colorTextHint));
        setColorSearchHint(colorSearchHint);

        Drawable drawableRight = typedArray.getDrawable(R.styleable.SearchBar_searchBarIconRight);
        Optional.from(drawableRight).doIfPresent(icon -> {
            imgRight.setImageDrawable(drawableRight);
            imgRight.setVisibility(VISIBLE);
        });

        boolean isHideIconLeft = typedArray.getBoolean(R.styleable.SearchBar_hideLeftIcon, false);
        imgLeft.setVisibility(isHideIconLeft ? GONE : VISIBLE);

        boolean isHideIconRight = typedArray.getBoolean(R.styleable.SearchBar_hideRightIcon, true);
        imgRight.setVisibility(isHideIconRight ? GONE : VISIBLE);

        typedArray.recycle();
    }

    public SearchBar onSearch(Consumer<String> keyword) {
        edtSearch.setOnEditorActionListener((textView, actionId, event) -> {
            if ((actionId == EditorInfo.IME_ACTION_SEARCH) ||
                    ((!event.isShiftPressed()) &&
                            (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) &&
                            (event.getAction() == KeyEvent.ACTION_DOWN))) {

                keyword.accept(textView.getText().toString().trim());
                return true;
            }
            return false;
        });
        return this;
    }

    public SearchBar onSearchOffline(Consumer<String> keyword) {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (keyword != null)
                    keyword.accept(s.toString().toLowerCase().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return this;
    }

    public void clear() {
        edtSearch.setText("");
    }

    public void setColorSearchHint(int colorSearchHint) {
        edtSearch.setHintTextColor(colorSearchHint);
    }

    public void setColorBgSearchBar(Drawable drawableColor) {
        linearLayout.setBackground(drawableColor);
    }

    public SearchBar onClickQrCode(OnClickListener onClickListener) {
        imgRight.setOnClickListener(onClickListener);
        return this;
    }

    public SearchBar setHint(String text) {
        edtSearch.setHint(text);
        return this;
    }

    public void setImgRight(ImageView imgRight) {
        this.imgRight = imgRight;
    }

    public ImageView getImgRight() {
        return imgRight;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);
    }
}
