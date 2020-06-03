package com.mc.books.fragments.notification.detailnotification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.notification.DetailNotificationAdmin;
import com.mc.utilities.AppUtils;

import butterknife.BindView;

public class DetailNotificationFragment extends BaseMvpFragment<IDetailNotificationView, IDetailNotificationPresenter<IDetailNotificationView>> implements IDetailNotificationView {

    @BindView(R.id.txtTitle)
    ExtTextView txtTitle;
    @BindView(R.id.imageNoti)
    ImageView imageNoti;
    @BindView(R.id.txtDescription)
    ExtTextView txtDescription;
    private static int idAdmin;

    public static DetailNotificationFragment newInstance(int id) {
        Bundle args = new Bundle();
        DetailNotificationFragment fragment = new DetailNotificationFragment();
        fragment.setArguments(args);
        idAdmin = id;
        return fragment;
    }

    @Override
    public IDetailNotificationPresenter<IDetailNotificationView> createPresenter() {
        return new DetailNotificationPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.detail_notification_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        presenter.getDetailNotification(idAdmin);
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }

    @Override
    public int getTitleId() {
        return R.string.title_notification;
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void showDetailNotification(DetailNotificationAdmin detailNotification) {
        if (detailNotification.getMediaUri() != null) {
            imageNoti.setVisibility(View.VISIBLE);
            AppUtils.setImageGlide(getAppContext(), detailNotification.getMediaUri() != null ?
                    detailNotification.getMediaUri() : "", R.drawable.ic_img_book_default, imageNoti);
        } else {
            imageNoti.setVisibility(View.GONE);
        }
        txtTitle.setText(detailNotification.getTitle() != null ? detailNotification.getTitle() : "");
        txtDescription.setText(Html.fromHtml(detailNotification.getContent() != null ? detailNotification.getContent() : "").toString().replaceAll("s/<(.*?)>//g", ""));
    }

}

