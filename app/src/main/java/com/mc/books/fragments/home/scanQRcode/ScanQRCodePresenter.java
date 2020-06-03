package com.mc.books.fragments.home.scanQRcode;

import android.util.Log;

import com.bon.util.StringUtils;
import com.mc.books.R;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.home.AddBook;
import com.mc.models.home.Chapter;
import com.mc.models.home.Lesson;
import com.mc.utilities.AppUtils;

import java.util.List;

import java8.util.stream.StreamSupport;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScanQRCodePresenter<V extends IScanQRCodeView> extends BaseDataPresenter<V> implements IScanQRCodePresenter<V> {

    protected ScanQRCodePresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void onCreateBook(String code) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.createBook(code).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<AddBook>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
//                    String code = AppUtils.getErrorMessageCode(e);
                    if (StringUtils.isEmpty(code)) v.onCreateBookError(R.string.invalid_qrcode);
                    else v.onCreateBookError(AppUtils.getErrorMessage(e));
                }

                @Override
                public void onNext(BaseResponse<AddBook> addBookBaseResponse) {

                    v.onCreateBookSuccess(addBookBaseResponse.getData());
                }
            });
        });
    }

    @Override
    public void onSearchLesson(int bookId, String keyword, String code) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.searchLesson(bookId, null, code).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<Chapter>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    v.onSearchLessonError(AppUtils.getErrorMessage(e));
                    Log.e("onSearchLesson err", e.toString());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<Chapter>> arrayResponseBaseResponse) {
//                    v.onGetChapters(arrayResponseBaseResponse.getData().getRows());
                    List<Chapter> chapters = arrayResponseBaseResponse.getData().getRows();
                    final Lesson[] lesson = new Lesson[1];
                    StreamSupport.stream(chapters).forEach(chapter -> {
                        if (chapter.getLessons().size() > 0) {
                            lesson[0] = chapter.getLessons().get(0);
                        }
                    });
                    Log.e("onNext err", lesson[0] + "");
                    v.onSearchLessonSuccess(lesson[0]);

                }
            });
        });
    }
}
