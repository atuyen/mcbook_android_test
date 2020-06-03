package com.bon.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

import com.bon.logger.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {
    private static String TAG = FileUtils.class.getSimpleName();
    public static final String LINE_SEP = System.getProperty("line.separator");

    /**
     * @param context
     * @param folderName
     * @return
     */
    public static File getRootFolderPath(Context context, String folderName) {
        try {
            // check sdcard existed
            if (isSdPresent()) {
                try {
                    File sdPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), folderName);
                    if (!sdPath.exists()) {
                        sdPath.mkdirs();
                    }

                    return sdPath;
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            } else {
                try {
                    File cacheDir = new File(context.getCacheDir(), folderName);
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs();
                    }

                    return cacheDir;
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * get path by path and folder name
     *
     * @param path
     * @param folderName
     * @return
     */
    public static File getFolderPath(String path, String folderName) {
        return createFolder(path, folderName);
    }

    /**
     * @return
     */
    public static boolean isSdPresent() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }

    /**
     * @param path
     * @param folderName
     */
    public static File createFolder(String path, String folderName) {
        try {
            Log.i("Create Folder", "path:: " + path + ", folderName:: " + folderName);
            File folder = new File(path, folderName);
            if (!folder.exists()) {
                folder.mkdirs();
                System.out.println("create folder is successfully!");
            }

            return folder;
        } catch (Exception ex) {
            System.out.println("create folder is fail! " + ex.getMessage());
        }

        return null;
    }

    /**
     * convert file to base64
     *
     * @param file
     * @return
     */
    public static String convertFileToBase64(File file) {
        try {
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[8192];
            int bytesRead = 0;

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }

            output64.close();

            return output.toString();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return "";
    }

    /**
     * get byte[] from path
     *
     * @param path
     * @return
     */
    public static byte[] getByteFromPath(String path) {
        try {
            if (StringUtils.isEmpty(path)) return null;
            File file = new File(path);
            if (!file.exists()) return null;

            byte[] buf = new byte[(int) file.length()];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream fis = new FileInputStream(file);

            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
                Log.i("", "read num bytes: " + readNum);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param filePath
     * @return
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean isFileExists(final String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    /**
     * @param filePath
     * @param newName
     * @return
     */
    public static boolean rename(final String filePath, final String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    /**
     * @param file
     * @param newName
     * @return
     */
    public static boolean rename(final File file, final String newName) {
        if (file == null || !file.exists() || isSpace(newName)) return false;

        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + newName);

        return !newFile.exists() && file.renameTo(newFile);
    }

    /**
     * @param dirPath
     * @return
     */
    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * @param file
     * @return
     */
    public static boolean isDir(final File file) {
        return isFileExists(file) && file.isDirectory();
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean isFile(final String filePath) {
        return isFile(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static boolean isFile(final File file) {
        return isFileExists(file) && file.isFile();
    }

    /**
     * @param dirPath
     * @return
     */
    public static boolean createOrExistsDir(final String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * @param file
     * @return
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;

        try {
            return file.createNewFile();
        } catch (IOException e) {
            Logger.e(TAG, e);
            return false;
        }
    }

    /**
     * @param file
     * @return
     */
    public static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        if (file.exists() && !file.delete()) return false;
        if (!createOrExistsDir(file.getParentFile())) return false;

        try {
            return file.createNewFile();
        } catch (IOException e) {
            Logger.e(TAG, e);
            return false;
        }
    }

    /**
     * @param srcDirPath
     * @param destDirPath
     * @param isMove
     * @return
     */
    private static boolean copyOrMoveDir(final String srcDirPath, final String destDirPath, final boolean isMove) {
        return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove);
    }

    /**
     * @param srcDir
     * @param destDir
     * @param isMove
     * @return
     */
    private static boolean copyOrMoveDir(final File srcDir, final File destDir, final boolean isMove) {
        if (srcDir == null || destDir == null) return false;

        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;

        if (destPath.contains(srcPath)) return false;
        if (!srcDir.exists() || !srcDir.isDirectory()) return false;
        if (!createOrExistsDir(destDir)) return false;

        File[] files = srcDir.listFiles();
        for (File file : files) {
            File oneDestFile = new File(destPath + file.getName());
            if (file.isFile()) {
                if (!copyOrMoveFile(file, oneDestFile, isMove)) return false;
            } else if (file.isDirectory()) {
                if (!copyOrMoveDir(file, oneDestFile, isMove)) return false;
            }
        }

        return !isMove || deleteDir(srcDir);
    }

    /**
     * @param srcFilePath
     * @param destFilePath
     * @param isMove
     * @return
     */
    private static boolean copyOrMoveFile(final String srcFilePath, final String destFilePath, final boolean isMove) {
        return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove);
    }

    /**
     * @param srcFile
     * @param destFile
     * @param isMove
     * @return
     */
    private static boolean copyOrMoveFile(final File srcFile, final File destFile, final boolean isMove) {
        if (srcFile == null || destFile == null) return false;
        if (!srcFile.exists() || !srcFile.isFile()) return false;
        if (destFile.exists() && destFile.isFile()) return false;
        if (!createOrExistsDir(destFile.getParentFile())) return false;

        try {
            return FileIOUtils.writeFileFromIS(destFile, new FileInputStream(srcFile), false) && !(isMove && !deleteFile(srcFile));
        } catch (FileNotFoundException e) {
            Logger.e(TAG, e);
            return false;
        }
    }

    /**
     * @param srcDirPath
     * @param destDirPath
     * @return
     */
    public static boolean copyDir(final String srcDirPath, final String destDirPath) {
        return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    /**
     * @param srcDir
     * @param destDir
     * @return
     */
    public static boolean copyDir(final File srcDir, final File destDir) {
        return copyOrMoveDir(srcDir, destDir, false);
    }

    /**
     * @param srcFilePath
     * @param destFilePath
     * @return
     */
    public static boolean copyFile(final String srcFilePath, final String destFilePath) {
        return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    /**
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean copyFile(final File srcFile, final File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    /**
     * @param srcDirPath
     * @param destDirPath
     * @return
     */
    public static boolean moveDir(final String srcDirPath, final String destDirPath) {
        return moveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    /**
     * @param srcDir
     * @param destDir
     * @return
     */
    public static boolean moveDir(final File srcDir, final File destDir) {
        return copyOrMoveDir(srcDir, destDir, true);
    }

    /**
     * @param srcFilePath
     * @param destFilePath
     * @return
     */
    public static boolean moveFile(final String srcFilePath, final String destFilePath) {
        return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    /**
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean moveFile(final File srcFile, final File destFile) {
        return copyOrMoveFile(srcFile, destFile, true);
    }

    /**
     * @param dirPath
     * @return
     */
    public static boolean deleteDir(final String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    /**
     * @param dir
     * @return
     */
    public static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        if (!dir.exists()) return true;
        if (!dir.isDirectory()) return false;

        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }

        return dir.delete();
    }

    /**
     * @param srcFilePath
     * @return
     */
    public static boolean deleteFile(final String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    /**
     * @param file
     * @return
     */
    public static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * @param dirPath
     * @return
     */
    public static boolean deleteAllInDir(final String dirPath) {
        return deleteAllInDir(getFileByPath(dirPath));
    }

    /**
     * @param dir
     * @return
     */
    public static boolean deleteAllInDir(final File dir) {
        return deleteFilesInDirWithFilter(dir, pathname -> true);
    }

    /**
     * @param dirPath
     * @return
     */
    public static boolean deleteFilesInDir(final String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    /**
     * @param dir
     * @return
     */
    public static boolean deleteFilesInDir(final File dir) {
        return deleteFilesInDirWithFilter(dir, pathname -> pathname.isFile());
    }

    /**
     * @param dirPath
     * @param filter
     * @return
     */
    public static boolean deleteFilesInDirWithFilter(final String dirPath, final FileFilter filter) {
        return deleteFilesInDirWithFilter(getFileByPath(dirPath), filter);
    }

    /**
     * @param dir
     * @param filter
     * @return
     */
    public static boolean deleteFilesInDirWithFilter(final File dir, final FileFilter filter) {
        if (dir == null) return false;
        if (!dir.exists()) return true;
        if (!dir.isDirectory()) return false;

        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file)) {
                    if (file.isFile()) {
                        if (!file.delete()) {
                            return false;
                        }
                    } else if (file.isDirectory()) {
                        if (!deleteDir(file)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * @param dirPath
     * @return
     */
    public static List<File> listFilesInDir(final String dirPath) {
        return listFilesInDir(dirPath, false);
    }

    /**
     * @param dir
     * @return
     */
    public static List<File> listFilesInDir(final File dir) {
        return listFilesInDir(dir, false);
    }

    /**
     * @param dirPath
     * @param isRecursive
     * @return
     */
    public static List<File> listFilesInDir(final String dirPath, final boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    /**
     * @param dir
     * @param isRecursive
     * @return
     */
    public static List<File> listFilesInDir(final File dir, final boolean isRecursive) {
        return listFilesInDirWithFilter(dir, pathname -> true, isRecursive);
    }

    /**
     * @param dirPath
     * @param filter
     * @return
     */
    public static List<File> listFilesInDirWithFilter(final String dirPath,
                                                      final FileFilter filter) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, false);
    }

    /**
     * @param dir
     * @param filter
     * @return
     */
    public static List<File> listFilesInDirWithFilter(final File dir,
                                                      final FileFilter filter) {
        return listFilesInDirWithFilter(dir, filter, false);
    }

    /**
     * @param dirPath
     * @param filter
     * @param isRecursive
     * @return
     */
    public static List<File> listFilesInDirWithFilter(final String dirPath,
                                                      final FileFilter filter,
                                                      final boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
    }

    /**
     * @param dir
     * @param filter
     * @param isRecursive
     * @return
     */
    public static List<File> listFilesInDirWithFilter(final File dir,
                                                      final FileFilter filter,
                                                      final boolean isRecursive) {
        if (!isDir(dir)) return null;

        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file)) {
                    list.add(file);
                }

                if (isRecursive && file.isDirectory()) {
                    //noinspection ConstantConditions
                    list.addAll(listFilesInDirWithFilter(file, filter, true));
                }
            }
        }

        return list;
    }

    /**
     * @param filePath
     * @return
     */
    public static long getFileLastModified(final String filePath) {
        return getFileLastModified(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static long getFileLastModified(final File file) {
        if (file == null) return -1;
        return file.lastModified();
    }

    /**
     * @param filePath
     * @return
     */
    public static String getFileCharsetSimple(final String filePath) {
        return getFileCharsetSimple(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static String getFileCharsetSimple(final File file) {
        int p = 0;
        InputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        } catch (IOException e) {
            Logger.e(TAG, e);
        } finally {
            CloseUtils.closeIO(is);
        }

        switch (p) {
            case 0xefbb:
                return "UTF-8";
            case 0xfffe:
                return "Unicode";
            case 0xfeff:
                return "UTF-16BE";
            default:
                return "GBK";
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static int getFileLines(final String filePath) {
        return getFileLines(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static int getFileLines(final File file) {
        int count = 1;
        InputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int readChars;

            if (LINE_SEP.endsWith("\n")) {
                while ((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for (int i = 0; i < readChars; ++i) {
                        if (buffer[i] == '\n') {
                            ++count;
                        }
                    }
                }
            } else {
                while ((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for (int i = 0; i < readChars; ++i) {
                        if (buffer[i] == '\r') {
                            ++count;
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger.e(TAG, e);
        } finally {
            CloseUtils.closeIO(is);
        }

        return count;
    }

    /**
     * @param dirPath
     * @return
     */
    public static String getDirSize(final String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }

    /**
     * @param dir
     * @return
     */
    public static String getDirSize(final File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    /**
     * @param filePath
     * @return
     */
    public static String getFileSize(final String filePath) {
        return getFileSize(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static String getFileSize(final File file) {
        long len = getFileLength(file);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    /**
     * @param dirPath
     * @return
     */
    public static long getDirLength(final String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }

    /**
     * @param dir
     * @return
     */
    public static long getDirLength(final File dir) {
        if (!isDir(dir)) return -1;

        long len = 0;
        File[] files = dir.listFiles();

        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    len += getDirLength(file);
                } else {
                    len += file.length();
                }
            }
        }

        return len;
    }

    /**
     * @param filePath
     * @return
     */
    public static long getFileLength(final String filePath) {
        return getFileLength(getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static long getFileLength(final File file) {
        if (!isFile(file)) return -1;
        return file.length();
    }

    /**
     * @param file
     * @return
     */
    public static String getDirName(final File file) {
        if (file == null) return null;
        return getDirName(file.getPath());
    }

    /**
     * @param filePath
     * @return
     */
    public static String getDirName(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }

    /**
     * @param file
     * @return
     */
    public static String getFileName(final File file) {
        if (file == null) return null;
        return getFileName(file.getPath());
    }

    /**
     * @param filePath
     * @return
     */
    public static String getFileName(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    /**
     * @param file
     * @return
     */
    public static String getFileNameNoExtension(final File file) {
        if (file == null) return null;
        return getFileNameNoExtension(file.getPath());
    }

    /**
     * @param filePath
     * @return
     */
    public static String getFileNameNoExtension(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    /**
     * @param file
     * @return
     */
    public static String getFileExtension(final File file) {
        if (file == null) return null;
        return getFileExtension(file.getPath());
    }

    /**
     * @param filePath
     * @return
     */
    public static String getFileExtension(final String filePath) {
        if (isSpace(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    @SuppressLint("DefaultLocale")
    public static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format("%.3fB", (double) byteNum + 0.0005);
        } else if (byteNum < 1048576) {
            return String.format("%.3fKB", (double) byteNum / 1024 + 0.0005);
        } else if (byteNum < 1073741824) {
            return String.format("%.3fMB", (double) byteNum / 1048576 + 0.0005);
        } else {
            return String.format("%.3fGB", (double) byteNum / 1073741824 + 0.0005);
        }
    }

    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
