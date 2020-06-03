package com.bon.customview.keyvaluepair;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.textview.ExtTextView;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtKeyValuePairAdapter<T extends ExtKeyValuePair> extends ExtBaseAdapter<T> {
    private static final String TAG = ExtKeyValuePairAdapter.class.getSimpleName();

    // text gravity
    private int textGravity = Gravity.CENTER;

    /**
     * @param items
     */
    public ExtKeyValuePairAdapter(Context context, List<T> items) {
        super(context, items);
    }

    /**
     * @param items
     */
    public ExtKeyValuePairAdapter(Context context, List<T> items, int textGravity) {
        super(context, items);
        this.textGravity = textGravity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.key_value_pair_row, parent, false);
            viewHolder = new ViewHolder<T>(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(context, getItem(position), textGravity);

        return convertView;
    }

    static class ViewHolder<T extends ExtKeyValuePair> {
        ExtTextView tvContent;

        ViewHolder(View view) {
            try {
                this.tvContent = (ExtTextView) view.findViewById(R.id.tvContent);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        public void setData(Context context, T keyValuePair, int textGravity) {
            try {
                if (keyValuePair == null) return;

                this.tvContent.setText(keyValuePair.getValue());
                this.tvContent.setGravity(textGravity);

                if (keyValuePair.isSelected()) {
                    TextUtils.setTextAppearance(context, tvContent, R.style.StyleContentBold);
                } else {
                    TextUtils.setTextAppearance(context, tvContent, R.style.StyleContent);
                }
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }
}
