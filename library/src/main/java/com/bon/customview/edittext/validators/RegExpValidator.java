package com.bon.customview.edittext.validators;

/**
 * RegExp validator extend;
 *
 * @author Dang
 * @year 2016
 */

import android.widget.EditText;

import java.util.regex.Pattern;

public class RegExpValidator extends Validator {
    protected Pattern pattern;

    public RegExpValidator(String regexp, String error) {
        super(error);
        this.pattern = Pattern.compile(regexp);
    }

    public RegExpValidator(Pattern ptr, String error) {
        super(error);
        this.pattern = ptr;
    }

    @Override
    public boolean check(EditText e) {
        return pattern.matcher(e.getText()).matches();
    }
}
