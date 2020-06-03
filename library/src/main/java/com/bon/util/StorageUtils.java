package com.bon.util;

import android.os.Environment;
import android.util.Log;

import com.bon.logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

@SuppressWarnings("ALL")
public class StorageUtils {
    private static final String TAG = "StorageUtils";

    /**
     * display information of hardware device
     */
    public static void displayInformation() {
        try {
            List<StorageInfo> lstStorageInfo = getStorageList();
            if (lstStorageInfo != null && lstStorageInfo.size() > 0) {
                for (StorageInfo storageInfo : lstStorageInfo) {
                    System.out.println("storageInfo:: " + storageInfo.getDisplayName());
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * get path internal local storage
     *
     * @return path internal
     */
    public static String getPathInternalStorage() {
        try {
            List<StorageInfo> storageInfoEntities = getStorageList();
            if (storageInfoEntities == null || storageInfoEntities.size() <= 0) return null;

            String path = "";
            for (StorageInfo storageInfoEntity : storageInfoEntities) {
                if (storageInfoEntity.internal && !StringUtils.isEmpty(storageInfoEntity.path) && !storageInfoEntity.readonly) {
                    path = storageInfoEntity.path;
                    break;
                }
            }

            return path;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * get path external local storage
     *
     * @return
     */
    public static String getPathExternalStorage() {
        try {
            List<StorageInfo> storageInfoEntities = getStorageList();
            if (storageInfoEntities == null || storageInfoEntities.size() <= 0) return null;

            String path = "";
            for (StorageInfo storageInfoEntity : storageInfoEntities) {
                if (!storageInfoEntity.internal && !StringUtils.isEmpty(storageInfoEntity.path) && !storageInfoEntity.readonly) {
                    path = storageInfoEntity.path;
                    break;
                }
            }

            return path;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param path
     */
    @SuppressWarnings("unused")
    private static void setPermission(String path, String permission) {
        try {
            System.out.println("path: " + path);
            Runtime.getRuntime().exec("mount -o remount,rw /");
            Runtime.getRuntime().exec("chmod " + permission + " " + path);
            Runtime.getRuntime().exec("mount -o remount,ro /");
        } catch (IOException e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * get list local storage
     *
     * @return
     */
    public static List<StorageInfo> getStorageList() {
        List<StorageInfo> storageInfoEntities = new ArrayList<>();
        String defPath = Environment.getExternalStorageDirectory().getPath();

        boolean defPathInternal = !Environment.isExternalStorageRemovable();
        String defPathState = Environment.getExternalStorageState();
        boolean defPathAvailable = defPathState.equals(Environment.MEDIA_MOUNTED) || defPathState.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean defPathReadonly = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);

        BufferedReader bufReader = null;
        try {
            HashSet<String> paths = new HashSet<>();
            bufReader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            int curDisplayNumber = 1;
            Log.d(TAG, "/proc/mounts");

            while ((line = bufReader.readLine()) != null) {
                Log.d(TAG, line);
                if (line.contains("vfat") || line.contains("/mnt")) {
                    StringTokenizer tokens = new StringTokenizer(line, " ");
                    @SuppressWarnings("unused")
                    String unused = tokens.nextToken(); // device
                    String mountPoint = tokens.nextToken(); // mount point
                    if (paths.contains(mountPoint)) continue;

                    unused = tokens.nextToken(); // file system
                    List<String> flags = Arrays.asList(tokens.nextToken().split(",")); // flags
                    boolean readonly = flags.contains("ro");

                    if (mountPoint.equals(defPath)) {
                        paths.add(defPath);
                        storageInfoEntities.add(0, new StorageInfo(defPath, defPathInternal, readonly, -1));
                    } else if (line.contains("/dev/block/vold")) {
                        if (!line.contains("/mnt/secure")
                                && !line.contains("/mnt/asec")
                                && !line.contains("/mnt/obb")
                                && !line.contains("/dev/mapper")
                                && !line.contains("tmpfs")) {
                            paths.add(mountPoint);
                            storageInfoEntities.add(new StorageInfo(mountPoint,
                                    false, readonly, curDisplayNumber++));
                        }
                    }
                }
            }

            if (!paths.contains(defPath) && defPathAvailable) {
                storageInfoEntities.add(0, new StorageInfo(defPath,
                        defPathInternal, defPathReadonly, -1));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return storageInfoEntities;
    }

    /**
     * Storage property
     */
    public static class StorageInfo {
        private final String path;
        private final boolean internal;
        private final boolean readonly;
        private final int displayNumber;

        StorageInfo(String path, boolean internal, boolean readonly,
                    int displayNumber) {
            this.path = path;
            this.internal = internal;
            this.readonly = readonly;
            this.displayNumber = displayNumber;
        }

        public String getDisplayName() {
            StringBuilder res = new StringBuilder();

            try {
                if (internal) {
                    res.append("\nInternal SD card // ").append(this.path);
                } else if (displayNumber > 1) {
                    res.append("\nSD card ").append(displayNumber).append(" // ").append(this.path);
                } else {
                    res.append("\nSD card // ").append(this.path);
                }

                if (readonly) res.append("\n(Read only)");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res.toString();
        }
    }
}