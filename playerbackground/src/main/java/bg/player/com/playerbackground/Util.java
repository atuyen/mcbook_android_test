package bg.player.com.playerbackground;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Util {
    public static String getDurationString(long durationMs, boolean negativePrefix) {
        long hours = TimeUnit.MILLISECONDS.toHours(durationMs);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs);
        if(hours > 0) {
            return String.format(Locale.getDefault(), "%s%02d:%02d:%02d",
                    negativePrefix ? "-" : "",
                    hours,
                    minutes - TimeUnit.HOURS.toMinutes(hours),
                    seconds - TimeUnit.MINUTES.toSeconds(minutes));
        }
        return String.format(Locale.getDefault(), "%s%02d:%02d",
                negativePrefix ? "-" : "",
                minutes,
                seconds - TimeUnit.MINUTES.toSeconds(minutes)
        );
    }
}
