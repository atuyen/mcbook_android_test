package bg.player.com.playerbackground.event;

public class UpdateCounterEvent {
    String pos;
    int position;
    int duration;
    long time;

    public UpdateCounterEvent(String pos, int position, int duration) {
        this.pos = pos;
        this.position = position;
        this.duration = duration;
    }

    public UpdateCounterEvent(String pos, int position, int duration, long time) {
        this.pos = pos;
        this.position = position;
        this.duration = duration;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
