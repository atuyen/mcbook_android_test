package com.mc.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.Answer;
import com.mc.models.home.DetailQuestion;
import com.mc.models.home.MediaPlayerResponse;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.timfreiheit.mathjax.android.MathJaxView;
import java8.util.function.Consumer;

import static com.mc.utilities.Constant.AUDIO;
import static com.mc.utilities.Constant.IMAGE;
import static com.mc.utilities.Constant.VIDEO;

public class SingleChooseAdapter extends BaseRecycleAdapter<Answer, SingleChooseAdapter.AnswerViewHolder> {
    private Activity context;
    private boolean isCheck;
    private int lastIndex;
    private boolean isCorrect;
    private Consumer<Answer> answerConsumer;
    private Consumer<MediaPlayerResponse> audioConsumer;
    private int padding;
    private DetailQuestion detailQuestion;
    private boolean isGrid = false;
    private Consumer<Integer> positionConsumer;
    private static final String CHECKED = "checked";
    private boolean isExam;
    private int posPlaying = -1;

    public SingleChooseAdapter(Activity context, DetailQuestion detailQuestion, Consumer<Answer> answerConsumer, Consumer<MediaPlayerResponse> audioConsumer, Consumer<Integer> positionConsumer, Integer position) {
        this.context = context;
        this.detailQuestion = detailQuestion;
        if (position == null) this.lastIndex = -1;
        else this.lastIndex = position;
        this.isCorrect = false;
        this.answerConsumer = answerConsumer;
        this.audioConsumer = audioConsumer;
        this.positionConsumer = positionConsumer;
        padding = AppUtils.dip2px(context, 8);
    }

    public boolean isChooseAnswer() {
        return lastIndex != -1;
    }

    public boolean isCorrectAnswer() {
        return isCorrect;
    }

    public void setIsCheck() {
        isCheck = true;
        isExam = false;
        notifyDataSetChanged();
    }

    public void setPositionPlaying(int pos) {
        posPlaying = pos;
        notifyDataSetChanged();
    }

    public void setIsCheckExam() {
        isCheck = true;
        isExam = true;
        notifyDataSetChanged();
    }

    public List<Answer> getSubmitAnswer() {
        for (int i = 0; i < listItems.size(); i++) {
            if (lastIndex == i)
                listItems.get(i).setTemp(CHECKED);
            else listItems.get(i).setTemp("");
        }
        return listItems;
    }

    @NonNull
    @Override
    public SingleChooseAdapter.AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_answer_single_choose, parent, false);
        return new SingleChooseAdapter.AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleChooseAdapter.AnswerViewHolder holder, int position) {
        try {
            Answer answer = listItems.get(position);
            String content = answer.getText();
            content = content.replace(Constant.TAG_P, "");
            content = content.replace(Constant.TAG_P_1, "");
            if (AppUtils.isMathJax(content)) {
                holder.itemMathJaxSingleChoose.setVisibility(View.VISIBLE);
                holder.txtAnswerContent.setVisibility(View.GONE);
                holder.itemMathJaxSingleChoose.setInputText(content);
            } else {
                holder.itemMathJaxSingleChoose.setVisibility(View.GONE);
                holder.txtAnswerContent.setVisibility(View.VISIBLE);
                holder.txtAnswerContent.setText(Html.fromHtml(content));
            }

            holder.llAnswer.setOnClickListener(v -> {
                lastIndex = position;
                positionConsumer.accept(position);
                notifyDataSetChanged();
            });

            holder.itemMathJaxSingleChoose.setRenderListener(new MathJaxView.OnMathJaxRenderListener() {
                @Override
                public void onRendered() {

                }

                @Override
                public void onClickView() {
                    if (!isCheck) {
                        lastIndex = position;
                        positionConsumer.accept(position);
                        context.runOnUiThread(SingleChooseAdapter.this::notifyDataSetChanged);
                    }
                }
            });

            holder.imgAudioSingleChoose.setVisibility(View.GONE);
            holder.imgContentAnswerSingleChoose.setVisibility(View.GONE);

            holder.imgContentAnswerSingleChoose.post(() -> {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.imgContentAnswerSingleChoose.getWidth(),
                        (int) (holder.imgContentAnswerSingleChoose.getWidth() / 1.3));
                holder.imgContentAnswerSingleChoose.setLayoutParams(params);
            });
            holder.imgPlayVideo.setVisibility(View.GONE);
            holder.txtAnswerContent.setMinLines(1);
            holder.llAnswerContent.setPadding(padding, padding, padding, padding);

            if (!StringUtils.isEmpty(answer.getTypeMedia())) {
                switch (answer.getTypeMedia()) {
                    case AUDIO:
                        holder.imgAudioSingleChoose.setVisibility(View.VISIBLE);
                        holder.imgAudioSingleChoose.setImageResource(posPlaying == position ? R.drawable.ic_pause_3 : R.drawable.ic_audio_single_choose);
                        holder.imgAudioSingleChoose.setOnClickListener(v -> audioConsumer.accept(new MediaPlayerResponse(answer.getMediaUri(), position)));
                        holder.txtAnswerContent.setMinLines(1);
                        holder.llAnswerContent.setPadding(padding, padding, padding, padding);
                        break;
                    case VIDEO:
                        holder.imgContentAnswerSingleChoose.setVisibility(View.VISIBLE);
                        AppUtils.setImageGlide(context, answer.getThumbUri(), R.drawable.img_default_video, holder.imgContentAnswerSingleChoose);
                        holder.imgPlayVideo.setVisibility(View.VISIBLE);
                        holder.imgPlayVideo.setOnClickListener(v -> answerConsumer.accept(answer));
                        holder.imgContentAnswerSingleChoose.setOnClickListener(v -> answerConsumer.accept(answer));
                        holder.txtAnswerContent.setMinLines(3);
                        break;
                    case IMAGE:
                        AppUtils.setImageGlide(context, answer.getMediaUri(), R.drawable.img_default_video, holder.imgContentAnswerSingleChoose);
                        holder.imgContentAnswerSingleChoose.setVisibility(View.VISIBLE);
                        holder.txtAnswerContent.setMinLines(3);
                        break;
                    default:
                        holder.txtAnswerContent.setMinLines(1);
                        holder.llAnswerContent.setPadding(padding, padding, padding, padding);
                        break;
                }
            } else {
                switch (detailQuestion.getType()) {
                    case Constant.SINGLE_CHOOSE:
                    case Constant.MIX_SINGLE_CHOOSE:
                        if ((StringUtils.isEmpty(answer.getTypeMedia()) || answer.getTypeMedia().equalsIgnoreCase(Constant.AUDIO)) && !isGrid) {
                            holder.imgContentAnswerSingleChoose.setVisibility(View.GONE);
                        } else {
                            // display default mc book
                            holder.imgContentAnswerSingleChoose.setVisibility(View.VISIBLE);
                            holder.imgContentAnswerSingleChoose.setImageResource(R.drawable.img_default_video);
                            holder.txtAnswerContent.setMinLines(3);
                        }
                        break;
                }
            }

            if (isCheck) {
                if (answer.getTemp() != null && answer.getTemp().equals(CHECKED) && !isExam) {
                    holder.llAnswer.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                    holder.imgCheckAnswer.setImageResource(R.drawable.ic_check_white);
                    holder.txtAnswerContent.setTextColor(context.getResources().getColor(R.color.colorWhite));
                }

                if (answer.getIsCorrect()) {
                    if (!isExam) {
                        holder.llAnswer.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                        holder.imgCheckAnswer.setImageResource(lastIndex == position || answer.getTemp() != null && answer.getTemp().equals(CHECKED)
                                ? R.drawable.ic_check_white : R.drawable.ic_uncheck_white);
                        holder.txtAnswerContent.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    }
                    isCorrect = lastIndex == position;
                } else {
                    if (lastIndex == position) {
                        if (!isExam) {
                            holder.llAnswer.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                            holder.imgCheckAnswer.setImageResource(R.drawable.ic_check_white);
                            holder.txtAnswerContent.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        }
                    }
                }
                holder.llAnswer.setOnClickListener(null);
                holder.imgPlayVideo.setOnClickListener(null);
                holder.imgAudioSingleChoose.setOnClickListener(null);
            } else {
                if (lastIndex == position) {
                    holder.llAnswer.setBackgroundColor(context.getResources().getColor(R.color.colorOrangeDialog));
                    holder.imgCheckAnswer.setImageResource(R.drawable.ic_check_white);
                    holder.txtAnswerContent.setTextColor(context.getResources().getColor(R.color.colorWhite));
                } else {
                    holder.llAnswer.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                    holder.imgCheckAnswer.setImageResource(R.drawable.ic_uncheck_blue);
                    holder.txtAnswerContent.setTextColor(context.getResources().getColor(R.color.colorTextGray));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llAnswer)
        LinearLayout llAnswer;
        @BindView(R.id.imgCheckAnswer)
        ImageView imgCheckAnswer;
        @BindView(R.id.txtAnswerContent)
        ExtTextView txtAnswerContent;
        @BindView(R.id.imgContentAnswerSingleChoose)
        ImageView imgContentAnswerSingleChoose;
        @BindView(R.id.imgPlayVideo)
        ImageView imgPlayVideo;
        @BindView(R.id.imgAudioSingleChoose)
        ImageView imgAudioSingleChoose;
        @BindView(R.id.llAnswerContent)
        LinearLayout llAnswerContent;
        @BindView(R.id.itemMathJaxSingleChoose)
        MathJaxView itemMathJaxSingleChoose;

        private AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
