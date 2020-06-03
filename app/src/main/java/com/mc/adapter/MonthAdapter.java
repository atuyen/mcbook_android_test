package com.mc.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
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
import java8.util.function.Consumer;

public class MonthAdapter extends BaseRecycleAdapter<String, MonthAdapter.MonthViewHolder> {

    private Context context;
    private Consumer<Integer> monthConsumer;
    private int monthSelected = 0;

    public MonthAdapter(Context context, Consumer<Integer> monthConsumer) {
        this.context = context;
        this.monthConsumer = monthConsumer;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false);
        return new MonthAdapter.MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthAdapter.MonthViewHolder holder, int position) {
        try {
            String strMonth = listItems.get(position);
            holder.txtMonth.setText(strMonth);
            holder.txtMonth.setOnClickListener(view -> {
                monthConsumer.accept(position + 1);
                monthSelected = position;
                notifyDataSetChanged();
            });
            if (monthSelected == position) {
                holder.txtMonth.setTextColor(context.getResources().getColor(R.color.colorDarkBlue));
                holder.txtMonth.setTypeface(null, Typeface.BOLD);
            } else {
                holder.txtMonth.setTextColor(context.getResources().getColor(R.color.colorTextGray));
                holder.txtMonth.setTypeface(null, Typeface.NORMAL);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMonth)
        ExtTextView txtMonth;

        private MonthViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
