package com.mc.books.fragments.more.listfriend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.bon.sharepreferences.AppPreferences;
import com.mc.adapter.ListFriendAdapter;
import com.mc.adapter.ListMemberAdapter;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.recyclerview.ExtRecyclerView;
import com.mc.customizes.searchbar.SearchBar;
import com.mc.models.more.Friend;
import com.mc.models.more.Members;
import com.mc.utilities.Constant;
import com.mc.utilities.SocialUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;


public class ListFriendFragment extends BaseMvpFragment<IListFriendView, IListFriendPresenter<IListFriendView>> implements IListFriendView {
    Unbinder unbinder;

    public static ListFriendFragment newInstance() {
        Bundle args = new Bundle();
        ListFriendFragment fragment = new ListFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.fabUser)
    FloatingActionButton fabUser;
    @BindView(R.id.fabGoogle)
    FloatingActionButton fabGoogle;
    @BindView(R.id.fabFacebook)
    FloatingActionButton fabFacebook;
    @BindView(R.id.btn_friend)
    Button btnFriend;
    @BindView(R.id.btn_member)
    Button btnMember;
    @BindView(R.id.rvFriend)
    ExtRecyclerView rvFriend;
    @BindView(R.id.rvMember)
    ExtRecyclerView rvMember;
    @BindView(R.id.searchbar)
    SearchBar searchBar;

    Animation fabClose, fabOpen;
    boolean isOpen = false;

    private ListFriendAdapter friendAdapter;
    private ListMemberAdapter memberAdapter;
    private static final int TAB_FRIEND = 0;
    private static final int TAB_MEMBER = 1;
    private int DEFAULT_TAB = TAB_FRIEND;
    String userId;
    private List<Friend> mListFriend = new ArrayList<>();
    private List<Members> mListMembers = new ArrayList<>();
    private String urlShareAndroid;
    boolean isSearchOff;
    String keyword = "";

    @NonNull
    @Override
    public IListFriendPresenter<IListFriendView> createPresenter() {
        return new ListFriendPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        loadFriend();
        loadMember();
    }

    private void loadMember() {
        presenter.getListMember(rvMember.getItemCount(), keyword);
    }


    private void loadFriend() {
        if (!isSearchOff || rvFriend.getItemCount() == 0)
            presenter.getMyFriend(rvFriend.getItemCount());

    }

    private void initView() {
        try {

            urlShareAndroid = AppPreferences.getInstance(getAppContext()).getString(Constant.KEY_ANDROID_SHARE);
            searchBar.setColorBgSearchBar(getResources().getDrawable(R.drawable.bg_btn_white));
            userId = AppPreferences.getInstance(getAppContext()).getString(Constant.KEY_USER_ID);
            fabOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
            fabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);

            friendAdapter = new ListFriendAdapter(getContext(), null, s -> presenter.unFollowFriend(userId, s));
            rvFriend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rvFriend.setAdapter(friendAdapter)
                    .setLoadMoreListener(this::loadFriend)
                    .setRefreshListener(this::loadFriend)
                    .build();

            memberAdapter = new ListMemberAdapter(getContext(), null, listMember -> {
                if (listMember.getIsFriend()) {
                    presenter.unFollowFriend(userId, listMember.getId());
                } else {
                    presenter.followFriend(userId, listMember.getId());
                }
            });
            rvMember.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rvMember.setAdapter(memberAdapter)
                    .setLoadMoreListener(this::loadMember)
                    .setRefreshListener(this::loadMember)
                    .build();

            renderTabContent(TAB_FRIEND);
        } catch (NoClassDefFoundError e) {
            Log.e("123456", e.getMessage());
        }
    }

    @Override
    public int getResourceId() {
        return R.layout.listfriend_fragment;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }

    @Override
    public int getTitleId() {
        return R.string.list_friend;
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
        rvFriend.setVisibility(tab == TAB_MEMBER ? View.GONE : View.VISIBLE);
        rvMember.setVisibility(tab == TAB_MEMBER ? View.VISIBLE : View.GONE);
        btnMember.setBackground(getContext().getDrawable(tab == TAB_MEMBER ? R.drawable.bg_btn_orange : R.drawable.bg_btn_white));
        btnMember.setTextColor(getContext().getResources().getColor(tab == TAB_MEMBER ? R.color.colorWhite : R.color.colorTextGray));
        btnFriend.setBackground(getContext().getDrawable(tab == TAB_MEMBER ? R.drawable.bg_btn_white : R.drawable.bg_btn_orange));
        btnFriend.setTextColor(getContext().getResources().getColor(tab == TAB_MEMBER ? R.color.colorTextGray : R.color.colorWhite));
        DEFAULT_TAB = tab;

        if (tab == TAB_FRIEND) {
            searchBar.onSearchOffline(keyword -> {
                isSearchOff = !keyword.equals("");
                List<Friend> friendList = StreamSupport.stream(mListFriend).filter(friend -> friend.getFirstName().toLowerCase().contains(keyword)).collect(Collectors.toList());
                rvFriend.clear();
                rvFriend.addItems(friendList);
            });
        } else {
            searchBar.onSearchOffline(null);
            searchBar.onSearch(keyword -> {
                this.keyword = keyword;
                rvMember.clear();
                presenter.getListMember(rvMember.getItemCount(), keyword);
            });
        }
    }


    private void animateFab() {
        if (isOpen) {
            fabFacebook.startAnimation(fabClose);
            fabGoogle.startAnimation(fabClose);
            fabFacebook.setClickable(false);
            fabGoogle.setClickable(false);
            isOpen = false;
        } else {
            fabFacebook.startAnimation(fabOpen);
            fabGoogle.startAnimation(fabOpen);
            fabFacebook.setClickable(true);
            fabGoogle.setClickable(true);
            isOpen = true;
        }

    }

    @OnClick({R.id.btn_member, R.id.btn_friend, R.id.fabUser, R.id.fabGoogle, R.id.fabFacebook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_member:
                rvMember.setVisibility(View.VISIBLE);
                if (DEFAULT_TAB == TAB_MEMBER) return;
                else renderTabContent(TAB_MEMBER);
                rvMember.clear();
                presenter.getListMember(rvMember.getItemCount(), keyword);
                break;
            case R.id.btn_friend:
                if (DEFAULT_TAB == TAB_FRIEND) return;
                else renderTabContent(TAB_FRIEND);
                rvFriend.clear();
                presenter.getMyFriend(rvFriend.getItemCount());
                break;
            case R.id.fabUser:
                animateFab();
                break;

            case R.id.fabGoogle:
                SocialUtils.shareGPlusSocial(getActivity(), urlShareAndroid, getString(R.string.title_mail));
                break;

            case R.id.fabFacebook:
                SocialUtils.shareFacebookSocial(getActivity(), urlShareAndroid);
                break;
        }

    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void getMyFriendListSuccess(List<Friend> listFriends) {
        if (listFriends.size() > 0) {
            mListFriend.clear();
            mListFriend.addAll(listFriends);
            rvFriend.addItems(listFriends);
        }

    }

    @Override
    public void showUnFriendSuccess() {

    }

    @Override
    public void showFollowSucces() {

    }

    @Override
    public void getListMemberSuccess(List<Members> listMembers, String key) {
//        mListMembers.clear();
        if (listMembers.size() > 0) {
            mListMembers.clear();
            mListMembers.addAll(listMembers);
            rvMember.addItems(listMembers);
        }

    }

}
