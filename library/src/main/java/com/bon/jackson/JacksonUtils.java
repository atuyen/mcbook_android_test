package com.bon.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;

import java.util.List;

/**
 * Created by Dang on 4/4/2016.
 */
@SuppressWarnings("ALL")
public class JacksonUtils {
    private static final String TAG = JacksonUtils.class.getSimpleName();
    private static ObjectMapper mapper = null;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * get instance mapper
     *
     * @return
     */
    public static ObjectMapper getJsonMapper() {
        return mapper;
    }

    /**
     * convert object to string
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String writeValueToString(T object) {
        try {
            return getJsonMapper().writeValueAsString(object);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * convert json to object
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T convertJsonToObject(String json, TypeReference<T> typeReference) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return (T) getJsonMapper().readValue(json, typeReference);
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * convert json to object
     *
     * @param tClass (Demo.class)
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T convertJsonToObject(String json, Class<T> tClass) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return getJsonMapper().readValue(json, tClass);
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * convert json to list
     *
     * @param tClass (Demo.class)
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<T> convertJsonToListObject(String json, Class<T> tClass) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return (List<T>) getJsonMapper().readValue(json, getListJavaType(tClass));
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * convert json to list
     *
     * @param json
     * @param javaType
     * @param <T>
     * @return
     */
    public static <T> List<T> convertJsonToListObject(String json, JavaType javaType) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return (List<T>) getJsonMapper().readValue(json, javaType);
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * get javatype from class
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> JavaType getListJavaType(Class<T> tClass) {
        return getJsonMapper().getTypeFactory().constructCollectionType(List.class, tClass);
    }
}
