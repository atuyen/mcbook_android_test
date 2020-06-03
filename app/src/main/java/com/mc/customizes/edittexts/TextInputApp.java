package com.mc.customizes.edittexts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.StringUtils;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TextInputApp extends LinearLayout {
    @BindView(R.id.tvLabel)
    ExtTextView tvLabel;
    @BindView(R.id.tvError)
    ExtTextView tvError;
    @BindView(R.id.etContent)
    ExtEditText etContent;
    @BindView(R.id.tvContent)
    ExtTextView tvContent;
    @BindView(R.id.ivIcon)
    AppCompatImageView ivIcon;

    Unbinder unbinder;

    public TextInputApp(Context context) {
        super(context);
        init(context, null);
    }

    public TextInputApp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextInputApp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextInputApp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_input_layout, this);
        unbinder = ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextInputApp);

        // label
        tvLabel.setText(typedArray.getString(R.styleable.TextInputApp_textInputAppLabel));

        // error
        tvError.setText(typedArray.getString(R.styleable.TextInputApp_textInputAppError));
        tvError.setVisibility(GONE);

        // update content
        etContent.setText(typedArray.getString(R.styleable.TextInputApp_textInputAppContent));
        etContent.setHint(typedArray.getString(R.styleable.TextInputApp_textInputAppHint));

        // text view content
        tvContent.setText(typedArray.getString(R.styleable.TextInputApp_textInputAppContent));
        tvContent.setHint(typedArray.getString(R.styleable.TextInputApp_textInputAppHint));
        String color = typedArray.getString(R.styleable.TextInputApp_textInputTextColor);
        if (color == null) {
            etContent.setTextColor(Color.parseColor("#333333"));
            tvContent.setTextColor(Color.parseColor("#333333"));
        } else {
            etContent.setTextColor(Color.parseColor(color));
            tvContent.setTextColor(Color.parseColor(color));
        }

        // disable view
        boolean isEnabled = typedArray.getBoolean(R.styleable.TextInputApp_textInputAppEnable, true);
        tvContent.setVisibility(isEnabled ? GONE : VISIBLE);
        etContent.setVisibility(!isEnabled ? GONE : VISIBLE);

        // icon
        ivIcon.setVisibility(GONE);
        Drawable drawable = typedArray.getDrawable(R.styleable.TextInputApp_textInputAppIcon);
        Optional.from(drawable).doIfPresent(icon -> {
            ivIcon.setImageDrawable(drawable);
            ivIcon.setVisibility(VISIBLE);
        });

        // input type
        int inputType = typedArray.getInt(R.styleable.TextInputApp_android_inputType, EditorInfo.TYPE_NULL);
        if (inputType != EditorInfo.TYPE_NULL) {
            etContent.setInputType(inputType);
        }

        // ime options
        int imeOptions = typedArray.getInt(R.styleable.TextInputApp_android_imeOptions, EditorInfo.IME_NULL);
        if (imeOptions != EditorInfo.IME_NULL) {
            etContent.setImeOptions(imeOptions);
        }

        typedArray.recycle();
    }

    public TextInputApp setError(String error) {
        tvError.setText(error);
        tvError.setVisibility(!StringUtils.isEmpty(error) ? VISIBLE : GONE);
        tvLabel.setVisibility(StringUtils.isEmpty(error) ? VISIBLE : GONE);
        return this;
    }

    public TextInputApp setDisable() {
        etContent.setFocusable(false);
        return this;
    }

    public ExtEditText getContentView() {
        return etContent;
    }

    public String getContent() {
        return etContent.getText().toString();
    }

    public TextInputApp setContent(String content) {
        tvContent.setText(content);
        etContent.setText(content);
        return this;
    }

    public void setClickableView(boolean enable) {
        ivIcon.setEnabled(enable);
        ivIcon.setClickable(enable);
        tvContent.setEnabled(enable);
        tvContent.setClickable(enable);
        etContent.setEnabled(enable);
        etContent.setClickable(enable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);
    }
}
