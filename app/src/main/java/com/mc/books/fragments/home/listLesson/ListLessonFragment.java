package com.mc.books.fragments.home.listLesson;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bon.collection.CollectionUtils;
import com.bon.jackson.JacksonUtils;
import com.bon.util.ToastUtils;
import com.mc.adapter.LessonHomeAdapter;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.dialog.ErrorConfirmDialog;
import com.mc.books.fragments.home.lesson.LessonFragment;
import com.mc.books.fragments.home.scanQRcode.ScanQRCodeFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.searchbar.SearchBar;
import com.mc.events.DownloadEvent;
import com.mc.models.home.Chapter;
import com.mc.models.home.ChapterTemp;
import com.mc.models.home.Lesson;
import com.mc.models.home.LessonTemp;
import com.mc.models.realm.LessonRealm;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import java8.util.stream.StreamSupport;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.mc.utilities.Constant.BOOK_ID;
import static com.mc.utilities.Constant.KEY_INDEX_LESSON;
import static com.mc.utilities.Constant.TYPE_QR_LESSON;

public class ListLessonFragment extends BaseMvpFragment<IListLessonView, IListLessonPresenter<IListLessonView>> implements IListLessonView {

    public static ListLessonFragment newInstance(int bookId, int idxChapter) {
        Bundle args = new Bundle();
        ListLessonFragment fragment = new ListLessonFragment();
        args.putSerializable(BOOK_ID, bookId);
        args.putInt(KEY_INDEX_LESSON, idxChapter);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.searchbar)
    SearchBar searchbar;
    @BindView(R.id.rvListLesson)
    RecyclerView rvListLesson;
    private LessonHomeAdapter lessonHomeAdapter;
    private int idxLesson;
    private int bookId;
    List<Chapter> chapters = new ArrayList<>();
    List<Chapter> chaptersOffline = new ArrayList<>();
    Realm realm;
    MenuItem miDownloadAll, miDownloadingAll, miDownloadDisable;
    ThinDownloadManager downloadManager;

    @NonNull
    @Override
    public IListLessonPresenter<IListLessonView> createPresenter() {
        return new ListLessonPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        setHasOptionsMenu(true);
        try {
            idxLesson = getArguments().getInt(KEY_INDEX_LESSON);
            bookId = getArguments().getInt(BOOK_ID);
            presenter.onGetChapters(bookId);
            realm = AppContext.getRealm();
            downloadManager = AppContext.getDownloadManager();
            initData();
            if (!isValidNetwork()) {
                loadOfflineData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOfflineData() {
        String data = AppUtils.readFromFile(getContext(), Constant.MY_LESSON + "_" + bookId);
        chaptersOffline = JacksonUtils.convertJsonToListObject(data, Chapter.class);
        onGetChapters(chaptersOffline);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.onShowBottomBar(false);
    }

    void initData() {
        try {
            lessonHomeAdapter = new LessonHomeAdapter(mActivity, bookId, idxLesson, this::goToLesson, this::confirmDownloadChapter,
                    this::confirmStopDownloadChapter, this::cancelDialogDownloadFile, this::downloadFile, isValidNetwork());
            rvListLesson.setLayoutManager(new LinearLayoutManager(mActivity));
            rvListLesson.setAdapter(lessonHomeAdapter);

            searchbar.getImgRight().setVisibility(View.VISIBLE);
            searchbar.onSearch(keyword -> presenter.onSearchLesson(getArguments().getInt(BOOK_ID), keyword, null, isValidNetwork() ? chapters : chaptersOffline));
            searchbar.onClickQrCode(v -> {
                if (!isValidNetwork()) showNetworkRequire();
                else
                    FragmentUtils.replaceFragment(getActivity(), ScanQRCodeFragment.newInstance(TYPE_QR_LESSON, bookId),
                            fragment -> mMainActivity.fragments.add(fragment));
            });

            // receiver download file event
            bus.subscribe(this, DownloadEvent.class, event -> {
                if (bookId == event.getBookId()) {
                    RealmResults<LessonRealm> lessonRealms = realm
                            .where(LessonRealm.class)
                            .equalTo(LessonRealm.BOOK_ID, bookId).equalTo(LessonRealm.DOWNLOAD_TYPE, Constant.DOWNLOAD_ALL)
                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                            .beginGroup()
                            .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.DOWNLOADING)
                            .or()
                            .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.PENDIND_DOWNLOAD)
                            .endGroup()
                            .findAll();
                    if (lessonRealms.size() == 0) {
                        miDownloadingAll.setVisible(false);
                        miDownloadAll.setVisible(true);
                    } else {
                        miDownloadingAll.setVisible(true);
                        miDownloadAll.setVisible(false);
                    }
                    if (event.getIndex() != -1)
                        lessonHomeAdapter.notifyItemChanged(event.getIndex());
                }
                showProgress(false);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToLesson(LessonTemp lesson) {
        FragmentUtils.replaceFragment(getActivity(), LessonFragment.newInstance(lesson.getLessons(), null, lesson.getIndex(), bookId, -1),
                fragment -> mMainActivity.fragments.add(fragment));
    }

    @Override
    public int getResourceId() {
        return R.layout.list_lesson_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.lesson;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }


    @Override
    public void onSearchLesson(List<Chapter> chapters) {
        lessonHomeAdapter.setDataList(new ArrayList<>());
        lessonHomeAdapter.setDataList(chapters);
        rvListLesson.scrollToPosition(0);
    }

    @Override
    public void onGetChapters(List<Chapter> chapters) {
        if (chapters.size() <= 0) {
            ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.empty_lesson));
            errorBoxDialog.show();
            miDownloadDisable.setVisible(true);
            miDownloadAll.setVisible(false);
            miDownloadingAll.setVisible(false);
        } else {
            lessonHomeAdapter.setDataList(chapters);
            realm.beginTransaction();
            //insert lesson to DB
            StreamSupport.stream(chapters).forEach(chapter ->
                    StreamSupport.stream(chapter.getLessons()).forEach(lesson -> {
                                LessonRealm lessonRealm = realm
                                        .where(LessonRealm.class)
                                        .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                        .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                        .findFirst();
                                if (lessonRealm == null)
                                    lessonRealm = realm.createObject(LessonRealm.class, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()));

                                lessonRealm.setBookId(bookId);
                                lessonRealm.setName(lesson.getName());
                                lessonRealm.setUserId(AppContext.getUserId());
                            }
                    )
            );
            realm.commitTransaction();

            this.chapters = chapters;
        }
    }

    @Override
    public void onStop() {
        mMainActivity.onShowBottomBar(true);
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_download, menu);

        //custom header gif image
        miDownloadAll = menu.findItem(R.id.action_download);
        miDownloadingAll = menu.findItem(R.id.action_downloading);
        miDownloadDisable = menu.findItem(R.id.action_download_disable);
        if (isValidNetwork()) {
            miDownloadDisable.setVisible(false);
            miDownloadingAll.setVisible(false);
            View actionView = miDownloadingAll.getActionView();
            GifImageView gifImageView = actionView.findViewById(R.id.progress);
            GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
            gifDrawable.setLoopCount(0);
            gifImageView.setOnClickListener(v -> confirmStopDownloadAll());
        } else {
            miDownloadDisable.setVisible(true);
            miDownloadAll.setVisible(false);
            miDownloadingAll.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                confirmDownloadAll();
                break;
            default:
                return false;
        }
        return true;
    }

    private void downloadAll() {
        if (CollectionUtils.isNotNullOrEmpty(chapters)) {
            miDownloadAll.setVisible(false);
            miDownloadingAll.setVisible(true);
            presenter.downloadAllLesson(chapters, bookId);

            //set pendding icon all data download
            new Handler().postDelayed(() -> {
                if (lessonHomeAdapter != null) lessonHomeAdapter.notifyDataSetChanged();
            }, 500);
        } else {
            ToastUtils.showToast(getContext(), getString(R.string.empty_chapter));
        }
    }

    void stopDownloadAll() {
        showProgress(true);
        miDownloadAll.setVisible(true);
        miDownloadingAll.setVisible(false);
        RealmResults<LessonRealm> lessonRealms = realm
                .where(LessonRealm.class)
                .equalTo(LessonRealm.DOWNLOAD_TYPE, Constant.DOWNLOAD_ALL)
                .equalTo(LessonRealm.BOOK_ID, bookId)
                .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                .beginGroup().equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.PENDIND_DOWNLOAD)
                .or()
                .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.DOWNLOADING)
                .endGroup()
                .findAll();
        if (lessonRealms != null && lessonRealms.size() > 0) {
            StreamSupport.stream(lessonRealms).forEach(lessonRealm -> {
                downloadManager.cancel(lessonRealm.getDownloadId());
                if (lessonRealm.getDownloadStatus() == Constant.DOWNLOADING) {
                    File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, lessonRealm.getFileName()));
                    if (file.exists()) file.delete();
                }
                realm.executeTransaction(realm -> {
                    lessonRealm.setDownloadStatus(Constant.NONE_DOWNLOAD);
                    lessonRealm.setProgress(0);
                });
            });
        }
        showProgress(false);
    }

    void downloadFile(Lesson lesson) {
        presenter.downloadLesson(lesson, bookId, lesson.getIndex());
    }

    void cancelDialogDownloadFile(Lesson lesson) {
        ErrorConfirmDialog errorBoxDialog = new ErrorConfirmDialog(getContext(), getString(R.string.yes), getString(R.string.dialog_stop_download), v -> cancelDownloadFile(lesson));
        errorBoxDialog.show();
    }

    void cancelDownloadFile(Lesson lesson) {
        showProgress(true);
        LessonRealm lessonDownload = realm.where(LessonRealm.class)
                .equalTo(LessonRealm.BOOK_ID, bookId)
                .equalTo(LessonRealm.INDEX, lesson.getIndex())
                .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.DOWNLOADING).findFirst();

        if (lessonDownload != null) {
            RealmResults<LessonRealm> results = realm.where(LessonRealm.class)
                    .equalTo(LessonRealm.BOOK_ID, bookId)
                    .equalTo(LessonRealm.INDEX, lesson.getIndex())
                    .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                    .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                    .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.DOWNLOADING)
                    .findAll();

            if (results != null && results.size() > 0) {
                StreamSupport.stream(results).forEach(lessonRealm -> {
                    downloadManager.cancel(lessonRealm.getDownloadId());
                    if (lessonRealm.getDownloadStatus() == Constant.DOWNLOADING) {
                        File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, lessonRealm.getFileName()));
                        if (file.exists()) file.delete();
                    }
                    realm.executeTransaction(realm -> {
                        lessonRealm.setDownloadStatus(Constant.NONE_DOWNLOAD);
                        lessonRealm.setProgress(0);
                    });
                });
            }
            File file = new File(AppUtils.getFilePath(getContext(), Constant.FOLDER_BOOK, bookId, lesson.getMedia()));
            if (file.exists()) file.delete();
            lessonHomeAdapter.notifyItemChanged(lesson.getIndex());
            if (results != null)
                results.deleteAllFromRealm();
        }
        showProgress(false);
    }

    void downloadChapter(ChapterTemp chapterTemp) {
        presenter.downloadChapter(chapterTemp, bookId);
    }

    void stopDownloadChapter(ChapterTemp chapterTemp) {
        showProgress(true);

        RealmResults<LessonRealm> lessonRealms = realm
                .where(LessonRealm.class)
                .equalTo(LessonRealm.DOWNLOAD_TYPE, Constant.DOWNLOAD_CHAPTER)
                .equalTo(LessonRealm.BOOK_ID, bookId)
                .equalTo(LessonRealm.INDEX, chapterTemp.getIndex())
                .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                .beginGroup()
                .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.PENDIND_DOWNLOAD)
                .or().equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.DOWNLOADING)
                .endGroup()
                .findAll();

        if (lessonRealms != null && lessonRealms.size() > 0) {
            StreamSupport.stream(lessonRealms).forEach(lessonRealm -> {
                downloadManager.cancel(lessonRealm.getDownloadId());
                if (lessonRealm.getDownloadStatus() == Constant.DOWNLOADING) {
                    File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, lessonRealm.getFileName()));
                    if (file.exists()) file.delete();
                }
                realm.executeTransaction(realm -> {
                    lessonRealm.setDownloadStatus(Constant.NONE_DOWNLOAD);
                    lessonRealm.setProgress(0);
                });
            });
        }
    }

    private void confirmStopDownloadAll() {
        if (!isValidNetwork())
            showNetworkRequire();
        else {
            ErrorConfirmDialog errorBoxDialog = new ErrorConfirmDialog(getContext(), getString(R.string.yes), getString(R.string.dialog_stop_download), v -> stopDownloadAll());
            errorBoxDialog.show();
        }
    }

    private void confirmDownloadAll() {
        if (!isValidNetwork())
            showNetworkRequire();
        else {
            ErrorConfirmDialog errorBoxDialog = new ErrorConfirmDialog(getContext(), getString(R.string.yes), getString(R.string.dialog_download_all), v -> downloadAll());
            errorBoxDialog.show();
        }
    }

    private void confirmStopDownloadChapter(ChapterTemp chapterTemp) {
        if (!isValidNetwork())
            showNetworkRequire();
        else {
            ErrorConfirmDialog errorBoxDialog = new ErrorConfirmDialog(getContext(), getString(R.string.yes), getString(R.string.dialog_stop_download), v -> stopDownloadChapter(chapterTemp));
            errorBoxDialog.show();
        }
    }

    private void confirmDownloadChapter(ChapterTemp chapterTemp) {
        if (!isValidNetwork())
            showNetworkRequire();
        else {
            ErrorConfirmDialog errorBoxDialog = new ErrorConfirmDialog(getContext(), getString(R.string.yes), getString(R.string.dialog_download_chapter), v -> downloadChapter(chapterTemp));
            errorBoxDialog.show();
        }
    }
}
