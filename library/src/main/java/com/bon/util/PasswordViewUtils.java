package com.bon.util;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import com.bon.customview.edittext.ExtEditTextPassword;
import com.bon.library.R;
import com.bon.logger.Logger;

/**
 * Created by Administrator on 12/01/2017.
 */

public class PasswordViewUtils {
    private static final String TAG = PasswordViewUtils.class.getSimpleName();

    /**
     * @param edtPassword
     */
    public static void setShowOrHidePassword(EditText edtPassword) {
        try {
            if (edtPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            edtPassword.setSelection(edtPassword.getText().length());
            edtPassword.setTypeface(Typeface.DEFAULT);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     * @param edtPassword
     */
    public static void setPasswordConfig(Activity activity, ExtEditTextPassword edtPassword) {
        try {
            edtPassword.setTypeface(Typeface.DEFAULT);
            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            edtPassword.setDrawableRightVisible(GeneralUtils.getDrawableFromResource(activity, R.drawable.ic_password_visibility_24dp));
            edtPassword.setDrawableRightHide(GeneralUtils.getDrawableFromResource(activity, R.drawable.ic_password_visibility_off_24dp));
            edtPassword.setDrawableClickListener(target -> {
                try {
                    switch (target) {
                        case RIGHT:
                            PasswordViewUtils.setShowOrHidePassword(edtPassword);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
