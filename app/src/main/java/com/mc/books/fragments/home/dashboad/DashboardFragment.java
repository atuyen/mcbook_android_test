package com.mc.books.fragments.home.dashboad;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bon.jackson.JacksonUtils;
import com.bon.sharepreferences.AppPreferences;
import com.mc.adapter.BookSectionAdapter;
import com.mc.books.R;
import com.mc.books.dialog.DeleteBookDialog;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.dialog.WarningGirlDialog;
import com.mc.books.fragments.home.booktab.BookTabFragment;
import com.mc.books.fragments.home.scanQRcode.ScanQRCodeFragment;
import com.mc.books.fragments.home.statistics.StatisticsFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.decoration.SpacesItemDecoration;
import com.mc.customizes.searchbar.SearchBar;
import com.mc.models.home.BookResponse;
import com.mc.models.home.CategoryResponse;
import com.mc.models.home.DeleteBook;
import com.mc.models.home.DialogBookMenuItem;
import com.mc.models.more.Config;
import com.mc.models.notification.NotificationLog;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;
import com.mc.utilities.MenuUtil;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.mc.utilities.Constant.TYPE_QR_BOOK;

public class DashboardFragment extends BaseMvpFragment<IDashboardView, IDashboardPresenter<IDashboardView>> implements IDashboardView, SwipeRefreshLayout.OnRefreshListener {
    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rvBook)
    RecyclerView rvBook;
    @BindView(R.id.fbCreateBook)
    LinearLayout fbCreateBook;
    @BindView(R.id.rlDashbroad)
    RelativeLayout rlDashbroad;
    @BindView(R.id.searchbar)
    SearchBar searchbar;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    private static final int DELETE_BOOK = 0;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private CustomPowerMenu dialogBookMenu;
    private int bookId;
    List<CategoryResponse> categoryResponseList = new ArrayList<>();

    @NonNull
    @Override
    public IDashboardPresenter<IDashboardView> createPresenter() {
        return new DashboardPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.dashbroad_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        setHasOptionsMenu(true);
        showDialogWarning();
        searchbar.clear();
        initData();
        presenter.onLoadCategoryBook();
        presenter.getConfig();
        presenter.getLogNotification();

        //load offline data
        if (!isValidNetwork()) loadOfflineData();
        mMainActivity.onShowBottomBar(true);
    }

    private void loadOfflineData() {
        String data = AppUtils.readFromFile(getContext(), Constant.MY_BOOK);
        categoryResponseList = JacksonUtils.convertJsonToListObject(data, CategoryResponse.class);
        onGetCategorySuccess(categoryResponseList);
    }

    private void showDialogWarning() {
        if (!AppPreferences.getInstance(getContext()).getBoolean(Constant.KEY_INSTALLED_APP)) {
            AppPreferences.getInstance(getContext()).putBoolean(Constant.KEY_INSTALLED_APP, true);
            WarningGirlDialog warningGirlDialog = new WarningGirlDialog(getContext(), android.R.style.Theme_Light);
            warningGirlDialog.show();
        }
    }

    void initData() {
        try {
            sectionAdapter = new SectionedRecyclerViewAdapter();
            dialogBookMenu = MenuUtil.getBookMenu(getAppContext(), this, onBookmenuListener);

            searchbar.onSearch(keyword -> presenter.onSearchBook(keyword, categoryResponseList));
            if (AppUtils.isTablet(getContext())) {
                GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
                glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch (sectionAdapter.getSectionItemViewType(position)) {
                            case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                                return 2;
                            default:
                                return 1;
                        }
                    }
                });
                rvBook.setLayoutManager(glm);
                rvBook.addItemDecoration(new SpacesItemDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.spacing)));
            } else
                rvBook.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

            rvBook.setAdapter(sectionAdapter);

            pullToRefresh.setOnRefreshListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTitleId() {
        return R.string.my_book;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mark:
                if (!isValidNetwork()) {
                    showNetworkRequire();
                    return false;
                }
                FragmentUtils.replaceFragment(getActivity(), StatisticsFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
            default:
                return false;
        }
        return true;
    }

    private OnMenuItemClickListener<DialogBookMenuItem> onBookmenuListener = (position, item) -> presenter.showContextMenu(false, null, position);

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void showContextMenuSuccess(boolean isShow, DeleteBook deleteBook, int position) {
        if (isShow) {
            dialogBookMenu.showAsDropDown(deleteBook.getView(), -350, 0);
            bookId = deleteBook.getBookResponse().getId();
        } else {
            dialogBookMenu.dismiss();
            if (position == DELETE_BOOK) {
                if (!isValidNetwork()) {
                    showNetworkRequire();
                    return;
                }
                DeleteBookDialog deleteBookDialog = new DeleteBookDialog(getActivity(), deleteConsumer -> presenter.onDeleteBook(bookId));
                deleteBookDialog.show();
            }
        }
    }

    @Override
    public void deleteBookSuccess(String message) {
        bookId = -1;
        presenter.onLoadCategoryBook();
    }

    @Override
    public void onGetCategorySuccess(List<CategoryResponse> categoryResponseList) {
        sectionAdapter.removeAllSections();
        for (CategoryResponse categoryResponse : categoryResponseList) {
            sectionAdapter.addSection(new BookSectionAdapter(getAppContext(), categoryResponse.getName(), categoryResponse.getBooks(),
                    deleteBookConsumer -> presenter.showContextMenu(true, deleteBookConsumer, -1), this::goToBookTab));
        }
        sectionAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetCategoryError(int message) {
        if (message == -1)
            return;
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(message));
        errorBoxDialog.show();
    }

    @Override
    public void onGetConfigSuccess(Config config) {
        AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_ANDROID_SHARE, config.getAndroidURL());
    }

    @Override
    public void onShowLogNotification(NotificationLog notificationLog) {
    }

    private void goToBookTab(BookResponse bookResponse) {
        FragmentUtils.replaceFragment(getActivity(), BookTabFragment.newInstance(bookResponse), fragment -> mMainActivity.fragments.add(fragment));
    }

    @OnClick(R.id.fbCreateBook)
    public void onViewClicked() {
        if (!isValidNetwork()) {
            showNetworkRequire();
            return;
        }
        FragmentUtils.replaceFragment(getActivity(), ScanQRCodeFragment.newInstance(TYPE_QR_BOOK, -1), fragment -> mMainActivity.fragments.add(fragment));
    }

    @Override
    public void onRefresh() {
        pullToRefresh.setRefreshing(false);
        presenter.onLoadCategoryBook();
    }
}
