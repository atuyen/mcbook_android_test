package com.mc.books.fragments.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.mc.adapter.NotificationAdapter;
import com.mc.books.R;
import com.mc.books.fragments.home.infomationBook.InformationBookFragment;
import com.mc.books.fragments.home.listLesson.ListLessonFragment;
import com.mc.books.fragments.home.listSubject.ListSubjectFragment;
import com.mc.books.fragments.notification.detailnotification.DetailNotificationFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.recyclerview.ExtRecyclerView;
import com.mc.models.notification.Notification;
import com.mc.models.notification.UnReadNotification;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import java.util.List;

import butterknife.BindView;

import static com.mc.utilities.Constant.KEY_COMMON_BOOK_NOTI;
import static com.mc.utilities.Constant.KEY_COURSES_BOOK_NOTI;
import static com.mc.utilities.Constant.KEY_EXERCISES_BOOK_NOTI;
import static com.mc.utilities.Constant.KEY_TYPE_NOTI_MANUAL;

public class NotificationFragment extends BaseMvpFragment<INotificationView, INotificationPresenter<INotificationView>> implements INotificationView {
    public static NotificationFragment newInstance() {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rvNotification)
    ExtRecyclerView rvNotification;

    @Override
    public INotificationPresenter<INotificationView> createPresenter() {
        return new NotificationPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        loadData();
        mMainActivity.onShowBottomBar(true);
    }

    private void loadData() {
        presenter.getListNotification(rvNotification.getItemCount());
    }

    private void initView() {
        rvNotification.setAdapter(new NotificationAdapter(getContext(), null, this::directNotification))
                .setLoadMoreListener(this::loadData)
                .setRefreshListener(this::loadData)
                .build();
    }

    private void directNotification(Notification notification){
        presenter.getUnReadNoti(notification.getId());
        if (notification.getType().equals(Constant.KEY_TYPE_NOTI_MYBOOK)) {
            int bookId = notification.getDetailMsg().getId();
            switch (notification.getAction()) {
                case KEY_COMMON_BOOK_NOTI:
                    mMainActivity.changeTabBottom(Constant.HOME_TAB);
                    FragmentUtils.replaceFragment(getActivity(), InformationBookFragment.newInstance(bookId), fragment -> mMainActivity.fragments.add(fragment));
                    break;

                case KEY_COURSES_BOOK_NOTI:
                    mMainActivity.changeTabBottom(Constant.HOME_TAB);
                    FragmentUtils.replaceFragment(getActivity(), ListLessonFragment.newInstance(bookId, 0), fragment -> mMainActivity.fragments.add(fragment));
                    break;

                case KEY_EXERCISES_BOOK_NOTI:
                    mMainActivity.changeTabBottom(Constant.HOME_TAB);
                    FragmentUtils.replaceFragment(getActivity(), ListSubjectFragment.newInstance(bookId), fragment -> mMainActivity.fragments.add(fragment));
                    break;
            }

        } else if (notification.getType().equals(KEY_TYPE_NOTI_MANUAL)) {
            int id = notification.getDataId();
            FragmentUtils.replaceFragment(getActivity(), DetailNotificationFragment.newInstance(id), fragment -> mMainActivity.fragments.add(fragment));
        }
    }

    @Override
    public int getTitleId() {
        return R.string.notification;
    }

    @Override
    public int getResourceId() {
        return R.layout.notification_fragment;
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void showNotification(List<Notification> notifications) {
        rvNotification.addItems(notifications);
    }

    @Override
    public void showUnReadNoti(UnReadNotification unReadNotification) {
        Log.e("tuan", String.valueOf(unReadNotification.getIsRead()));
    }
}
