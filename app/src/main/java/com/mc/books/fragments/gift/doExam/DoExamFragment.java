package com.mc.books.fragments.gift.doExam;


import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.bon.util.ToastUtils;
import com.mc.adapter.FillWordAnswerAdapter;
import com.mc.adapter.MatchingAdapter;
import com.mc.adapter.SingleChooseAdapter;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.dialog.ConfirmExamDialog;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.books.dialog.ListExamQuestionDialog;
import com.mc.books.dialog.VideoDialog;
import com.mc.books.fragments.gift.resultExam.ResultExamFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.models.home.Answer;
import com.mc.models.home.DetailQuestion;
import com.mc.models.home.MediaPlayerResponse;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.timfreiheit.mathjax.android.MathJaxView;
import java8.util.stream.StreamSupport;

import static com.mc.utilities.Constant.AUDIO;
import static com.mc.utilities.Constant.IMAGE;
import static com.mc.utilities.Constant.KEY_EXAM_ID;
import static com.mc.utilities.Constant.KEY_GIFT;
import static com.mc.utilities.Constant.KEY_TIME;
import static com.mc.utilities.Constant.LINE_SHOW_MORE;
import static com.mc.utilities.Constant.MAXIUM_LINE;
import static com.mc.utilities.Constant.MAX_LINE;
import static com.mc.utilities.Constant.VIDEO;

public class DoExamFragment extends BaseMvpFragment<IDoExamView, IDoExamPresenter<IDoExamView>> implements IDoExamView, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public static DoExamFragment newInstance(List<DetailQuestion> detailQuestionList, int time, int examId, int mGiftUserId) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.KEY_LIST_QUESTION, (Serializable) detailQuestionList);
        args.putInt(Constant.KEY_TIME, time);
        args.putInt(Constant.KEY_EXAM_ID, examId);
        args.putInt(Constant.KEY_GIFT, mGiftUserId);
        DoExamFragment fragment = new DoExamFragment();
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
    FillWordAnswerAdapter fillWordAnswerAdapter;
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
    SingleChooseAdapter singleChooseAdapter;
    //end

    //View matching
    @BindView(R.id.rootMatching)
    NestedScrollView rootMatching;
    MatchingAdapter matchingAdapter;
    @BindView(R.id.rvMatching)
    RecyclerView rvMatching;
    @BindView(R.id.txtContentMatching)
    ExtTextView txtContentMatching;
    //end

    //view root
    @BindView(R.id.llAnswer)
    LinearLayout llAnswer;
    @BindView(R.id.llListQuestion)
    LinearLayout llListQuestion;
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
    @BindView(R.id.llViewRoot)
    LinearLayout llViewRoot;
    @BindView(R.id.txtTitleActionBar)
    TextView txtTitleActionBar;
    @BindView(R.id.imgListQuestion)
    ImageView imgListQuestion;
    @BindView(R.id.txtFinishExam)
    ExtTextView txtFinishExam;
    @BindView(R.id.txtTimer)
    ExtTextView txtTimer;

    @BindView(R.id.mathjaxSingleChoose)
    MathJaxView mathjaxSingleChoose;
    @BindView(R.id.mixMathjax)
    MathJaxView mixMathjax;
    @BindView(R.id.mathjaxWordFill)
    MathJaxView mathjaxWordFill;
    @BindView(R.id.mathjaxMatching)
    MathJaxView mathjaxMatching;

    List<DetailQuestion> detailQuestionList;
    int position = 0;
    int maxSizeQuestions;
    DetailQuestion detailQuestion;
    boolean isShowMore;
    boolean isShowMoreMix;
    List<Answer> answerArray;
    int time;
    CountDownTimer countDownTimer;
    int examId;
    MediaPlayer mediaPlayer;
    int posPlaying = -1;
    ImageView viewPlaying;
    int mGiftUserId;

    @NonNull
    @Override
    public IDoExamPresenter<IDoExamView> createPresenter() {
        return new DoExamPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.do_exam_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            bindButterKnife(view);
            mMainActivity.onShowBottomBar(false);
            mActivity.getSupportActionBar().hide();
            //disable back button
            disableBackButton(view);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            detailQuestionList = (List<DetailQuestion>) getArguments().getSerializable(Constant.KEY_LIST_QUESTION);
            time = getArguments().getInt(KEY_TIME);
            examId = getArguments().getInt(KEY_EXAM_ID);
            mGiftUserId = getArguments().getInt(KEY_GIFT);
            setCountDown();
            if (detailQuestionList != null) maxSizeQuestions = detailQuestionList.size();
            initData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setCountDown() {
        countDownTimer = new CountDownTimer(time * 60000, 1000) {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(AppUtils.getTimeCountDown(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                stopMediaplayer();
                showDialogTimeup();
            }
        }.start();
    }

    void showDialogTimeup() {
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.timeup_exam), R.drawable.ic_clock, true);
        errorBoxDialog.show();
        errorBoxDialog.setOnDismissListener(dialogInterface -> {
            setCorrectQuestion();
            showProgress(true);
            new Handler().postDelayed(this::setAnswerForNoAnswerQuestion, 300);
            new Handler().postDelayed(() -> FragmentUtils.replaceFragment(getActivity(), ResultExamFragment.newInstance(detailQuestionList, examId, mGiftUserId),
                    fragment -> mMainActivity.fragments.add(fragment)), 300);
            showProgress(false);
        });
    }

    void disableBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP);
    }

    void initData() {
        hideAll();
        if (detailQuestionList.size() > 0) detailQuestion = detailQuestionList.get(position);
        else ToastUtils.showToast(getContext(), getString(R.string.empty_exam));
        txtTitleActionBar.setText(String.format(getString(R.string.question_name_title), position + 1, StringUtils.isNullOrEmpty(detailQuestion.getName()) ? "" : detailQuestion.getName()));
        llMixQuestion.setVisibility(View.GONE);
        llAnswer.setVisibility(View.VISIBLE);
        llListQuestion.setVisibility(View.VISIBLE);
        llNoteAnswer.setVisibility(View.GONE);
        txtDescriptionForm.setVisibility(View.GONE);
        switch (detailQuestion.getType()) {
            case Constant.SINGLE_CHOOSE:
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderSingleChoose();
                break;
            case Constant.FILL_WORD:
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderFillWord();
                break;
            case Constant.MATCHING:
                renderMatching();
                break;
            case Constant.MIX_SINGLE_CHOOSE:
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderMixQuestionLayout(detailQuestion);
                renderSingleChoose();
                break;
            case Constant.MIX_FILL_WORD:
                txtDescriptionForm.setVisibility(View.VISIBLE);
                renderMixQuestionLayout(detailQuestion);
                renderFillWord();
                break;
            case Constant.MIX_MATCHING:
                renderMixQuestionLayout(detailQuestion);
                renderMatching();
                break;
        }

        if (detailQuestion.getDescriptionQuestionForms().isEmpty())
            txtDescriptionForm.setVisibility(View.GONE);

        txtDescriptionForm.setText(Html.fromHtml(detailQuestion.getDescriptionQuestionForms()));
    }

    void renderMixQuestionLayout(DetailQuestion detailQuestion) {
        isShowMoreMix = false;
        llMixQuestion.setVisibility(View.VISIBLE);
        imgMixQuestionContent.setVisibility(View.GONE);
        imgMixQuestionMedia.setVisibility(View.GONE);

        if (AppUtils.isMathJax(detailQuestion.getDescription())) {
            txtMixQuestionShowMore.setVisibility(View.GONE);
            mixMathjax.setVisibility(View.VISIBLE);
            txtMixQuestionContent.setVisibility(View.GONE);
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
                } else txtMixQuestionShowMore.setVisibility(View.GONE);
            });
        }

        txtMixQuestionContent.post(() -> {
            int lineCount = txtMixQuestionContent.getLineCount();
            if (lineCount > LINE_SHOW_MORE) {
                txtMixQuestionContent.setMaxLines(MAX_LINE);
                txtMixQuestionShowMore.setVisibility(View.VISIBLE);
            } else txtMixQuestionShowMore.setVisibility(View.GONE);
        });

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

    void renderMatching() {
        try {
            rootMatching.setVisibility(View.VISIBLE);
            llAnswer.setVisibility(View.VISIBLE);
            llListQuestion.setVisibility(View.VISIBLE);
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
            matchingAdapter.setDataList(answerArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void renderFillWord() {
        try {
            llRootWordFill.setVisibility(View.VISIBLE);
            llAnswer.setVisibility(View.VISIBLE);
            llListQuestion.setVisibility(View.VISIBLE);
            imgMediaFillWord.setVisibility(View.GONE);
            imgContentFillWord.setVisibility(View.GONE);
            if (AppUtils.isMathJax(detailQuestion.getContent())) {
                txtShowMoreContentWordFill.setVisibility(View.GONE);
                txtContentWordFill.setVisibility(View.GONE);
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
                    } else txtShowMoreContentWordFill.setVisibility(View.GONE);
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
            fillWordAnswerAdapter.setDataList(detailQuestion.getAnswers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void renderSingleChoose() {
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
            }, pos -> detailQuestion.setSingleChoosePosition(pos), detailQuestion.getSingleChoosePosition());
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
            singleChooseAdapter.setDataList(detailQuestion.getAnswers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setCorrectQuestion() {
        switch (detailQuestion.getType()) {
            case Constant.SINGLE_CHOOSE:
            case Constant.MIX_SINGLE_CHOOSE:
                logicSingleChoose(detailQuestion);
                break;
            case Constant.FILL_WORD:
            case Constant.MIX_FILL_WORD:
                logicFillWord(detailQuestion);
                break;
            case Constant.MATCHING:
            case Constant.MIX_MATCHING:
                logicMatching(detailQuestion);
                break;
        }
    }

    void logicSingleChoose(DetailQuestion detailQuestion) {
        try {
            if (singleChooseAdapter.isChooseAnswer()) {
                showProgress(true);
                singleChooseAdapter.setIsCheckExam();
                new Handler().postDelayed(() -> {
                    detailQuestion.setCorrect(singleChooseAdapter.isCorrectAnswer());
                    detailQuestion.setSubmit(true);
                    detailQuestion.setAnswer(true);
                    detailQuestion.setSubmitAnswer(AppUtils.setJSONString(singleChooseAdapter.getSubmitAnswer()));
                    showProgress(false);
                }, 300);
            } else {
                detailQuestion.setCorrect(false);
                detailQuestion.setSubmit(true);
                detailQuestion.setAnswer(false);
                detailQuestion.setSubmitAnswer(AppUtils.setJSONString(singleChooseAdapter.getSubmitAnswer()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void logicFillWord(DetailQuestion detailQuestion) {
        try {
            showProgress(true);
            List<Answer> empty = new ArrayList<>();
            fillWordAnswerAdapter.setDataList(empty);
            fillWordAnswerAdapter.setIsCheckExam();
            new Handler().postDelayed(() -> fillWordAnswerAdapter.setDataList(detailQuestion.getAnswers()), 100);
            new Handler().postDelayed(() -> {
                detailQuestion.setCorrect(fillWordAnswerAdapter.isCorrect());
                detailQuestion.setSubmit(true);
                detailQuestion.setAnswer(fillWordAnswerAdapter.getContentAnswer() != null && !fillWordAnswerAdapter.getContentAnswer().equals(""));
                detailQuestion.setSubmitAnswer(AppUtils.setJSONString(fillWordAnswerAdapter.getSubmitAnswer()));
                showProgress(false);
            }, 250);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void logicMatching(DetailQuestion detailQuestion) {
        try {
            if (matchingAdapter.isChooseAnswer()) {
                showProgress(true);
                matchingAdapter.setIsCheckExam();
                new Handler().postDelayed(() -> {
                    detailQuestion.setCorrect(matchingAdapter.isCorrect());
                    detailQuestion.setSubmit(true);
                    detailQuestion.setAnswer(true);
                    detailQuestion.setSubmitAnswer(AppUtils.setJSONString(matchingAdapter.getSubmitAnswer()));
                    showProgress(false);
                }, 300);
            } else {
                detailQuestion.setCorrect(false);
                detailQuestion.setSubmit(true);
                detailQuestion.setAnswer(false);
                List<Answer> detailQuestionList1 = detailQuestion.getAnswers();
                StreamSupport.stream(detailQuestionList1).forEach(answer -> answer.setMatching(true));
                detailQuestion.setSubmitAnswer(AppUtils.setJSONString(detailQuestionList1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showVideoDialog(String url) {
        stopMediaplayer();
        if (viewPlaying != null)
            viewPlaying.setImageResource(R.drawable.ic_audio_play);
        if (singleChooseAdapter != null) singleChooseAdapter.setPositionPlaying(-1);
        VideoDialog videoDialog = new VideoDialog(getContext(), url, detailQuestion.getName(), android.R.style.Theme_Light, getActivity());
        videoDialog.show();

    }

    void showAudioDialog(String url, ImageView view) {
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
    void showAudioDialog(MediaPlayerResponse mediaPlayerResponse) {
        if (viewPlaying != null) viewPlaying.setImageResource(R.drawable.ic_audio_play);
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

    void hideAll() {
        llRootWordFill.setVisibility(View.GONE);
        llRootSingleChoose.setVisibility(View.GONE);
        rootMatching.setVisibility(View.GONE);
    }

    void onNextFile() {
        if (position + 1 >= maxSizeQuestions)
            ToastUtils.showToast(getContext(), getString(R.string.last_exam));
        else {
            position++;
            initData();
        }
    }

    void onPreviousFile() {
        if (position - 1 < 0) {
            ToastUtils.showToast(getContext(), getString(R.string.first_exam));
        } else {
            position--;
            initData();
        }
    }

    void resetView() {
        svRoot.scrollTo(0, 0);
        AppUtils.hideKeybroad(getContext(), llViewRoot);
    }

    @OnClick({R.id.llListQuestion, R.id.llNextQuestion, R.id.llPreviousQuestion, R.id.txtShowMoreContentWordFill, R.id.imgMediaFillWord,
            R.id.imgMediaQuestionSingleChoose, R.id.txtMixQuestionShowMore, R.id.imgMixQuestionMedia, R.id.imgListQuestion, R.id.txtFinishExam})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llNextQuestion:
                stopMediaplayer();
                setCorrectQuestion();
                resetView();
                new Handler().postDelayed(this::onNextFile, 400);
                break;
            case R.id.llPreviousQuestion:
                stopMediaplayer();
                setCorrectQuestion();
                resetView();
                new Handler().postDelayed(this::onPreviousFile, 400);
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
                if (detailQuestion.getDescriptionMediaType() != null) {
                    if (detailQuestion.getDescriptionMediaType().equals(AUDIO))
                        showAudioDialog(detailQuestion.getDescriptionMediaUri(), imgMixQuestionMedia);
                    else if (detailQuestion.getDescriptionMediaType().equals(VIDEO))
                        showVideoDialog(detailQuestion.getDescriptionMediaUri());
                }
                break;
            case R.id.imgListQuestion:
                stopMediaplayer();
                setCorrectQuestion();
                ListExamQuestionDialog listExamQuestionDialog = new ListExamQuestionDialog(getContext(), detailQuestionList, position -> {
                    this.position = position;
                    initData();
                }, position, android.R.style.Theme_Light, false);
                listExamQuestionDialog.show();
                break;
            case R.id.txtFinishExam:
                stopMediaplayer();
                setCorrectQuestion();
                ConfirmExamDialog confirmExamDialog = new ConfirmExamDialog(getContext(), v -> {
                    countDownTimer.cancel();
                    showProgress(true);
                    setAnswerForNoAnswerQuestion();
                    FragmentUtils.replaceFragment(getActivity(), ResultExamFragment.newInstance(detailQuestionList, examId, mGiftUserId),
                            fragment -> mMainActivity.fragments.add(fragment));
                    showProgress(false);
                });
                confirmExamDialog.show();
                break;

        }
    }

    void setAnswerForNoAnswerQuestion() {
        StreamSupport.stream(detailQuestionList).forEach(detailQuestion -> {
            if (!detailQuestion.isAnswer()) {
                switch (detailQuestion.getType()) {
                    case Constant.SINGLE_CHOOSE:
                    case Constant.MIX_SINGLE_CHOOSE:
                        detailQuestion.setCorrect(false);
                        detailQuestion.setSubmit(true);
                        detailQuestion.setAnswer(false);
                        detailQuestion.setSubmitAnswer(AppUtils.setJSONString(getSubmitAnswer(detailQuestion.getAnswers())));
                        break;
                    case Constant.FILL_WORD:
                    case Constant.MIX_FILL_WORD:
                        detailQuestion.setCorrect(false);
                        detailQuestion.setSubmit(true);
                        detailQuestion.setAnswer(false);
                        detailQuestion.setSubmitAnswer(AppUtils.setJSONString(getSubmitAnswer(detailQuestion.getAnswers())));
                        break;
                    case Constant.MATCHING:
                    case Constant.MIX_MATCHING:
                        detailQuestion.setCorrect(false);
                        detailQuestion.setSubmit(true);
                        detailQuestion.setAnswer(false);
                        List<Answer> detailQuestionList1 = detailQuestion.getAnswers();
                        StreamSupport.stream(detailQuestionList1).forEach(answer -> answer.setMatching(true));
                        detailQuestion.setSubmitAnswer(AppUtils.setJSONString(detailQuestionList1));
                        break;
                }
            }
        });
    }

    List<Answer> getSubmitAnswer(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setTemp("");
        }
        return answers;
    }

    @Override
    public void onDestroyView() {
        mActivity.getAppSupportActionBar().show();
        mediaPlayer.stop();
        mediaPlayer.release();
        if (countDownTimer != null)
            countDownTimer.cancel();
        super.onDestroyView();
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public String getTitleString() {
        return String.format(getString(R.string.question_name_title), position + 1, "");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (viewPlaying != null)
            viewPlaying.setImageResource(R.drawable.ic_audio_play);
        if (singleChooseAdapter != null) singleChooseAdapter.setPositionPlaying(-1);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    void stopMediaplayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            if (viewPlaying != null) {
                viewPlaying.setImageResource(R.drawable.ic_audio_play);
                if (singleChooseAdapter != null && singleChooseAdapter.getItemCount() > 0)
                    singleChooseAdapter.setPositionPlaying(-1);
            }
        }
    }
}
