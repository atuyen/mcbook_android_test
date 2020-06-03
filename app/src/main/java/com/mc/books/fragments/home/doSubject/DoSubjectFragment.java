package com.mc.books.fragments.home.doSubject;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.bon.util.ToastUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mc.adapter.FillWordAnswerAdapter;
import com.mc.adapter.MatchingAdapter;
import com.mc.adapter.SingleChooseAdapter;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.dialog.NoteAnswerDialog;
import com.mc.books.dialog.VideoDialog;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.Answer;
import com.mc.models.home.DetailQuestion;
import com.mc.models.home.MediaPlayerResponse;
import com.mc.models.home.Question;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.timfreiheit.mathjax.android.MathJaxView;


import static com.mc.utilities.Constant.AUDIO;
import static com.mc.utilities.Constant.BOOK_ID;
import static com.mc.utilities.Constant.IMAGE;
import static com.mc.utilities.Constant.KEY_LIST_QUESTION;
import static com.mc.utilities.Constant.KEY_POSITION;
import static com.mc.utilities.Constant.KEY_QUESTION;
import static com.mc.utilities.Constant.LINE_SHOW_MORE;
import static com.mc.utilities.Constant.MAXIUM_LINE;
import static com.mc.utilities.Constant.MAX_LINE;
import static com.mc.utilities.Constant.VIDEO;

public class DoSubjectFragment extends BaseMvpFragment<IDoSubjectView, IDoSubjectPresenter<IDoSubjectView>>
        implements IDoSubjectView, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public static DoSubjectFragment newInstance(List<Question> questions, int position, Question question, int bookId) {
        Bundle args = new Bundle();
        DoSubjectFragment fragment = new DoSubjectFragment();
        args.putSerializable(KEY_LIST_QUESTION, (Serializable) questions);
        args.putInt(KEY_POSITION, position);
        args.putSerializable(KEY_QUESTION, question);
        args.putInt(BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    //View fillword
    @BindView(R.id.llRootWordFill)
    LinearLayout llRootWordFill;
    @BindView(R.id.txtWordFillDescription)
    ExtTextView txtWordFillDescription;
    @BindView(R.id.txtContentWordFill)
    ExtTextView txtContentWordFill;
    @BindView(R.id.txtShowMoreContentWordFill)
    ExtTextView txtShowMoreContentWordFill;
    @BindView(R.id.llResult)
    LinearLayout llResult;
    @BindView(R.id.rvResultWordFill)
    RecyclerView rvResultWordFill;
    @BindView(R.id.imgContentFillWord)
    ImageView imgContentFillWord;
    @BindView(R.id.imgMediaFillWord)
    ImageView imgMediaFillWord;
    private FillWordAnswerAdapter fillWordAnswerAdapter;
    //end

    //View single choose
    @BindView(R.id.llRootSingleChoose)
    LinearLayout llRootSingleChoose;
    @BindView(R.id.txtSingleChooseDescription)
    ExtTextView txtSingleChooseDescription;
    @BindView(R.id.txtContentSingleChoose)
    ExtTextView txtContentSingleChoose;
    @BindView(R.id.imgMediaQuestionSingleChoose)
    ImageView imgMediaQuestionSingleChoose;
    @BindView(R.id.rvSingleChoose)
    RecyclerView rvSingleChoose;
    @BindView(R.id.imgContentSingleChoose)
    ImageView imgContentSingleChoose;
    private SingleChooseAdapter singleChooseAdapter;
    //end

    //View matching
    @BindView(R.id.rootMatching)
    NestedScrollView rootMatching;
    private MatchingAdapter matchingAdapter;
    @BindView(R.id.rvMatching)
    RecyclerView rvMatching;
    @BindView(R.id.txtContentMatching)
    ExtTextView txtContentMatching;
    //end

    //view root
    @BindView(R.id.llAnswer)
    LinearLayout llAnswer;
    @BindView(R.id.llCheckResult)
    LinearLayout llCheckResult;
    @BindView(R.id.llNoteAnswer)
    LinearLayout llNoteAnswer;
    @BindView(R.id.llPreviousQuestion)
    LinearLayout llPreviousQuestion;
    @BindView(R.id.llNextQuestion)
    LinearLayout llNextQuestion;
    @BindView(R.id.svRoot)
    NestedScrollView svRoot;
    @BindView(R.id.llMixQuestion)
    LinearLayout llMixQuestion;
    @BindView(R.id.imgMixQuestionContent)
    ImageView imgMixQuestionContent;
    @BindView(R.id.imgMixQuestionMedia)
    ImageView imgMixQuestionMedia;
    @BindView(R.id.txtMixQuestionContent)
    ExtTextView txtMixQuestionContent;
    @BindView(R.id.txtMixQuestionShowMore)
    ExtTextView txtMixQuestionShowMore;
    @BindView(R.id.txtDescriptionForm)
    ExtTextView txtDescriptionForm;

    @BindView(R.id.mathjaxSingleChoose)
    MathJaxView mathjaxSingleChoose;
    @BindView(R.id.mixMathjax)
    MathJaxView mixMathjax;
    @BindView(R.id.mathjaxWordFill)
    MathJaxView mathjaxWordFill;
    @BindView(R.id.mathjaxMatching)
    MathJaxView mathjaxMatching;

    private MediaPlayer mPlayer;
    private MediaPlayer mediaPlayer;
    VideoDialog videoDialog;
    //end

    private List<Question> questions;
    private int position;
    private int maxSizeQuestions;
    private Question question;
    private DetailQuestion detailQuestion;
    private boolean isShowMore;
    private boolean isShowMoreMix;
    private int bookId;
    private List<Answer> answerArray;
    private Menu menu;
    private MenuInflater menuInflater;
    private String dataAnswerList;
    private Gson gson = new Gson();
    int posPlaying = -1;
    ImageView viewPlaying;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        setHasOptionsMenu(true);
        mPlayer = new MediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        questions = (List<Question>) getArguments().getSerializable(KEY_LIST_QUESTION);
        if (questions != null) maxSizeQuestions = questions.size();
        position = getArguments().getInt(KEY_POSITION);
        question = (Question) getArguments().getSerializable(KEY_QUESTION);
        bookId = getArguments().getInt(BOOK_ID);
        if (question != null)
            presenter.getDetailQuestion(bookId, question.getRootId(), question.getChildrenId());
    }

    @Override
    public int getResourceId() {
        return R.layout.do_subject_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.onShowBottomBar(false);
    }

    private void initData() {
        hideAll();
        mActivity.setToolbarTitle(questions.get(position).getFullName());
        llMixQuestion.setVisibility(View.GONE);
        llAnswer.setVisibility(View.VISIBLE);
        llCheckResult.setVisibility(View.VISIBLE);
        llNoteAnswer.setVisibility(View.GONE);
        txtDescriptionForm.setVisibility(View.GONE);
        switch (detailQuestion.getType()) {
            case Constant.SINGLE_CHOOSE:
                menu.clear();
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderSingleChoose();
                break;
            case Constant.FILL_WORD:
                menu.clear();
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderFillWord();
                break;
            case Constant.MATCHING:
                renderMenuMatching();
                renderMatching();
                break;
            case Constant.MIX_SINGLE_CHOOSE:
                menu.clear();
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderMixQuestionLayout(detailQuestion);
                renderSingleChoose();
                break;
            case Constant.MIX_FILL_WORD:
                menu.clear();
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderMixQuestionLayout(detailQuestion);
                renderFillWord();
                break;
            case Constant.MIX_MATCHING:
                renderMenuMatching();
                renderMixQuestionLayout(detailQuestion);
                renderMatching();
                break;
        }

        if (detailQuestion.getDescriptionQuestionForms().isEmpty())
            txtDescriptionForm.setVisibility(View.GONE);

        txtDescriptionForm.setText(Html.fromHtml(detailQuestion.getDescriptionQuestionForms()));
    }

    private void renderMixQuestionLayout(DetailQuestion detailQuestion) {
        isShowMoreMix = false;
        llMixQuestion.setVisibility(View.VISIBLE);
        imgMixQuestionContent.setVisibility(View.GONE);
        imgMixQuestionMedia.setVisibility(View.GONE);

        if (AppUtils.isMathJax(detailQuestion.getDescription())) {
            mixMathjax.setVisibility(View.VISIBLE);
            txtMixQuestionContent.setVisibility(View.GONE);
            txtMixQuestionShowMore.setVisibility(View.GONE);
            mixMathjax.setInputText(detailQuestion.getDescription());
        } else {
            txtMixQuestionContent.setVisibility(View.VISIBLE);
            mixMathjax.setVisibility(View.GONE);
            txtMixQuestionContent.setText(Html.fromHtml(detailQuestion.getDescription()));
            txtMixQuestionContent.post(() -> {
                int lineCount = txtMixQuestionContent.getLineCount();
                if (lineCount > LINE_SHOW_MORE) {
                    txtMixQuestionContent.setMaxLines(MAX_LINE);
                    txtMixQuestionShowMore.setVisibility(View.VISIBLE);
                } else
                    txtMixQuestionShowMore.setVisibility(View.GONE);
            });
        }

        if (detailQuestion.getDescriptionMediaType() != null) {
            switch (detailQuestion.getDescriptionMediaType()) {
                case VIDEO:
                    imgMixQuestionMedia.setVisibility(View.VISIBLE);
                    imgMixQuestionMedia.setImageResource(R.drawable.ic_media_play);
                    break;
                case AUDIO:
                    imgMixQuestionMedia.setVisibility(View.VISIBLE);
                    imgMixQuestionMedia.setImageResource(R.drawable.ic_audio_play);
                    break;
                case IMAGE:
                    imgMixQuestionContent.setVisibility(View.VISIBLE);
                    AppUtils.setImageGlide(getContext(), detailQuestion.getDescriptionMediaUri(), R.drawable.ic_img_book_default, imgMixQuestionContent);
                    break;
            }
        }
    }

    private void renderMatching() {
        try {
            rootMatching.setVisibility(View.VISIBLE);
            llAnswer.setVisibility(View.VISIBLE);
            llCheckResult.setVisibility(View.VISIBLE);

            if (AppUtils.isMathJax(detailQuestion.getContent())) {
                txtContentMatching.setVisibility(View.GONE);
                mathjaxMatching.setVisibility(View.VISIBLE);
                mathjaxMatching.setInputText(detailQuestion.getContent());
            } else {
                txtContentMatching.setVisibility(View.VISIBLE);
                mathjaxMatching.setVisibility(View.GONE);
                txtContentMatching.setText(Html.fromHtml(detailQuestion.getContent()));
            }

            matchingAdapter = new MatchingAdapter(mActivity, detailQuestion.getAnswers(), answer -> {
                rvMatching.findViewHolderForAdapterPosition(answer.getPostion());
                answerArray.remove(answer.getPostion());
                answer.setAnimation(true);
                answerArray.add(0, answer);
                svRoot.scrollTo(0, 0);
                matchingAdapter.setDataList(answerArray);
            }, answer -> {
                answerArray.remove(answer.getPostion());
                answerArray.add(answer);
                matchingAdapter.setDataList(answerArray);
            });

            rvMatching.setLayoutManager(new LinearLayoutManager(getContext()));
            rvMatching.setAdapter(matchingAdapter);
            rvMatching.setNestedScrollingEnabled(false);
            answerArray = new ArrayList<>();
            answerArray.addAll(detailQuestion.getAnswerRandom());
            dataAnswerList = gson.toJson(detailQuestion.getAnswerRandom());
            if (detailQuestion.isSubmit()) {
                List<Answer> answers = new ArrayList<>(getSubmitAnswerJson(detailQuestion.getSubmitAnswer()));
                matchingAdapter.setIsCheck();
                matchingAdapter.setDataList(answers);
                showNoteAnswer();
            } else
                matchingAdapter.setDataList(answerArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderFillWord() {
        try {
            llRootWordFill.setVisibility(View.VISIBLE);
            llAnswer.setVisibility(View.VISIBLE);
            llCheckResult.setVisibility(View.VISIBLE);
            imgMediaFillWord.setVisibility(View.GONE);
            imgContentFillWord.setVisibility(View.GONE);

            if (AppUtils.isMathJax(detailQuestion.getContent())) {
                txtContentWordFill.setVisibility(View.GONE);
                txtShowMoreContentWordFill.setVisibility(View.GONE);
                mathjaxWordFill.setVisibility(View.VISIBLE);
                mathjaxWordFill.setInputText(detailQuestion.getContent());
            } else {
                txtContentWordFill.setVisibility(View.VISIBLE);
                mathjaxWordFill.setVisibility(View.GONE);
                txtContentWordFill.setText(Html.fromHtml(detailQuestion.getContent()));
                isShowMore = false;
                txtContentWordFill.post(() -> {
                    int lineCount = txtContentWordFill.getLineCount();
                    if (lineCount > LINE_SHOW_MORE) {
                        txtContentWordFill.setMaxLines(MAX_LINE);
                        txtShowMoreContentWordFill.setVisibility(View.VISIBLE);
                    } else
                        txtShowMoreContentWordFill.setVisibility(View.GONE);
                });
            }

            if (detailQuestion.getContentMediaType() != null) {
                switch (detailQuestion.getContentMediaType()) {
                    case VIDEO:
                        imgMediaFillWord.setVisibility(View.VISIBLE);
                        imgMediaFillWord.setImageResource(R.drawable.ic_media_play);
                        break;
                    case AUDIO:
                        imgMediaFillWord.setVisibility(View.VISIBLE);
                        imgMediaFillWord.setImageResource(R.drawable.ic_audio_play);
                        break;
                    case IMAGE:
                        imgContentFillWord.setVisibility(View.VISIBLE);
                        AppUtils.setImageGlide(getContext(), detailQuestion.getContentMediaUri(), R.drawable.ic_img_book_default, imgContentFillWord);
                        break;
                }
            }

            fillWordAnswerAdapter = new FillWordAnswerAdapter(getContext());
            rvResultWordFill.setLayoutManager(new LinearLayoutManager(getContext()));
            rvResultWordFill.setAdapter(fillWordAnswerAdapter);
            rvResultWordFill.setNestedScrollingEnabled(false);
            fillWordAnswerAdapter.setDataList(detailQuestion.getAnswers());
            if (detailQuestion.isSubmit()) {
                List<Answer> answers = new ArrayList<>(getSubmitAnswerJson(detailQuestion.getSubmitAnswer()));
                fillWordAnswerAdapter.setIsCheck();
                fillWordAnswerAdapter.setDataList(answers);
                showNoteAnswer();
            } else
                fillWordAnswerAdapter.setDataList(detailQuestion.getAnswers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderSingleChoose() {
        try {
            llRootSingleChoose.setVisibility(View.VISIBLE);
            imgContentSingleChoose.setVisibility(View.GONE);
            imgMediaQuestionSingleChoose.setVisibility(View.GONE);

            if (AppUtils.isMathJax(detailQuestion.getContent())) {
                txtContentSingleChoose.setVisibility(View.GONE);
                mathjaxSingleChoose.setVisibility(View.VISIBLE);
                mathjaxSingleChoose.setInputText(detailQuestion.getContent());
            } else {
                txtContentSingleChoose.setVisibility(View.VISIBLE);
                mathjaxSingleChoose.setVisibility(View.GONE);
                txtContentSingleChoose.setText(Html.fromHtml(detailQuestion.getContent()));
                isShowMore = false;
            }

            if (detailQuestion.getContentMediaType() != null) {
                switch (detailQuestion.getContentMediaType()) {
                    case VIDEO:
                        imgMediaQuestionSingleChoose.setVisibility(View.VISIBLE);
                        imgMediaQuestionSingleChoose.setImageResource(R.drawable.ic_video_small);
                        break;
                    case AUDIO:
                        imgMediaQuestionSingleChoose.setVisibility(View.VISIBLE);
                        imgMediaQuestionSingleChoose.setImageResource(R.drawable.ic_audio_small);
                        break;
                    case IMAGE:
                        imgContentSingleChoose.setVisibility(View.VISIBLE);
                        AppUtils.setImageGlide(getContext(), detailQuestion.getContentMediaUri(), R.drawable.ic_img_book_default, imgContentSingleChoose);
                        break;
                }
            }
            singleChooseAdapter = new SingleChooseAdapter(mActivity, detailQuestion, answer -> {
                if (answer.getMediaUri() != null) showVideoDialog(answer.getMediaUri());
            }, mediaPlayerResponse -> {
                if (mediaPlayerResponse.getUrl() != null)
                    showAudioDialog(mediaPlayerResponse);
            }, pos -> {
            }, null);
            if (detailQuestion.getAnswers().size() > 0 && (detailQuestion.getAnswers().get(0).getMediaUri() != null &&
                    (!detailQuestion.getAnswers().get(0).getTypeMedia().equals(AUDIO) && !detailQuestion.getAnswers().get(0).getTypeMedia().equals("")))) {
                rvSingleChoose.setLayoutManager(new GridLayoutManager(getContext(), AppUtils.isTablet(getContext()) ? 3 : 2));
                singleChooseAdapter.setGrid(true);
            } else {
                rvSingleChoose.setLayoutManager(new LinearLayoutManager(getContext()));
                singleChooseAdapter.setGrid(false);
            }

            rvSingleChoose.setAdapter(singleChooseAdapter);
            rvSingleChoose.setNestedScrollingEnabled(false);
            if (detailQuestion.isSubmit()) {
                List<Answer> answers = new ArrayList<>(getSubmitAnswerJson(detailQuestion.getSubmitAnswer()));
                singleChooseAdapter.setIsCheck();
                singleChooseAdapter.setDataList(answers);
                showNoteAnswer();
            } else
                singleChooseAdapter.setDataList(detailQuestion.getAnswers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Answer> getSubmitAnswerJson(String jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Answer>>() {
        }.getType();
        return gson.fromJson(jsonArray, type);
    }

    private void hideAll() {
        llRootWordFill.setVisibility(View.GONE);
        llRootSingleChoose.setVisibility(View.GONE);
        rootMatching.setVisibility(View.GONE);
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @NonNull
    @Override
    public IDoSubjectPresenter<IDoSubjectView> createPresenter() {
        return new DoSubjectPresenter<>(getAppComponent());
    }

    @OnClick({R.id.llPreviousQuestion, R.id.llCheckResult, R.id.llNextQuestion, R.id.txtShowMoreContentWordFill, R.id.imgMediaFillWord,
            R.id.imgMediaQuestionSingleChoose, R.id.txtMixQuestionShowMore, R.id.imgMixQuestionMedia, R.id.llNoteAnswer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llPreviousQuestion:
                stopMediaplayer();
                llCheckResult.setEnabled(true);
                svRoot.scrollTo(0, 0);
                onPreviousFile();
                break;
            case R.id.llNextQuestion:
                stopMediaplayer();
                llCheckResult.setEnabled(true);
                svRoot.scrollTo(0, 0);
                onNextFile();
                break;
            case R.id.llCheckResult:
                question = questions.get(position);
                switch (question.getType()) {
                    case Constant.SINGLE_CHOOSE:
                    case Constant.MIX_SINGLE_CHOOSE:
                        if (detailQuestion.isSubmit())
                            llCheckResult.setEnabled(false);
                        else {
                            stopMediaplayer();
                            logicSingleChoose();
                        }
                        break;
                    case Constant.FILL_WORD:
                    case Constant.MIX_FILL_WORD:
                        if (detailQuestion.isSubmit())
                            llCheckResult.setEnabled(false);
                        else {
                            stopMediaplayer();
                            logicFillWord(view);
                        }
                        break;
                    case Constant.MATCHING:
                    case Constant.MIX_MATCHING:
                        if (detailQuestion.isSubmit())
                            llCheckResult.setEnabled(false);
                        else {
                            stopMediaplayer();
                            logicMatching();
                        }
                        break;
                }
                break;
            case R.id.llNoteAnswer:
                if (StringUtils.isEmpty(detailQuestion.getNote()))
                    ToastUtils.showToast(getContext(), getString(R.string.no_note_answer));
                else {
                    NoteAnswerDialog noteAnswerDialog = new NoteAnswerDialog(getContext(), android.R.style.Theme_Light, detailQuestion, getActivity());
                    noteAnswerDialog.show();
                }
                break;
            case R.id.txtShowMoreContentWordFill:
                if (isShowMore) {
                    txtContentWordFill.setMaxLines(MAX_LINE);
                    isShowMore = false;
                    txtShowMoreContentWordFill.setText(R.string.show_more);
                } else {
                    txtContentWordFill.setMaxLines(MAXIUM_LINE);
                    txtShowMoreContentWordFill.setText(R.string.show_less);
                    isShowMore = true;
                }
                break;
            case R.id.txtMixQuestionShowMore:
                if (isShowMoreMix) {
                    txtMixQuestionContent.setMaxLines(MAX_LINE);
                    isShowMoreMix = false;
                    txtMixQuestionShowMore.setText(R.string.show_more);
                } else {
                    txtMixQuestionContent.setMaxLines(MAXIUM_LINE);
                    isShowMoreMix = true;
                    txtMixQuestionShowMore.setText(R.string.show_less);
                }
                break;
            case R.id.imgMediaQuestionSingleChoose:
                if (detailQuestion.getContentMediaType() != null) {
                    if (detailQuestion.getContentMediaType().equals(AUDIO))
                        showAudioDialog(detailQuestion.getContentMediaUri(), imgMediaQuestionSingleChoose);
                    else if (detailQuestion.getContentMediaType().equals(VIDEO))
                        showVideoDialog(detailQuestion.getContentMediaUri());
                }
                break;
            case R.id.imgMediaFillWord:
                if (detailQuestion.getContentMediaType() != null) {
                    if (detailQuestion.getContentMediaType().equals(AUDIO))
                        showAudioDialog(detailQuestion.getContentMediaUri(), imgMediaFillWord);
                    else if (detailQuestion.getContentMediaType().equals(VIDEO))
                        showVideoDialog(detailQuestion.getContentMediaUri());

                }
                break;
            case R.id.imgMixQuestionMedia:
                if (detailQuestion.getDescriptionMediaType().equals(AUDIO))
                    showAudioDialog(detailQuestion.getDescriptionMediaUri(), imgMixQuestionMedia);
                else if (detailQuestion.getDescriptionMediaType().equals(VIDEO))
                    showVideoDialog(detailQuestion.getDescriptionMediaUri());
                break;
        }
    }

    private void onNextFile() {
        if (position + 1 >= maxSizeQuestions)
            ToastUtils.showToast(getContext(), getString(R.string.last_lesson));
        else {
            position++;
            presenter.getDetailQuestion(bookId, questions.get(position).getRootId(), questions.get(position).getChildrenId());
        }
    }

    private void onPreviousFile() {
        if (position - 1 < 0)
            ToastUtils.showToast(getContext(), getString(R.string.first_lesson));
        else {
            position--;
            presenter.getDetailQuestion(bookId, questions.get(position).getRootId(), questions.get(position).getChildrenId());
        }
    }

    private void logicSingleChoose() {
        try {
            if (singleChooseAdapter.isChooseAnswer()) {
                singleChooseAdapter.setIsCheck();
                new Handler().postDelayed(() -> {
                    playSound(singleChooseAdapter.isCorrectAnswer());
                    if (!singleChooseAdapter.isCorrectAnswer()) AppUtils.setVibrator(getContext());
                }, 200);
                disableCheckAnswerButton();
                new Handler().postDelayed(() -> presenter.setLogQuestion(questions.get(position).getRootId(), singleChooseAdapter.isCorrectAnswer(), bookId,
                        AppUtils.setJSONString(singleChooseAdapter.getSubmitAnswer()), questions.get(position).isChild() ? Constant.CHILD : Constant.QUESTION, questions.get(position).getChildrenId()), 200);
                showNoteAnswer();
            } else AppUtils.showErrorDialog(getContext(), getString(R.string.empty_single_choose));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logicFillWord(View view) {
        try {
            List<Answer> empty = new ArrayList<>();
            fillWordAnswerAdapter.setDataList(empty);
            fillWordAnswerAdapter.setIsCheck();
            new Handler().postDelayed(() -> fillWordAnswerAdapter.setDataList(detailQuestion.getAnswers()), 100);
            AppUtils.hideKeybroad(getContext(), view);
            new Handler().postDelayed(() -> {
                playSound(fillWordAnswerAdapter.isCorrect());
                if (!fillWordAnswerAdapter.isCorrect()) AppUtils.setVibrator(getContext());
            }, 250);
            disableCheckAnswerButton();
            new Handler().postDelayed(() -> presenter.setLogQuestion(questions.get(position).getRootId(), fillWordAnswerAdapter.isCorrect(), bookId,
                    AppUtils.setJSONString(fillWordAnswerAdapter.getSubmitAnswer()), questions.get(position).isChild() ? Constant.CHILD : Constant.QUESTION, questions.get(position).getChildrenId()), 200);
            showNoteAnswer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logicMatching() {
        try {
            if (matchingAdapter.isChooseAnswer()) {
                matchingAdapter.setIsCheck();
                new Handler().postDelayed(() -> {
                    playSound(matchingAdapter.isCorrect());
                    if (!matchingAdapter.isCorrect()) AppUtils.setVibrator(getContext());
                }, 100);
                disableCheckAnswerButton();
                new Handler().postDelayed(() -> presenter.setLogQuestion(questions.get(position).getRootId(), matchingAdapter.isCorrect(), bookId,
                        AppUtils.setJSONString(matchingAdapter.getSubmitAnswer()), questions.get(position).isChild() ? Constant.CHILD : Constant.QUESTION, questions.get(position).getChildrenId()), 200);
                showNoteAnswer();
            } else
                AppUtils.showErrorDialog(getContext(), getString(R.string.empty_matching));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNoteAnswer() {
        llCheckResult.setVisibility(View.GONE);
        llNoteAnswer.setVisibility(View.VISIBLE);
    }

    private void disableCheckAnswerButton() {
        llCheckResult.setEnabled(false);
    }

    private void playSound(Boolean isCorrect) {
        stopAudio();
        mPlayer = MediaPlayer.create(getContext(), isCorrect ? R.raw.mcb_true : R.raw.mcb_false);
        mPlayer.start();
    }

    private void showVideoDialog(String url) {
        stopAudio();
        stopMediaplayer();
        if (viewPlaying != null)
            viewPlaying.setImageResource(R.drawable.ic_audio_play);
        if (singleChooseAdapter != null)
            singleChooseAdapter.setPositionPlaying(-1);

        videoDialog = new VideoDialog(getContext(), url, detailQuestion.getName(), android.R.style.Theme_Light, getActivity());
        videoDialog.show();
    }

    private void showAudioDialog(String url, ImageView view) {
        if (posPlaying != -1 && singleChooseAdapter != null) {
            singleChooseAdapter.setPositionPlaying(-1);
            posPlaying = -1;
            mediaPlayer.stop();
        }

        try {
            if (viewPlaying != null && viewPlaying != view) {
                viewPlaying.setImageResource(R.drawable.ic_audio_play);
                view.setImageResource(R.drawable.ic_pause_3);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getAppContext(), Uri.parse(url), AppContext.getHeader());
                mediaPlayer.prepare();
                mediaPlayer.start();
                viewPlaying = view;
            } else if (mediaPlayer.isPlaying()) {
                view.setImageResource(R.drawable.ic_audio_play);
                mediaPlayer.stop();
            } else {
                view.setImageResource(R.drawable.ic_pause_3);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getAppContext(), Uri.parse(url), AppContext.getHeader());
                mediaPlayer.prepare();
                mediaPlayer.start();
                viewPlaying = view;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // mediaplayer for adapter
    private void showAudioDialog(MediaPlayerResponse mediaPlayerResponse) {
        if (viewPlaying != null)
            viewPlaying.setImageResource(R.drawable.ic_audio_play);
        if (mediaPlayerResponse.getPosition() == posPlaying) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            singleChooseAdapter.setPositionPlaying(-1);
            posPlaying = -1;
        } else {
            posPlaying = mediaPlayerResponse.getPosition();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(getContext(), Uri.parse(mediaPlayerResponse.getUrl()), AppContext.getHeader());
                singleChooseAdapter.setPositionPlaying(mediaPlayerResponse.getPosition());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopAudio() {
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.stop();
    }

    private void stopMediaplayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            if (viewPlaying != null)
                viewPlaying.setImageResource(R.drawable.ic_audio_play);

            if (singleChooseAdapter != null && singleChooseAdapter.getItemCount() > 0)
                singleChooseAdapter.setPositionPlaying(-1);
        }
    }

    @Override
    public void onDestroyView() {
        mPlayer.stop();
        mPlayer.release();
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        if (videoDialog != null)
            videoDialog.pauseVideo();
        super.onStop();
    }

    @Override
    public String getTitleString() {
        return question.getFullName();
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onLoadLessonSuccess(DetailQuestion detailQuestion) {
        this.detailQuestion = detailQuestion;
        initData();
    }

    @Override
    public void onErrorDetailQuestion(int error) {
        ToastUtils.showToast(getContext(), getString(R.string.error_detail_question_data));
    }

    private void renderMenuMatching() {
        if (menu != null) {
            menu.clear();
            if (!detailQuestion.isSubmit())
                menuInflater.inflate(R.menu.matching, menu);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        this.menuInflater = inflater;
        menu.clear();
        if (detailQuestion != null && (detailQuestion.getType().equals(Constant.MATCHING) || detailQuestion.getType().equals(Constant.MIX_MATCHING)))
            inflater.inflate(R.menu.matching, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                if (llCheckResult.isEnabled()) {
                    matchingAdapter.setDataList(new ArrayList<>());
                    answerArray = getSubmitAnswerJson(dataAnswerList);
                    matchingAdapter.setDataList(answerArray);
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (viewPlaying != null)
            viewPlaying.setImageResource(R.drawable.ic_audio_play);
        if (singleChooseAdapter != null)
            singleChooseAdapter.setPositionPlaying(-1);
    }
}
