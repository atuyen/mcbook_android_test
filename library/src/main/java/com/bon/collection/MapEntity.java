package com.bon.collection;

import java.util.List;

/**
 * Created by Dang on 5/26/2016.
 */
public class MapEntity<K, T> {
    private K key;
    private List<T> entities;

    public MapEntity() {
    }

    public MapEntity(K key, List<T> entities) {
        this.key = key;
        this.entities = entities;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "MapEntity{" +
                "key='" + key + '\'' +
                ", entities=" + entities +
                '}';
    }
}
