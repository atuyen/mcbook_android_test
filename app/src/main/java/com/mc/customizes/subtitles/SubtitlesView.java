package com.mc.customizes.subtitles;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.halilibo.betteraudioplayer.HelperMethods;
import com.halilibo.betteraudioplayer.subtitle.DownloadCallback;
import com.halilibo.betteraudioplayer.subtitle.DownloadFile;
import com.mc.books.R;
import com.mc.books.fragments.home.doTraining.subtraining.ISubTitlesCallback;
import com.mc.models.home.SubTitles;
import com.mc.models.home.TrainingAudio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SubtitlesView extends AppCompatTextView {

    private static final String TAG = "SubtitleView";
    private static final String LINE_BREAK = "<br/>";
    private TreeMap<Long, Line> track;
    private ISubTitlesCallback iSubTitlesCallback;
    private String name;
    private String roleName;
    private int[] color = new int[]{R.color.colorSub1, R.color.colorSub2, R.color.colorSub3, R.color.colorSub4,
            R.color.colorSub5, R.color.colorSub6, R.color.colorSub7};

    public SubtitlesView(Context context) {
        super(context);
    }

    public SubtitlesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubtitlesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCaptionsSource(int ResID) {
        track = getSubtitleFile(ResID);

    }

    public static TreeMap<Long, Line> parse(InputStream in) throws IOException {
        return parseSrt(in);
    }

    private static boolean isInteger(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.length() == 1 && Character.digit(s.charAt(i), 10) < 0) return false;
            else continue;
        }
        return true;
    }

    private static long parseSrt(String in) {
        String[] timeSections = in.split(":");
        String[] secondAndMillisecond = timeSections[2].split(",");
        long hours = Long.parseLong(timeSections[0].trim());
        long minutes = Long.parseLong(timeSections[1].trim());
        long seconds = Long.parseLong(secondAndMillisecond[0].trim());
        long millies = Long.parseLong(secondAndMillisecond[1].trim());

        return hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000 + millies;

    }

    public static TreeMap<Long, Line> parseSrt(InputStream is) throws IOException {
        LineNumberReader r = new LineNumberReader(new InputStreamReader(is, "UTF-8"));
        TreeMap<Long, Line> track = new TreeMap<>();
        String lineEntry;
        StringBuilder textStringBuilder = new StringBuilder();
        Line line = null;
        TrackParseState state = TrackParseState.NEW_TRACK;
        int lineNumber = 0;
        while ((lineEntry = r.readLine()) != null) {
            Log.e("tuan : lineEntry", lineEntry.toString());
            lineNumber++;
            if (state == TrackParseState.NEW_TRACK) {
                // Try to parse the cue number.
                if (lineEntry.isEmpty()) {
                    // empty string, move along.
                    continue;
                } else if (isInteger(lineEntry)) {
                    // We've reach a new cue.
                    state = TrackParseState.PARSED_CUE;
                    if (line != null && textStringBuilder.length() > 0) {
                        // Add the previous track.
                        String lineText = textStringBuilder.toString();
                        Log.e("tuan : lineText", lineText.toString());
                        line.setText(lineText.substring(0, lineText.length() - LINE_BREAK.length()));
                        Log.e("tuan : line", line.toString());
                        addTrack(track, line);
                        line = null;
                        textStringBuilder.setLength(0); // reset the string builder
                    }
                    continue;
                } else {
                    if (textStringBuilder.length() > 0) {
                        // Support invalid formats which have line spaces between text.
                        textStringBuilder.append(lineEntry).append(LINE_BREAK);
                        continue;
                    }
                    // Be lenient, just log the error and move along.
                    Log.w(TAG, "No cue number found at line: " + lineNumber);
                }
            }

            if (state == TrackParseState.PARSED_CUE) {
                // Try to parse the time codes.
                String[] times = lineEntry.split("-->");
                if (times.length == 2) {
                    long startTime = parseSrt(times[0]);
                    long endTime = parseSrt(times[1]);
                    line = new Line(startTime, endTime);
                    state = TrackParseState.PARSED_TIME;
                    continue;
                }
                // Handle invalid formats gracefully. Better to have some subtitle than none.
                Log.w(TAG, "No time-code found at line: " + lineNumber);
            }

            if (state == TrackParseState.PARSED_TIME) {
                // Try to parse the text.
                if (!lineEntry.isEmpty()) {
                    textStringBuilder.append(lineEntry).append(LINE_BREAK);
                } else {
                    state = TrackParseState.NEW_TRACK;
                }
            }
        }
        if (line != null && textStringBuilder.length() > 0) {
            // Add the final track.
            String lineText = textStringBuilder.toString();
            line.setText(lineText.substring(0, lineText.length() - LINE_BREAK.length()));
            addTrack(track, line);
        }

        return track;
    }

    private static void addTrack(TreeMap<Long, Line> track, Line line) {
        track.put(line.from, line);
    }

    private enum TrackParseState {
        NEW_TRACK,
        PARSED_CUE,
        PARSED_TIME,
    }

    private TreeMap<Long, Line> getSubtitleFile(int resId) {
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(resId);
            TreeMap<Long, Line> result = parse(inputStream);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void setSubTitlesCallback(ISubTitlesCallback subTitlesCallback) {
        this.iSubTitlesCallback = subTitlesCallback;
    }

    public void getSubtitleUri(Uri path, List<TrainingAudio> trainingAudios, List<ExtKeyValuePair> roles) {
        ArrayList<SubTitles> list = new ArrayList<>();
        try {
            URL url = new URL(path.toString());
            DownloadFile downloader = new DownloadFile(getContext(), new DownloadCallback() {
                @Override
                public void onDownload(File file) {
                    try {
                        track = getSubtitleFile(file.getPath());
                        if (track != null && track.entrySet().size() > 0) {
                            for (Map.Entry<Long, Line> entry : track.entrySet()) {
                                long timeStart = entry.getValue().from;
                                long timeEnd = entry.getValue().to;
                                int colorSub = 0;
                                String text = entry.getValue().text.replace("$author=", "")
                                        .replace("$", ":").replace("<br/>", " ");
                                if (entry.getValue().text.contains("$author=")) {
                                    for (int j = 0; j < roles.size(); j++) {
                                        if (text.split(":")[0].indexOf(roles.get(j).getValue()) != -1) {
                                            colorSub = color[j];
                                            break;
                                        }
                                    }
                                } else {
                                    colorSub = R.color.colorSub;
                                }
                                list.add(new SubTitles(text, timeStart, timeEnd, false, colorSub));
                            }
                            if (iSubTitlesCallback != null) {
                                iSubTitlesCallback.subTitleSuccess(list);
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFail(Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            });
            Log.d(TAG, "url: " + url.toString());
            downloader.execute(url.toString(), "subtitle.srt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void getSubtitleFile(final URL url) {
        DownloadFile downloader = new DownloadFile(getContext(), new DownloadCallback() {
            @Override
            public void onDownload(File file) {

            }

            @Override
            public void onFail(Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });
        Log.d(TAG, "url: " + url.toString());
        downloader.execute(url.toString(), "subtitle.srt");
    }

    private TreeMap<Long, Line> getSubtitleFile(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
            TreeMap<Long, Line> tracks = parse(inputStream);
            return tracks;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void setCaptionsSource(Uri path) {
        if (path == null) {
            track = new TreeMap<>();
            return;
        }
        if (HelperMethods.isRemotePath(path)) {
            try {
                URL url = new URL(path.toString());
                getSubtitleFile(url);
            } catch (MalformedURLException | NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            track = getSubtitleFile(path.toString());
        }

    }


    public static class Line {
        long from;
        long to;
        String text;

        public Line(long from, long to, String text) {
            this.from = from;
            this.to = to;
            this.text = text;
        }

        public Line(long from, long to) {
            this.from = from;
            this.to = to;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Line{" +
                    "from=" + from +
                    ", to=" + to +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

}

