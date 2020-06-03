package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.textview.ExtTextView;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.dialog.ErrorConfirmDialog;
import com.mc.models.home.Lesson;
import com.mc.models.home.LessonTemp;
import com.mc.models.realm.LessonRealm;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import java8.util.function.Consumer;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.mc.utilities.Constant.KEY_AUDIO;
import static com.mc.utilities.Constant.KEY_PDF;
import static com.mc.utilities.Constant.KEY_VIDEO;
import static com.mc.utilities.Constant.NONE_DOWNLOAD;

public class LessonSectionListViewAdapter extends ExtBaseAdapter<Lesson> {
    private Consumer<LessonTemp> lessonConsumer;
    private int bookId;
    private Consumer<Object> cancelChapterConsumer;
    private Consumer<Object> downloadChapterConsumer;
    private Consumer<Lesson> progressConsumer;
    private Consumer<Lesson> downloadConsumer;
    private int index;
    private int progress;
    private boolean isOnline;

    public LessonSectionListViewAdapter(Context context, int bookId, int index, List<Lesson> lessons, Consumer<LessonTemp> lessonConsumer, Consumer<Object> downloadChapterConsumer,
                                        Consumer<Object> cancelChapterConsumer, Consumer<Lesson> progressConsumer, Consumer<Lesson> downloadConsumer, boolean isOnline) {
        super(context, lessons);
        this.lessonConsumer = lessonConsumer;
        this.bookId = bookId;
        this.downloadChapterConsumer = downloadChapterConsumer;
        this.cancelChapterConsumer = cancelChapterConsumer;
        this.index = index;
        this.isOnline = isOnline;
        this.progressConsumer = progressConsumer;
        this.downloadConsumer = downloadConsumer;
    }

    @Override
    public int getCount() {
        return getItems().size() + 1;
    }

    @SuppressLint("DefaultLocale")
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        //fake footer
        if (position == getItems().size()) {
            holder.llLesson.setVisibility(View.GONE);
            holder.viewLine.setVisibility(View.GONE);
            holder.llChapterFooter.setVisibility(View.VISIBLE);
            if (isOnline && getItems().size() > 0) {
                RealmResults<LessonRealm> results = AppContext.getRealm()
                        .where(LessonRealm.class).equalTo(LessonRealm.BOOK_ID, bookId)
                        .equalTo(LessonRealm.DOWNLOAD_TYPE, Constant.DOWNLOAD_CHAPTER)
                        .equalTo(LessonRealm.USER_ID, AppContext.getUserId()).equalTo(LessonRealm.INDEX, index)
                        .beginGroup()
                        .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.PENDIND_DOWNLOAD)
                        .or()
                        .equalTo(LessonRealm.DOWNLOAD_STATUS, Constant.DOWNLOADING)
                        .endGroup()
                        .findAll();
                if (results.size() > 0) {
                    holder.txtDownload.setText(context.getString(R.string.cancel_chapter_footer));
                    holder.imgDownload.setVisibility(View.GONE);
                    holder.imgProgressChapter.setVisibility(View.VISIBLE);
                    GifDrawable gifDrawable = (GifDrawable) holder.imgProgressChapter.getDrawable();
                    gifDrawable.setLoopCount(0);
//                    holder.donutProgressChapter.setProgress((((getItems().size() - results.size()) * 100) + progress) / getItems().size());
                    holder.llChapterFooter.setOnClickListener(v -> cancelChapterConsumer.accept(null));
                } else {
                    holder.txtDownload.setText(context.getString(R.string.download_chapter_footer));
                    holder.imgDownload.setVisibility(View.VISIBLE);
                    holder.imgProgressChapter.setVisibility(View.GONE);
                    holder.llChapterFooter.setOnClickListener(v -> downloadChapterConsumer.accept(null));
                }
            } else {
                //disable download chapter
                holder.txtDownload.setText(context.getString(R.string.download_chapter_footer));
                holder.txtDownload.setTextColor(context.getResources().getColor(R.color.disable));
                holder.imgDownload.setVisibility(View.VISIBLE);
                holder.imgDownload.setImageResource(R.drawable.ic_download_disable);
                holder.imgProgressChapter.setVisibility(View.GONE);
                holder.llChapterFooter.setOnClickListener(null);
            }

        } else {
            // content
            try {
                Lesson lesson = items.get(position);
                holder.llChapterFooter.setVisibility(View.GONE);
                holder.llLesson.setVisibility(View.VISIBLE);
                File file = new File(AppUtils.getFilePath(AppContext.getInstance(), Constant.FOLDER_BOOK, bookId, lesson.getMedia()));
                LessonRealm lessonRealm = AppContext.getRealm()
                        .where(LessonRealm.class).equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                        .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                        .findFirst();
                if (!isOnline) {
                    holder.txtLessonDuration.setTextColor(context.getResources().getColor(R.color.disable));
                    holder.txtLessonName.setTextColor(context.getResources().getColor(R.color.disable));
                    holder.txtLessonPage.setTextColor(context.getResources().getColor(R.color.disable));
                    holder.imgCheck.setImageResource(R.drawable.ic_check_disable);
                }
                if (lessonRealm != null) {
                    holder.imgStatusFile.setVisibility(View.GONE);
                    holder.imgProgress.setVisibility(View.GONE);
                    switch (lessonRealm.getDownloadStatus()) {
                        case Constant.PENDIND_DOWNLOAD:
                            holder.imgStatusFile.setVisibility(View.VISIBLE);
                            holder.imgStatusFile.setImageResource(R.drawable.ic_download_wait);
                            holder.imgStatusFile.setOnClickListener(null);
                            break;
                        case Constant.DOWNLOAD_ERROR:
                            holder.imgStatusFile.setVisibility(View.VISIBLE);
                            holder.imgStatusFile.setImageResource(R.drawable.ic_download_error);
                            holder.imgStatusFile.setOnClickListener(null);
                            break;
                        case Constant.DOWNLOADED:
                            if (!isOnline) {
                                holder.txtLessonDuration.setTextColor(context.getResources().getColor(R.color.colorTextGray));
                                holder.txtLessonName.setTextColor(context.getResources().getColor(R.color.colorViolet));
                                holder.txtLessonPage.setTextColor(context.getResources().getColor(R.color.colorDarkOrange));
                                holder.imgCheck.setImageResource(R.drawable.ic_check);
                            }
                            holder.imgStatusFile.setVisibility(View.VISIBLE);
                            holder.imgStatusFile.setImageResource(R.drawable.ic_download_delete);
                            holder.imgStatusFile.setOnClickListener(v -> {
                                ErrorConfirmDialog errorConfirmDialog = new ErrorConfirmDialog(context, context.getString(R.string.yes), context.getString(R.string.dialog_delete_lesson), view -> {
                                    AppContext.getRealm().beginTransaction();
                                    LessonRealm lessonRealm1 = AppContext.getRealm()
                                            .where(LessonRealm.class).equalTo(LessonRealm.ID, AppUtils.getLessonRealmId(lesson.getId(), lesson.getMedia()))
                                            .equalTo(LessonRealm.USER_ID, AppContext.getUserId())
                                            .findFirst();
                                    if (lessonRealm1 != null) {
                                        lessonRealm1.setDownloadStatus(NONE_DOWNLOAD);
                                        lessonRealm1.setProgress(0);
                                    }

                                    AppContext.getRealm().commitTransaction();
                                    file.delete();

                                    if (isOnline) {
                                        holder.imgStatusFile.setImageResource(R.drawable.ic_download);
                                        holder.imgStatusFile.setOnClickListener(v1 -> downloadConsumer.accept(lesson));
                                    } else {
                                        holder.imgStatusFile.setImageResource(R.drawable.ic_download_disable);
                                        holder.imgStatusFile.setOnClickListener(null);
                                    }
                                });
                                errorConfirmDialog.show();
                            });
                            break;
                        case Constant.DOWNLOADING:
                            holder.imgProgress.setVisibility(View.VISIBLE);
                            GifDrawable gifDrawable = (GifDrawable) holder.imgProgress.getDrawable();
                            gifDrawable.setLoopCount(0);
                            progress = lessonRealm.getProgress();
//                            holder.donut_progress.setProgress(lessonRealm.getProgress());
                            holder.imgProgress.setOnClickListener(v -> progressConsumer.accept(lesson));
                            break;
                        case Constant.NONE_DOWNLOAD:
                            holder.imgProgress.setVisibility(View.GONE);
                            holder.imgStatusFile.setVisibility(View.VISIBLE);

                            if (isOnline) {
                                holder.imgStatusFile.setImageResource(R.drawable.ic_download);
                                holder.imgStatusFile.setOnClickListener(v -> downloadConsumer.accept(lesson));
                            } else {
                                holder.imgStatusFile.setImageResource(R.drawable.ic_download_disable);
                                holder.imgStatusFile.setOnClickListener(null);
                            }
                            break;
                    }
                }

                holder.txtLessonName.setText(lesson.getName());
                String duration = lesson.getDuration() == null ? "" : AppUtils.convertTime(lesson.getDuration()) + "";
                if (lesson.getType().toLowerCase().equals(KEY_PDF))
                    holder.txtLessonDuration.setText(renderType(lesson.getType()));
                else
                    holder.txtLessonDuration.setText(String.format("%s: %s", renderType(lesson.getType()), duration));


                String page;
                if (lesson.getPage() == null)
                    page = "0";
                else
                    page = String.format("%d", lesson.getPage());


                holder.txtLessonPage.setText(String.format("%s %s", context.getString(R.string.page), page));
                holder.imgCheck.setVisibility(lesson.getChecked() ? View.VISIBLE : View.INVISIBLE);
                holder.llLesson.setOnClickListener(v -> {
                    if (file.exists() || isOnline) {
                        LessonTemp lessonTemp = new LessonTemp();
                        lessonTemp.setIndex(position);
                        lessonTemp.setLesson(lesson);
                        lessonConsumer.accept(lessonTemp);
                    }
                });

                holder.viewLine.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LessonViewHolder lessonViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_lesson_content, parent, false);

            lessonViewHolder = new LessonViewHolder(convertView);
            convertView.setTag(lessonViewHolder);
        } else
            lessonViewHolder = (LessonViewHolder) convertView.getTag();

        onBindViewHolder(lessonViewHolder, position);
        return convertView;
    }

    class LessonViewHolder {
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
        //        @BindView(R.id.donut_progress)
//        DonutProgress donut_progress;
        @BindView(R.id.llChapterFooter)
        LinearLayout llChapterFooter;
        @BindView(R.id.imgDownload)
        ImageView imgDownload;
        //        @BindView(R.id.donut_progress_chapter)
//        DonutProgress donutProgressChapter;
        @BindView(R.id.txtDownload)
        ExtTextView txtDownload;
        @BindView(R.id.imgProgress)
        GifImageView imgProgress;
        @BindView(R.id.imgProgressChapter)
        GifImageView imgProgressChapter;

        private LessonViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
} 

