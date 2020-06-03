package com.mc.books.fragments.home.doTraining.subtraining;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;

public class ListRecordPresenter<V extends IListRecordView> extends BaseDataPresenter<V> implements IListRecordPresenter<V> {

    /**
     * @param appComponent
     */
    protected ListRecordPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
