package com.mc.books.fragments.home.listSubject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.ToastUtils;
import com.mc.adapter.SubjectAdapter;
import com.mc.books.R;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.dialog.ErrorConfirmDialog;
import com.mc.books.fragments.home.doSubject.DoSubjectFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.Question;
import com.mc.utilities.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mc.utilities.Constant.BOOK_ID;

public class ListSubjectFragment extends BaseMvpFragment<IListSubjectView, IListSubjectPresenter<IListSubjectView>> implements IListSubjectView {
    public static ListSubjectFragment newInstance(int bookId) {
        Bundle args = new Bundle();
        ListSubjectFragment fragment = new ListSubjectFragment();
        args.putInt(BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rvSubject)
    RecyclerView rvSubject;
    @BindView(R.id.txtPoint)
    ExtTextView txtPoint;
    @BindView(R.id.txtResetQuestion)
    ExtTextView txtResetQuestion;
    private SubjectAdapter subjectAdapter;
    private int bookId;
    private List<Question> tempQuestions;

    @NonNull
    @Override
    public IListSubjectPresenter<IListSubjectView> createPresenter() {
        return new ListSubjectPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        bookId = getArguments().getInt(BOOK_ID);
        presenter.getListQuestion(bookId);
        initData();
    }

    private void initData() {
        subjectAdapter = new SubjectAdapter(getContext(), this::goToDoSubject, this::goToDoSubjectQuestionSingle);
        rvSubject.setLayoutManager(new LinearLayoutManager(mActivity));
        rvSubject.setAdapter(subjectAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.onShowBottomBar(false);
    }

    private void goToDoSubject(Question question) {
        FragmentUtils.replaceFragment(getActivity(), DoSubjectFragment.newInstance(tempQuestions, getPosition(question), question, bookId),
                fragment -> mMainActivity.fragments.add(fragment));
    }

    private void goToDoSubjectQuestionSingle(Question question) {
        FragmentUtils.replaceFragment(getActivity(), DoSubjectFragment.newInstance(tempQuestions, getPosition(question), question, bookId),
                fragment -> mMainActivity.fragments.add(fragment));
    }

    private int getPosition(Question question) {
        for (int i = 0; i < tempQuestions.size(); i++) {
            if (question.getFullName().equals(tempQuestions.get(i).getFullName())) return i;
        }
        return 0;
    }

    @Override
    public int getResourceId() {
        return R.layout.list_subject_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.exercise;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onStop() {
        mMainActivity.onShowBottomBar(true);
        super.onStop();
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void getListQuestionSuccess(String point, List<Question> questions) {
        txtPoint.setText(point);

        if (questions.size() < 1) {
            txtResetQuestion.setVisibility(View.GONE);
            ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.empty_subject));
            errorBoxDialog.show();
        } else {
            txtResetQuestion.setVisibility(View.VISIBLE);
            subjectAdapter.setDataList(questions);
            tempQuestions = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                if (questions.get(i).getChildren().size() > 0) {
                    for (int j = 0; j < questions.get(i).getChildren().size(); j++) {
                        Question question = questions.get(i).getChildren().get(j);
                        question.setFullName(String.format(getString(R.string.question_gop_name), i + 1, j + 1, question.getName()));
                        question.setChildrenId(question.getId());
                        question.setRootId(question.getQuestionId());
                        question.setChild(true);
                        tempQuestions.add(questions.get(i).getChildren().get(j));
                    }
                } else {
                    Question question = questions.get(i);
                    question.setFullName(String.format(getString(R.string.question_name_title), i + 1, question.getName()));
                    question.setRootId(question.getId());
                    question.setChildrenId(0);
                    question.setChild(false);
                    tempQuestions.add(questions.get(i));
                }
            }
        }
    }

    @Override
    public void resetQuestionSuccess() {
        ToastUtils.showToast(getContext(), getContext().getResources().getString(R.string.reset_question_success));
        presenter.getListQuestion(bookId);
    }

    @OnClick(R.id.txtResetQuestion)
    void onResetQuestion() {
        ErrorConfirmDialog errorConfirmDialog = new ErrorConfirmDialog(getContext(), getString(R.string.repeat), getString(R.string.reset_question_content), ok -> presenter.onResetQuestion(bookId));
        errorConfirmDialog.show();
    }
}
