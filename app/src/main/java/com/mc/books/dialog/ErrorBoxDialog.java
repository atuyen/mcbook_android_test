package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorBoxDialog extends Dialog {
    @BindView(R.id.txtMessage)
    ExtTextView txtMessage;
    @BindView(R.id.rvGift)
    RecyclerView rvGift;
    @BindView(R.id.llMesssageDialog)
    LinearLayout llMesssageDialog;
    @BindView(R.id.imgError)
    ImageView imgError;
    @BindView(R.id.btnClose)
    ExtButton btnClose;
    private List<String> giftList = null;
    private String message;
    private int icon;
    private boolean showButton = false;

    public ErrorBoxDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    public ErrorBoxDialog(@NonNull Context context, String message, int icon) {
        super(context);
        this.message = message;
        this.icon = icon;
    }

    public ErrorBoxDialog(@NonNull Context context, String message, int icon, boolean showButton) {
        super(context);
        this.message = message;
        this.icon = icon;
        this.showButton = showButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_error_box);
            ButterKnife.bind(this);
            txtMessage.setText(message);
            if (icon > 0)
                imgError.setImageResource(icon);
            if (giftList != null)
                rvGift.setVisibility(View.VISIBLE);
            if (showButton)
                btnClose.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.llMesssageDialog)
    public void onViewClicked() {
        dismiss();
    }

    @OnClick(R.id.btnClose)
    public void onClose() {
        dismiss();
    }
}
