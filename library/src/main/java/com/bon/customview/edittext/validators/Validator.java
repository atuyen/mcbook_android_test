package com.bon.customview.edittext.validators;

/**
 * Validator is abstract class, for using in ExtEditText.
 *
 * @author Dang
 * @year 2016
 */

import android.widget.EditText;

public abstract class Validator {
    protected String errorMessage;

    public Validator(String error) {
        errorMessage = error;
    }

    public abstract boolean check(EditText e);

    public String getErrorMessage(EditText e) {
        return errorMessage;
    }
}
