package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.customizes.gifts.ExpandableCardView;
import com.mc.models.home.Chapter;
import com.mc.models.home.ChapterTemp;
import com.mc.models.home.Lesson;
import com.mc.models.home.LessonTemp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class LessonHomeAdapter extends BaseRecycleAdapter<Chapter, LessonHomeAdapter.LessonViewHolder> {
    private Context context;
    private Consumer<LessonTemp> lessonConsumer;
    private Consumer<ChapterTemp> cancelChapterConsumer;
    private Consumer<ChapterTemp> downloadChapterConsumer;
    private Consumer<Lesson> progressConsumer;
    private Consumer<Lesson> downloadConsumer;
    private int idxLesson;
    private int bookId;
    private boolean isOnline;


    public LessonHomeAdapter(Context context, int bookId, int idxLesson, Consumer<LessonTemp> lessonConsumer, Consumer<ChapterTemp> downloadChapterConsumer,
                             Consumer<ChapterTemp> cancelChapterConsumer, Consumer<Lesson> progressConsumer, Consumer<Lesson> downloadConsumer, boolean isOnline) {
        this.context = context;
        this.lessonConsumer = lessonConsumer;
        this.idxLesson = idxLesson;
        this.bookId = bookId;
        this.isOnline = isOnline;
        this.downloadChapterConsumer = downloadChapterConsumer;
        this.cancelChapterConsumer = cancelChapterConsumer;
        this.downloadConsumer = downloadConsumer;
        this.progressConsumer = progressConsumer;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expand_card_view, parent, false);
        return new LessonViewHolder(view);
    }

    void goToDetailLesson(LessonTemp lessonTemp, List<Lesson> lessons) {
        lessonTemp.setLessons(lessons);
        lessonConsumer.accept(lessonTemp);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        try {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.ecvLesson.getContainerView().setLayoutParams(layoutParams);

            Chapter chapter = listItems.get(position);
            holder.ecvLesson.setUseListView(true);
            holder.ecvLesson.setTitle(chapter.getName());
            holder.ecvLesson.setOnExpandedListener((v, isExpanded) -> {
                chapter.setIsExpand(isExpanded);
                chapter.setIsClickExpand(isExpanded);
                holder.ecvLesson.getLvItems().setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            });

            List<Lesson> lessons = chapter.getLessons();

            LessonSectionListViewAdapter lessonAdapter = new LessonSectionListViewAdapter(context, bookId, position, lessons, lessonTemp -> goToDetailLesson(lessonTemp, lessons),
                    downloadChaper -> downloadChapterConsumer.accept(new ChapterTemp(chapter, position)),
                    cancelChapter -> cancelChapterConsumer.accept(new ChapterTemp(chapter, position)),
                    progressLesson -> {
                        progressLesson.setIndex(position);
                        progressConsumer.accept(progressLesson);
                    }, downloadLesson -> {
                downloadLesson.setIndex(position);
                downloadConsumer.accept(downloadLesson);
            }, isOnline);

            holder.ecvLesson.getLvItems().setAdapter(lessonAdapter);

            if (position == idxLesson && !chapter.isClickExpand()) {
                chapter.setIsExpand(true);
                holder.ecvLesson.setExpanded(true);
                holder.ecvLesson.runAnimationArrow(ExpandableCardView.EXPANDING);
            }

            holder.ecvLesson.getLvItems().setVisibility(chapter.isExpand() ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LessonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ecv)
        ExpandableCardView ecvLesson;

        private LessonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
