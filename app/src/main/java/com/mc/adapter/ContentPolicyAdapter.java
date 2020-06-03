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

public class ContentPolicyAdapter extends BaseRecycleAdapter<String, ContentPolicyAdapter.ContentPolicyViewHolder> {

    private Context context;

    public ContentPolicyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ContentPolicyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_policy, parent, false);
        return new ContentPolicyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentPolicyViewHolder holder, int position) {
        try {
            String content = listItems.get(position);
            holder.content.setText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ContentPolicyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content)
        ExtTextView content;

        private ContentPolicyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

