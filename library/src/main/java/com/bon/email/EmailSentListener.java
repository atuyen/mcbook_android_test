package com.bon.email;

public interface EmailSentListener {
    void onStarted();

    void onSuccess();

    void onFail();

    void onFinished();
}
