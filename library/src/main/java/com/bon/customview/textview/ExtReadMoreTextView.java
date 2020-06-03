package com.bon.customview.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.bon.library.R;
import com.bon.logger.Logger;

/**
 * Created by Dang on 5/27/2016.
 */
public class ExtReadMoreTextView extends ExtTextView {
    private static final String TAG = ExtReadMoreTextView.class.getSimpleName();

    // const
    private static final int DEFAULT_TRIM_LENGTH = 240;
    private static final boolean DEFAULT_SHOW_TRIM_EXPANDED_TEXT = true;
    private static final String ELLIPSIZE = "... ";

    // variable
    private ReadMoreClickableSpan viewMoreSpan;
    private BufferType bufferType;

    private CharSequence text;
    private CharSequence trimCollapsedText;
    private CharSequence trimExpandedText;

    private int colorClickableText;
    private int trimLength;

    private boolean showTrimExpandedText;
    private boolean readMore = true;

    public ExtReadMoreTextView(Context context) {
        this(context, null);
    }

    public ExtReadMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtReadMoreTextView);
            if (typedArray != null) {
                this.trimLength = typedArray.getInt(R.styleable.ExtReadMoreTextView_trimLength, DEFAULT_TRIM_LENGTH);
                this.trimCollapsedText = getResources().getString(typedArray.getResourceId(R.styleable.ExtReadMoreTextView_trimCollapsedText, R.string.read_more));
                this.trimExpandedText = getResources().getString(typedArray.getResourceId(R.styleable.ExtReadMoreTextView_trimExpandedText, R.string.read_less));
                this.colorClickableText = typedArray.getColor(R.styleable.ExtReadMoreTextView_colorClickableText, ContextCompat.getColor(context, R.color.accent));
                this.showTrimExpandedText = typedArray.getBoolean(R.styleable.ExtReadMoreTextView_showTrimExpandedText, DEFAULT_SHOW_TRIM_EXPANDED_TEXT);
                typedArray.recycle();
            }

            viewMoreSpan = new ReadMoreClickableSpan();
            setText();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void setText() {
        try {
            super.setText(getDisplayableText(), bufferType);
            setMovementMethod(LinkMovementMethod.getInstance());
            setHighlightColor(Color.TRANSPARENT);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @return
     */
    private CharSequence getDisplayableText() {
        return getTrimmedText(text);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            this.text = text;
            this.bufferType = type;
            this.setText();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param text
     * @return
     */
    private CharSequence getTrimmedText(CharSequence text) {
        try {
            if (text != null && text.length() > trimLength) {
                if (readMore) {
                    return updateCollapsedText();
                } else {
                    return updateExpandedText();
                }
            }

            return text;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @return
     */
    private CharSequence updateCollapsedText() {
        try {
            SpannableStringBuilder s = new SpannableStringBuilder(text, 0, trimLength + 1)
                    .append(ELLIPSIZE).append(trimCollapsedText);
            return addClickableSpan(s, trimCollapsedText);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @return
     */
    private CharSequence updateExpandedText() {
        try {
            if (showTrimExpandedText) {
                SpannableStringBuilder s = new SpannableStringBuilder(text, 0, text.length())
                        .append(trimExpandedText);
                return addClickableSpan(s, trimExpandedText);
            }

            return text;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param spannableStringBuilder
     * @param trimText
     * @return
     */
    private CharSequence addClickableSpan(SpannableStringBuilder spannableStringBuilder, CharSequence trimText) {
        try {
            spannableStringBuilder.setSpan(viewMoreSpan, spannableStringBuilder.length() - trimText.length(),
                    spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableStringBuilder;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public ExtReadMoreTextView setTrimLength(int trimLength) {
        try {
            this.trimLength = trimLength;
            this.setText();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    public ExtReadMoreTextView setColorClickableText(int colorClickableText) {
        this.colorClickableText = colorClickableText;
        return this;
    }

    public ExtReadMoreTextView setTrimCollapsedText(CharSequence trimCollapsedText) {
        this.trimCollapsedText = trimCollapsedText;
        return this;
    }

    public ExtReadMoreTextView setTrimExpandedText(CharSequence trimExpandedText) {
        this.trimExpandedText = trimExpandedText;
        return this;
    }

    private class ReadMoreClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            try {
                readMore = !readMore;
                setText();
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            try {
                ds.setColor(colorClickableText);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }
}