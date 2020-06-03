package bg.player.com.playerbackground.module;

public interface AudioPlayerInteface {
    void onComplete();

    void onNext();

    void onPrevious();

    void onDuration(long dur);

    void onEventTraining();
}
