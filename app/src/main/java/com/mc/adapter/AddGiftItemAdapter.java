package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddGiftItemAdapter extends BaseRecycleAdapter<String, AddGiftItemAdapter.StringViewHolder> {
    private Context context;

    public AddGiftItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_gift, parent, false);
        return new StringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
        try {
            String item = listItems.get(position);
            holder.txtGiftName.setText(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class StringViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtGiftName)
        ExtTextView txtGiftName;

        private StringViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
