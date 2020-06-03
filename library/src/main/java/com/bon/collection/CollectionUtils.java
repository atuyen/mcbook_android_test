package com.bon.collection;

import com.bon.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java8.util.function.Consumer;

/**
 * Created by Dang on 5/26/2016.
 */
public class CollectionUtils {
    private static final String TAG = CollectionUtils.class.getSimpleName();

    /**
     * convert Map<String, List<MapEntity<T>> to List<Map<T>>
     *
     * @param maps
     * @param <T>
     * @return
     */
    public static <K, T> List<MapEntity<K, T>> convertMapToList(Map<K, List<T>> maps) {
        List<MapEntity<K, T>> mapEntities = new ArrayList<>();

        try {
            if (maps != null && maps.size() > 0) {
                for (Map.Entry<K, List<T>> entry : maps.entrySet()) {
                    mapEntities.add(new MapEntity<>(entry.getKey(), entry.getValue()));
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return mapEntities;
    }

    public static <T> boolean isNullOrEmpty(T... collections) {
        return collections == null || collections.length <= 0;
    }

    /**
     * @param collections
     * @param <T>
     * @return true if collection not null or not empty
     */
    public static <T> boolean isNotNullOrEmpty(T... collections) {
        return !isNullOrEmpty(collections);
    }

    /**
     * @param collections
     * @param <T>
     * @return true if collection null or empty
     */
    public static <T> boolean isNullOrEmpty(List<T> collections) {
        return collections == null || collections.size() <= 0;
    }

    /**
     * @param collections
     * @param <T>
     * @return true if collection not null or not empty
     */
    public static <T> boolean isNotNullOrEmpty(List<T> collections) {
        return !isNullOrEmpty(collections);
    }

    /**
     * @param collections
     * @param consumer
     * @param <T>
     */
    public static <T> void isNullOrEmpty(List<T> collections, Consumer<List<T>> consumer) {
        if (isNullOrEmpty(collections) && consumer != null) consumer.accept(collections);
    }

    /**
     * @param collections
     * @param consumer
     * @param <T>
     */
    public static <T> void isNotNullOrEmpty(List<T> collections, Consumer<List<T>> consumer) {
        if (!isNullOrEmpty(collections) && consumer != null) consumer.accept(collections);
    }
}
