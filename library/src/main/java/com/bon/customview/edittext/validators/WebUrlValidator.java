package com.bon.customview.edittext.validators;

/**
 * Web URL validator extend;
 *
 * @author Dang
 * @year 2016
 */

import android.os.Build;

import java.util.regex.Pattern;

public class WebUrlValidator extends RegExpValidator {
    public static Pattern CNDITIONEXP;

    static {
        if (Build.VERSION.SDK_INT >= 8) {
            CNDITIONEXP = android.util.Patterns.WEB_URL;
        } else {
            CNDITIONEXP = Pattern.compile("/^(https]?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$/");
        }
    }

    public WebUrlValidator(String error) {
        super(CNDITIONEXP, error);
    }
}
