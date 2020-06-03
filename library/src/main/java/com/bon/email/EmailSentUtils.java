package com.bon.email;

import android.os.AsyncTask;

import com.bon.logger.Logger;

public class EmailSentUtils {
    private static final String TAG = EmailSentUtils.class.getSimpleName();

    // default
    private static final String port = "465";
    private static final String sport = "465";
    private static final String smtpHost = "smtp.gmail.com";
    private static final String userName = "abc@gmail.com";
    private static final String password = "xxx";
    private static final String sender = "abc@gmail.com";

    private String[] emailTo = null;
    private String subject = null;
    private String body = null;
    private EmailSentListener emailSentListener = null;

    /**
     * instance to sent email
     *
     * @return
     */
    public static EmailSentUtils getInstance() {
        return new EmailSentUtils();
    }

    /**
     * @param emailTo
     * @return
     */
    public EmailSentUtils setEmailTo(String[] emailTo) {
        this.emailTo = emailTo;
        return this;
    }

    /**
     * @param subject
     * @return
     */
    public EmailSentUtils setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * @param body
     * @return
     */
    public EmailSentUtils setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * @param emailSentListener
     * @return
     */
    public EmailSentUtils setEmailSentListener(EmailSentListener emailSentListener) {
        this.emailSentListener = emailSentListener;
        return this;
    }

    /**
     * AsyncTask send email
     */
    private class EmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (emailSentListener != null) emailSentListener.onStarted();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return EmailSentAuthenticator.getInstance(userName, password)
                        .setPort(port).setSport(sport).setHost(smtpHost)
                        .setTo(emailTo).setFrom(sender).setSubject(subject)
                        .setBody(body).send();
            } catch (Exception e) {
                Logger.e(TAG, e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            try {
                // raise event
                if (emailSentListener != null) {
                    // finish
                    emailSentListener.onFinished();

                    if (result) emailSentListener.onSuccess();// sent successful
                    else emailSentListener.onFail();// sent fail
                }
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }


    /**
     * send email
     */
    public void sendEmail() {
        try {
            new EmailAsyncTask().execute();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
