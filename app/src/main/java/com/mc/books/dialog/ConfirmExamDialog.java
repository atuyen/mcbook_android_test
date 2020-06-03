package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.bon.customview.button.ExtButton;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;

public class ConfirmExamDialog extends Dialog {

    @BindView(R.id.btnCancel)
    ExtButton btnCancel;
    @BindView(R.id.btnConfirm)
    ExtButton btnConfirm;
    private Consumer<String> confirmConsumer;

    public ConfirmExamDialog(@NonNull Context context, Consumer<String> confirmConsumer) {
        super(context);
        this.confirmConsumer = confirmConsumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_finish_exam);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnCancel, R.id.btnConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                dismiss();
                confirmConsumer.accept("");
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }
}