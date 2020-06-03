package com.bon.util;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * Created by dangpp on 9/11/2017.
 */

public class SnackBarUtils {
    private static final String TAG = SnackBarUtils.class.getSimpleName();

    // const
    private static final int DEFAULT_COLOR = 0xFEFFFFFF;
    private static final int SUCCESS = 0xFF2BB600;
    private static final int WARNING = 0xFFFFC100;
    private static final int ERROR = 0xFFFF0000;
    private static final int MESSAGE = 0xFFFFFFFF;

    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_SHORT = -1;
    public static final int LENGTH_LONG = 0;

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    private static WeakReference<Snackbar> snackBarWeakReference;
    private WeakReference<View> parent;

    private CharSequence message;
    private int messageColor;
    private int bgColor;
    private int bgResource;
    private int duration;
    private CharSequence actionText;
    private int actionTextColor;
    private View.OnClickListener actionListener;
    private int bottomMargin;

    private SnackBarUtils(final View parent) {
        this.setDefault();
        this.parent = new WeakReference<>(parent);
    }

    private void setDefault() {
        try {
            message = "";
            messageColor = DEFAULT_COLOR;
            bgColor = DEFAULT_COLOR;
            bgResource = -1;
            duration = LENGTH_SHORT;
            actionText = "";
            actionTextColor = DEFAULT_COLOR;
            bottomMargin = 0;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param parent
     * @return {@link SnackBarUtils}
     */
    public static SnackBarUtils with(@NonNull final View parent) {
        return new SnackBarUtils(parent);
    }

    /**
     * @param msg
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setMessage(@NonNull final CharSequence msg) {
        this.message = msg;
        return this;
    }

    /**
     * @param color
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setMessageColor(@ColorInt final int color) {
        this.messageColor = color;
        return this;
    }

    /**
     * @param color
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setBgColor(@ColorInt final int color) {
        this.bgColor = color;
        return this;
    }

    /**
     * @param bgResource
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setBgResource(@DrawableRes final int bgResource) {
        this.bgResource = bgResource;
        return this;
    }

    /**
     * @param duration <ul>
     *                 <li>{@link Duration#LENGTH_INDEFINITE}</li>
     *                 <li>{@link Duration#LENGTH_SHORT}</li>
     *                 <li>{@link Duration#LENGTH_LONG}</li>
     *                 </ul>
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setDuration(@Duration final int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * @param text
     * @param listener
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setAction(@NonNull final CharSequence text, @NonNull final View.OnClickListener listener) {
        return setAction(text, DEFAULT_COLOR, listener);
    }

    /**
     * @param text
     * @param color
     * @param listener
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setAction(@NonNull final CharSequence text, @ColorInt final int color, @NonNull final View.OnClickListener listener) {
        this.actionText = text;
        this.actionTextColor = color;
        this.actionListener = listener;
        return this;
    }

    /**
     * @param bottomMargin
     * @return {@link SnackBarUtils}
     */
    public SnackBarUtils setBottomMargin(@IntRange(from = 1) final int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    /**
     * show snack bar
     */
    public void show() {
        try {
            final View view = parent.get();
            if (view == null) return;

            // message color
            if (messageColor != DEFAULT_COLOR) {
                SpannableString spannableString = new SpannableString(message);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(messageColor);
                spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                snackBarWeakReference = new WeakReference<>(Snackbar.make(view, spannableString, duration));
            } else {
                snackBarWeakReference = new WeakReference<>(Snackbar.make(view, message, duration));
            }

            final Snackbar snackbar = snackBarWeakReference.get();
            final View snackBarView = snackbar.getView();

            // background color
            if (bgResource != -1) {
                snackBarView.setBackgroundResource(bgResource);
            } else if (bgColor != DEFAULT_COLOR) {
                snackBarView.setBackgroundColor(bgColor);
            }

            //margin bottom
            if (bottomMargin != 0) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackBarView.getLayoutParams();
                params.bottomMargin = bottomMargin;
            }

            // set action
            if (actionText.length() > 0 && actionListener != null) {
                if (actionTextColor != DEFAULT_COLOR) {
                    snackbar.setActionTextColor(actionTextColor);
                }
                snackbar.setAction(actionText, actionListener);
            }

            // show snack bar
            snackbar.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * success
     */
    public void showSuccess() {
        bgColor = SUCCESS;
        messageColor = MESSAGE;
        actionTextColor = MESSAGE;
        show();
    }

    /**
     * warming
     */
    public void showWarning() {
        bgColor = WARNING;
        messageColor = MESSAGE;
        actionTextColor = MESSAGE;
        show();
    }

    /**
     * error
     */
    public void showError() {
        bgColor = ERROR;
        messageColor = MESSAGE;
        actionTextColor = MESSAGE;
        show();
    }

    /**
     * dismiss
     */
    public static void dismiss() {
        try {
            if (snackBarWeakReference != null && snackBarWeakReference.get() != null) {
                snackBarWeakReference.get().dismiss();
                snackBarWeakReference = null;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * get view
     *
     * @return View
     */
    public static View getView() {
        try {
            Snackbar snackbar = snackBarWeakReference.get();
            if (snackbar == null) return null;

            return snackbar.getView();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param layoutId
     * @param params
     */
    public static void addView(@LayoutRes final int layoutId, @NonNull final ViewGroup.LayoutParams params) {
        try {
            final View view = getView();
            if (view != null) {
                view.setPadding(0, 0, 0, 0);
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
                View child = LayoutInflater.from(view.getContext()).inflate(layoutId, null);
                layout.addView(child, -1, params);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param child
     * @param params
     */
    public static void addView(@NonNull final View child, @NonNull final ViewGroup.LayoutParams params) {
        try {
            final View view = getView();
            if (view != null) {
                view.setPadding(0, 0, 0, 0);
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
                layout.addView(child, params);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
