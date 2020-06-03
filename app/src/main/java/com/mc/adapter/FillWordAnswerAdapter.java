package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.customizes.edittexts.EditTextFillWord;
import com.mc.models.home.Answer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FillWordAnswerAdapter extends BaseRecycleAdapter<Answer, FillWordAnswerAdapter.AnswerViewHolder> {
    private Context context;
    private boolean isCheck;
    private boolean isCorrect;
    private boolean isExam;
    String content;

    public FillWordAnswerAdapter(Context context) {
        this.context = context;
        this.isCorrect = true;
    }

    public void setIsCheck() {
        isCheck = true;
        isExam = false;
        notifyDataSetChanged();
    }

    public void setIsCheckExam() {
        isCheck = true;
        isExam = true;
        notifyDataSetChanged();
    }

    public List<Answer> getSubmitAnswer() {
        return listItems;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getContentAnswer() {
        return content;
    }

    @NonNull
    @Override
    public FillWordAnswerAdapter.AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_answer_fill_word, parent, false);
        return new FillWordAnswerAdapter.AnswerViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull FillWordAnswerAdapter.AnswerViewHolder holder, int position) {
        try {
            Answer answer = listItems.get(position);
            if (listItems.size() <= 1) {
                holder.edtAnswer.hideId();
            } else {
                holder.edtAnswer.setId(String.format("%d", position + 1));
            }
            holder.edtAnswer.setText(answer.getFillWordAnswer());
            content = answer.getFillWordAnswer();

            if (isCheck) {
                if (answer.getTemp() != null && answer.getContent() != null && answer.getContent().toLowerCase().trim().equals(answer.getTemp().toLowerCase().trim())) {
                    if (!isExam)
                        holder.edtAnswer.showCorrectAnswer(answer);
                } else if (answer.getContent() != null && answer.getFillWordAnswer() != null && answer.getContent().toLowerCase().trim().equals(answer.getFillWordAnswer().toLowerCase().trim())) {
                    if (!isExam)
                        holder.edtAnswer.showCorrectAnswer(answer);
                } else {
                    if (!isExam)
                        holder.edtAnswer.showWrongAnswer(answer);
                    isCorrect = false;

                }
                answer.setTemp(answer.getFillWordAnswer());
            } else {
                holder.edtAnswer.getAnswer(answer::setFillWordAnswer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.edtAnswer)
        EditTextFillWord edtAnswer;

        private AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}