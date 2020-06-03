package com.bon.customview.listview;

import android.widget.BaseExpandableListAdapter;

import com.bon.logger.Logger;

import java.util.List;

/**
 * Created by DangPP on 7/11/2016.
 */
public abstract class ExtBaseExpandableListAdapter<T extends ExtBaseExpandableEntity<K>, K> extends BaseExpandableListAdapter {
    private static final String TAG = ExtBaseExpandableListAdapter.class.getSimpleName();
    protected List<T> items;

    public ExtBaseExpandableListAdapter(List<T> items) {
        this.items = items;
    }

    public void notifyDataSetChanged(List<T> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        try {
            return this.items == null ? 0 : this.items.size();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    @Override
    public T getGroup(int groupPosition) {
        try {
            return getGroupCount() <= 0 ? null : this.items.get(groupPosition);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return getGroup(groupPosition) == null || getGroup(groupPosition).getItems() == null ? 0
                    : getGroup(groupPosition).getItems().size();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    @Override
    public K getChild(int groupPosition, int childPosition) {
        try {
            return getGroup(groupPosition) == null || getGroup(groupPosition).getItems() == null ? null
                    : getGroup(groupPosition).getItems().get(childPosition);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
