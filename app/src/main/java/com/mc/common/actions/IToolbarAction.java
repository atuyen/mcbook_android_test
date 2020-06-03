package com.mc.common.actions;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by dangpp on 2/21/2018.
 */

public interface IToolbarAction {
    void setToolbarTitle(@StringRes int titleId);

    void setToolbarTitle(@NonNull String titleId);
}
