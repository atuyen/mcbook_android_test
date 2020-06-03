package com.mc.books.fragments.gift.examinfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.sharepreferences.AppPreferences;
import com.bon.util.ToastUtils;
import com.mc.adapter.ExamInfomationAdapter;
import com.mc.books.R;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.fragments.gift.doExam.DoExamFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.gift.InfomationExam;
import com.mc.models.home.DetailQuestion;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mc.utilities.Constant.HARDEST_EXAMS;
import static com.mc.utilities.Constant.HARD_EXAMS;
import static com.mc.utilities.Constant.KEY_OPEN_DIALOG;
import static com.mc.utilities.Constant.LINE_SHOW_MORE;
import static com.mc.utilities.Constant.MAXIUM_LINE;
import static com.mc.utilities.Constant.MAX_LINE;


public class ExamInfomationFragment extends BaseMvpFragment<IExamInfomationView, IExamInfomationPresenter<IExamInfomationView>> implements IExamInfomationView {

    public static String nameCategoryGift;
    public static int mExamId = -1;
    private ExamInfomationAdapter examInfomationAdapter;

    @BindView(R.id.txtIntroduce)
    ExtTextView txtIntroduce;
    @BindView(R.id.txtClass)
    ExtTextView txtClass;
    @BindView(R.id.txtSubject)
    ExtTextView txtSubject;
    @BindView(R.id.txtLevel)
    ExtTextView txtLevel;
    @BindView(R.id.txtTime)
    ExtTextView txtTime;
    @BindView(R.id.txtNumOffExam)
    ExtTextView txtNumOffExam;
    @BindView(R.id.txtNumOffQuestion)
    ExtTextView txtNumOffQuestion;
    @BindView(R.id.recycleHistory)
    RecyclerView recycleHistory;
    @BindView(R.id.btn_Exam)
    ExtButton btnXam;
    @BindView(R.id.txtReadMore)
    ExtTextView txtReadMore;
    private boolean isShowReadMore = false;
    private InfomationExam infomationExam;
    private static int mGiftId;
    private static int mGiftUserId;

    public static ExamInfomationFragment newInstance(String giftName, int examId, int giftId, int giftUserId) {
        Bundle args = new Bundle();
        ExamInfomationFragment fragment = new ExamInfomationFragment();
        if (giftName != null && examId != 0 && giftId != 0 && giftUserId != 0) {
            nameCategoryGift = giftName;
            mExamId = examId;
            mGiftId = giftId;
            mGiftUserId = giftUserId;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public IExamInfomationPresenter<IExamInfomationView> createPresenter() {
        return new ExamInfomationPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.exam_info_fragment;
    }

    @Override
    public String getTitleString() {
        return nameCategoryGift;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            bindButterKnife(view);
            AppPreferences.getInstance(getContext()).putBoolean(KEY_OPEN_DIALOG, false);
            mMainActivity.onShowBottomBar(true);
            presenter.showInfomationExam(mExamId, mGiftId, mGiftUserId);
            mActivity.getSupportActionBar().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        txtIntroduce.post(() -> {
            int lineCount = txtIntroduce.getLineCount();
            if (lineCount > LINE_SHOW_MORE) {
                txtIntroduce.setMaxLines(MAX_LINE);
                txtReadMore.setVisibility(View.VISIBLE);
            }
        });

        examInfomationAdapter = new ExamInfomationAdapter(getContext());
        recycleHistory.setNestedScrollingEnabled(false);
        recycleHistory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycleHistory.setAdapter(examInfomationAdapter);
    }

    @OnClick({R.id.txtReadMore, R.id.btn_Exam})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.txtReadMore:
                if (isShowReadMore) {
                    isShowReadMore = false;
                    txtIntroduce.setMaxLines(MAX_LINE);
                    txtReadMore.setText(R.string.show_more);
                } else {
                    isShowReadMore = true;
                    txtIntroduce.setMaxLines(MAXIUM_LINE);
                    txtReadMore.setText(R.string.show_less);
                }
                break;
            case R.id.btn_Exam:
                if (mExamId != -1)
                    presenter.getListQuestionExam(mExamId, mGiftId);
                else ToastUtils.showToast(getContext(), getString(R.string.empty_exam_id));
                break;
            default:
                break;
        }
    }

    @Override
    public void onShowLoading(boolean loading) {
        showProgress(loading);
    }

    @Override
    public void getListQuestionExamSuccess(List<DetailQuestion> detailQuestionList) {
        FragmentUtils.replaceFragment(getActivity(), DoExamFragment.newInstance(detailQuestionList, infomationExam.getTime(), mExamId, mGiftUserId));
    }

    @Override
    public void getListQuestionExamError() {
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.end_turn_exam));
        errorBoxDialog.show();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void getInfomationExam(InfomationExam infomationExam) {
        try {
            this.infomationExam = infomationExam;
            initView();
            txtIntroduce.setText(infomationExam.getDescription());
            StringBuilder stringBuilder = new StringBuilder();
            if (infomationExam.getClassIds().size() > 0) {
                List<Integer> mClassId = infomationExam.getClassIds();
                for (int i = 0; i < mClassId.size(); i++) {
                    stringBuilder.append(mClassId.get(i)).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                txtClass.setText(stringBuilder.toString());
            }
            txtSubject.setText(infomationExam.getSubjectName());

            String level = "";
            switch (infomationExam.getLevel()) {
                case Constant.EASY_EXAMS:
                    level = getString(R.string.easy);
                    break;
                case Constant.MEDIUM_EXAMS:
                    level = getString(R.string.medium);
                    break;
                case HARD_EXAMS:
                    level = getString(R.string.hard);
                    break;
                case HARDEST_EXAMS:
                    level = getString(R.string.hardest);
                    break;
            }
            txtLevel.setText(level);
            txtTime.setText(String.format(getString(R.string.exam_minute), String.valueOf(infomationExam.getTime())));
            txtNumOffExam.setText(String.valueOf(String.format("%d/%d", infomationExam.getNumberOfExams(), infomationExam.getMaxExams())));
            txtNumOffQuestion.setText(String.format(getString(R.string.exam_number_question), String.valueOf(infomationExam.getNumberQuestions())));
            examInfomationAdapter.setDataList(infomationExam.getHistories());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getInfomationExamError() {
        ToastUtils.showToast(getContext(), getString(R.string.empty_exam_info));
    }
}
