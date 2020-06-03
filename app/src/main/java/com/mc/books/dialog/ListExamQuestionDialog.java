package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mc.adapter.ListExamQuestionAdapter;
import com.mc.books.R;
import com.mc.models.home.DetailQuestion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;

public class ListExamQuestionDialog extends Dialog {

    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.recycle_question)
    RecyclerView recycleQuestion;
    private Context context;
    private Consumer<Integer> positionConsumer;
    private List<DetailQuestion> detailQuestionList;
    private int position;
    private boolean isResult;

    public ListExamQuestionDialog(@NonNull Context context, List<DetailQuestion> detailQuestionList,
                                  Consumer<Integer> positionConsumer, int position, int themeResId, boolean isResult) {
        super(context, themeResId);
        this.context = context;
        this.positionConsumer = positionConsumer;
        this.detailQuestionList = detailQuestionList;
        this.position = position;
        this.isResult = isResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimary));
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.dialog_list_exam_question);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            ButterKnife.bind(this);
            initData();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        ListExamQuestionAdapter listExamQuestionAdapter = new ListExamQuestionAdapter(getContext(), position, pos -> {
            positionConsumer.accept(pos);
            dismiss();
        }, isResult);
        recycleQuestion.setNestedScrollingEnabled(false);
        recycleQuestion.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycleQuestion.setAdapter(listExamQuestionAdapter);
        listExamQuestionAdapter.setDataList(detailQuestionList);
    }

    @OnClick(R.id.imgClose)
    public void onClickView() {
        dismiss();
    }
}
