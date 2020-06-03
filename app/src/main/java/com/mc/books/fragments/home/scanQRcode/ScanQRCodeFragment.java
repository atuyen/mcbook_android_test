package com.mc.books.fragments.home.scanQRcode;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.mc.books.R;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.dialog.MessageBoxDialog;
import com.mc.books.fragments.home.booktab.BookTabFragment;
import com.mc.books.fragments.home.lessonPdf.LessonPdfFragment;
import com.mc.books.fragments.home.lessonVideo.LessonVideoFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.searchbar.SearchBar;
import com.mc.models.home.AddBook;
import com.mc.models.home.BookResponse;
import com.mc.models.home.Lesson;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import butterknife.BindView;

import static com.mc.utilities.Constant.BOOK_ID;
import static com.mc.utilities.Constant.KEY_QR;
import static com.mc.utilities.Constant.TYPE_QR_LESSON;

public class ScanQRCodeFragment extends BaseMvpFragment<IScanQRCodeView, IScanQRCodePresenter<IScanQRCodeView>>
        implements IScanQRCodeView, QRCodeReaderView.OnQRCodeReadListener, ActivityCompat.OnRequestPermissionsResultCallback {

    public static ScanQRCodeFragment newInstance(int typeQR, int bookId) {
        Bundle args = new Bundle();
        ScanQRCodeFragment fragment = new ScanQRCodeFragment();
        args.putInt(KEY_QR, typeQR);
        args.putInt(BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.edtQRCode)
    SearchBar edtQRCode;
    @BindView(R.id.flScanQR)
    FrameLayout flScanQR;
    private boolean stopScan;
    private int typeQR;
    private int bookId;

    private QRCodeReaderView qrCodeReaderView;

    @NonNull
    @Override
    public IScanQRCodePresenter<IScanQRCodeView> createPresenter() {
        return new ScanQRCodePresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        typeQR = getArguments().getInt(KEY_QR);
        bookId = getArguments().getInt(BOOK_ID);

        if (typeQR == TYPE_QR_LESSON) edtQRCode.setVisibility(View.GONE);

        try {
            initQRCodeReaderView();
            edtQRCode.onSearch(code -> {
                if (StringUtils.isEmpty(code)) {
                    AppUtils.showErrorDialog(mActivity, getString(R.string.invalid_qrcode_empty));
                    return;
                }

                presenter.onCreateBook(code);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (qrCodeReaderView != null) qrCodeReaderView.startCamera();
        mMainActivity.onShowBottomBar(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public int getResourceId() {
        return R.layout.scan_qr_code_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.qr_code;
    }

    private void initQRCodeReaderView() {
        View content = getLayoutInflater().inflate(R.layout.item_scan_qr_code, flScanQR, true);
        ExtTextView txtGuideQR = content.findViewById(R.id.txtGuideQR);
        if (typeQR == TYPE_QR_LESSON)
            txtGuideQR.setText(R.string.scan_qr_lesson);
        else txtGuideQR.setText(R.string.qr_code_hint);

        qrCodeReaderView = content.findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onQRCodeRead(String code, PointF[] points) {
        if (!stopScan) {
            stopScan = true;
            if (typeQR == TYPE_QR_LESSON)
                presenter.onSearchLesson(bookId, null, code);
            else
                presenter.onCreateBook(code);
        }
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onCreateBookSuccess(AddBook addBook) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(addBook.getBookId());
        bookResponse.setName(addBook.getBookName());
        MessageBoxDialog messageBoxDialog = new MessageBoxDialog(getActivity(), getResources().getString(R.string.add_book_success, addBook.getBookName()), true, v -> {
            FragmentUtils.replaceFragment(getActivity(), BookTabFragment.newInstance(bookResponse), fragment -> this.mMainActivity.fragments.add(fragment));
        });
        messageBoxDialog.show();
        messageBoxDialog.setOnDismissListener(dialogInterface -> stopScan = false);
    }

    @Override
    public void onCreateBookError(int stringError) {
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getActivity(), getString(stringError));
        errorBoxDialog.show();
        errorBoxDialog.setOnDismissListener(dialogInterface -> stopScan = false);
    }

    @Override
    public void onSearchLessonSuccess(Lesson lesson) {
        if (lesson != null)
            goToLesson(lesson);
        else onSearchLessonError(R.string.nullLesson);
    }

    @Override
    public void onSearchLessonError(int stringError) {
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getActivity(), getString(stringError));
        errorBoxDialog.show();
        errorBoxDialog.setOnDismissListener(dialogInterface -> stopScan = false);
    }

    private void goToLesson(Lesson lesson) {
        if (lesson.getMedia() != null) {
            switch (lesson.getType()) {
                case Constant.KEY_AUDIO:
                case Constant.KEY_VIDEO:
                    FragmentUtils.replaceFragment(getActivity(), LessonVideoFragment.newInstance(lesson, null, bookId), null);
                    break;
                case Constant.KEY_PDF:
                    FragmentUtils.replaceFragment(getActivity(), LessonPdfFragment.newInstance(lesson, null, bookId), null);
                    break;
            }
        }
    }

    @Override
    public void onStop() {
        mMainActivity.onShowBottomBar(true);
        super.onStop();
    }
}
