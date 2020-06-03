package com.bon.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bon.logger.Logger;

import java.lang.reflect.Method;

/**
 * Created by dangpp on 8/24/2017.
 */

public final class BarUtils {
    private static final String TAG = BackUtils.class.getSimpleName();

    ///////////////////////////////////////////////////////////////////////////
    // status bar
    ///////////////////////////////////////////////////////////////////////////
    private static final int DEFAULT_ALPHA = 112;
    private static final String TAG_COLOR = "TAG_COLOR";
    private static final String TAG_ALPHA = "TAG_ALPHA";
    private static final int TAG_OFFSET = -123;

    /**
     * @return
     */
    public static int getStatusBarHeight() {
        try {
            Resources resources = ExtUtils.getApp().getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            return resources.getDimensionPixelSize(resourceId);
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    /**
     * @param view
     */
    public static void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        try {
            Object haveSetOffset = view.getTag(TAG_OFFSET);
            if (haveSetOffset != null && (Boolean) haveSetOffset) return;

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(),
                    layoutParams.rightMargin, layoutParams.bottomMargin);
            view.setTag(TAG_OFFSET, true);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param view
     */
    public static void subtractMarginTopEqualStatusBarHeight(@NonNull View view) {
        try {
            Object haveSetOffset = view.getTag(TAG_OFFSET);
            if (haveSetOffset == null || !(Boolean) haveSetOffset) return;
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin - getStatusBarHeight(),
                    layoutParams.rightMargin, layoutParams.bottomMargin);
            view.setTag(TAG_OFFSET, false);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color) {
        setStatusBarColor(activity, color, DEFAULT_ALPHA, false);
    }

    /**
     * @param activity
     * @param color
     * @param alpha
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        setStatusBarColor(activity, color, alpha, false);
    }

    /**
     * @param activity
     * @param color
     * @param alpha
     * @param isDecor
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha,
                                         final boolean isDecor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

        hideAlphaView(activity);
        transparentStatusBar(activity);
        addStatusBarColor(activity, color, alpha, isDecor);
    }

    /**
     * @param activity
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity) {
        setStatusBarAlpha(activity, DEFAULT_ALPHA, false);
    }

    /**
     * @param activity
     * @param alpha
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        setStatusBarAlpha(activity, alpha, false);
    }

    /**
     * @param activity
     * @param alpha
     * @param isDecor
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity,
                                         @IntRange(from = 0, to = 255) final int alpha,
                                         final boolean isDecor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

        hideColorView(activity);
        transparentStatusBar(activity);
        addStatusBarAlpha(activity, alpha, isDecor);
    }

    /**
     * @param fakeStatusBar
     * @param color
     */
    public static void setStatusBarColor(@NonNull final View fakeStatusBar, @ColorInt final int color) {
        setStatusBarColor(fakeStatusBar, color, DEFAULT_ALPHA);
    }

    /**
     * @param fakeStatusBar
     * @param color
     * @param alpha
     */
    public static void setStatusBarColor(@NonNull final View fakeStatusBar,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

            fakeStatusBar.setVisibility(View.VISIBLE);
            transparentStatusBar((Activity) fakeStatusBar.getContext());
            ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = BarUtils.getStatusBarHeight();
            fakeStatusBar.setBackgroundColor(getStatusBarColor(color, alpha));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param fakeStatusBar
     */
    public static void setStatusBarAlpha(@NonNull final View fakeStatusBar) {
        setStatusBarAlpha(fakeStatusBar, DEFAULT_ALPHA);
    }

    /**
     * @param fakeStatusBar
     * @param alpha
     */
    public static void setStatusBarAlpha(@NonNull final View fakeStatusBar,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

            fakeStatusBar.setVisibility(View.VISIBLE);
            transparentStatusBar((Activity) fakeStatusBar.getContext());
            ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = BarUtils.getStatusBarHeight();
            fakeStatusBar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * DrawerLayout
     * <p>DrawLayout {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity
     * @param drawer
     * @param fakeStatusBar
     * @param color
     * @param isTop
     */
    public static void setStatusBarColor4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @ColorInt final int color,
                                                final boolean isTop) {
        setStatusBarColor4Drawer(activity, drawer, fakeStatusBar, color, DEFAULT_ALPHA, isTop);
    }

    /**
     * DrawerLayout
     * <p>DrawLayout {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity
     * @param drawer
     * @param fakeStatusBar
     * @param color
     * @param alpha
     * @param isTop
     */
    public static void setStatusBarColor4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @ColorInt final int color,
                                                @IntRange(from = 0, to = 255) final int alpha,
                                                final boolean isTop) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

            drawer.setFitsSystemWindows(false);
            transparentStatusBar(activity);
            setStatusBarColor(fakeStatusBar, color, isTop ? alpha : 0);

            for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
                drawer.getChildAt(i).setFitsSystemWindows(false);
            }

            if (isTop) {
                hideAlphaView(activity);
            } else {
                addStatusBarAlpha(activity, alpha, false);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * DrawerLayout
     * <p>DrawLayout {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity
     * @param drawer
     * @param fakeStatusBar
     * @param isTop
     */
    public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                final boolean isTop) {
        setStatusBarAlpha4Drawer(activity, drawer, fakeStatusBar, DEFAULT_ALPHA, isTop);
    }

    /**
     * DrawerLayout
     * <p>DrawLayout {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity
     * @param drawer
     * @param fakeStatusBar
     * @param alpha
     * @param isTop
     */
    public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @IntRange(from = 0, to = 255) final int alpha,
                                                final boolean isTop) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

            drawer.setFitsSystemWindows(false);
            transparentStatusBar(activity);
            setStatusBarAlpha(fakeStatusBar, isTop ? alpha : 0);

            for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
                drawer.getChildAt(i).setFitsSystemWindows(false);
            }

            if (isTop) {
                hideAlphaView(activity);
            } else {
                addStatusBarAlpha(activity, alpha, false);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     * @param color
     * @param alpha
     * @param isDecor
     */
    private static void addStatusBarColor(final Activity activity, final int color, final int alpha, boolean isDecor) {
        try {
            ViewGroup parent = isDecor ? (ViewGroup) activity.getWindow().getDecorView() :
                    (ViewGroup) activity.findViewById(android.R.id.content);
            View fakeStatusBarView = parent.findViewWithTag(TAG_COLOR);

            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }

                fakeStatusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
            } else {
                parent.addView(createColorStatusBarView(parent.getContext(), color, alpha));
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     * @param alpha
     * @param isDecor
     */
    private static void addStatusBarAlpha(final Activity activity, final int alpha, boolean isDecor) {
        try {
            ViewGroup parent = isDecor ? (ViewGroup) activity.getWindow().getDecorView() :
                    (ViewGroup) activity.findViewById(android.R.id.content);
            View fakeStatusBarView = parent.findViewWithTag(TAG_ALPHA);

            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }

                fakeStatusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
            } else {
                parent.addView(createAlphaStatusBarView(parent.getContext(), alpha));
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     */
    private static void hideColorView(final Activity activity) {
        try {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);

            if (fakeStatusBarView == null) return;

            fakeStatusBarView.setVisibility(View.GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     */
    private static void hideAlphaView(final Activity activity) {
        try {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewWithTag(TAG_ALPHA);

            if (fakeStatusBarView == null) return;

            fakeStatusBarView.setVisibility(View.GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param color
     * @param alpha
     * @return
     */
    private static int getStatusBarColor(final int color, final int alpha) {
        try {
            if (alpha == 0) return color;

            float a = 1 - alpha / 255f;
            int red = (color >> 16) & 0xff;
            int green = (color >> 8) & 0xff;
            int blue = color & 0xff;
            red = (int) (red * a + 0.5);
            green = (int) (green * a + 0.5);
            blue = (int) (blue * a + 0.5);

            return Color.argb(255, red, green, blue);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    /**
     * @param context
     * @param color
     * @param alpha
     * @return
     */
    private static View createColorStatusBarView(final Context context, final int color, final int alpha) {
        try {
            View statusBarView = new View(context);
            statusBarView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
            statusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
            statusBarView.setTag(TAG_COLOR);
            return statusBarView;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param context
     * @param alpha
     * @return
     */
    private static View createAlphaStatusBarView(final Context context, final int alpha) {
        try {
            View statusBarView = new View(context);
            statusBarView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
            statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
            statusBarView.setTag(TAG_ALPHA);
            return statusBarView;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    private static void transparentStatusBar(final Activity activity) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                window.getDecorView().setSystemUiVisibility(option);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // action bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param activity
     * @return
     */
    public static int getActionBarHeight(@NonNull final Activity activity) {
        try {
            TypedValue tv = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    ///////////////////////////////////////////////////////////////////////////
    // notification bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>{@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>}</p>
     *
     * @param context
     * @param isSettingPanel
     */
    public static void showNotificationBar(@NonNull final Context context, final boolean isSettingPanel) {
        try {
            String methodName = (Build.VERSION.SDK_INT <= 16) ? "expand" : (isSettingPanel ? "expandSettingsPanel" : "expandNotificationsPanel");
            invokePanels(context, methodName);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>}
     *
     * @param context
     */
    public static void hideNotificationBar(@NonNull final Context context) {
        try {
            String methodName = (Build.VERSION.SDK_INT <= 16) ? "collapse" : "collapsePanels";
            invokePanels(context, methodName);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param methodName
     */
    private static void invokePanels(@NonNull final Context context, final String methodName) {
        try {
            @SuppressLint("WrongConstant")
            Object service = context.getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName);
            expand.invoke(service);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // navigation bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @return
     */
    public static int getNavBarHeight() {
        try {
            boolean hasMenuKey = ViewConfiguration.get(ExtUtils.getApp()).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

            if (!hasMenuKey && !hasBackKey) {
                Resources res = ExtUtils.getApp().getResources();
                int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                return res.getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    /**
     * @param activity
     */
    public static void hideNavBar(@NonNull final Activity activity) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return;

            if (getNavBarHeight() > 0) {
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     * @param colorPrimaryDark
     */
    public static void setTranslucentForDrawer(Activity activity, @ColorRes int colorPrimaryDark) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Do something for lollipop and above versions

                Window window = activity.getWindow();

                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                // finally change the color again to dark
                window.setStatusBarColor(activity.getResources().getColor(colorPrimaryDark));
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}