package com.bon.customview.datetime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.bon.customview.textview.ExtTextView;
import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.bon.library.R;
import com.bon.logger.Logger;

import java.util.Calendar;

import java8.util.function.Consumer;
import java8.util.function.Function;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtDatePickerDialogFragment extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtDatePickerDialogFragment.class.getSimpleName();

    // instance
    public static ExtDatePickerDialogFragment newInstance() {
        return new ExtDatePickerDialogFragment();
    }

    // view
    ExtTextView tvCancel;
    ExtTextView tvSave;
    DatePicker dpDatePicker;

    // view
    Consumer<Calendar> calendarConsumer;
    Function<Calendar, Boolean> conditionFunction;

    // variable
    long milliseconds;
    Calendar calendar;
    Calendar minDate;
    Calendar maxDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.date_picker_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // null point exception value
            if (this.minDate == null || this.maxDate == null || this.calendar == null || this.calendarConsumer == null
                    || this.conditionFunction == null) {
                throw new NullPointerException();
            }

            // cancel
            this.tvCancel = view.findViewById(R.id.tvCancel);
            this.tvCancel.setOnClickListener(v -> onClickCancel());

            // save
            this.tvSave = view.findViewById(R.id.tvSave);
            this.tvSave.setOnClickListener(v -> onClickSave());

            // date picker
            this.dpDatePicker = view.findViewById(R.id.dpDatePicker);

            this.dpDatePicker.init(this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH),
                    this.calendar.get(Calendar.DAY_OF_MONTH), (viewDatePicker, year, monthOfYear, dayOfMonth) -> {
                try {
                    this.calendar.set(Calendar.YEAR, year);
                    this.calendar.set(Calendar.MONTH, monthOfYear);
                    this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    this.setDisplayButtonDone(conditionFunction.apply(calendar));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // set min, max date
            this.dpDatePicker.setMinDate(minDate.getTimeInMillis());
            this.dpDatePicker.setMaxDate(this.maxDate.getTimeInMillis());
            this.setDisplayButtonDone(conditionFunction.apply(calendar));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void setDisplayButtonDone(boolean isVisible) {
        try {
            tvSave.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void onClickCancel() {
        dismiss();
    }

    void onClickSave() {
        try {
            if (this.calendarConsumer != null) {
                this.calendarConsumer.accept(calendar);
            }
            dismiss();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public ExtDatePickerDialogFragment setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis(milliseconds);
        return this;
    }

    public ExtDatePickerDialogFragment setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    public ExtDatePickerDialogFragment setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public ExtDatePickerDialogFragment setCalendarConsumer(Consumer<Calendar> calendarConsumer) {
        this.calendarConsumer = calendarConsumer;
        return this;
    }

    public ExtDatePickerDialogFragment setConditionFunction(Function<Calendar, Boolean> conditionFunction) {
        this.conditionFunction = conditionFunction;
        return this;
    }
}
