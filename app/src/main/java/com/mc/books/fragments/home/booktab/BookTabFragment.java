package com.mc.books.fragments.home.booktab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.mc.books.R;
import com.mc.books.fragments.home.infomationBook.InformationBookFragment;
import com.mc.books.fragments.home.listLesson.ListLessonFragment;
import com.mc.books.fragments.home.listSubject.ListSubjectFragment;
import com.mc.books.fragments.home.training.TrainingFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.BookResponse;
import com.mc.utilities.FragmentUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mc.utilities.Constant.KEY_BOOK_TAB;

public class BookTabFragment extends BaseMvpFragment<IBookTabView, IBookTabPresenter<IBookTabView>> implements IBookTabView {

    public static BookTabFragment newInstance(BookResponse bookResponse) {
        Bundle args = new Bundle();
        BookTabFragment fragment = new BookTabFragment();
        args.putSerializable(KEY_BOOK_TAB, bookResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.imgBookInformation)
    ImageView imgBookInformation;
    @BindView(R.id.imgBookLesson)
    ImageView imgBookLesson;
    @BindView(R.id.imgBookSubject)
    ImageView imgBookSubject;
    @BindView(R.id.imgBookTraining)
    ImageView imgBookTraining;

    private BookResponse bookResponse;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        try {
            bookResponse = (BookResponse) getArguments().getSerializable(KEY_BOOK_TAB);
            mMainActivity.onShowBottomBar(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBookTabPresenter<IBookTabView> createPresenter() {
        return new BookTabPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.booktab_fragment;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public String getTitleString() {
        return bookResponse.getName();
    }

    @OnClick({R.id.imgBookInformation, R.id.imgBookLesson, R.id.imgBookSubject, R.id.imgBookTraining})
    public void onViewClicked(View view) {
        BaseMvpFragment fragment = null;
        int bookId = bookResponse.getId();
        switch (view.getId()) {
            case R.id.imgBookInformation:
                fragment = InformationBookFragment.newInstance(bookId);
                break;
            case R.id.imgBookLesson:
                fragment = ListLessonFragment.newInstance(bookId, 0);
                break;
            case R.id.imgBookSubject:
                if (!isValidNetwork()) {
                    showNetworkRequire();
                    return;
                }
                fragment = ListSubjectFragment.newInstance(bookId);
                break;
            case R.id.imgBookTraining:
                if (!isValidNetwork()) {
                    showNetworkRequire();
                    return;
                }
                fragment = TrainingFragment.newInstance(bookId, bookResponse.getName());
                break;
        }
        FragmentUtils.replaceFragment(getActivity(), fragment, frag -> mMainActivity.fragments.add(frag));
    }
}
