package com.mc.books.fragments.home.infomationBook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.jackson.JacksonUtils;
import com.mc.adapter.AuthorAdapter;
import com.mc.adapter.ChapterAdapter;
import com.mc.adapter.RelatedBookAdapter;
import com.mc.books.R;
import com.mc.books.fragments.home.listLesson.ListLessonFragment;
import com.mc.books.fragments.home.listRelatedBook.ListRelatedBookFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.BookResponse;
import com.mc.models.home.RelatedBook;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.mc.utilities.Constant.BOOK_ID;
import static com.mc.utilities.Constant.LINE_SHOW_MORE;
import static com.mc.utilities.Constant.MAXIUM_LINE;
import static com.mc.utilities.Constant.MAX_LINE;

public class InformationBookFragment extends BaseMvpFragment<IInformationBookView, IInformationBookPresenter<IInformationBookView>> implements IInformationBookView {

    public static InformationBookFragment newInstance(int bookId) {
        Bundle args = new Bundle();
        InformationBookFragment fragment = new InformationBookFragment();
        args.putInt(BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.txtGeneral)
    ExtTextView txtGeneral;
    @BindView(R.id.txtReadMore)
    ExtTextView txtReadMore;
    @BindView(R.id.txtReadMoreProgramStudy)
    ExtTextView txtReadMoreProgramStudy;
    @BindView(R.id.rvAuthor)
    RecyclerView rvAuthor;
    @BindView(R.id.rvProgramStudy)
    RecyclerView rvProgramStudy;
    @BindView(R.id.llProgramStudy)
    LinearLayout llProgramStudy;
    @BindView(R.id.llCommunityBook)
    LinearLayout llCommunityBook;
    @BindView(R.id.rvRelatedBook)
    RecyclerView rvRelatedBook;
    @BindView(R.id.txtReadMoreRelatedBook)
    ExtTextView txtReadMoreRelatedBook;


    private boolean isReadMoreGeneal = false;
    private boolean isReadMoreProgramStudy = false;
    private ChapterAdapter chapterAdapter;
    private BookResponse bookResponse;

    @Override
    public IInformationBookPresenter<IInformationBookView> createPresenter() {
        return new InformationBookPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.information_book_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.information_book;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        presenter.getDetailBook(getArguments().getInt(BOOK_ID));
        mMainActivity.onShowBottomBar(true);
        if (!isValidNetwork()) loadOfflineData();
    }

    private void loadOfflineData() {
        String data = AppUtils.readFromFile(getContext(), Constant.INFO_BOOK + "_" + getArguments().getInt(BOOK_ID));
        BookResponse bookResponse = JacksonUtils.convertJsonToObject(data, BookResponse.class);
        onGetDetailBookSuccess(bookResponse);
    }

    void initData() {
        try {
            txtGeneral.setText(bookResponse.getDescription());

            AuthorAdapter authorAdapter = new AuthorAdapter(getAppContext());
            rvAuthor.setLayoutManager(new GridLayoutManager(getAppContext(), 2));
            rvAuthor.setAdapter(authorAdapter);
            rvAuthor.setNestedScrollingEnabled(false);
            authorAdapter.setDataList(bookResponse.getAuthors());

            chapterAdapter = new ChapterAdapter(getAppContext(), chapter -> goToDetailProgramStudy(chapter.getIndex()));
            rvProgramStudy.setLayoutManager(new LinearLayoutManager(getAppContext()));
            rvProgramStudy.setAdapter(chapterAdapter);
            rvProgramStudy.setNestedScrollingEnabled(false);

            if (bookResponse.getChapters().size() >= LINE_SHOW_MORE) {
                chapterAdapter.setDataList(StreamSupport.stream(bookResponse.getChapters())
                        .limit(MAX_LINE).collect(Collectors.toList()));
                txtReadMoreProgramStudy.setVisibility(View.VISIBLE);
            } else chapterAdapter.setDataList(bookResponse.getChapters());

            txtGeneral.post(() -> {
                int lineCount = txtGeneral.getLineCount();
                if (lineCount > LINE_SHOW_MORE) {
                    txtGeneral.setMaxLines(MAX_LINE);
                    txtReadMore.setVisibility(View.VISIBLE);
                }
            });


            RelatedBookAdapter relatedBookAdapter = new RelatedBookAdapter(getAppContext(), this::goToWebView, this::goToDetailRelatedBook);
            rvRelatedBook.setLayoutManager(new LinearLayoutManager(getAppContext()));
            rvRelatedBook.setAdapter(relatedBookAdapter);
            rvRelatedBook.setNestedScrollingEnabled(false);
            List<RelatedBook> relatedBookList = bookResponse.getRealtedBooks();
            relatedBookAdapter.setDataList(relatedBookList);

            if (relatedBookList.size() >= LINE_SHOW_MORE)
                txtReadMoreRelatedBook.setVisibility(View.VISIBLE);
            else txtReadMoreRelatedBook.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToWebView(String uri) {
        try {
            if (!isValidNetwork()) {
                showNetworkRequire();
                return;
            }
            if (uri != null && uri.isEmpty())
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToDetailRelatedBook(RelatedBook relatedBook) {
        try {
            if (!isValidNetwork()) {
                showNetworkRequire();
                return;
            }
            if (relatedBook.getUri() != null)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(relatedBook.getUri())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToDetailProgramStudy(int index) {
        FragmentUtils.replaceFragment(getActivity(), ListLessonFragment.newInstance(bookResponse.getId(), index), fragment -> mMainActivity.fragments.add(fragment));
    }

    @OnClick({R.id.txtReadMore, R.id.llProgramStudy, R.id.txtReadMoreProgramStudy, R.id.llCommunityBook, R.id.txtReadMoreRelatedBook})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.txtReadMore:
                    if (isReadMoreGeneal) {
                        isReadMoreGeneal = false;
                        txtGeneral.setMaxLines(MAX_LINE);
                        txtReadMore.setText(R.string.show_more);
                    } else {
                        isReadMoreGeneal = true;
                        txtGeneral.setMaxLines(MAXIUM_LINE);
                        txtReadMore.setText(R.string.show_less);
                    }
                    break;
                case R.id.llProgramStudy:
                    goToDetailProgramStudy(0);
                    break;
                case R.id.txtReadMoreProgramStudy:
                    if (isReadMoreProgramStudy) {
                        isReadMoreProgramStudy = false;
                        chapterAdapter.setDataList(StreamSupport.stream(bookResponse.getChapters()).limit(5).collect(Collectors.toList()));
                        txtReadMoreProgramStudy.setText(R.string.show_more);
                    } else {
                        isReadMoreProgramStudy = true;
                        chapterAdapter.setDataList(bookResponse.getChapters());
                        txtReadMoreProgramStudy.setText(R.string.show_less);
                    }
                    break;
                case R.id.llCommunityBook:
                    if (!isValidNetwork()) {
                        showNetworkRequire();
                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bookResponse.getLinkCommunity())));
                    break;
                case R.id.txtReadMoreRelatedBook:
                    if (!isValidNetwork()) {
                        showNetworkRequire();
                        return;
                    }
                    FragmentUtils.replaceFragment(getActivity(), ListRelatedBookFragment.newInstance(bookResponse), fragment -> mMainActivity.fragments.add(fragment));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onGetDetailBookSuccess(BookResponse bookResponse) {
        this.bookResponse = bookResponse;
        initData();
    }
}
