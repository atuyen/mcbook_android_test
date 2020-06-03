package com.mc.books.fragments.home.doTraining.detailrecord;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.mc.books.fragments.home.doTraining.IDoTrainingPresenter;
import com.mc.books.fragments.home.doTraining.IDoTrainingView;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.models.home.RecordItem;
import com.mc.utilities.AppUtils;

import java.io.File;
import java.util.ArrayList;

public class DetailRecordTrainingPresenter<V extends IDetailRecordView> extends BaseDataPresenter<V> implements IDetailRecordPresenter<V> {
    /**
     * @param appComponent
     */
    protected DetailRecordTrainingPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
