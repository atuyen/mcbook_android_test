package com.bon.util;

import com.bon.logger.Logger;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dang on 6/8/2016.
 */
public class StreamUtils {
    private static final String TAG = StreamUtils.class.getSimpleName();

    /**
     * @param is
     * @param os
     */
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}
