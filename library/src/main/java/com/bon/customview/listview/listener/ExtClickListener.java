package com.bon.customview.listview.listener;

/**
 * Created by Dang on 6/9/2016.
 */
public interface ExtClickListener<T> {
    /**
     * onClick
     *
     * @param position
     * @param item
     */
    void onClick(int position, T item);
}
