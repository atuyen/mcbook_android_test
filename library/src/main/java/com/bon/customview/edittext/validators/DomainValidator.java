package com.bon.customview.edittext.validators;

/**
 * Domain name validator extend;
 *
 * @author Dang
 * @year 2016
 */

import android.os.Build;

import java.util.regex.Pattern;

public class DomainValidator extends RegExpValidator {
    public static Pattern CNDITIONEXP;

    static {
        if (Build.VERSION.SDK_INT >= 8) {
            CNDITIONEXP = android.util.Patterns.DOMAIN_NAME;
        } else {
            CNDITIONEXP = Pattern.compile("^[a-zA-Z0-9]+\\.[a-zA-Z0-9\\.]+$");
        }
    }

    public DomainValidator(String error) {
        super(CNDITIONEXP, error);
    }
}
