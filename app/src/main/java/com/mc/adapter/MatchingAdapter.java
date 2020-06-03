package com.mc.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.viewanimation.Techniques;
import com.bon.viewanimation.YoYo;
import com.bumptech.glide.Glide;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.Answer;
import com.mc.models.home.Mathching;
import com.mc.utilities.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.timfreiheit.mathjax.android.MathJaxView;
import java8.util.function.Consumer;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class MatchingAdapter extends BaseRecycleAdapter<Answer, RecyclerView.ViewHolder> {
    private Activity context;
    private static final int TYPE_MATCHED = 0;
    private static final int TYPE_MATCHING = 1;
    private int positionLeft = -1;
    private int positionRight = -1;
    private Consumer<Answer> matchingConsumer;
    private Consumer<Answer> breakConsumer;
    private RecyclerView rvMatching;
    private boolean isCheck;
    private List<Answer> correctAnswer;
    private boolean isCorrect;
    private boolean isExam;

    public MatchingAdapter(Activity context, List<Answer> correctAnswer, Consumer<Answer> matchingConsumer, Consumer<Answer> breakConsumer) {
        this.context = context;
        this.matchingConsumer = matchingConsumer;
        this.breakConsumer = breakConsumer;
        this.correctAnswer = correctAnswer;
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

    public boolean isCorrect() {
        return isCorrect;
    }

    public boolean isChooseAnswer() {
        List<Answer> answerList = StreamSupport.stream(listItems).filter(item -> !item.isMatching()).collect(Collectors.toList());
        return answerList.size() == 0;
    }

    private void resetSelect() {
        positionLeft = -1;
        positionRight = -1;
    }

    public List<Answer> getSubmitAnswer() {
        return listItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rvMatching = (RecyclerView) parent;
        if (viewType == TYPE_MATCHED) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_matched_question, parent, false);
            return new MatchedViewHolder(view);
        } else if (viewType == TYPE_MATCHING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_matching_question, parent, false);
            return new MatchingViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private void bindViewMatchedLeft(Answer answer, RecyclerView.ViewHolder holder) {
        try {
            if (answer.getLeft().getMediaUri() != null && !answer.getLeft().getMediaUri().isEmpty()) {
                ((MatchedViewHolder) holder).imgMatchingContentLeft.setVisibility(View.VISIBLE);
                ((MatchedViewHolder) holder).txtMatchingContentLeft.setVisibility(View.GONE);
                ((MatchedViewHolder) holder).mathjaxContentLeft.setVisibility(View.GONE);
                Glide.with(context).load(answer.getLeft().getMediaUri()).into(((MatchedViewHolder) holder).imgMatchingContentLeft);
            } else {
                ((MatchedViewHolder) holder).imgMatchingContentLeft.setVisibility(View.GONE);
                if (AppUtils.isMathJax(answer.getLeft().getText())) {
                    ((MatchedViewHolder) holder).txtMatchingContentLeft.setVisibility(View.GONE);
                    ((MatchedViewHolder) holder).mathjaxContentLeft.setVisibility(View.VISIBLE);
                    ((MatchedViewHolder) holder).mathjaxContentLeft.setInputText(answer.getLeft().getText());
                } else {
                    ((MatchedViewHolder) holder).txtMatchingContentLeft.setVisibility(View.VISIBLE);
                    ((MatchedViewHolder) holder).mathjaxContentLeft.setVisibility(View.GONE);
                    ((MatchedViewHolder) holder).txtMatchingContentLeft.setText(Html.fromHtml(answer.getLeft().getText()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViewMatchedRight(Answer answer, RecyclerView.ViewHolder holder) {
        try {
            if (answer.getRight().getMediaUri() != null && !answer.getRight().getMediaUri().isEmpty()) {
                ((MatchedViewHolder) holder).imgMatchingContentRight.setVisibility(View.VISIBLE);
                ((MatchedViewHolder) holder).txtMatchingContentRight.setVisibility(View.GONE);
                ((MatchedViewHolder) holder).mathjaxContentRight.setVisibility(View.GONE);
                Glide.with(context).load(answer.getRight().getMediaUri()).into(((MatchedViewHolder) holder).imgMatchingContentRight);
            } else {
                ((MatchedViewHolder) holder).imgMatchingContentRight.setVisibility(View.GONE);
                if (AppUtils.isMathJax(answer.getRight().getText())) {
                    ((MatchedViewHolder) holder).txtMatchingContentRight.setVisibility(View.GONE);
                    ((MatchedViewHolder) holder).mathjaxContentRight.setVisibility(View.VISIBLE);
                    ((MatchedViewHolder) holder).mathjaxContentRight.setInputText(answer.getRight().getText());
                } else {
                    ((MatchedViewHolder) holder).txtMatchingContentRight.setVisibility(View.VISIBLE);
                    ((MatchedViewHolder) holder).mathjaxContentRight.setVisibility(View.GONE);
                    ((MatchedViewHolder) holder).txtMatchingContentRight.setText(Html.fromHtml(answer.getRight().getText()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViewMatchingLeft(Answer answer, RecyclerView.ViewHolder holder) {
        try {
            if (answer.getLeft().getMediaUri() != null && !answer.getLeft().getMediaUri().isEmpty()) {
                Glide.with(context).load(answer.getLeft().getMediaUri()).into(((MatchingViewHolder) holder).imgMatchingContentLeft);
                ((MatchingViewHolder) holder).imgMatchingContentLeft.setVisibility(View.VISIBLE);
                ((MatchingViewHolder) holder).txtMatchingContentLeft.setVisibility(View.GONE);
                ((MatchingViewHolder) holder).mathjaxContentLeft.setVisibility(View.GONE);
            } else {
                ((MatchingViewHolder) holder).imgMatchingContentLeft.setVisibility(View.GONE);
                if (AppUtils.isMathJax(answer.getLeft().getText())) {
                    ((MatchingViewHolder) holder).txtMatchingContentLeft.setVisibility(View.GONE);
                    ((MatchingViewHolder) holder).mathjaxContentLeft.setVisibility(View.VISIBLE);
                    ((MatchingViewHolder) holder).mathjaxContentLeft.setInputText(answer.getLeft().getText());
                } else {
                    ((MatchingViewHolder) holder).txtMatchingContentLeft.setVisibility(View.VISIBLE);
                    ((MatchingViewHolder) holder).mathjaxContentLeft.setVisibility(View.GONE);
                    ((MatchingViewHolder) holder).txtMatchingContentLeft.setText(Html.fromHtml(answer.getLeft().getText()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViewMatchingRight(Answer answer, RecyclerView.ViewHolder holder) {
        try {
            if (answer.getRight().getMediaUri() != null && !answer.getRight().getMediaUri().isEmpty()) {
                Glide.with(context).load(answer.getRight().getMediaUri()).into(((MatchingViewHolder) holder).imgMatchingContentRight);
                ((MatchingViewHolder) holder).imgMatchingContentRight.setVisibility(View.VISIBLE);
                ((MatchingViewHolder) holder).txtMatchingContentRight.setVisibility(View.GONE);
                ((MatchingViewHolder) holder).mathjaxContentRight.setVisibility(View.GONE);
            } else {
                ((MatchingViewHolder) holder).imgMatchingContentRight.setVisibility(View.GONE);
                if (AppUtils.isMathJax(answer.getRight().getText())) {
                    ((MatchingViewHolder) holder).txtMatchingContentRight.setVisibility(View.GONE);
                    ((MatchingViewHolder) holder).mathjaxContentRight.setVisibility(View.VISIBLE);
                    ((MatchingViewHolder) holder).mathjaxContentRight.setInputText(answer.getRight().getText());
                } else {
                    ((MatchingViewHolder) holder).txtMatchingContentRight.setVisibility(View.VISIBLE);
                    ((MatchingViewHolder) holder).mathjaxContentRight.setVisibility(View.GONE);
                    ((MatchingViewHolder) holder).txtMatchingContentRight.setText(Html.fromHtml(answer.getRight().getText()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            Answer answer = listItems.get(position);
            if (holder instanceof MatchedViewHolder) {
                bindViewMatchedLeft(answer, holder);
                bindViewMatchedRight(answer, holder);
                answer.setPostion(position);
                resetAnimation(((MatchedViewHolder) holder).llLeft, ((MatchedViewHolder) holder).llRight);
                if (answer.isAnimation()) {
                    slideInView(((MatchedViewHolder) holder).llLeft, ((MatchedViewHolder) holder).llRight);
                    answer.setAnimation(false);
                }

                ((MatchedViewHolder) holder).imgBreak.setOnClickListener(v -> {
                    ((MatchedViewHolder) holder).imgBreak.setOnClickListener(null);
                    fadeOutView(((MatchedViewHolder) holder).llLeft, ((MatchedViewHolder) holder).llRight);
                    new Handler().postDelayed(() -> {
                        answer.setMatching(false);
                        breakConsumer.accept(answer);
                    }, 350);
                });

                if (isCheck) {
                    ((MatchedViewHolder) holder).imgBreak.setVisibility(View.GONE);
                    if (answer.getLeft().getKey().equals(answer.getRight().getKey())) {
                        if (!isExam) {
                            ((MatchedViewHolder) holder).llRight.setBackground(context.getDrawable(R.drawable.bg_border_right_green));
                            ((MatchedViewHolder) holder).llLeft.setBackground(context.getDrawable(R.drawable.bg_border_left_green));
                            ((MatchedViewHolder) holder).viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                        }

                    } else {
                        isCorrect = false;
                        if (!isExam) {
                            ((MatchedViewHolder) holder).llRight.setBackground(context.getDrawable(R.drawable.bg_border_right_red));
                            ((MatchedViewHolder) holder).llLeft.setBackground(context.getDrawable(R.drawable.bg_border_left_red));
                            ((MatchedViewHolder) holder).viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                            ((MatchedViewHolder) holder).btnAnswer.setVisibility(View.VISIBLE);
                        }
                        ((MatchedViewHolder) holder).btnAnswer.setOnClickListener(v -> {
                            ((MatchedViewHolder) holder).btnAnswer.setVisibility(View.GONE);
                            List<Answer> answers = StreamSupport.stream(correctAnswer).filter(answer1 -> answer1.getLeft().getKey().equals(answer.getLeft().getKey())).collect(Collectors.toList());
                            bindViewMatchedRight(answers.get(0), holder);
                            flipView(((MatchedViewHolder) holder).llRight);
                        });
                    }
                }
            } else if (holder instanceof MatchingViewHolder) {
                bindViewMatchingLeft(answer, holder);
                bindViewMatchingRight(answer, holder);
                resetAnimation(((MatchingViewHolder) holder).cvLeft, ((MatchingViewHolder) holder).cvRight);

                if (positionLeft >= 0) {
                    ((MatchingViewHolder) holder).cvLeft.setOnClickListener(null);
                    ((MatchingViewHolder) holder).mathjaxContentLeft.setRenderListener(null);
                } else {
                    ((MatchingViewHolder) holder).cvLeft.setOnClickListener(v -> {
                        ((MatchingViewHolder) holder).cvLeft.setOnClickListener(null);
                        positionLeft = position;
                        ((MatchingViewHolder) holder).llLeft.setBackgroundColor(context.getResources().getColor(R.color.colorOrangeDialog));
                        notifyDataSetChanged();
                        if (positionRight >= 0) {
                            Mathching temp, temp1, temp2;
                            temp1 = listItems.get(positionRight).getRight();
                            temp2 = answer.getRight();
                            temp = temp1;
                            temp1 = temp2;
                            temp2 = temp;
                            fadeOutView(((MatchingViewHolder) holder).cvLeft, getView(positionRight, R.id.cvRight));
                            new Handler().postDelayed(() -> {
                                resetSelect();
                                callBack(answer, position);
                            }, 300);
                            answer.setRight(temp2);
                            listItems.get(positionRight).setRight(temp1);
                        }
                    });
                    ((MatchingViewHolder) holder).mathjaxContentLeft.setRenderListener(new MathJaxView.OnMathJaxRenderListener() {
                        @Override
                        public void onRendered() {

                        }

                        @Override
                        public void onClickView() {
                            context.runOnUiThread(() -> {
                                ((MatchingViewHolder) holder).mathjaxContentLeft.setRenderListener(null);
                                positionLeft = position;
                                ((MatchingViewHolder) holder).llLeft.setBackgroundColor(context.getResources().getColor(R.color.colorOrangeDialog));
                                MatchingAdapter.this.notifyDataSetChanged();
                                if (positionRight >= 0) {
                                    Mathching temp, temp1, temp2;
                                    temp1 = listItems.get(positionRight).getRight();
                                    temp2 = answer.getRight();
                                    temp = temp1;
                                    temp1 = temp2;
                                    temp2 = temp;
                                    fadeOutView(((MatchingViewHolder) holder).cvLeft, getView(positionRight, R.id.cvRight));
                                    new Handler().postDelayed(() -> {
                                        resetSelect();
                                        callBack(answer, position);
                                    }, 300);
                                    answer.setRight(temp2);
                                    listItems.get(positionRight).setRight(temp1);
                                }
                            });

                        }
                    });
                }

                if (positionRight >= 0) {
                    ((MatchingViewHolder) holder).cvRight.setOnClickListener(null);
                    ((MatchingViewHolder) holder).mathjaxContentRight.setRenderListener(null);
                } else {
                    ((MatchingViewHolder) holder).cvRight.setOnClickListener(v -> {
                        ((MatchingViewHolder) holder).cvRight.setOnClickListener(null);
                        positionRight = position;
                        ((MatchingViewHolder) holder).llRight.setBackgroundColor(context.getResources().getColor(R.color.colorOrangeDialog));
                        notifyDataSetChanged();
                        if (positionLeft >= 0) {
                            Mathching temp, temp1, temp2;
                            temp1 = listItems.get(positionLeft).getLeft();
                            temp2 = answer.getLeft();
                            temp = temp1;
                            temp1 = temp2;
                            temp2 = temp;
                            fadeOutView(getView(positionLeft, R.id.cvLeft), ((MatchingViewHolder) holder).cvRight);
                            new Handler().postDelayed(() -> {
                                resetSelect();
                                callBack(answer, position);
                            }, 300);

                            answer.setLeft(temp2);
                            listItems.get(positionLeft).setLeft(temp1);
                        }
                    });

                    ((MatchingViewHolder) holder).mathjaxContentRight.setRenderListener(new MathJaxView.OnMathJaxRenderListener() {
                        @Override
                        public void onRendered() {

                        }

                        @Override
                        public void onClickView() {
                            context.runOnUiThread(() -> {
                                ((MatchingViewHolder) holder).mathjaxContentRight.setRenderListener(null);
                                positionRight = position;
                                ((MatchingViewHolder) holder).llRight.setBackgroundColor(context.getResources().getColor(R.color.colorOrangeDialog));
                                MatchingAdapter.this.notifyDataSetChanged();
                                if (positionLeft >= 0) {
                                    Mathching temp, temp1, temp2;
                                    temp1 = listItems.get(positionLeft).getLeft();
                                    temp2 = answer.getLeft();
                                    temp = temp1;
                                    temp1 = temp2;
                                    temp2 = temp;
                                    fadeOutView(getView(positionLeft, R.id.cvLeft), ((MatchingViewHolder) holder).cvRight);
                                    new Handler().postDelayed(() -> {
                                        resetSelect();
                                        callBack(answer, position);
                                    }, 300);

                                    answer.setLeft(temp2);
                                    listItems.get(positionLeft).setLeft(temp1);
                                }
                            });

                        }
                    });
                }

                if (positionLeft != position)
                    ((MatchingViewHolder) holder).llLeft.setBackground(context.getResources().getDrawable(R.drawable.bg_white));

                if (positionRight != position)
                    ((MatchingViewHolder) holder).llRight.setBackground(context.getResources().getDrawable(R.drawable.bg_white));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void slideInView(View left, View right) {
        YoYo.with(Techniques.SlideInLeft)
                .duration(350)
                .playOn(left);

        YoYo.with(Techniques.SlideInRight)
                .duration(350)
                .playOn(right);
    }

    private void flipView(View view) {
        YoYo.with(Techniques.FlipInY)
                .duration(350)
                .playOn(view);
    }

    private void fadeOutView(View left, View right) {
        YoYo.with(Techniques.FadeOutRight)
                .duration(350)
                .playOn(right);

        YoYo.with(Techniques.FadeOutLeft)
                .duration(350)
                .playOn(left);
    }

    private void resetAnimation(View left, View right) {
        left.setAlpha(1);
        right.setAlpha(1);
        left.setTranslationX(0);
        right.setTranslationX(0);
    }

    private void callBack(Answer answer, int position) {
        new Handler().postDelayed(() -> {
            resetSelect();
            answer.setPostion(position);
            answer.setMatching(true);
            matchingConsumer.accept(answer);
        }, 300);
    }

    private View getView(int position, int id) {
        View view = rvMatching.getChildAt(position);
        return view.<CardView>findViewById(id);
    }

    @Override
    public int getItemViewType(int position) {
        if (isMatched(position)) {
            return TYPE_MATCHED;
        }
        return TYPE_MATCHING;
    }

    private boolean isMatched(int position) {
        return listItems.get(position).isMatching();
    }

    class MatchedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMatchingContentLeft)
        ExtTextView txtMatchingContentLeft;
        @BindView(R.id.imgMatchingContentLeft)
        ImageView imgMatchingContentLeft;
        @BindView(R.id.txtMatchingContentRight)
        ExtTextView txtMatchingContentRight;
        @BindView(R.id.imgMatchingContentRight)
        ImageView imgMatchingContentRight;
        @BindView(R.id.imgBreak)
        ImageView imgBreak;
        @BindView(R.id.llLeft)
        LinearLayout llLeft;
        @BindView(R.id.rlRight)
        RelativeLayout llRight;
        @BindView(R.id.viewLine)
        View viewLine;
        @BindView(R.id.btnAnswer)
        ExtButton btnAnswer;
        @BindView(R.id.mathjaxContentLeft)
        MathJaxView mathjaxContentLeft;
        @BindView(R.id.mathjaxContentRight)
        MathJaxView mathjaxContentRight;


        private MatchedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MatchingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMatchingContentLeft)
        ExtTextView txtMatchingContentLeft;
        @BindView(R.id.imgMatchingContentLeft)
        ImageView imgMatchingContentLeft;
        @BindView(R.id.txtMatchingContentRight)
        ExtTextView txtMatchingContentRight;
        @BindView(R.id.imgMatchingContentRight)
        ImageView imgMatchingContentRight;
        @BindView(R.id.cvLeft)
        CardView cvLeft;
        @BindView(R.id.cvRight)
        CardView cvRight;
        @BindView(R.id.llLeft)
        LinearLayout llLeft;
        @BindView(R.id.llRight)
        LinearLayout llRight;
        @BindView(R.id.mathjaxContentLeft)
        MathJaxView mathjaxContentLeft;
        @BindView(R.id.mathjaxContentRight)
        MathJaxView mathjaxContentRight;

        private MatchingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
