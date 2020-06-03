package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.collection.CollectionUtils;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.customizes.gifts.ExpandableCardView;

import com.mc.models.home.Question;


import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

import static com.mc.utilities.Constant.QUESTION_ANSWER;
import static com.mc.utilities.Constant.QUESTION_CORRECT;
import static com.mc.utilities.Constant.QUESTION_INCORRECT;
import static com.mc.utilities.Constant.QUESTION_NOT_ANSWER;


public class SubjectAdapter extends BaseRecycleAdapter<Question, RecyclerView.ViewHolder> {

    private Context context;
    private Consumer<Question> questionConsumer;
    private Consumer<Question> questionSingleChooseConsumer;
    private final int TYPE_MIX_QUESTION = 0;
    private final int TYPE_SINGLE_QUESTION = 1;

    public SubjectAdapter(Context context, Consumer<Question> questionConsumer, Consumer<Question> questionSingleChooseConsumer) {
        this.context = context;
        this.questionConsumer = questionConsumer;
        this.questionSingleChooseConsumer = questionSingleChooseConsumer;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MIX_QUESTION) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
            return new SubjectAdapter.MixQuestionViewHolder(view);
        } else if (viewType == TYPE_SINGLE_QUESTION) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_single_subject, parent, false);
            return new SubjectAdapter.SingleQuestionViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        Question question = listItems.get(position);
        if (CollectionUtils.isNotNullOrEmpty(question.getChildren()))
            return TYPE_MIX_QUESTION;
        else
            return TYPE_SINGLE_QUESTION;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            Question question = listItems.get(position);
            if (holder instanceof MixQuestionViewHolder) {
                ((MixQuestionViewHolder) holder).ecvSubject.setSingleLine();
                ((MixQuestionViewHolder) holder).ecvSubject.setTitle(String.format(context.getString(R.string.question_name_title), position + 1, question.getName()));
                question.setFullName(String.format(context.getString(R.string.question_name_title_mix), position + 1));
                List<Question> questions = question.getChildren();
                QuestionItemAdapter questionItemAdapter = new QuestionItemAdapter(context, position, qt -> questionConsumer.accept(qt));
                ((MixQuestionViewHolder) holder).rvListSubject = holder.itemView.findViewById(R.id.rvListSubject);
                ((MixQuestionViewHolder) holder).rvListSubject.setLayoutManager(new LinearLayoutManager(context));
                ((MixQuestionViewHolder) holder).rvListSubject.setAdapter(questionItemAdapter);
                if (question.getStatus() != null) {
                    switch (question.getStatus()) {
                        case QUESTION_NOT_ANSWER:
                            ((MixQuestionViewHolder) holder).ecvSubject.setTypeFace(Typeface.BOLD);
                            break;
                        case QUESTION_ANSWER:
                            ((MixQuestionViewHolder) holder).ecvSubject.setTypeFace(Typeface.NORMAL);
                            break;
                    }
                }
                questionItemAdapter.setDataList(questions);
            } else if (holder instanceof SingleQuestionViewHolder) {
                ((SingleQuestionViewHolder) holder).txtQuestionName.setText(String.format(context.getString(R.string.question_name_title), position + 1, StringUtils.isNullOrEmpty(question.getName()) ? "" : question.getName()));
                question.setFullName(String.format(context.getString(R.string.question_name_title), position + 1, StringUtils.isNullOrEmpty(question.getName()) ? "" : question.getName()));
                switch (question.getStatus()) {
                    case QUESTION_NOT_ANSWER:
                        ((SingleQuestionViewHolder) holder).imgStatusQuestion.setImageResource(0);
                        ((SingleQuestionViewHolder) holder).txtQuestionName.setTypeface(null, Typeface.BOLD);
                        break;
                    case QUESTION_CORRECT:
                        ((SingleQuestionViewHolder) holder).imgStatusQuestion.setImageResource(R.drawable.ic_check_green);
                        ((SingleQuestionViewHolder) holder).txtQuestionName.setTypeface(null, Typeface.NORMAL);
                        break;
                    case QUESTION_INCORRECT:
                        ((SingleQuestionViewHolder) holder).imgStatusQuestion.setImageResource(R.drawable.ic_wrong);
                        ((SingleQuestionViewHolder) holder).txtQuestionName.setTypeface(null, Typeface.NORMAL);
                        break;
                }
                question.setChildrenId(0);
                question.setRootId(question.getId());
                ((SingleQuestionViewHolder) holder).llQuestionSingleChoose.setOnClickListener(v -> {
                    question.setPosition(position);
                    questionSingleChooseConsumer.accept(question);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MixQuestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ecvSubject)
        ExpandableCardView ecvSubject;
        RecyclerView rvListSubject;

        private MixQuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SingleQuestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtQuestionName)
        ExtTextView txtQuestionName;
        @BindView(R.id.imgStatusQuestion)
        ImageView imgStatusQuestion;
        @BindView(R.id.llQuestionSingleChoose)
        LinearLayout llQuestionSingleChoose;

        private SingleQuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}