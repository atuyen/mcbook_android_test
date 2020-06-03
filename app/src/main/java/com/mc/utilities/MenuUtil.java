package com.mc.utilities;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.mc.books.R;
import com.mc.common.adapters.ContextMenuBookAdapter;
import com.mc.models.home.DialogBookMenuItem;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;

public class MenuUtil {

    public static CustomPowerMenu getBookMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener onMenuItemClickListener) {
        ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(R.color.color_gray));
        return new CustomPowerMenu.Builder<>(context, new ContextMenuBookAdapter())
                .addItem(new DialogBookMenuItem(context.getResources().getString(R.string.delete_book)))
                .setLifecycleOwner(lifecycleOwner)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setDivider(drawable)
                .setDividerHeight(1)
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
    }

    public static CustomPowerMenu getGiftMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener onMenuItemClickListener) {
        ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(R.color.color_gray));
        return new CustomPowerMenu.Builder<>(context, new ContextMenuBookAdapter())
                .addItem(new DialogBookMenuItem(context.getResources().getString(R.string.delete_gift)))
                .setLifecycleOwner(lifecycleOwner)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setDivider(drawable)
                .setDividerHeight(1)
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
    }
}
