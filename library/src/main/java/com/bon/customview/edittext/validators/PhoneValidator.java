package com.bon.customview.edittext.validators;

/**
 * Phone validator extend;
 *
 * @author Dang
 * @year 2016
 */

import android.os.Build;

import java.util.regex.Pattern;

public class PhoneValidator extends RegExpValidator {

    public static Pattern CNDITIONEXP;

    static {
        if (Build.VERSION.SDK_INT >= 8) {
            CNDITIONEXP = android.util.Patterns.PHONE;
        } else {
            CNDITIONEXP = Pattern.compile("^(\\d{3}-\\d{7}|\\d{10,11})$");
        }
    }

    public PhoneValidator(String error) {
        super(CNDITIONEXP, error);
    }
}
