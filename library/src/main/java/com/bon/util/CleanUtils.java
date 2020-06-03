package com.bon.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by dangpp on 8/24/2017.
 */

public final class CleanUtils {
    /**
     * @return
     */
    public static boolean cleanInternalCache() {
        return FileUtils.deleteFilesInDir(ExtUtils.getApp().getCacheDir());
    }

    /**
     * @return
     */
    public static boolean cleanInternalFiles() {
        return FileUtils.deleteFilesInDir(ExtUtils.getApp().getFilesDir());
    }

    /**
     * @return
     */
    public static boolean cleanInternalDbs() {
        return FileUtils.deleteFilesInDir(ExtUtils.getApp().getFilesDir().getParent() + File.separator + "databases");
    }

    /**
     * @param dbName
     * @return
     */
    public static boolean cleanInternalDbByName(final String dbName) {
        return ExtUtils.getApp().deleteDatabase(dbName);
    }

    /**
     * @return
     */
    public static boolean cleanInternalSP() {
        return FileUtils.deleteFilesInDir(ExtUtils.getApp().getFilesDir().getParent() + File.separator + "shared_prefs");
    }

    /**
     * @return
     */
    public static boolean cleanExternalCache() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && FileUtils.deleteFilesInDir(ExtUtils.getApp().getExternalCacheDir());
    }

    /**
     * @param dirPath
     * @return
     */
    public static boolean cleanCustomCache(final String dirPath) {
        return FileUtils.deleteFilesInDir(dirPath);
    }

    /**
     * @param dir
     * @return
     */
    public static boolean cleanCustomCache(final File dir) {
        return FileUtils.deleteFilesInDir(dir);
    }
}