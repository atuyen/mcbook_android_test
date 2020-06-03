package com.bon.customview.listview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dang on 7/27/2016.
 */
public class ExtBaseExpandableEntity<K> implements Serializable {
    protected List<K> items;

    public ExtBaseExpandableEntity() {
    }

    public ExtBaseExpandableEntity(List<K> items) {
        this.items = items;
    }

    public List<K> getItems() {
        return this.items == null ? this.items = new ArrayList<>() : this.items;
    }

    public void setItems(List<K> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ExtBaseExpandableEntity{" +
                "items=" + items +
                '}';
    }
}
