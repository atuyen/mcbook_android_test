package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DateTimeUtils;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.gift.HistoryExam;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamInfomationAdapter extends BaseRecycleAdapter<HistoryExam, ExamInfomationAdapter.ExamInfoViewHolder> {

    private Context context;

    public ExamInfomationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ExamInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_history_exam, parent, false);
        return new ExamInfoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExamInfoViewHolder holder, int position) {
        try {
            HistoryExam historyExam = listItems.get(position);
            holder.txtTimeStart.setText(DateTimeUtils.getTimeConvert(historyExam.getStartAt()));
            holder.txtDateStart.setText(DateTimeUtils.getCalendarConvert(historyExam.getStartAt()));
            holder.txtPoint.addSlice(new Slice.Builder(historyExam.getPoint() + " ")
                    .textColor(context.getResources().getColor(R.color.colorDarkOrange))
                    .style(Typeface.BOLD)
                    .build());
            holder.txtPoint.addSlice(new Slice.Builder(context.getString(R.string.point))
                    .style(Typeface.NORMAL)
                    .textColor(context.getResources().getColor(R.color.colorDarkOrange))
                    .build());
            holder.txtPoint.display();
            if (position == listItems.size() - 1)
                holder.viewLine.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    public class ExamInfoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtTimeStart)
        ExtTextView txtTimeStart;
        @BindView(R.id.txtDateStart)
        ExtTextView txtDateStart;
        @BindView(R.id.txtPoint)
        SpannableTextView txtPoint;
        @BindView(R.id.viewLine)
        View viewLine;

        private ExamInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
