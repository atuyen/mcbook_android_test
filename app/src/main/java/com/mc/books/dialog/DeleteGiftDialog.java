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

public class DeleteGiftDialog extends Dialog {

    @BindView(R.id.btnCancel)
    ExtButton btnCancel;
    @BindView(R.id.btnDelete)
    ExtButton btnDelete;
    private Consumer<String> deleteConsumer;

    public DeleteGiftDialog(@NonNull Context context, Consumer<String> deleteConsumer) {
        super(context);
        this.deleteConsumer = deleteConsumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_gift);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnCancel, R.id.btnDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                dismiss();
                deleteConsumer.accept("");
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }
}
