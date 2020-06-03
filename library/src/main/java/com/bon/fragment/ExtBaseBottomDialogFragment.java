package com.bon.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bon.library.R;
import com.bon.logger.Logger;

/**
 * Created by Dang on 6/23/2016.
 */
public abstract class ExtBaseBottomDialogFragment extends DialogFragment {
    private static final String TAG = ExtBaseBottomDialogFragment.class.getSimpleName();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getDialog().getWindow().setGravity(Gravity.BOTTOM);
            getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, attributes.height);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
