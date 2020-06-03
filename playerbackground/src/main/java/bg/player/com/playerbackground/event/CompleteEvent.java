package bg.player.com.playerbackground.event;

public class CompleteEvent {
    private String message;

    public CompleteEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
