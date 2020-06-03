package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.BookStatistic;
import com.mc.utilities.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BookStatisticAdapter extends BaseRecycleAdapter<BookStatistic, BookStatisticAdapter.BookStatisticViewHolder> {
    private Context context;

    public BookStatisticAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BookStatisticViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View bookView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_statistic, parent, false);
        return new BookStatisticViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookStatisticViewHolder holder, int position) {
        BookStatistic bookStatistic = listItems.get(position);
        holder.bindingData(bookStatistic);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class BookStatisticViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgPictureBook)
        ImageView imgPictureBook;
        @BindView(R.id.txtBookName)
        ExtTextView txtBookName;
        @BindView(R.id.txtStudyTime)
        ExtTextView txtStudyTime;
        @BindView(R.id.txtSumaryPoint)
        ExtTextView txtSumaryPoint;
        @BindView(R.id.bookProgress)
        ProgressBar bookProgress;
        @BindView(R.id.txtProgress)
        ExtTextView txtProgress;

        private BookStatisticViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        void bindingData(BookStatistic bookStatistic) {
            AppUtils.setImageGlide(context, bookStatistic.getAvatar(), R.drawable.ic_default_avatar, imgPictureBook);
            txtBookName.setText(bookStatistic.getName());
            txtProgress.setText(String.format("%d%%", bookStatistic.getProcess()));
            txtStudyTime.setText(String.format("%d %s", bookStatistic.getStudyTime(), context.getString(R.string.day)));
            txtSumaryPoint.setText(String.format("%d", bookStatistic.getTotalPoint()));
            bookProgress.setProgress(bookStatistic.getProcess());
        }
    }
}

