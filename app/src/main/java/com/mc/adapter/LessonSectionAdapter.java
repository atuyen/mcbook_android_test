package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.Lesson;
import com.mc.models.home.LessonTemp;
import com.mc.models.realm.LessonRealm;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import java8.util.function.Consumer;

import static com.mc.utilities.Constant.KEY_AUDIO;
import static com.mc.utilities.Constant.KEY_PDF;
import static com.mc.utilities.Constant.KEY_VIDEO;

public class LessonSectionAdapter extends BaseRecycleAdapter<Lesson, LessonSectionAdapter.LessonViewHolder> {

    private Consumer<LessonTemp> lessonConsumer;
    private Context context;
    private int bookId;

    public LessonSectionAdapter(Context context, int bookId, Consumer<LessonTemp> lessonConsumer) {
        this.context = context;
        this.lessonConsumer = lessonConsumer;
        this.bookId = bookId;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson_content, parent, false);
        return new LessonViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        try {
            Lesson lesson = listItems.get(position);

            File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, lesson.getMedia()));
            LessonRealm lessonRealm = AppContext.getRealm()
                    .where(LessonRealm.class)
                    .equalTo(LessonRealm.ID, lesson.getId())
                    .findFirst();
            if (lessonRealm != null) {
                holder.imgStatusFile.setVisibility(View.GONE);
                holder.donut_progress.setVisibility(View.GONE);
                switch (lessonRealm.getDownloadStatus()) {
                    case Constant.PENDIND_DOWNLOAD:
                        holder.imgStatusFile.setVisibility(View.VISIBLE);
                        holder.imgStatusFile.setImageResource(R.drawable.ic_download_wait);
                        break;
                    case Constant.DOWNLOAD_ERROR:
                        holder.imgStatusFile.setVisibility(View.VISIBLE);
                        holder.imgStatusFile.setImageResource(R.drawable.ic_download_error);
                        break;
                    case Constant.DOWNLOADED:
                        holder.imgStatusFile.setVisibility(View.VISIBLE);
                        holder.imgStatusFile.setImageResource(R.drawable.ic_download_delete);
                        holder.imgStatusFile.setOnClickListener(v -> {
                            AppContext.getRealm().executeTransaction(realm -> {
                                RealmResults<LessonRealm> result = AppContext.getRealm()
                                        .where(LessonRealm.class)
                                        .equalTo(LessonRealm.ID, lesson.getId())
                                        .findAll();
                                result.deleteAllFromRealm();
                            });
                            file.delete();
                            holder.imgStatusFile.setVisibility(View.GONE);
                        });
                        break;
                    case Constant.DOWNLOADING:
                        holder.donut_progress.setVisibility(View.VISIBLE);
                        holder.donut_progress.setProgress(lessonRealm.getProgress());
                        break;
                    case Constant.NONE_DOWNLOAD:
                        holder.imgStatusFile.setVisibility(View.GONE);
                        holder.donut_progress.setVisibility(View.GONE);
                        break;
                }
            }

            holder.txtLessonName.setText(lesson.getName());
            String duration = lesson.getDuration() == null ? "" : AppUtils.convertTime(lesson.getDuration()) + "";
            if (lesson.getType().toLowerCase().equals(Constant.KEY_PDF))
                holder.txtLessonDuration.setText(renderType(lesson.getType()));
            else
                holder.txtLessonDuration.setText(String.format("%s: %s", renderType(lesson.getType()), duration));


            String page;
            if (lesson.getPage() == null)
                page = "0";
            else
                page = String.format("%d", lesson.getPage());
            holder.txtLessonPage.setText(context.getString(R.string.page) + " " + page);
            holder.imgCheck.setVisibility(lesson.getChecked() ? View.VISIBLE : View.INVISIBLE);
            holder.llLesson.setOnClickListener(v -> {
                LessonTemp lessonTemp = new LessonTemp();
                lessonTemp.setIndex(position);
                lessonTemp.setLesson(lesson);
                lessonConsumer.accept(lessonTemp);
            });
            if (position == listItems.size() - 1) {
                holder.viewLine.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String renderType(String type) {
        switch (type) {
            case KEY_PDF:
                return context.getString(R.string.pdf);
            case KEY_AUDIO:
                return context.getString(R.string.audio);
            case KEY_VIDEO:
                return context.getString(R.string.video);
        }
        return "";
    }

    class LessonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtLessonName)
        ExtTextView txtLessonName;
        @BindView(R.id.txtLessonDuration)
        ExtTextView txtLessonDuration;
        @BindView(R.id.txtLessonPage)
        ExtTextView txtLessonPage;
        @BindView(R.id.llLesson)
        LinearLayout llLesson;
        @BindView(R.id.imgCheck)
        ImageView imgCheck;
        @BindView(R.id.viewLine)
        View viewLine;
        @BindView(R.id.imgStatusFile)
        ImageView imgStatusFile;
        @BindView(R.id.donut_progress)
        DonutProgress donut_progress;
        @BindView(R.id.tvProgress)
        ExtTextView tvProgress;

        private LessonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
} 

