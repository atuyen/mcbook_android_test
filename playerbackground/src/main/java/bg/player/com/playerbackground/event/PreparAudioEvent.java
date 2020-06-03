package bg.player.com.playerbackground.event;

public class PreparAudioEvent {
    int duration;

    public PreparAudioEvent(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
