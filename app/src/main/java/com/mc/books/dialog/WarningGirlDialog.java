package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.RelativeLayout;

import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WarningGirlDialog extends Dialog {
    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;

    public WarningGirlDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.warning_girl_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rlRoot)
    void onDismiss() {
        dismiss();
    }
}