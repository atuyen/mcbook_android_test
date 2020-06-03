package com.mc.books.fragments.home.lesson;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.bon.sharepreferences.AppPreferences;
import com.mc.application.AppContext;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.BaseResponse;
import com.mc.models.home.LogLesson;
import com.mc.models.realm.LessonRealm;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mc.utilities.Constant.DOWNLOADED;
import static com.mc.utilities.Constant.DOWNLOAD_ERROR;
import static com.mc.utilities.Constant.PENDIND_DOWNLOAD;

public class LessonPresenter<V extends ILessonView> extends BaseDataPresenter<V> implements ILessonPresenter<V> {
    protected LessonPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    private ThinDownloadManager downloadManager = AppContext.getDownloadManager();
    private Realm realm = AppContext.getRealm();

    @Override
    public void onDownloadFile(String url, String fileName, Context context, int type, String folderDownload, int folderId, int fileId, String name) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            if (url == null || fileName == null) return;
            Log.e("url", url);
            Uri downloadUri = Uri.parse(url);
            Uri destinationUri = Uri.parse(AppUtils.getFilePath(context, folderDownload, folderId, fileName));
            File file = new File(AppUtils.getFilePath(AppContext.getInstance(), folderDownload, folderId, fileName));
            if (!file.exists()) {
                v.onShowLoading(true);
                if (folderDownload.equals(Constant.FOLDER_BOOK)) {
                    realm.beginTransaction();
                    LessonRealm lessonRealm = realm
                            .where(LessonRealm.class)
                            .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(fileId, url))
                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                            .findFirst();
                    if (lessonRealm == null) {
                        lessonRealm = realm.createObject(LessonRealm.class, AppUtils.getLessonRealmId(fileId, url));
                        if (folderDownload.equals(Constant.FOLDER_BOOK))
                            lessonRealm.setBookId(folderId);
                        else
                            lessonRealm.setGiftId(folderId);

                        lessonRealm.setName(name);
                    }
                    lessonRealm.setFileName(fileName);
                    lessonRealm.setIndex(0);
                    lessonRealm.setDownloadType(Constant.DOWNLOAD_FILE);
                    lessonRealm.setDownloadStatus(PENDIND_DOWNLOAD);

                    DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                            .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                            .addCustomHeader(Constant.DEVICE, Constant.ANDROID)
                            .setRetryPolicy(new DefaultRetryPolicy())
                            .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                            .setStatusListener(new DownloadStatusListenerV1() {
                                @Override
                                public void onDownloadComplete(DownloadRequest downloadRequest) {
                                    LessonRealm results = realm
                                            .where(LessonRealm.class)
                                            .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(fileId, url))
                                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                            .findFirst();
                                    if (results != null)
                                        realm.executeTransaction(realm -> results.setDownloadStatus(DOWNLOADED));

                                    v.onShowLoading(false);
                                    v.onDownloadSuccess(type);

                                    Log.e("onDownloadComplete", destinationUri.toString());
                                }

                                @Override
                                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                    LessonRealm results = realm
                                            .where(LessonRealm.class)
                                            .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(fileId, url))
                                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                            .findFirst();
                                    if (results != null) {
                                        if (results.getDownloadStatus() != Constant.NONE_DOWNLOAD)
                                            realm.executeTransaction(realm -> results.setDownloadStatus(DOWNLOAD_ERROR));
                                    }
                                    v.onShowLoading(false);
                                    v.onDownloadFail(errorMessage);
                                    Log.e("onDownloadFailed", destinationUri.toString());
                                }

                                @Override
                                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                                    v.onDownloadProgressing(progress);
                                }
                            });

                    lessonRealm.setDownloadId(downloadManager.add(downloadRequest));
                    realm.commitTransaction();
                } else {
                    DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                            .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                            .addCustomHeader(Constant.DEVICE, Constant.ANDROID)
                            .setRetryPolicy(new DefaultRetryPolicy())
                            .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                            .setStatusListener(new DownloadStatusListenerV1() {
                                @Override
                                public void onDownloadComplete(DownloadRequest downloadRequest) {
                                    v.onShowLoading(false);
                                    v.onDownloadSuccess(type);

                                    Log.e("onDownloadComplete", destinationUri.toString());
                                }

                                @Override
                                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                    v.onShowLoading(false);
                                    v.onDownloadFail(errorMessage);
                                    Log.e("onDownloadFailed", destinationUri.toString());
                                }

                                @Override
                                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {

                                }
                            });
                    downloadManager.add(downloadRequest);
                }
            }
        });
    }

    @Override
    public void onDownloadSub(String url, String fileName, Context context, String folderDownload, int folderId, int fileId, String name) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;
            if (url == null || fileName == null) return;
            Log.e("url", url);
            Uri downloadUri = Uri.parse(url);
            Uri destinationUri = Uri.parse(AppUtils.getFilePath(context, folderDownload, folderId, fileName));

            File file = new File(AppUtils.getFilePath(AppContext.getInstance(), folderDownload, folderId, fileName));
            if (!file.exists()) {
                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                        .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                        .setRetryPolicy(new DefaultRetryPolicy())
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                        .setStatusListener(new DownloadStatusListenerV1() {
                            @Override
                            public void onDownloadComplete(DownloadRequest downloadRequest) {
                                Log.e("onDownloadComplete", destinationUri.toString());
                            }

                            @Override
                            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                Log.e("onDownloadFailed", destinationUri.toString());
                            }

                            @Override
                            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {

                            }
                        });

                downloadManager.add(downloadRequest);
            }

        });
    }

    @Override
    public void sendLogLesson(int lessonId, int bookId) {
        apiService.sendLogLesson(lessonId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<LogLesson>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<LogLesson> logLessonBaseResponse) {

            }
        });
    }
}
