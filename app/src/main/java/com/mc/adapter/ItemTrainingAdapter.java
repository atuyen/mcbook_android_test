package com.mc.adapter;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.ItemTrainingResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class ItemTrainingAdapter extends BaseRecycleAdapter<ItemTrainingResponse, ItemTrainingAdapter.ViewHolder> {
    Consumer<ItemTrainingResponse> consumer;

    public ItemTrainingAdapter(Consumer<ItemTrainingResponse> consumer) {
        this.consumer = consumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemTrainingResponse itemTrainingResponse = listItems.get(position);
        if (itemTrainingResponse.getIsRead()) {
            holder.txtName.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.txtName.setTypeface(null, Typeface.BOLD);
        }
        holder.txtName.setText(itemTrainingResponse.getName());
        if (position == listItems.size() - 1) {
            holder.viewLine.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> consumer.accept(itemTrainingResponse));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtName)
        ExtTextView txtName;
        @BindView(R.id.viewLine)
        View viewLine;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
