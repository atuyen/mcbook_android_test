package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.DetailQuestion;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class ListExamQuestionAdapter extends BaseRecycleAdapter<DetailQuestion, ListExamQuestionAdapter.ListQuestionViewHolder> {

    private Context context;
    private int currentPosition;
    private Consumer<Integer> positionConsumer;
    private boolean isResult;

    public ListExamQuestionAdapter(Context context, int position, Consumer<Integer> positionConsumer, boolean isResult) {
        this.context = context;
        this.currentPosition = position;
        this.positionConsumer = positionConsumer;
        this.isResult = isResult;
    }

    @NonNull
    @Override
    public ListQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_question, parent, false);
        return new ListQuestionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListQuestionViewHolder holder, int position) {
        try {
            try {
                DetailQuestion detailQuestion = listItems.get(position);
                holder.txtQuestion.setText(String.format(context.getString(R.string.question_name_title), position + 1, StringUtils.isNullOrEmpty(detailQuestion.getName()) ? "" : detailQuestion.getName()));
                if (isResult) {
                    holder.imgCheckAnswer.setVisibility(View.VISIBLE);
                    holder.imgCheckAnswer.setImageResource(detailQuestion.isCorrect() ? R.drawable.ic_check_green : R.drawable.ic_wrong);
                } else {
                    if (detailQuestion.isAnswer()) {
                        holder.txtQuestion.setTypeface(null, Typeface.NORMAL);
                        holder.txtQuestion.setTextColor(context.getResources().getColor(R.color.colorDisable));
                    } else {
                        holder.txtQuestion.setTypeface(null, Typeface.BOLD);
                        holder.txtQuestion.setTextColor(context.getResources().getColor(R.color.colorTextGray));
                    }

                    if (currentPosition == position) {
                        holder.txtQuestion.setTypeface(null, Typeface.BOLD);
                        holder.txtQuestion.setTextColor(context.getResources().getColor(R.color.colorDarkOrange));
                    }
                }
                holder.llRoot.setOnClickListener(v -> positionConsumer.accept(position));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ListQuestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtQuestion)
        ExtTextView txtQuestion;
        @BindView(R.id.viewLine)
        View viewLine;
        @BindView(R.id.llRoot)
        LinearLayout llRoot;
        @BindView(R.id.imgCheckAnswer)
        ImageView imgCheckAnswer;

        private ListQuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
