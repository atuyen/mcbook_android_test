package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.bon.database.orm.annotations.Column;
import com.bon.database.orm.annotations.Embedded;
import com.bon.logger.Logger;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * This is the main class for using MicroOrm. MicroOrm is typically used by
 * first constructing a MicroOrm instance and then invoking
 * {@link #fromCursor(android.database.Cursor, Class)} or
 * {@link #toContentValues(Object)} methods on it.
 * <p>
 * You can create a MicroOrm instance by invoking {@code new MicroOrm()} if the
 * default configuration is all you need. You can also use
 * {@link MicroOrm.Builder} to build a MicroOrm instance
 * with support for custom fields' types.
 */
@SuppressWarnings("ConstantConditions")
public class MicroOrm {
    private static final String TAG = MicroOrm.class.getSimpleName();

    private static MicroOrm microOrm = null;
    private static final ImmutableMap<Class<?>, TypeAdapter<?>> TYPE_ADAPTERS;
    private final ImmutableMap<Class<?>, TypeAdapter<?>> mTypeAdapters;
    private final Map<Class<?>, DaoAdapter<?>> mDaoAdapterCache = Maps.newHashMap();

    static {
        Map<Class<?>, TypeAdapter<?>> typeAdapters = Maps.newHashMap();
        // variable
        typeAdapters.put(short.class, new TypeAdapters.ShortAdapter());
        typeAdapters.put(int.class, new TypeAdapters.IntegerAdapter());
        typeAdapters.put(long.class, new TypeAdapters.LongAdapter());
        typeAdapters.put(boolean.class, new TypeAdapters.BooleanAdapter());
        typeAdapters.put(float.class, new TypeAdapters.FloatAdapter());
        typeAdapters.put(double.class, new TypeAdapters.DoubleAdapter());
        // namespace
        typeAdapters.put(Short.class, new OptionalTypeAdapter<>(new TypeAdapters.ShortAdapter()));
        typeAdapters.put(Integer.class, new OptionalTypeAdapter<>(new TypeAdapters.IntegerAdapter()));
        typeAdapters.put(Long.class, new OptionalTypeAdapter<>(new TypeAdapters.LongAdapter()));
        typeAdapters.put(Boolean.class, new OptionalTypeAdapter<>(new TypeAdapters.BooleanAdapter()));
        typeAdapters.put(Float.class, new OptionalTypeAdapter<>(new TypeAdapters.FloatAdapter()));
        typeAdapters.put(Double.class, new OptionalTypeAdapter<>(new TypeAdapters.DoubleAdapter()));
        typeAdapters.put(String.class, new OptionalTypeAdapter<>(new TypeAdapters.StringAdapter()));
        // adapter
        TYPE_ADAPTERS = ImmutableMap.copyOf(typeAdapters);
    }

    /**
     * get instance of micro ORM
     *
     * @return
     */
    public static MicroOrm getInstance() {
        if (microOrm == null) {
            synchronized (MicroOrm.class) {
                if (microOrm == null) {
                    microOrm = new MicroOrm();
                }
            }
        }

        return microOrm;
    }

    /**
     * Constructs a MicroOrm object with default configuration, i.e. with support
     * only for primitives, boxed primitives and String fields.
     */
    public MicroOrm() {
        this(TYPE_ADAPTERS);
    }

    private MicroOrm(ImmutableMap<Class<?>, TypeAdapter<?>> typeAdapters) {
        mTypeAdapters = typeAdapters;
    }

    /**
     * Creates an object of the specified type from the current row in
     * {@link Cursor}.
     *
     * @param <T>    the type of the desired object
     * @param c      an open {@link Cursor} with position set to valid row
     * @param tClass The {@link Class} of the desired object
     * @return an object of type T created from the current row in {@link Cursor}
     */
    public <T> T fromCursor(Cursor c, Class<T> tClass) {
        try {
            DaoAdapter<T> adapter = getAdapter(tClass);
            return adapter.fromCursor(c, adapter != null ? adapter.createInstance() : null);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * Fills the field in the provided object with data from the current row in
     * {@link Cursor}.
     *
     * @param <T>    the type of the provided object
     * @param c      an open {@link Cursor} with position set to valid row
     * @param object the instance to be filled with data
     * @return the same object for easy chaining
     */
    @SuppressWarnings("unchecked")
    public <T> T fromCursor(Cursor c, T object) {
        try {
            return ((DaoAdapter<T>) getAdapter(object.getClass())).fromCursor(c, object);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * Creates the {@link ContentValues} from the provided object.
     *
     * @param <T>    the type of the provided object
     * @param object the object to be converted into {@link ContentValues}
     * @return the {@link ContentValues} created from the provided object
     */
    @SuppressWarnings("unchecked")
    public <T> ContentValues toContentValues(T object) {
        try {
            DaoAdapter<T> adapter = (DaoAdapter<T>) getAdapter(object.getClass());
            return adapter.toContentValues(adapter != null ? adapter.createContentValues() : null, object);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * Convenience method for converting the whole {@link Cursor} into
     * {@link List} of objects of specified type.
     *
     * @param <T>    the type of the provided object
     * @param c      a valid {@link Cursor}; the provided {@link Cursor} will not be
     *               closed
     * @param tClass The {@link Class} of the desired object
     * @return the {@link List} of object of type T created from the entire
     * {@link Cursor}
     */
    public <T> List<T> listFromCursor(Cursor c, Class<T> tClass) {
        try {
            List<T> result = Lists.newArrayList();

            if (c != null && c.moveToFirst()) {
                DaoAdapter<T> adapter = getAdapter(tClass);
                do {
                    result.add(adapter.fromCursor(c, adapter != null ? adapter.createInstance() : null));
                } while (c.moveToNext());
            }

            return result;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * Method for acquiring the {@link Function} converting the {@link Cursor}
     * row into object of specified type.
     *
     * @param <T>    the type of the provided object
     * @param tClass The {@link Class} of the function output type
     * @return the {@link Function} converting {@link Cursor} row into object
     * of type T.
     */
    public <T> Function<Cursor, T> getFunctionFor(final Class<T> tClass) {
        try {
            return new Function<Cursor, T>() {
                private final DaoAdapter<T> mAdapter = getAdapter(tClass);

                @Override
                public T apply(Cursor c) {
                    try {
                        return mAdapter.fromCursor(c, mAdapter.createInstance());
                    } catch (Exception e) {
                        Logger.e(TAG, e);
                    }

                    return null;
                }
            };
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * Constructs new {@link ColumnFunctionBuilder} for specified
     * {@code columnName}.
     */
    public ColumnFunctionBuilder getColumn(final String columnName) {
        try {
            Preconditions.checkNotNull(columnName);

            return new ColumnFunctionBuilder() {
                @SuppressWarnings("unchecked")
                @Override
                public <T> Function<Cursor, T> as(Class<T> tClass) {
                    try {
                        Preconditions.checkArgument(mTypeAdapters.containsKey(tClass));
                        final TypeAdapter<T> adapter = (TypeAdapter<T>) mTypeAdapters.get(tClass);

                        return c -> {
                            try {
                                return adapter.fromCursor(c, columnName);
                            } catch (Exception e) {
                                Logger.e(TAG, e);
                            }

                            return null;
                        };
                    } catch (Exception e) {
                        Logger.e(TAG, e);
                    }

                    return null;
                }
            };
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * Returns an array containing column names needed by {@link MicroOrm} to
     * successfully create an object of the specified type from {@link Cursor}.
     *
     * @param tClass The {@link Class} of the object, for which the projection
     *               should be generated
     * @return the {@link String[]} containing column names
     */
    public <T> String[] getProjection(Class<T> tClass) {
        try {
            return getAdapter(tClass).getProjection();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> DaoAdapter<T> getAdapter(Class<T> tClass) {
        try {
            DaoAdapter<?> cached = mDaoAdapterCache.get(tClass);
            if (cached != null) {
                return (DaoAdapter<T>) cached;
            }

            DaoAdapter<T> adapter = buildDaoAdapter(tClass);
            mDaoAdapterCache.put(tClass, adapter);

            return adapter;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    private <T> DaoAdapter<T> buildDaoAdapter(Class<T> tClass) {
        try {
            ImmutableList.Builder<FieldAdapter> fieldAdapters = ImmutableList.builder();
            ImmutableList.Builder<EmbeddedFieldInitializer> fieldInitializers = ImmutableList.builder();

            for (Field field : Fields.allFieldsIncludingPrivateAndSuper(tClass)) {
                field.setAccessible(true);
                Column columnAnnotation = field.getAnnotation(Column.class);

                if (columnAnnotation != null) {
                    if (field.getType().isPrimitive() && columnAnnotation.treatNullAsDefault()) {
                        throw new IllegalArgumentException("Cannot set treatNullAsDefault on primitive members");
                    }

                    if (columnAnnotation.treatNullAsDefault() && columnAnnotation.readonly()) {
                        throw new IllegalArgumentException("It doesn't make sense to set treatNullAsDefault on readonly column");
                    }

                    ColumnFieldAdapter fieldAdapter = new ColumnFieldAdapter(field, mTypeAdapters.get(field.getType()));
                    fieldAdapters.add(fieldAdapter);
                }

                Embedded embeddedAnnotation = field.getAnnotation(Embedded.class);
                if (embeddedAnnotation != null) {
                    DaoAdapter<?> daoAdapter = getAdapter(field.getType());
                    EmbeddedFieldAdapter fieldAdapter = new EmbeddedFieldAdapter(field, daoAdapter);

                    fieldAdapters.add(fieldAdapter);
                    fieldInitializers.add(new EmbeddedFieldInitializer(field, daoAdapter));
                }
            }

            return new ReflectiveDaoAdapter<>(tClass, fieldAdapters.build(), fieldInitializers.build());
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * Constructs {@link Function} converting single column in {@link Cursor}
     * row into object of given type. You can get builder instances
     */
    public interface ColumnFunctionBuilder {
        /**
         * @param <T>    the type of the requested object
         * @param tClass The {@link Class} of the function output type
         * @return the {@link Function} converting {@link Cursor} row into object
         * of type T using {@link TypeAdapter}s registered in current
         */
        <T> Function<Cursor, T> as(Class<T> tClass);
    }

    /**
     * Use this builder to construct a {@link MicroOrm} instance when you need
     * support for custom fields' types. For {@link MicroOrm} with default
     * configuration, it is simpler to use {@code new MicroOrm()}. {@link Builder}
     * is best used by creating it, and then invoking
     * {@link #registerTypeAdapter(Class, TypeAdapter)} for each custom type, and
     * finally calling {@link #build()}.</p>
     */
    public static class Builder {
        private final Map<Class<?>, TypeAdapter<?>> mTypeAdapters;

        public Builder() {
            mTypeAdapters = Maps.newHashMap(TYPE_ADAPTERS);
        }

        /**
         * Configures MicroOrm for custom conversion of fields of given types.
         *
         * @param clazz       the {@link Class} of the type the {@link TypeAdapter} being
         *                    registered
         * @param typeAdapter implementation defining how the custom type should be
         *                    converted to {@link ContentValues} and from {@link Cursor}.
         * @return a reference to this {@link Builder} object to fulfill the
         * "Builder" pattern
         */
        public <T> Builder registerTypeAdapter(Class<T> clazz, TypeAdapter<T> typeAdapter) {
            try {
                mTypeAdapters.put(clazz, typeAdapter);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }

            return this;
        }

        /**
         * Creates a {@link MicroOrm} instance with support for custom types that
         * were registered with this {@link Builder}. This method is free of
         * side-effects to this {@code Builder} instance and hence can be called
         * multiple times.
         *
         * @return an instance of MicroOrm with support for custom types that were
         * registered with this this builder
         */
        public MicroOrm build() {
            try {
                return new MicroOrm(ImmutableMap.copyOf(mTypeAdapters));
            } catch (Exception e) {
                Logger.e(TAG, e);
            }

            return null;
        }
    }
}