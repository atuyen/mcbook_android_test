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
import com.mc.models.home.Chapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

import static com.mc.utilities.Constant.MAX_LINE;

public class ChapterAdapter extends BaseRecycleAdapter<Chapter, ChapterAdapter.ChapterViewHolder> {
    private Context context;
    private Consumer<Chapter> chapterConsumer;

    public ChapterAdapter(Context context, Consumer<Chapter> chapterConsumer) {
        this.context = context;
        this.chapterConsumer = chapterConsumer;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        try {
            Chapter chapter = listItems.get(position);
            holder.txtChapterName.setText(chapter.getName());
            chapter.setIndex(position);
            holder.llChapter.setOnClickListener(v -> chapterConsumer.accept(chapter));
            if (position == listItems.size() - 1 && listItems.size() < MAX_LINE) holder.viewLine.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtChapterName)
        ExtTextView txtChapterName;
        @BindView(R.id.llChapter)
        LinearLayout llChapter;
        @BindView(R.id.viewLine)
        View viewLine;

        private ChapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
