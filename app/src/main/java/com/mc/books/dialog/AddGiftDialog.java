package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.bon.customview.button.ExtButton;
import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;

public class AddGiftDialog extends Dialog {

    @BindView(R.id.btnCancel)
    ExtButton btnCancel;
    @BindView(R.id.btnAccept)
    ExtButton btnAccept;
    @BindView(R.id.txtError)
    ExtTextView txtError;
    @BindView(R.id.txtAddGift)
    ExtTextView txtAddGift;
    @BindView(R.id.edtGiftCode)
    ExtEditText edtGiftCode;

    private Consumer<String> acceptConsumer;
    private Context context;

    public AddGiftDialog(@NonNull Context context, Consumer<String> acceptConsumer) {
        super(context);
        this.acceptConsumer = acceptConsumer;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_add_gift);
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowError(boolean isShow, String textError) {
        if (isShow) {
            txtError.setText(textError);
            txtError.setVisibility(View.VISIBLE);
            txtAddGift.setVisibility(View.GONE);
        } else {
            txtError.setVisibility(View.GONE);
            txtAddGift.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.btnCancel, R.id.btnAccept})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAccept:
                if (!edtGiftCode.getText().toString().equals(""))
                    acceptConsumer.accept(edtGiftCode.getText().toString());
                else
                    onShowError(true, context.getString(R.string.empty_gift_code));
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }
}