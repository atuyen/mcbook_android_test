package com.mc.books.fragments.gift.myGift;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.collection.CollectionUtils;
import com.bon.jackson.JacksonUtils;
import com.bon.util.ToastUtils;
import com.mc.adapter.GiftAdapter;
import com.mc.books.R;
import com.mc.books.dialog.AddGiftDialog;
import com.mc.books.dialog.DeleteGiftDialog;
import com.mc.books.dialog.MessageBoxDialog;
import com.mc.books.fragments.gift.examinfo.ExamInfomationFragment;
import com.mc.books.fragments.home.lesson.LessonFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.searchbar.SearchBar;
import com.mc.models.gift.AddGift;
import com.mc.models.gift.CategoryGift;
import com.mc.models.gift.GiftTemp;
import com.mc.models.home.DialogBookMenuItem;
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
import java8.util.stream.StreamSupport;

public class GiftFragment extends BaseMvpFragment<IGiftView, IGiftPresenter<IGiftView>> implements IGiftView, SwipeRefreshLayout.OnRefreshListener {
    public static GiftFragment newInstance() {
        Bundle args = new Bundle();
        GiftFragment fragment = new GiftFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.searchbar)
    SearchBar searchbar;
    @BindView(R.id.rvGift)
    RecyclerView rvGift;
    @BindView(R.id.fbAddGift)
    LinearLayout fbAddGift;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    GiftAdapter giftAdapter;
    private CustomPowerMenu dialogGiftMenu;
    private AddGiftDialog addGiftDialog;
    private static final int DELETE_GIFT = 0;
    List<CategoryGift> categoryGift = new ArrayList<>();

    @NonNull
    @Override
    public IGiftPresenter<IGiftView> createPresenter() {
        return new GiftPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.gift_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.gift;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        presenter.onGetCategoryGift();
        searchbar.clear();
        initData();
        if (!isValidNetwork()) {
//            loadOfflineData();
        }
    }

    private void loadOfflineData() {
        String data = AppUtils.readFromFile(getContext(), Constant.MY_GIFT);
        categoryGift = JacksonUtils.convertJsonToListObject(data, CategoryGift.class);
        onGetCategoryGiftSuccess(categoryGift);
    }

    void initData() {
        try {
            giftAdapter = new GiftAdapter(mActivity, this::goToGift, imgMoreViewHolder -> presenter.onShowDialogDelete(true, imgMoreViewHolder, -1),
                    this::onDeleteGift);
            rvGift.setLayoutManager(new LinearLayoutManager(mActivity));
            rvGift.setAdapter(giftAdapter);
            dialogGiftMenu = MenuUtil.getGiftMenu(getAppContext(), this, onGiftmenuListener);
            searchbar.onSearch(keyword -> presenter.onSearchGift(keyword, categoryGift));
            pullToRefresh.setOnRefreshListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToGift(GiftTemp gift) {
        switch (gift.getGift().getType()) {
            case Constant.KEY_COURSE:
                if (!isValidNetwork()) {
                    showNetworkRequire();
                    return;
                }
                if (gift.getGift().getContentUri() == null) return;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(gift.getGift().getContentUri())));
                break;
            case Constant.KEY_EXAM:
                if (!isValidNetwork()) {
                    showNetworkRequire();
                    return;
                }
                FragmentUtils.replaceFragment(getActivity(), ExamInfomationFragment.newInstance(gift.getGift().getName(), gift.getGift().getExamData().getId(), gift.getGift().getId(), gift.getGiftUserId()), fragment -> mMainActivity.fragments.add(fragment));
                break;
            default:
                FragmentUtils.replaceFragment(getActivity(), LessonFragment.newInstance(null, gift.getGifts(), gift.getIndex(), -1, gift.getId()),
                        fragment -> mMainActivity.fragments.add(fragment));
                break;
        }
    }

    private void onDeleteGift(CategoryGift gift) {
        if (!isValidNetwork()) {
            showNetworkRequire();
            return;
        }
        DeleteGiftDialog deleteGiftDialog = new DeleteGiftDialog(getContext(), v -> presenter.deleteGift(gift.getGiftUserId()));
        deleteGiftDialog.show();
    }

    private OnMenuItemClickListener<DialogBookMenuItem> onGiftmenuListener = (position, item) -> presenter.onShowDialogDelete(false, null, position);

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onShowDialogDelete(boolean isShow, View dialog, int position) {
        if (isShow)
            dialogGiftMenu.showAsDropDown(dialog, -350, 0);
        else {
            dialogGiftMenu.dismiss();
            if (position == DELETE_GIFT) {
                DeleteGiftDialog deleteBookDialog = new DeleteGiftDialog(getActivity(),
                        deleteConsumer -> ToastUtils.showToast(getActivity(), getString(R.string.deleted)));
                deleteBookDialog.show();
            }
        }
    }


    @Override
    public void onGetCategoryGiftSuccess(List<CategoryGift> categoryGift) {
        List<CategoryGift> newCategoryGifts = new ArrayList<>();
        if (CollectionUtils.isNotNullOrEmpty(categoryGift)) {
            StreamSupport.stream(categoryGift).forEach(categoryGift1 -> {
                if (categoryGift1.getGifts().size() > 0)
                    newCategoryGifts.add(categoryGift1);
            });
        }
        giftAdapter.setDataList(newCategoryGifts);
    }

    @Override
    public void onCreateGiftFail(int stringError) {
        addGiftDialog.onShowError(true, getString(stringError));
    }

    @Override
    public void onCreateGiftSuccess(AddGift addGift) {
        addGiftDialog.dismiss();
        MessageBoxDialog messageBoxDialog = new MessageBoxDialog(getActivity(), getResources().getString(R.string.add_gift_success, addGift.getName()), addGift.getGifts());
        messageBoxDialog.show();
        presenter.onGetCategoryGift();
    }

    @Override
    public void onDeleteGiftSuccess() {
        presenter.onGetCategoryGift();
    }

    @OnClick(R.id.fbAddGift)
    public void onViewClicked() {
        if (!isValidNetwork()) {
            showNetworkRequire();
            return;
        }
        addGiftDialog = new AddGiftDialog(getActivity(), (code) -> presenter.onCreateGiftCode(code));
        addGiftDialog.show();
    }

    @Override
    public void onRefresh() {
        pullToRefresh.setRefreshing(false);
        presenter.onGetCategoryGift();
    }
}
