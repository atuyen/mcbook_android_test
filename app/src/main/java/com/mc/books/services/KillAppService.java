package com.mc.books.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mc.application.AppContext;
import com.mc.models.realm.LessonRealm;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;

public class KillAppService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    void resetDownloadStatus() {
        AppContext.getRealm().refresh();
        AppContext.getRealm().executeTransaction(realm -> {
            RealmResults<LessonRealm> results = AppContext.getRealm()
                    .where(LessonRealm.class)
                    .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.DOWNLOADING)
                    .or()
                    .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.PENDIND_DOWNLOAD)
                    .findAll();
            for (LessonRealm lessonRealm : results) {
                lessonRealm.setDownloadStatus(Constant.NONE_DOWNLOAD);
                lessonRealm.setProgress(0);
                File file = new File(AppUtils.getFilePath(getApplicationContext(), Constant.FOLDER_BOOK, lessonRealm.getBookId(), lessonRealm.getFileName()));
                if (file.exists()) file.delete();
            }
        });
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // clean db download when app killed
        resetDownloadStatus();

        //stop service
        this.stopSelf();
    }

    @Override
    public void onDestroy() {
        // clean db download when logout
        resetDownloadStatus();
        super.onDestroy();
    }
}