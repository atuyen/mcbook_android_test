package com.bon.logger;

import android.text.TextUtils;
import android.util.Log;

import com.bon.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogFile {
    private static final String TAG = LogFile.class.getSimpleName();
    private static ExecutorService executor = null;

    /**
     * log string to file on sdcard
     *
     * @param path
     * @param msg
     */
    protected static void logToFile(final String path, final String msg) {
        try {
            if (StringUtils.isEmpty(path)) return;
            if (LogFile.executor == null) {
                LogFile.executor = Executors.newSingleThreadExecutor();
            }

            // execute log file
            LogFile.executor.execute(() -> {
                PrintWriter out = null;
                try {
                    String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(new Date());
                    File file = getFileFromPath(path);
                    if (file != null && file.exists()) {
                        out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                        out.println(time + " \n " + msg + " \n");
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) out.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get File form the file path.<BR>
     * if the file does not exist, create it and return it.
     *
     * @param path the file path
     * @return the file
     */
    private static File getFileFromPath(String path) {
        try {
            if (TextUtils.isEmpty(path)) return null;

            File file = new File(path);
            if (file.exists()) {
                if (!file.canWrite()) {
                    Log.e(TAG, "The \"log file\" can not be written.");
                    return null;
                }
            } else {
                // create the log file
                try {
                    if (file.createNewFile()) {
                        Log.i(TAG, "The \"log file\" was successfully created! -" + file.getAbsolutePath());
                    } else {
                        Log.i(TAG, "The \"log file\" exist! -" + file.getAbsolutePath());
                    }

                    if (!file.canWrite()) {
                        Log.e(TAG, "The \"log file\" can not be written.");
                        return null;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Failed to create The \"log file\".");
                    e.printStackTrace();
                }
            }

            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
