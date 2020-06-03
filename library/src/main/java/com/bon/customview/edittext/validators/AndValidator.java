package com.bon.customview.edittext.validators;

/**
 * AND validator extend;
 *
 * @author Dang
 * @year 2016
 */

import android.widget.EditText;

public class AndValidator extends OrValidator {
    public AndValidator() {
        super();
    }

    public AndValidator(String error) {
        super(error);
    }

    @Override
    public boolean check(EditText e) {
        for (Validator v : validators) {
            try {
                if (!v.check(e)) {
                    errorMsg = v.getErrorMessage(e);
                    return false;
                }
            } catch (Exception ex) {
                errorMsg = v.getErrorMessage(e);
                return false;
            }
        }

        return true;
    }
}
