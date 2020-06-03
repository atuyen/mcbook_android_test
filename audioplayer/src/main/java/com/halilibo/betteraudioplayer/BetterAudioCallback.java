package com.halilibo.betteraudioplayer;

/**
 * @author Aidan Follestad
 * Modified by Halil Ozercan
 */
public interface BetterAudioCallback {

    void onStarted(BetterAudioPlayer player);

    void onPaused(BetterAudioPlayer player);

    void onPreparing(BetterAudioPlayer player);

    void onPrepared(BetterAudioPlayer player);

    void onBuffering(int percent);

    void onError(BetterAudioPlayer player, Exception e);

    void onCompletion(BetterAudioPlayer player);

    void onToggleControls(BetterAudioPlayer player, boolean isShowing);

    void onNext();

    void onPrevious();

//    void onFullScreen(boolean isFullScreen);
}