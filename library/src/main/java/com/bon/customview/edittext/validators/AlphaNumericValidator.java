package com.bon.customview.edittext.validators;

/**
 * Alphabet and numeric validator extend;
 *
 * @author Dang
 * @year 2016
 */

public class AlphaNumericValidator extends RegExpValidator {
    public AlphaNumericValidator(String error) {
        super("^[a-zA-Z0-9 \\./-]*$", error);
    }
}
