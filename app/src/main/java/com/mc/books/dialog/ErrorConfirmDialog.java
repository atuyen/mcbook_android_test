package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;

public class ErrorConfirmDialog extends Dialog {

    @BindView(R.id.btnCancel)
    ExtButton btnCancel;
    @BindView(R.id.btnConfirm)
    ExtButton btnConfirm;
    private Consumer<String> acceptConsumer;
    @BindView(R.id.txtError)
    ExtTextView txtError;
    private String textConfirm;
    private String textContent;

    public ErrorConfirmDialog(@NonNull Context context, String textConfirm, String textContent, Consumer<String> acceptConsumer) {
        super(context);
        this.acceptConsumer = acceptConsumer;
        this.textConfirm = textConfirm;
        this.textContent = textContent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_confirm);
            ButterKnife.bind(this);
            btnConfirm.setText(textConfirm);
            txtError.setText(textContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnCancel, R.id.btnConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                dismiss();
                acceptConsumer.accept("");
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }
}
