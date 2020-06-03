package com.bon.database.orm;

import com.bon.logger.Logger;

import java.lang.reflect.Field;

/**
 * Created by Dang on 4/4/2016.
 */
public class EmbeddedFieldInitializer {
    private static final String TAG = EmbeddedFieldInitializer.class.getSimpleName();

    private  Field mField;
    private  DaoAdapter<Object> mDaoAdapter;

    @SuppressWarnings("unchecked")
    public EmbeddedFieldInitializer(Field field, DaoAdapter<?> daoAdapter) {
        try {
            mField = field;
            mDaoAdapter = ((DaoAdapter<Object>) daoAdapter);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public void initEmbeddedField(Object obj) {
        try {
            mField.set(obj, mDaoAdapter.createInstance());
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}