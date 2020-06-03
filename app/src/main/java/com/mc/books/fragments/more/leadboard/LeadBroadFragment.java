package com.mc.books.fragments.more.leadboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.sharepreferences.AppPreferences;
import com.bon.util.DateTimeUtils;
import com.mc.adapter.LeadBroadBookAdapter;
import com.mc.adapter.LeadBroadMemberAdapter;
import com.mc.books.R;
import com.mc.books.dialog.FilterBookDialog;
import com.mc.books.dialog.FilterMemberDialog;
import com.mc.books.fragments.more.listfriend.ListFriendFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.more.BookLeadBoad;
import com.mc.models.more.BookLeadBoadUpdate;
import com.mc.models.more.BookRanking;
import com.mc.models.more.CategoryLeadBoad;
import com.mc.models.more.UserRanking;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LeadBroadFragment extends BaseMvpFragment<ILeadBroadView, ILeadBroadPresenter<ILeadBroadView>> implements ILeadBroadView {
    Unbinder unbinder;
    private static final String TAG = LeadBroadFragment.class.getCanonicalName();

    public static LeadBroadFragment newInstance() {
        Bundle args = new Bundle();
        LeadBroadFragment fragment = new LeadBroadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.btn_member)
    Button btnMember;
    @BindView(R.id.btn_book)
    Button btnBook;
    @BindView(R.id.txtUpdateTime)
    ExtTextView txtUpdateTime;
    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.fbMember)
    LinearLayout fbMember;
    @BindView(R.id.rvBook)
    RecyclerView rvBook;
    @BindView(R.id.rvMember)
    RecyclerView rvMember;

    private LeadBroadBookAdapter leadBroadBookAdapter;
    private LeadBroadMemberAdapter leadBroadMemberAdapter;
    private static final int TAB_MEMBER = 0;
    private static final int TAB_BOOK = 1;

    private int DEFAULT_TAB = TAB_MEMBER;
    private List<BookRanking> bookRankingList = new ArrayList<>();
    private List<BookRanking> bookRankingFillterList = new ArrayList<>();
    private boolean isLoadBookFillter = false;
    private boolean isLoadMemberFillter = false;
    private List<UserRanking> userRankingList = new ArrayList<>();
    private List<UserRanking> userRankingListFillter = new ArrayList<>();
    private List<BookLeadBoad> bookLeadBoadsList = new ArrayList<>();
    private List<CategoryLeadBoad> categoryLeadBoadsList = new ArrayList<>();
    private List<BookLeadBoad> bookLeadBoadListBox = new ArrayList<>();
    private List<CategoryLeadBoad> categoryLeadBoadListBox = new ArrayList<>();
    private String userId;

    @NonNull
    @Override
    public ILeadBroadPresenter<ILeadBroadView> createPresenter() {
        return new LeadBroadPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    private void initView() {
        userId = AppPreferences.getInstance(getAppContext()).getString(Constant.KEY_USER_ID);
        Log.e(TAG, "userId " + userId);
        isLoadMemberFillter = false;
        isLoadBookFillter = false;
        presenter.getLeadBroadMember(Constant.ALL_FILTER, Constant.ALL_FILTER, Constant.WEEK_FILTER);
        presenter.getLeadBroadBook(Constant.ALL_FILTER, Constant.ALL_FILTER, Constant.WEEK_FILTER);
        presenter.getCategoryLeadBoad();
        presenter.getBookLeadBoad();
        presenter.getBookLeadBoadUpdate(Constant.DATE_FILTER);
        leadBroadBookAdapter = new LeadBroadBookAdapter(getContext());
        rvBook.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvBook.setAdapter(leadBroadBookAdapter);

        leadBroadMemberAdapter = new LeadBroadMemberAdapter(getContext(), userId);
        rvMember.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvMember.setAdapter(leadBroadMemberAdapter);
    }

    @Override
    public int getResourceId() {
        return R.layout.leadbroad_fragment;
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void getLeadBroadMemberSuccess(List<UserRanking> leadMemberBroads) {
        if (leadMemberBroads != null) {
            if (isLoadMemberFillter) {
                userRankingListFillter.clear();
                userRankingListFillter.addAll(leadMemberBroads);
                leadBroadMemberAdapter.setDataList(userRankingListFillter);
            } else {
                userRankingList.clear();
                userRankingList.addAll(leadMemberBroads);
                leadBroadMemberAdapter.setDataList(userRankingList);
            }
        }
    }

    @Override
    public void getLeadBroadBookSuccess(List<BookRanking> leadBookBroads) {
        if (leadBookBroads != null) {
            if (isLoadBookFillter) {
                bookRankingFillterList.clear();
                bookRankingFillterList.addAll(leadBookBroads);
                leadBroadBookAdapter.setDataList(bookRankingFillterList);
            } else {
                bookRankingList.clear();
                bookRankingList.addAll(leadBookBroads);
                leadBroadBookAdapter.setDataList(bookRankingList);
            }
        }
    }

    @Override
    public void getBookLeadBoadSuccess(List<BookLeadBoad> bookLeadBoads) {
        if (isLoadMemberFillter) {
            bookLeadBoadListBox.clear();
            bookLeadBoadListBox.addAll(bookLeadBoads);
            filterMemberDialog.setDataSearch(bookLeadBoadListBox);
        } else {
            bookLeadBoadsList.clear();
            bookLeadBoadsList.addAll(bookLeadBoads);
            filterMemberDialog.setDataSearch(bookLeadBoadsList);
        }

    }

    @Override
    public void getCategoryLeadBoad(List<CategoryLeadBoad> categoryLeadBoads) {
        if (isLoadBookFillter) {
            categoryLeadBoadListBox.clear();
            categoryLeadBoadListBox.addAll(categoryLeadBoads);
            filterBookDialog.setDataSearch(categoryLeadBoadListBox);
        } else {
            categoryLeadBoadsList.clear();
            categoryLeadBoadsList.addAll(categoryLeadBoads);
            filterBookDialog.setDataSearch(categoryLeadBoadsList);
        }

    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void getBookLeadBoadUpdateSuccess(BookLeadBoadUpdate bookLeadBoadUpdates) {
        String dateTime = DateTimeUtils.convertDateTimeUpdate(bookLeadBoadUpdates.getCreatedAt());
        txtUpdateTime.setText(String.format(getString(R.string.time_update, dateTime)));
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }

    @Override
    public int getTitleId() {
        return R.string.charts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void renderTabContent(int tab) {
        rvMember.setVisibility(tab == TAB_MEMBER ? View.VISIBLE : View.GONE);
        rvBook.setVisibility(tab == TAB_MEMBER ? View.GONE : View.VISIBLE);
        btnMember.setBackground(getContext().getDrawable(tab == TAB_MEMBER ? R.drawable.bg_btn_orange : R.drawable.bg_btn_white));
        btnMember.setTextColor(getContext().getResources().getColor(tab == TAB_MEMBER ? R.color.colorWhite : R.color.colorTextGray));
        btnBook.setBackground(getContext().getDrawable(tab == TAB_MEMBER ? R.drawable.bg_btn_white : R.drawable.bg_btn_orange));
        btnBook.setTextColor(getContext().getResources().getColor(tab == TAB_MEMBER ? R.color.colorTextGray : R.color.colorWhite));
        DEFAULT_TAB = tab;
    }

    FilterMemberDialog filterMemberDialog;
    FilterBookDialog filterBookDialog;

    private void openFilter() {
        if (DEFAULT_TAB == TAB_MEMBER) {
            isLoadMemberFillter = true;
            filterMemberDialog = new FilterMemberDialog(getContext(),
                    filter -> presenter.getLeadBroadMember(filter.getFilterBook(), filter.getFilterUser(), filter.getFilterTime()),
                    android.R.style.Theme_Light, getFragmentManager(), bookLeadBoadsList, bookLeadBoadListBox,
                    s -> presenter.getBookSearch("0", "20", s), fillterTime -> {
                presenter.getBookLeadBoadUpdate(fillterTime.getFilterTime());
            });
            filterMemberDialog.show();
        } else {
            isLoadBookFillter = true;
            filterBookDialog = new FilterBookDialog(getContext(),
                    filter -> presenter.getLeadBroadBook(filter.getCategory(), filter.getFilterBook(), filter.getFilterTime()),
                    android.R.style.Theme_Light, getFragmentManager(), categoryLeadBoadsList, categoryLeadBoadListBox,
                    s -> presenter.getCategoryFillter(s), fillterTime -> {
                presenter.getBookLeadBoadUpdate(fillterTime.getFilterTime());
            });
            filterBookDialog.show();
        }
    }

    @OnClick({R.id.btn_member, R.id.btn_book, R.id.img_filter, R.id.fbMember})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_member:
                if (DEFAULT_TAB == TAB_MEMBER) return;
                else renderTabContent(TAB_MEMBER);
                isLoadMemberFillter = false;
                break;
            case R.id.btn_book:
                if (DEFAULT_TAB == TAB_BOOK) return;
                else renderTabContent(TAB_BOOK);
                isLoadBookFillter = false;
                break;
            case R.id.img_filter:
                openFilter();
                break;
            case R.id.fbMember:
                FragmentUtils.replaceFragment(getActivity(), ListFriendFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
        }
    }
}

