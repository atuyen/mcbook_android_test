package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;

import com.mc.models.more.FAQ;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class FAQAdapter extends BaseRecycleAdapter<FAQ, FAQAdapter.FAQViewHolder> {
    private Context context;
    private Consumer<FAQ> faqConsumer;

    public FAQAdapter(Context context, Consumer<FAQ> faqConsumer) {
        this.context = context;
        this.faqConsumer = faqConsumer;
    }

    @NonNull
    @Override
    public FAQAdapter.FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_faq, parent, false);
        return new FAQAdapter.FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQAdapter.FAQViewHolder holder, int position) {
        try {
            FAQ faq = listItems.get(position);
            holder.txtFAQ.setText(faq.getName());
            holder.llFAQ.setOnClickListener(v -> faqConsumer.accept(faq));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class FAQViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llFAQ)
        LinearLayout llFAQ;
        @BindView(R.id.txtFAQ)
        ExtTextView txtFAQ;

        private FAQViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

