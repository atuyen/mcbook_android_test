package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;

public class ConfirmRecordDialog extends Dialog {
    private Context context;
    @BindView(R.id.llRoot)
    LinearLayout llRoot;
    @BindView(R.id.tvRecordText)
    ExtTextView tvRecordText;
    @BindView(R.id.btnContinue)
    ExtButton btnContinue;
    @BindView(R.id.btnSave)
    ExtButton btnSave;
    @BindView(R.id.btnCancel)
    ExtButton btnCancel;
    String fileName;
    private Consumer<String> saveConsumer;
    private Consumer<String> continueConsummer;
    private Consumer<String> cancelConsummer;

    public ConfirmRecordDialog(@NonNull Context context, int themeResId, Consumer<String> consumer, String fileName, Consumer<String> continueConsummer, Consumer<String> cancelConsummer) {
        super(context, themeResId);
        this.context = context;
        this.fileName = fileName;
        this.saveConsumer = consumer;
        this.continueConsummer = continueConsummer;
        this.cancelConsummer = cancelConsummer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimary));
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.confirm_record_dialog);
            ButterKnife.bind(this);
            tvRecordText.setText(fileName != null ? String.format(context.getResources().getString(R.string.record), fileName) : "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnCancel, R.id.btnSave, R.id.btnContinue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                continueConsummer.accept("");
                dismiss();
                break;
            case R.id.btnSave:
                saveConsumer.accept("");
                dismiss();
                break;
            case R.id.btnCancel:
                cancelConsummer.accept("");
                dismiss();
                break;
        }
    }

}
