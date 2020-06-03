package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DateTimeUtils;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.RecordItem;
import com.mc.utilities.AppUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class RecordAdapter extends BaseRecycleAdapter<RecordItem, RecordAdapter.RecordViewHolder> {

    private Context context;
    private Consumer<RecordItem> recordItemConsumer;

    public RecordAdapter(Context context, Consumer<RecordItem> recordItemConsumer) {
        this.context = context;
        this.recordItemConsumer = recordItemConsumer;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordAdapter.RecordViewHolder(itemView);
    }

    @SuppressLint({"StringFormatInvalid", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordItem item = listItems.get(position);
        long itemDuration = item.getLength();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);
        holder.txtTitleRecord.setText(AppUtils.removeEndPath(item.getName()));
        holder.txtRole.setText(String.format(context.getResources().getString(R.string.role), item.getRole()));
        holder.txtTimeRecord.setText(DateTimeUtils.getTimeAgosRecord(item.getTime()));
        holder.txtLengthRecord.setText(String.format("%02d:%02d", minutes, seconds));
        holder.llRoot.setOnClickListener(view -> recordItemConsumer.accept(item));
    }

    class RecordViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtTitleRecord)
        ExtTextView txtTitleRecord;
        @BindView(R.id.txtRole)
        ExtTextView txtRole;
        @BindView(R.id.txtTimeRecord)
        ExtTextView txtTimeRecord;
        @BindView(R.id.txtLengthRecord)
        ExtTextView txtLengthRecord;
        @BindView(R.id.llRoot)
        LinearLayout llRoot;

        private RecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
