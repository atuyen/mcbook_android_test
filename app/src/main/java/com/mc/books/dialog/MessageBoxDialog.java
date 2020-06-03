package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.bon.collection.CollectionUtils;
import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.mc.adapter.AddGiftItemAdapter;
import com.mc.books.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;

public class MessageBoxDialog extends Dialog {
    @BindView(R.id.txtMessage)
    ExtTextView txtMessage;
    @BindView(R.id.rvGift)
    RecyclerView rvGift;
    @BindView(R.id.llMesssageDialog)
    LinearLayout llMesssageDialog;
    @BindView(R.id.btnGoStudy)
    ExtButton btnGoStudy;
    private Context context;
    private List<String> giftList = null;
    private String message;
    private boolean isShowButton;
    private Consumer<String> goStudy;

    public MessageBoxDialog(@NonNull Context context, String message, boolean isShowButton, Consumer<String> goStudy) {
        super(context);
        this.context = context;
        this.message = message;
        this.isShowButton = isShowButton;
        this.goStudy = goStudy;
    }

    public MessageBoxDialog(@NonNull Context context, String message, List<String> giftList) {
        super(context);
        this.context = context;
        this.giftList = giftList;
        this.message = message;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_message_box);
            ButterKnife.bind(this);
            txtMessage.setText(message);
            if (CollectionUtils.isNotNullOrEmpty(giftList)) {
                rvGift.setVisibility(View.VISIBLE);
                AddGiftItemAdapter addGiftItemAdapter = new AddGiftItemAdapter(context);
                rvGift.setLayoutManager(new LinearLayoutManager(context));
                rvGift.setAdapter(addGiftItemAdapter);
                addGiftItemAdapter.setDataList(giftList);
            } else
                rvGift.setVisibility(View.GONE);

            if (isShowButton) btnGoStudy.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.llMesssageDialog)
    public void onViewClicked() {
        dismiss();
    }

    @OnClick(R.id.btnGoStudy)
    public void onGoStudy() {
        dismiss();
        goStudy.accept("");
    }
}
