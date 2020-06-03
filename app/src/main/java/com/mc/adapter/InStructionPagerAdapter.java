package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mc.books.R;

import java.util.List;

public class InStructionPagerAdapter extends PagerAdapter {
    private List<Integer> data;
    private LayoutInflater mInflator;

    public InStructionPagerAdapter(Context context, List<Integer> d) {
        data = d;
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        ViewGroup layout;
        layout = (ViewGroup) mInflator.inflate(R.layout.pager_item, collection, false);
        ImageView imageView = layout.findViewById(R.id.image);
        imageView.setImageResource(Integer.parseInt(String.valueOf(data.get(position))));
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
