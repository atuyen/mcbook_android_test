package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.mc.models.home.Question;


import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

import static com.mc.utilities.Constant.QUESTION_CORRECT;
import static com.mc.utilities.Constant.QUESTION_INCORRECT;
import static com.mc.utilities.Constant.QUESTION_NOT_ANSWER;

public class QuestionItemAdapter extends BaseRecycleAdapter<Question, QuestionItemAdapter.QuestionViewHolder> {

    private Context context;
    private Consumer<Question> questionConsumer;
    private int position;

    public QuestionItemAdapter(Context context, int position, Consumer<Question> questionConsumer) {
        this.context = context;
        this.questionConsumer = questionConsumer;
        this.position = position;
    }

    @NonNull
    @Override
    public QuestionItemAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionItemAdapter.QuestionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QuestionItemAdapter.QuestionViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        try {
            Question question = listItems.get(position);
            holder.txtQuestionName.setText(String.format(context.getString(R.string.question_gop_name), this.position + 1, position + 1, StringUtils.isNullOrEmpty(question.getName()) ? "" : question.getName()));
            question.setFullName(String.format(context.getString(R.string.question_gop_name), this.position + 1, position + 1, StringUtils.isNullOrEmpty(question.getName()) ? "" : question.getName()));
            question.setChildrenId((question.getId()));
            question.setRootId((question.getQuestionId()));

            if (question.getStatus() != null) {
                switch (question.getStatus()) {
                    case QUESTION_NOT_ANSWER:
                        holder.imgCheck.setImageResource(0);
                        holder.txtQuestionName.setTypeface(null, Typeface.BOLD);
                        break;
                    case QUESTION_CORRECT:
                        holder.imgCheck.setImageResource(R.drawable.ic_check_green);
                        holder.txtQuestionName.setTypeface(null, Typeface.NORMAL);
                        break;
                    case QUESTION_INCORRECT:
                        holder.imgCheck.setImageResource(R.drawable.ic_wrong);
                        holder.txtQuestionName.setTypeface(null, Typeface.NORMAL);
                        break;
                }
            }

            holder.llLesson.setOnClickListener(v -> questionConsumer.accept(question));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llLesson)
        LinearLayout llLesson;
        @BindView(R.id.imgCheck)
        ImageView imgCheck;
        @BindView(R.id.txtQuestionName)
        ExtTextView txtQuestionName;
        @BindView(R.id.viewLine)
        View viewLine;

        private QuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
} 