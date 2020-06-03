package com.bon.util;

import com.bon.logger.Logger;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by dangpp on 8/24/2017.
 */

public final class CloseUtils {
    private static final String TAG = CloseUtils.class.getSimpleName();

    /**
     * @param closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    Logger.e(TAG, e);
                }
            }
        }
    }

    /**
     * @param closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    Logger.e(TAG, e);
                }
            }
        }
    }
}