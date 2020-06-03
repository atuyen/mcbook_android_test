package com.mc.books.fragments.home.lessonPdf;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bon.util.ToastUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.gift.Gift;
import com.mc.models.home.Lesson;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;

import butterknife.BindView;

import static com.mc.utilities.Constant.BOOK_ID;
import static com.mc.utilities.Constant.KEY_GIFT;
import static com.mc.utilities.Constant.KEY_LESSON;

public class LessonPdfFragment extends BaseMvpFragment<ILessonPdfView, ILessonPdfPresenter<ILessonPdfView>> implements ILessonPdfView {

    public static LessonPdfFragment newInstance(Lesson lesson, Gift gift, int bookId) {
        Bundle args = new Bundle();
        LessonPdfFragment fragment = new LessonPdfFragment();
        args.putSerializable(KEY_LESSON, lesson);
        args.putSerializable(KEY_GIFT, gift);
        args.putInt(BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.pdfView)
    PDFView pdfView;
    private Lesson lesson;
    private Gift gift;
    private String fileName;
    private String url;
    private File file;
    private int bookId;
    private String folderDownload;
    private int folderId;
    private int fileId;
    private String name;
    MenuItem miDownloadDisable, miDownload;

    @Override
    public ILessonPdfPresenter<ILessonPdfView> createPresenter() {
        return new LessonPdfPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.lesson_pdf_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        try {
            bookId = getArguments().getInt(BOOK_ID);
            lesson = (Lesson) getArguments().getSerializable(KEY_LESSON);
            gift = (Gift) getArguments().getSerializable(KEY_GIFT);
            folderDownload = lesson != null ? Constant.FOLDER_BOOK : Constant.FOLDER_GIFT;
            folderId = bookId != -1 ? bookId : gift.getId();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        try {

            if (lesson != null) {
                if (lesson.getMedia() == null) return;
                fileName = AppUtils.getFileName(lesson.getMedia());
                fileId = lesson.getId();
                name = lesson.getName();
            } else if (gift != null) {
                if (gift.getContentUri() == null) return;
                fileName = AppUtils.getFileName(gift.getContentUri());
                fileId = gift.getId();
                name = gift.getName();
            }

            if (lesson != null)
                url = lesson.getMedia();
            else if (gift != null)
                url = gift.getContentUri();


            if (url.isEmpty() || fileName.isEmpty()) return;
            file = new File(AppUtils.getFilePath(getContext(), folderDownload, folderId, fileName));

            if (file.exists())
                file.delete();

            presenter.onDownloadFile(url, fileName, getAppContext(), folderDownload, folderId, fileId, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPdfView() {
        if (lesson != null)
            presenter.sendLogLesson(lesson.getId(), bookId);
        pdfView.fromUri(Uri.fromFile(file))
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .spacing(0)
                .load();
    }

    @Override
    public String getTitleString() {
        if (lesson != null)
            return lesson.getName();
        else if (gift != null)
            return gift.getName();
        return "";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.download, menu);
        miDownloadDisable = menu.findItem(R.id.action_download_disable);
        miDownload = menu.findItem(R.id.action_download);
        miDownload.setVisible(false);
        miDownloadDisable.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                showProgress(true);
                new Handler().postDelayed(() -> {
                    showProgress(false);
                    ToastUtils.showToast(getAppContext(), getString(R.string.download_success));
                }, 3000);
                ToastUtils.showToast(getAppContext(), getString(R.string.download_success));
                break;
            default:
                return false;
        }
        return true;
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
    public void onDownloadSuccess() {
        initPdfView();
    }

    @Override
    public void onDownloadFail(String error) {
        ToastUtils.showToast(getAppContext(), getAppContext().getResources().getString(R.string.default_error));
    }

    @Override
    public void onDownloadProgressing(int progress) {

    }
}
