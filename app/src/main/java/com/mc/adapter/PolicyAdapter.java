package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.customizes.gifts.ExpandableCardView;

import com.mc.models.more.Policy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PolicyAdapter extends BaseRecycleAdapter<Policy, PolicyAdapter.PolicyViewHolder> {

    private Context context;

    public PolicyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PolicyAdapter.PolicyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expand_card_view, parent, false);
        return new PolicyAdapter.PolicyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyAdapter.PolicyViewHolder holder, int position) {
        try {
            Policy policy = listItems.get(position);
            holder.ecv.setTitle(policy.getName());
            List<String> content = policy.getContent();
            ContentPolicyAdapter contentPolicyAdapter = new ContentPolicyAdapter(context);
            holder.rvPolicy = holder.itemView.findViewById(R.id.rvItem);
            holder.rvPolicy.setLayoutManager(new LinearLayoutManager(context));
            contentPolicyAdapter.setDataList(content);
            holder.rvPolicy.setAdapter(contentPolicyAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PolicyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ecv)
        ExpandableCardView ecv;
        RecyclerView rvPolicy;

        private PolicyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
