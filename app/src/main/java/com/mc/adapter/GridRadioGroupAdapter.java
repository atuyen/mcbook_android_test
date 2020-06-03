package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridRadioGroupAdapter extends BaseRecycleAdapter<String, GridRadioGroupAdapter.GridRadioGroupViewHolder> {

    private Context context;
    private int checked;

    public GridRadioGroupAdapter(Context context, int checked) {
        this.context = context;
        this.checked = checked;
    }

    public int getCheckedItem() {
        return checked;
    }

    public String getValueCheckedItem() {
        return listItems.get(checked);
    }

    @NonNull
    @Override
    public GridRadioGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_gradio_group, parent, false);
        return new GridRadioGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridRadioGroupViewHolder holder, int position) {
        try {
            String str = listItems.get(position);
            holder.rbItem.setText(str);
            holder.rbItem.setOnClickListener(v -> {
                checked = position;
                notifyDataSetChanged();
            });
            if (checked != position) holder.rbItem.setChecked(false);
            else holder.rbItem.setChecked(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GridRadioGroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rbItem)
        RadioButton rbItem;

        private GridRadioGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
