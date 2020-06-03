package com.mc.books.fragments.gift.resultExam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.sharepreferences.AppPreferences;
import com.mc.books.R;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.dialog.ListExamQuestionDialog;
import com.mc.books.fragments.gift.noteExam.NoteExamFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.DetailQuestion;
import com.mc.models.home.ExamResultQuestion;
import com.mc.utilities.AppUtils;
import com.mc.utilities.FragmentUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.mc.utilities.Constant.KEY_EXAM_ID;
import static com.mc.utilities.Constant.KEY_GIFT;
import static com.mc.utilities.Constant.KEY_LIST_QUESTION;
import static com.mc.utilities.Constant.KEY_OPEN_DIALOG;
import static com.mc.utilities.Constant.KEY_POINT;

public class ResultExamFragment extends BaseMvpFragment<IResultExamView, IResultExamPresenter<IResultExamView>> implements IResultExamView {
    public static ResultExamFragment newInstance(List<DetailQuestion> detailQuestionList, int examId, int mGiftUserId) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_LIST_QUESTION, (Serializable) detailQuestionList);
        args.putInt(KEY_EXAM_ID, examId);
        args.putInt(KEY_GIFT, mGiftUserId);
        ResultExamFragment fragment = new ResultExamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.imgResultExam)
    ImageView imgResultExam;
    @BindView(R.id.txtResultExam)
    ExtTextView txtResultExam;
    @BindView(R.id.txtScore)
    ExtTextView txtScore;
    @BindView(R.id.btnDetailResult)
    ExtButton btnDetailResult;
    private int position;
    private List<DetailQuestion> detailQuestionList;
    private ListExamQuestionDialog listExamQuestionDialog;
    private String correctNumber;
    int mGiftUserId;

    @NonNull
    @Override
    public IResultExamPresenter<IResultExamView> createPresenter() {
        return new ResultExamPresenter<>(getAppComponent());
    }

    @SuppressLint({"SetTextI18n, DefaultLocale"})
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            bindButterKnife(view);
            mGiftUserId = getArguments().getInt(KEY_GIFT);
            detailQuestionList = (List<DetailQuestion>) getArguments().getSerializable(KEY_LIST_QUESTION);
            int examId = getArguments().getInt(KEY_EXAM_ID);
            listExamQuestionDialog = new ListExamQuestionDialog(getContext(), detailQuestionList, position -> {
                FragmentUtils.replaceFragment(getActivity(), NoteExamFragment.newInstance(detailQuestionList, position), fragment -> mMainActivity.fragments.add(fragment));
            }, position, android.R.style.Theme_Light, true);

            if (AppPreferences.getInstance(getContext()).getBoolean(KEY_OPEN_DIALOG)) {
                if (AppPreferences.getInstance(getContext()).getFloat(KEY_POINT) > -1)
                    showSorce(AppPreferences.getInstance(getContext()).getFloat(KEY_POINT));
                listExamQuestionDialog.show();
            } else {
                List<ExamResultQuestion> examResultQuestions = new ArrayList<>();
                StreamSupport.stream(detailQuestionList).forEach(detailQuestion -> {
                    ExamResultQuestion examResultQuestion = new ExamResultQuestion();
                    examResultQuestion.setId(detailQuestion.getId());
                    examResultQuestion.setCorrect(detailQuestion.isCorrect());
                    examResultQuestions.add(examResultQuestion);
                });
                presenter.sendLogExam(examId, AppUtils.setJSONString(examResultQuestions), mGiftUserId);
            }

            if (detailQuestionList != null) {
                List<DetailQuestion> correctList = StreamSupport.stream(detailQuestionList).filter(DetailQuestion::isCorrect).collect(Collectors.toList());
                correctNumber = String.format("%d/%d", correctList.size(), detailQuestionList.size());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("DefaultLocale")
    private void showSorce(float point) {
        txtScore.setText(String.format("%s", point));
        if (point >= 0 && point < 5) {
            imgResultExam.setImageResource(R.drawable.ic_star_1);
            txtResultExam.setText(getString(R.string.text_result_exam_low, correctNumber));
        } else if (point >= 5 && point < 8) {
            imgResultExam.setImageResource(R.drawable.ic_star_2);
            txtResultExam.setText(getString(R.string.text_result_exam_low, correctNumber));
        } else if (point <= 10) {
            imgResultExam.setImageResource(R.drawable.ic_star);
            txtResultExam.setText(getString(R.string.text_result_exam, correctNumber));
        }
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public int getResourceId() {
        return R.layout.result_exam_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.result_exam;
    }

    @OnClick(R.id.btnDetailResult)
    public void onViewClicked() {
        AppPreferences.getInstance(getContext()).putBoolean(KEY_OPEN_DIALOG, true);
        listExamQuestionDialog.show();
    }

    @Override
    public void onShowLoading(boolean loading) {
        showProgress(loading);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void sendLogExamSuccess(float point) {
        AppPreferences.getInstance(getContext()).putFloat(KEY_POINT, point);
        showSorce(point);
    }

    @Override
    public void sendLogExamError() {
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.error_log_exam));
        errorBoxDialog.show();
    }
}
