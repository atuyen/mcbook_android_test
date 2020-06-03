package com.mc.customizes.edittexts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.models.home.Answer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java8.util.function.Consumer;

public class EditTextFillWord extends LinearLayout {

    @BindView(R.id.edtAnswer1)
    ExtEditText edtAnswer;
    @BindView(R.id.tvAnswer)
    ExtTextView tvAnswer;
    @BindView(R.id.edtCorrectAnswer)
    ExtEditText edtCorrectAnswer;
    @BindView(R.id.viewCorrectAnswer)
    View viewCorrectAnswer;
    @BindView(R.id.extIdAnwser)
    ExtTextView extIdAnwser;

    private Context context;
    Unbinder unbinder;
    private Consumer<String> content;

    public EditTextFillWord(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextFillWord(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextFillWord(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EditTextFillWord(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_text_fill_word, this);
        unbinder = ButterKnife.bind(this, view);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextFillWord);

        edtAnswer.setText(typedArray.getString(R.styleable.EditTextFillWord_textAnswer));
        edtCorrectAnswer.setText(typedArray.getString(R.styleable.EditTextFillWord_textCorrectAnswer));
        extIdAnwser.setText(typedArray.getString(R.styleable.EditTextFillWord_textId));
        typedArray.recycle();
    }

    public EditTextFillWord showWrongAnswer(Answer answer) {
        edtAnswer.setTextColor(context.getResources().getColor(R.color.colorRed));
        tvAnswer.setTextColor(context.getResources().getColor(R.color.colorRed));

        edtCorrectAnswer.setVisibility(VISIBLE);
        viewCorrectAnswer.setVisibility(VISIBLE);

        edtAnswer.setText(answer.getTemp() == null ? answer.getFillWordAnswer() : answer.getTemp());
        tvAnswer.setText(answer.getTemp() == null ? answer.getFillWordAnswer() : answer.getTemp());

        edtCorrectAnswer.setText(answer.getContent());

        edtAnswer.setFocusable(false);
        edtAnswer.setVisibility(GONE);
        tvAnswer.setVisibility(VISIBLE);

        edtCorrectAnswer.setFocusable(false);
        return this;
    }

    public EditTextFillWord showCorrectAnswer(Answer answer) {
        edtAnswer.setText(answer.getFillWordAnswer());
        edtAnswer.setTextColor(context.getResources().getColor(R.color.colorGreen));
        edtAnswer.setFocusable(false);
        edtAnswer.setVisibility(GONE);

        tvAnswer.setText(answer.getFillWordAnswer());
        tvAnswer.setTextColor(context.getResources().getColor(R.color.colorGreen));
        tvAnswer.setFocusable(false);
        tvAnswer.setVisibility(VISIBLE);

        return this;
    }

    public EditTextFillWord setMultiple() {
        edtAnswer.setSingleLine(false);
        edtAnswer.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        edtAnswer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edtAnswer.setVerticalScrollBarEnabled(true);
        edtAnswer.setMovementMethod(ScrollingMovementMethod.getInstance());
        edtAnswer.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        return this;
    }

    public EditTextFillWord setText(String text) {
        edtAnswer.setText(text);
        return this;
    }

    public EditTextFillWord getAnswer(Consumer<String> content) {
        edtAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                content.accept(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        content.accept(edtAnswer.getText().toString());
        return this;
    }


    public EditTextFillWord setId(String id) {
        extIdAnwser.setText("(" + id + ")");
        return this;
    }

    public EditTextFillWord hideId() {
        extIdAnwser.setVisibility(GONE);
        return this;
    }
}
