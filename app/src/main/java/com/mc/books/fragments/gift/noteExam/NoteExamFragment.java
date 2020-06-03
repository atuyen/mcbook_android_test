package com.mc.books.fragments.gift.noteExam;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import static com.mc.utilities.Constant.IMAGE;
import static com.mc.utilities.Constant.KEY_LIST_QUESTION;
import static com.mc.utilities.Constant.KEY_POSITION;
import static com.mc.utilities.Constant.LINE_SHOW_MORE;
import static com.mc.utilities.Constant.MAXIUM_LINE;
import static com.mc.utilities.Constant.MAX_LINE;
import static com.mc.utilities.Constant.VIDEO;

public class NoteExamFragment extends BaseMvpFragment<INoteExamView, INoteExamPresenter<INoteExamView>> implements INoteExamView, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public static NoteExamFragment newInstance(List<DetailQuestion> detailQuestions, int position) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_LIST_QUESTION, (Serializable) detailQuestions);
        args.putInt(KEY_POSITION, position);
        NoteExamFragment fragment = new NoteExamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public INoteExamPresenter<INoteExamView> createPresenter() {
        return new NoteExamPresenter<>(getAppComponent());
    }

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
    //end

    @BindView(R.id.mathjaxSingleChoose)
    MathJaxView mathjaxSingleChoose;
    @BindView(R.id.mixMathjax)
    MathJaxView mixMathjax;
    @BindView(R.id.mathjaxWordFill)
    MathJaxView mathjaxWordFill;
    @BindView(R.id.mathjaxMatching)
    MathJaxView mathjaxMatching;

    private int position;
    private int maxSizeQuestions;
    private DetailQuestion detailQuestion;
    private boolean isShowMore;
    private boolean isShowMoreMix;
    private List<Answer> answerArray;
    private List<DetailQuestion> detailQuestionList;
    private MediaPlayer mediaPlayer;
    int posPlaying = -1;
    ImageView viewPlaying;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            bindButterKnife(view);
            mMainActivity.onShowBottomBar(false);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            detailQuestionList = (List<DetailQuestion>) getArguments().getSerializable(Constant.KEY_LIST_QUESTION);
            position = getArguments().getInt(KEY_POSITION);
            if (detailQuestionList != null) maxSizeQuestions = detailQuestionList.size();
            llCheckResult.setVisibility(View.GONE);
            llNoteAnswer.setVisibility(View.VISIBLE);
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        try {
            hideAll();
            if (detailQuestionList.size() > 0) {
                detailQuestion = detailQuestionList.get(position);
                if (!detailQuestion.isAnswer()) {
                    String questioName = String.format(getString(R.string.question_name_title), position + 1, StringUtils.isNullOrEmpty(detailQuestion.getName()) ? "" : detailQuestion.getName());
                    ToastUtils.showToast(getContext(), getString(R.string.not_do_question, questioName), Gravity.CENTER, Toast.LENGTH_SHORT);
                }
            } else ToastUtils.showToast(getContext(), getString(R.string.empty_exam));
            mActivity.setToolbarTitle(String.format(getString(R.string.question_name_title), position + 1, StringUtils.isNullOrEmpty(detailQuestion.getName()) ? "" : detailQuestion.getName()));
            llMixQuestion.setVisibility(View.GONE);
            llAnswer.setVisibility(View.VISIBLE);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderMixQuestionLayout(DetailQuestion detailQuestion) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderMatching() {
        try {
            rootMatching.setVisibility(View.VISIBLE);
            llAnswer.setVisibility(View.VISIBLE);
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
            if (detailQuestion.isSubmit()) {
                List<Answer> answers = new ArrayList<>(getSubmitAnswerJson(detailQuestion.getSubmitAnswer()));
                matchingAdapter.setIsCheck();
                matchingAdapter.setDataList(answers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderFillWord() {
        try {
            llRootWordFill.setVisibility(View.VISIBLE);
            llAnswer.setVisibility(View.VISIBLE);
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
            if (detailQuestion.isSubmit()) {
                List<Answer> answers = new ArrayList<>(getSubmitAnswerJson(detailQuestion.getSubmitAnswer()));
                fillWordAnswerAdapter.setIsCheck();
                fillWordAnswerAdapter.setDataList(answers);
            }
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.llPreviousQuestion, R.id.llCheckResult, R.id.llNextQuestion, R.id.txtShowMoreContentWordFill, R.id.imgMediaFillWord,
            R.id.imgMediaQuestionSingleChoose, R.id.txtMixQuestionShowMore, R.id.imgMixQuestionMedia, R.id.llNoteAnswer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llPreviousQuestion:
                stopMediaplayer();
                svRoot.scrollTo(0, 0);
                onPreviousFile();
                break;
            case R.id.llNextQuestion:
                stopMediaplayer();
                svRoot.scrollTo(0, 0);
                onNextFile();
                break;
            case R.id.llNoteAnswer:
                stopMediaplayer();
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
                if (detailQuestion.getDescriptionMediaType() != null) {
                    if (detailQuestion.getDescriptionMediaType().equals(AUDIO))
                        showAudioDialog(detailQuestion.getDescriptionMediaUri(), imgMixQuestionMedia);
                    else if (detailQuestion.getDescriptionMediaType().equals(VIDEO))
                        showVideoDialog(detailQuestion.getDescriptionMediaUri());
                }
                break;
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

    private void showVideoDialog(String url) {
        stopMediaplayer();
        if (viewPlaying != null)
            viewPlaying.setImageResource(R.drawable.ic_audio_play);
        if (singleChooseAdapter != null) singleChooseAdapter.setPositionPlaying(-1);
        VideoDialog videoDialog = new VideoDialog(getContext(), url, detailQuestion.getName(), android.R.style.Theme_Light, getActivity());
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
                mediaPlayer.stop();
                view.setImageResource(R.drawable.ic_audio_play);
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

    private void onNextFile() {
        if (position + 1 >= maxSizeQuestions)
            ToastUtils.showToast(getContext(), getString(R.string.last_exam));
        else {
            position++;
            initData();
        }
    }

    private void onPreviousFile() {
        if (position - 1 < 0) ToastUtils.showToast(getContext(), getString(R.string.first_exam));
        else {
            position--;
            initData();
        }
    }

    @Override
    public String getTitleString() {
        return String.format(getString(R.string.question_name_title), position + 1, "");
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }


    @Override
    public int getResourceId() {
        return R.layout.do_subject_fragment;
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

    @Override
    public void onDestroyView() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroyView();
    }

    private void stopMediaplayer() {
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
