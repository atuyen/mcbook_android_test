package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.Window;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.models.more.FAQ;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FAQDialog extends Dialog {

    @BindView(R.id.txtNameFaq)
    ExtTextView txtNameFaq;
    @BindView(R.id.txtContentFaq)
    ExtTextView txtContentFaq;
    @BindView(R.id.btnClose)
    ExtButton btnClose;
    private FAQ faq;

    public FAQDialog(@NonNull Context context, FAQ faq) {
        super(context);
        this.faq = faq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_faq);
            ButterKnife.bind(this);
            txtContentFaq.setText(Html.fromHtml(faq.getContent()));
            txtNameFaq.setText(faq.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnClose)
    void onCLick() {
        dismiss();
    }

}
