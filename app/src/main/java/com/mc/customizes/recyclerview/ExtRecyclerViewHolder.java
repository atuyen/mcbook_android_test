package com.mc.customizes.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class ExtRecyclerViewHolder extends RecyclerView.ViewHolder {
    public ExtRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
