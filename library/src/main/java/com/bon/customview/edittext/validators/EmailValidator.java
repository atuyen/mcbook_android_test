package com.bon.customview.edittext.validators;

/**
 * EmailSentAuthenticator validator extend;
 *
 * @author Dang
 * @year 2016
 */

import android.os.Build;

import java.util.regex.Pattern;

public class EmailValidator extends RegExpValidator {

    public static Pattern CNDITIONEXP;

    static {
        if (Build.VERSION.SDK_INT >= 8) {
            CNDITIONEXP = android.util.Patterns.EMAIL_ADDRESS;
        } else {
            CNDITIONEXP = Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                            "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" +
                            "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
        }
    }

    public EmailValidator(String error) {
        super(CNDITIONEXP, error);
    }
}
