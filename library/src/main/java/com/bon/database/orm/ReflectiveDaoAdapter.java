package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Sets;
import com.bon.logger.Logger;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Dang on 4/4/2016.
 */
public class ReflectiveDaoAdapter<T> implements DaoAdapter<T> {
    private static final String TAG = ReflectiveDaoAdapter.class.getSimpleName();

    private Class<T> tClass;
    private ImmutableList<FieldAdapter> mFieldAdapters;
    private ImmutableList<EmbeddedFieldInitializer> mFieldInitializers;
    private String[] mProjection;
    private String[] mWritableColumns;
    private ImmutableSet<String> mWritableDuplicates;

    public ReflectiveDaoAdapter(Class<T> tClass, ImmutableList<FieldAdapter> fieldAdapters, ImmutableList<EmbeddedFieldInitializer> fieldInitializers) {
        try {
            this.tClass = tClass;
            this.mFieldAdapters = fieldAdapters;
            this.mFieldInitializers = fieldInitializers;

            ImmutableList.Builder<String> projectionBuilder = ImmutableList.builder();
            ImmutableList.Builder<String> writableColumnsBuilder = ImmutableList.builder();

            for (FieldAdapter fieldAdapter : fieldAdapters) {
                projectionBuilder.add(fieldAdapter.getColumnNames());
                writableColumnsBuilder.add(fieldAdapter.getWritableColumnNames());
            }

            this.mProjection = array(projectionBuilder.build());
            this.mWritableColumns = array(writableColumnsBuilder.build());
            this.mWritableDuplicates = findDuplicates(mWritableColumns);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private static String[] array(Collection<String> collection) {
        try {
            return collection.toArray(new String[collection.size()]);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    private static <T> ImmutableSet<T> findDuplicates(T[] array) {
        try {
            final Builder<T> result = ImmutableSet.builder();
            final Set<T> uniques = Sets.newHashSet();

            for (T element : array) {
                if (!uniques.add(element)) {
                    result.add(element);
                }
            }

            return result.build();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public T createInstance() {
        try {
            return createInstance(tClass);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    private T createInstance(Class<T> tClass) {
        try {
            T instance = tClass.newInstance();
            for (EmbeddedFieldInitializer fieldInitializer : mFieldInitializers) {
                fieldInitializer.initEmbeddedField(instance);
            }

            return instance;
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public T fromCursor(Cursor c, T object) {
        try {
            for (FieldAdapter fieldAdapter : mFieldAdapters) {
                fieldAdapter.setValueFromCursor(c, object);
            }
            return object;
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public ContentValues toContentValues(ContentValues values, T object) {
        if (!mWritableDuplicates.isEmpty()) {
            throw new IllegalArgumentException("Duplicate columns definitions: " + Joiner.on(", ").join(mWritableDuplicates));
        }

        try {
            for (FieldAdapter fieldAdapter : mFieldAdapters) {
                fieldAdapter.putToContentValues(object, values);
            }

            return values;
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public ContentValues createContentValues() {
        try {
            return new ContentValues(mWritableColumns.length);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public String[] getProjection() {
        try {
            return mProjection.clone();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public String[] getWritableColumns() {
        try {
            return mWritableColumns.clone();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }
}