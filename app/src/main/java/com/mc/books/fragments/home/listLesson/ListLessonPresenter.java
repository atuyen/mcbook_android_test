package com.mc.books.fragments.home.listLesson;

import android.net.Uri;
import android.util.Log;

import com.bon.jackson.JacksonUtils;
import com.mc.application.AppContext;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.events.DownloadEvent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.home.Chapter;
import com.mc.models.home.ChapterTemp;
import com.mc.models.home.Lesson;
import com.mc.models.realm.LessonRealm;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mc.utilities.Constant.DOWNLOADED;
import static com.mc.utilities.Constant.DOWNLOADING;
import static com.mc.utilities.Constant.DOWNLOAD_ERROR;
import static com.mc.utilities.Constant.PENDIND_DOWNLOAD;

public class ListLessonPresenter<V extends IListLessonView> extends BaseDataPresenter<V> implements IListLessonPresenter<V> {
    protected ListLessonPresenter(AppComponent appComponent) {
        super(appComponent);
    }


    Calendar currentCalendar;

    @Override
    public void onSearchLesson(int bookId, String keyword, String code, List<Chapter> chapters) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) {
                List<Chapter> filter = StreamSupport.stream(new ArrayList<>(chapters)).map(chapter -> new Chapter(chapter.getId(), chapter.getName(),
                        StreamSupport.stream(chapter.getLessons()).filter(n -> n.getName().toLowerCase().contains(keyword.toLowerCase()))
                                .collect(Collectors.toList()))).filter(chapter -> chapter.getLessons().size() > 0).collect(Collectors.toList());
                v.onSearchLesson(filter);
            } else {
                v.onShowLoading(true);
                apiService.searchLesson(bookId, keyword.trim(), code).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<Chapter>>>() {
                    @Override
                    public void onCompleted() {
                        v.onShowLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        v.onShowLoading(false);
                    }

                    @Override
                    public void onNext(BaseResponse<ArrayResponse<Chapter>> arrayResponseBaseResponse) {
                        v.onSearchLesson(arrayResponseBaseResponse.getData().getRows());
                    }
                });
            }
        });
    }

    @Override
    public void onGetChapters(int bookId) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getListChapter(bookId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<ArrayResponse<Chapter>>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                    Log.e("onGetChapters err", e.toString());
                }

                @Override
                public void onNext(BaseResponse<ArrayResponse<Chapter>> arrayResponseBaseResponse) {
                    AppUtils.writeToFile(AppContext.getInstance(), JacksonUtils.writeValueToString(arrayResponseBaseResponse.getData().getRows()), Constant.MY_LESSON + "_" + bookId);
                    v.onGetChapters(arrayResponseBaseResponse.getData().getRows());
                }
            });
        });
    }

    @Override
    public void downloadAllLesson(List<Chapter> chapters, int bookId) {
        getOptView().doIfPresent(v -> {
            final boolean[] isDownload = {false};
            AppContext.getRealm().beginTransaction();
            ThinDownloadManager downloadManager = AppContext.getDownloadManager();
            for (int i = 0; i < chapters.size(); i++) {
                Chapter chapter = chapters.get(i);
                int finalI = i;
                StreamSupport.stream(chapter.getLessons()).forEach(lesson -> {
                    if (lesson.getMedia() != null) {
                        Uri downloadUri = Uri.parse(lesson.getMedia());
                        String fileName = AppUtils.getFileName(lesson.getMedia());
                        Uri destinationUri = Uri.parse(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                        File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                        if (!file.exists()) {
                            isDownload[0] = true;
                            v.onShowLoading(true);
                            LessonRealm lessonRealm = AppContext.getRealm()
                                    .where(LessonRealm.class)
                                    .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                    .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                    .findFirst();
                            if (lessonRealm == null) {
                                lessonRealm = AppContext.getRealm().createObject(LessonRealm.class, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()));
                                lessonRealm.setBookId(bookId);
                                lessonRealm.setName(lesson.getName());
                                lessonRealm.setUserId(AppContext.getUserId());
                            }
                            lessonRealm.setFileName(fileName);
                            lessonRealm.setIndex(finalI);
                            lessonRealm.setDownloadType(Constant.DOWNLOAD_ALL);
                            lessonRealm.setDownloadStatus(PENDIND_DOWNLOAD);
                            DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                                    .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                                    .addCustomHeader(Constant.DEVICE, Constant.ANDROID)
                                    .setRetryPolicy(new DefaultRetryPolicy())
                                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                                    .setStatusListener(new DownloadStatusListenerV1() {
                                        @Override
                                        public void onDownloadComplete(DownloadRequest downloadRequest) {
                                            LessonRealm results = AppContext.getRealm()
                                                    .where(LessonRealm.class)
                                                    .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                                    .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                                    .findFirst();
                                            if (results != null) {
                                                AppContext.getRealm().executeTransaction(realm -> results.setDownloadStatus(DOWNLOADED));
                                                bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                            }
                                            currentCalendar = null;
                                            Log.e("onDownloadComplete", destinationUri.toString());
                                        }

                                        @Override
                                        public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                            LessonRealm results = AppContext.getRealm()
                                                    .where(LessonRealm.class)
                                                    .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                                    .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                                    .findFirst();
                                            if (results != null) {
                                                if (results.getDownloadStatus() != Constant.NONE_DOWNLOAD)
                                                    AppContext.getRealm().executeTransaction(realm -> results.setDownloadStatus(DOWNLOAD_ERROR));
                                                bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                            }
                                            currentCalendar = null;
                                            Log.e("onDownloadFailed", destinationUri.toString());
                                        }

                                        @Override
                                        public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                                            //logic delay progress, stop spam progress send bus every 0.1s
                                            if (currentCalendar == null) {
                                                currentCalendar = Calendar.getInstance();
                                                LessonRealm results = AppContext.getRealm()
                                                        .where(LessonRealm.class)
                                                        .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                                        .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                                        .findFirst();
                                                if (results != null) {
                                                    if (results.getDownloadStatus() != Constant.DOWNLOAD_ERROR) {
                                                        AppContext.getRealm().executeTransaction(realm -> {
                                                            results.setDownloadStatus(DOWNLOADING);
                                                            results.setProgress(progress);
                                                        });
                                                    }
                                                    bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                                }
                                            }
//                                            else {
//                                                long diffSecond = calendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
//                                                // 6s update UI
//                                                if (diffSecond >= 7000) {
//                                                    currentCalendar = calendar;
//                                                    LessonRealm results = AppContext.getRealm()
//                                                            .where(LessonRealm.class)
//                                                            .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
//                                                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
//                                                            .findFirst();
//                                                    if (results != null) {
//                                                        AppContext.getRealm().executeTransaction(realm -> {
//                                                            if (results.getDownloadStatus() != Constant.DOWNLOAD_ERROR) {
//                                                                results.setDownloadStatus(DOWNLOADING);
//                                                                results.setProgress(progress);
//                                                            }
//                                                        });
//                                                        bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
//                                                    }
//                                                }
//                                            }
                                        }
                                    });
                            lessonRealm.setDownloadId(downloadManager.add(downloadRequest));
                        }
                    }

                    if (lesson.getSubtitle() != null) {
                        Uri downloadUri = Uri.parse(lesson.getSubtitle());
                        String filename = AppUtils.getFileName(lesson.getSubtitle());
                        Uri destinationUri = Uri.parse(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, filename));
                        File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, filename));
                        if (!file.exists()) {
                            isDownload[0] = true;
                            DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                                    .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                                    .setRetryPolicy(new DefaultRetryPolicy())
                                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                                    .setDownloadResumable(true)
                                    .setDeleteDestinationFileOnFailure(true)
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
                    }
                });
            }
            AppContext.getRealm().commitTransaction();
            if (!isDownload[0]) bus.send(new DownloadEvent(-1, bookId));
        });
    }

    @Override
    public void downloadChapter(ChapterTemp chapterTemp, int bookId) {
        getOptView().doIfPresent(v -> {
            final boolean[] isDownload = {false};
            ThinDownloadManager downloadManager = AppContext.getDownloadManager();
            AppContext.getRealm().beginTransaction();
            StreamSupport.stream(chapterTemp.getChapter().getLessons()).forEach(lesson -> {
                if (lesson.getMedia() != null) {
                    Uri downloadUri = Uri.parse(lesson.getMedia());
                    String fileName = AppUtils.getFileName(lesson.getMedia());
                    Uri destinationUri = Uri.parse(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                    File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                    if (!file.exists()) {
                        v.onShowLoading(true);
                        isDownload[0] = true;
                        LessonRealm lessonRealm = AppContext.getRealm()
                                .where(LessonRealm.class)
                                .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                .findFirst();
                        if (lessonRealm == null) {
                            lessonRealm = AppContext.getRealm().createObject(LessonRealm.class, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()));
                            lessonRealm.setBookId(bookId);
                            lessonRealm.setName(lesson.getName());
                            lessonRealm.setUserId(AppContext.getUserId());
                        }
                        lessonRealm.setFileName(fileName);
                        lessonRealm.setIndex(chapterTemp.getIndex());
                        lessonRealm.setDownloadType(Constant.DOWNLOAD_CHAPTER);
                        lessonRealm.setDownloadStatus(PENDIND_DOWNLOAD);
                        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                                .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                                .addCustomHeader(Constant.DEVICE, Constant.ANDROID)
                                .setRetryPolicy(new DefaultRetryPolicy())
                                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                                .setStatusListener(new DownloadStatusListenerV1() {
                                    @Override
                                    public void onDownloadComplete(DownloadRequest downloadRequest) {
                                        LessonRealm results = AppContext.getRealm()
                                                .where(LessonRealm.class)
                                                .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                                .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                                .findFirst();
                                        if (results != null) {
                                            AppContext.getRealm().executeTransaction(realm -> results.setDownloadStatus(DOWNLOADED));
                                            bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                        }
                                        currentCalendar = null;
                                        Log.e("onDownloadComplete", destinationUri.toString());
                                    }

                                    @Override
                                    public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {

                                        LessonRealm results = AppContext.getRealm()
                                                .where(LessonRealm.class)
                                                .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                                .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                                .findFirst();
                                        if (results != null) {
                                            if (results.getDownloadStatus() != Constant.NONE_DOWNLOAD)
                                                AppContext.getRealm().executeTransaction(realm -> results.setDownloadStatus(DOWNLOAD_ERROR));
                                            bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                        }
                                        currentCalendar = null;
                                        Log.e("onDownloadFailed", destinationUri.toString());
                                    }

                                    @Override
                                    public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                                        //logic delay progress, stop spam progress send bus every 0.1s

                                        if (currentCalendar == null) {
                                            LessonRealm results = AppContext.getRealm()
                                                    .where(LessonRealm.class)
                                                    .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                                    .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                                    .findFirst();
                                            currentCalendar = Calendar.getInstance();
                                            if (results != null) {
                                                if (results.getDownloadStatus() != Constant.DOWNLOAD_ERROR) {
                                                    AppContext.getRealm().executeTransaction(realm -> {
                                                        results.setDownloadStatus(DOWNLOADING);
                                                        results.setProgress(progress);
                                                    });
                                                }
                                                bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                            }
                                        }
//                                        else {
//                                            long diffSecond = calendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
//                                            // 6s update UI
//                                            if (diffSecond >= 7000) {
//                                                currentCalendar = calendar;
//                                                if (results != null) {
//                                                    AppContext.getRealm().executeTransaction(realm -> {
//                                                        if (results.getDownloadStatus() != Constant.DOWNLOAD_ERROR) {
//                                                            results.setDownloadStatus(DOWNLOADING);
//                                                            results.setProgress(progress);
//                                                        }
//                                                    });
//                                                    bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
//                                                }
//                                            }
//                                        }
                                    }
                                });
                        lessonRealm.setDownloadId(downloadManager.add(downloadRequest));
                    }

                }

                if (lesson.getSubtitle() != null) {
                    Uri downloadUri = Uri.parse(lesson.getSubtitle());
                    String fileName = AppUtils.getFileName(lesson.getSubtitle());
                    Uri destinationUri = Uri.parse(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                    File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                    if (!file.exists()) {
                        isDownload[0] = true;
                        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                                .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                                .setRetryPolicy(new DefaultRetryPolicy())
                                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                                .setDownloadResumable(true)
                                .setDeleteDestinationFileOnFailure(true)
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
                }
            });
            AppContext.getRealm().commitTransaction();
            if (!isDownload[0]) bus.send(new DownloadEvent(-1, bookId));
        });
    }

    @Override
    public void downloadLesson(Lesson lesson, int bookId, int index) {
        getOptView().doIfPresent(v -> {

            ThinDownloadManager downloadManager = AppContext.getDownloadManager();
            AppContext.getRealm().beginTransaction();
            if (lesson.getMedia() != null) {
                Uri downloadUri = Uri.parse(lesson.getMedia());
                String fileName = AppUtils.getFileName(lesson.getMedia());
                Uri destinationUri = Uri.parse(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                if (!file.exists()) {
                    v.onShowLoading(true);

                    LessonRealm lessonRealm = AppContext.getRealm()
                            .where(LessonRealm.class)
                            .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                            .findFirst();
                    if (lessonRealm == null) {
                        lessonRealm = AppContext.getRealm().createObject(LessonRealm.class, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()));
                        lessonRealm.setBookId(bookId);
                        lessonRealm.setName(lesson.getName());
                        lessonRealm.setUserId(AppContext.getUserId());
                    }
                    lessonRealm.setFileName(fileName);
                    lessonRealm.setIndex(index);
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
                                    LessonRealm results = AppContext.getRealm()
                                            .where(LessonRealm.class)
                                            .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                            .findFirst();
                                    if (results != null) {
                                        AppContext.getRealm().executeTransaction(realm -> results.setDownloadStatus(DOWNLOADED));
                                        bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                    }
                                    currentCalendar = null;
                                    Log.e("onDownloadComplete", destinationUri.toString());
                                }

                                @Override
                                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {

                                    LessonRealm results = AppContext.getRealm()
                                            .where(LessonRealm.class)
                                            .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                            .findFirst();
                                    if (results != null) {
                                        if (results.getDownloadStatus() != Constant.NONE_DOWNLOAD)
                                            AppContext.getRealm().executeTransaction(realm -> results.setDownloadStatus(DOWNLOAD_ERROR));
                                        bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                    }
                                    currentCalendar = null;
                                    Log.e("onDownloadFailed", destinationUri.toString());
                                }

                                @Override
                                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                                    //logic delay progress, stop spam progress send bus every 0.1s
                                    if (currentCalendar == null) {
                                        LessonRealm results = AppContext.getRealm()
                                                .where(LessonRealm.class)
                                                .equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                                .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                                .findFirst();
                                        currentCalendar = Calendar.getInstance();
                                        if (results != null) {
                                            if (results.getDownloadStatus() != Constant.DOWNLOAD_ERROR) {
                                                AppContext.getRealm().executeTransaction(realm -> {
                                                    results.setDownloadStatus(DOWNLOADING);
                                                    results.setProgress(progress);
                                                });
                                            }
                                            bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
                                        }
                                    }
//                                    else {
//                                        long diffSecond = calendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
//                                        // 6s update UI
//                                        if (diffSecond >= 7000) {
//                                            currentCalendar = calendar;
//                                            if (results != null) {
//                                                AppContext.getRealm().executeTransaction(realm -> {
//                                                    if (results.getDownloadStatus() != Constant.DOWNLOAD_ERROR) {
//                                                        results.setDownloadStatus(DOWNLOADING);
//                                                        results.setProgress(progress);
//                                                    }
//                                                });
//                                                bus.send(new DownloadEvent(results.getIndex(), results.getBookId()));
//                                            }
//                                        }
//                                    }
                                }
                            });
                    lessonRealm.setDownloadId(downloadManager.add(downloadRequest));
                }
            }

            if (lesson.getSubtitle() != null) {
                Uri downloadUri = Uri.parse(lesson.getSubtitle());
                String fileName = AppUtils.getFileName(lesson.getSubtitle());
                Uri destinationUri = Uri.parse(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, fileName));
                if (!file.exists()) {

                    DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                            .addCustomHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                            .setRetryPolicy(new DefaultRetryPolicy())
                            .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                            .setDownloadResumable(true)
                            .setDeleteDestinationFileOnFailure(true)
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
            }
        });
        AppContext.getRealm().commitTransaction();
    }

}
