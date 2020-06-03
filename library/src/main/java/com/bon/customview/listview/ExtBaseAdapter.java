package com.bon.customview.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.bon.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dang on 5/27/2016.
 */

public abstract class ExtBaseAdapter<T> extends BaseAdapter {
    private static final String TAG = ExtBaseAdapter.class.getSimpleName();

    protected Context context;
    protected List<T> items = null;
    protected LayoutInflater layoutInflater;

    /**
     * @param context
     * @param items
     */
    public ExtBaseAdapter(Context context, List<T> items) {
        this.context = context;
        this.items = items;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * notification data
     *
     * @param items
     */
    public void notifyDataSetChanged(List<T> items) {
        try {
            this.items = items;
            this.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param newItem
     */
    public void addNewItem(T newItem) {
        try {
            if (newItem == null) return;
            if (this.items == null) {
                this.items = new ArrayList<>();
            }

            this.items.add(newItem);
            this.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param newItems
     */
    public void addNewItems(List<T> newItems) {
        try {
            if (newItems == null || newItems.size() <= 0) return;
            if (this.items == null) {
                this.items = new ArrayList<>();
            }

            this.items.addAll(newItems);
            this.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * remove item
     *
     * @param item
     */
    public void removeItem(T item) {
        try {
            if (item == null || this.items == null || this.items.size() <= 0) return;
            this.items.remove(item);
            this.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * remove items
     *
     * @param items
     */
    public void removeItems(List<T> items) {
        try {
            if (items == null || items.size() <= 0 || this.items == null || this.items.size() <= 0) {
                return;
            }

            this.items.removeAll(items);
            this.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * clear data
     */
    public void clearList() {
        try {
            if (this.items == null || this.items.size() <= 0) return;
            this.items.clear();
            this.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return this.items == null ? 0 : this.items.size();
    }

    @Override
    public T getItem(int position) {
        try {
            return this.items == null ? null : this.items.get(position);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public List<T> getItems() {
        return items;
    }
}
