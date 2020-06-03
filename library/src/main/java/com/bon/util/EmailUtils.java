package com.bon.util;

import com.bon.logger.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {
    private static final String TAG = EmailUtils.class.getSimpleName();

    // const
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate hex with regular expression
     *
     * @return true valid hex, false invalid email
     */
    public static boolean isValidate(String email) {
        try {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);

            return matcher.matches();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }

    /**
     * @param email
     * @param emailPattern "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
     *                     "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
     * @return true valid hex, false invalid email
     */
    public static boolean isValidate(String email, String emailPattern) {
        try {
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(email);

            return matcher.matches();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }
}
