package com.bon.util;

import com.bon.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangpp on 8/24/2017.
 */

public final class FileIOUtils {
    private static final String TAG = FileIOUtils.class.getSimpleName();

    // buffer size
    private static int sBufferSize = 8192;

    /**
     * @param filePath
     * @param is
     * @return
     */
    public static boolean writeFileFromIS(final String filePath, final InputStream is) {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), is, false);
    }

    /**
     * @param filePath
     * @param is
     * @param append
     * @return
     */
    public static boolean writeFileFromIS(final String filePath, final InputStream is, final boolean append) {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), is, append);
    }

    /**
     * @param file
     * @param is
     * @return
     */
    public static boolean writeFileFromIS(final File file, final InputStream is) {
        return writeFileFromIS(file, is, false);
    }

    /**
     * @param file
     * @param is
     * @param append
     * @return
     */
    public static boolean writeFileFromIS(final File file, final InputStream is, final boolean append) {
        if (!FileUtils.createOrExistsFile(file) || is == null) return false;
        OutputStream os = null;

        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[sBufferSize];

            int len = 0;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
            }

            return true;
        } catch (IOException e) {
            Logger.e(TAG, e);
            return false;
        } finally {
            CloseUtils.closeIO(is, os);
        }
    }

    /**
     * @param filePath
     * @param bytes
     * @return
     */
    public static boolean writeFileFromBytesByStream(final String filePath, final byte[] bytes) {
        return writeFileFromBytesByStream(FileUtils.getFileByPath(filePath), bytes, false);
    }

    /**
     * @param filePath
     * @param bytes
     * @param append
     * @return
     */
    public static boolean writeFileFromBytesByStream(final String filePath, final byte[] bytes, final boolean append) {
        return writeFileFromBytesByStream(FileUtils.getFileByPath(filePath), bytes, append);
    }

    /**
     * @param file
     * @param bytes
     * @return
     */
    public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes) {
        return writeFileFromBytesByStream(file, bytes, false);
    }

    /**
     * @param file
     * @param bytes
     * @param append
     * @return
     */
    public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes, final boolean append) {
        if (bytes == null || !FileUtils.createOrExistsFile(file)) return false;

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(bytes);

            return true;
        } catch (IOException e) {
            Logger.e(TAG, e);
            return false;
        } finally {
            CloseUtils.closeIO(bos);
        }
    }

    /**
     * @param filePath
     * @param bytes
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByChannel(final String filePath, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByChannel(FileUtils.getFileByPath(filePath), bytes, false, isForce);
    }

    /**
     * @param filePath
     * @param bytes
     * @param append
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByChannel(final String filePath, final byte[] bytes, final boolean append, final boolean isForce) {
        return writeFileFromBytesByChannel(FileUtils.getFileByPath(filePath), bytes, append, isForce);
    }

    /**
     * @param file
     * @param bytes
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByChannel(final File file, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByChannel(file, bytes, false, isForce);
    }

    /**
     * @param file
     * @param bytes
     * @param append
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByChannel(final File file, final byte[] bytes, final boolean append, final boolean isForce) {
        if (bytes == null) return false;

        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(bytes));

            if (isForce) {
                fc.force(true);
            }

            return true;
        } catch (IOException e) {
            Logger.e(TAG, e);
            return false;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }

    /**
     * @param filePath
     * @param bytes
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByMap(final String filePath, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByMap(filePath, bytes, false, isForce);
    }

    /**
     * @param filePath
     * @param bytes
     * @param append
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByMap(final String filePath, final byte[] bytes, final boolean append, final boolean isForce) {
        return writeFileFromBytesByMap(FileUtils.getFileByPath(filePath), bytes, append, isForce);
    }

    /**
     * @param file
     * @param bytes
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByMap(final File file, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByMap(file, bytes, false, isForce);
    }

    /**
     * @param file
     * @param bytes
     * @param append
     * @param isForce
     * @return
     */
    public static boolean writeFileFromBytesByMap(final File file, final byte[] bytes, final boolean append, final boolean isForce) {
        if (bytes == null || !FileUtils.createOrExistsFile(file)) return false;

        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.length);
            mbb.put(bytes);

            if (isForce) {
                mbb.force();
            }

            return true;
        } catch (IOException e) {
            Logger.e(TAG, e);
            return false;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }

    /**
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFileFromString(final String filePath, final String content) {
        return writeFileFromString(FileUtils.getFileByPath(filePath), content, false);
    }

    /**
     * @param filePath
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFileFromString(final String filePath, final String content, final boolean append) {
        return writeFileFromString(FileUtils.getFileByPath(filePath), content, append);
    }

    /**
     * @param file
     * @param content
     * @return
     */
    public static boolean writeFileFromString(final File file, final String content) {
        return writeFileFromString(file, content, false);
    }

    /**
     * @param file
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFileFromString(final File file, final String content, final boolean append) {
        if (file == null || content == null) return false;
        if (!FileUtils.createOrExistsFile(file)) return false;

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);

            return true;
        } catch (IOException e) {
            Logger.e(TAG, e);
            return false;
        } finally {
            CloseUtils.closeIO(bw);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // the divide line of write and read
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param filePath
     * @return
     */
    public static List<String> readFile2List(final String filePath) {
        return readFile2List(FileUtils.getFileByPath(filePath), null);
    }

    /**
     * @param filePath
     * @param charsetName
     * @return
     */
    public static List<String> readFile2List(final String filePath, final String charsetName) {
        return readFile2List(FileUtils.getFileByPath(filePath), charsetName);
    }

    /**
     * @param file
     * @return
     */
    public static List<String> readFile2List(final File file) {
        return readFile2List(file, 0, 0x7FFFFFFF, null);
    }

    /**
     * @param file
     * @param charsetName
     * @return
     */
    public static List<String> readFile2List(final File file, final String charsetName) {
        return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
    }

    /**
     * @param filePath
     * @param st
     * @param end
     * @return
     */
    public static List<String> readFile2List(final String filePath, final int st, final int end) {
        return readFile2List(FileUtils.getFileByPath(filePath), st, end, null);
    }

    /**
     * @param filePath
     * @param st
     * @param end
     * @param charsetName
     * @return
     */
    public static List<String> readFile2List(final String filePath, final int st, final int end, final String charsetName) {
        return readFile2List(FileUtils.getFileByPath(filePath), st, end, charsetName);
    }

    /**
     * @param file
     * @param st
     * @param end
     * @return
     */
    public static List<String> readFile2List(final File file, final int st, final int end) {
        return readFile2List(file, st, end, null);
    }

    /**
     * @param file
     * @param st
     * @param end
     * @param charsetName
     * @return
     */
    public static List<String> readFile2List(final File file, final int st, final int end, final String charsetName) {
        if (!FileUtils.isFileExists(file)) return null;
        if (st > end) return null;

        BufferedReader reader = null;
        try {
            String line;
            int curLine = 1;
            List<String> list = new ArrayList<>();

            if (FileUtils.isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }

            while ((line = reader.readLine()) != null) {
                if (curLine > end) break;
                if (st <= curLine && curLine <= end) list.add(line);
                ++curLine;
            }

            return list;
        } catch (IOException e) {
            Logger.e(TAG, e);
            return null;
        } finally {
            CloseUtils.closeIO(reader);
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static String readFile2String(final String filePath) {
        return readFile2String(FileUtils.getFileByPath(filePath), null);
    }

    /**
     * @param filePath
     * @param charsetName
     * @return
     */
    public static String readFile2String(final String filePath, final String charsetName) {
        return readFile2String(FileUtils.getFileByPath(filePath), charsetName);
    }

    /**
     * @param file
     * @return
     */
    public static String readFile2String(final File file) {
        return readFile2String(file, null);
    }

    /**
     * @param file
     * @param charsetName
     * @return
     */
    public static String readFile2String(final File file, final String charsetName) {
        if (!FileUtils.isFileExists(file)) return null;
        BufferedReader reader = null;

        try {
            StringBuilder sb = new StringBuilder();
            if (FileUtils.isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }

            String line;
            if ((line = reader.readLine()) != null) {
                sb.append(line);
                while ((line = reader.readLine()) != null) {
                    sb.append(FileUtils.LINE_SEP).append(line);
                }
            }

            return sb.toString();
        } catch (IOException e) {
            Logger.e(TAG, e);
            return null;
        } finally {
            CloseUtils.closeIO(reader);
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static byte[] readFile2BytesByStream(final String filePath) {
        return readFile2BytesByStream(FileUtils.getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static byte[] readFile2BytesByStream(final File file) {
        if (!FileUtils.isFileExists(file)) return null;

        FileInputStream fis = null;
        ByteArrayOutputStream os = null;
        try {
            fis = new FileInputStream(file);
            os = new ByteArrayOutputStream();
            byte[] b = new byte[sBufferSize];

            int len;
            while ((len = fis.read(b, 0, sBufferSize)) != -1) {
                os.write(b, 0, len);
            }

            return os.toByteArray();
        } catch (IOException e) {
            Logger.e(TAG, e);
            return null;
        } finally {
            CloseUtils.closeIO(fis, os);
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static byte[] readFile2BytesByChannel(final String filePath) {
        return readFile2BytesByChannel(FileUtils.getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static byte[] readFile2BytesByChannel(final File file) {
        if (!FileUtils.isFileExists(file)) return null;

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());

            while (true) {
                if (!((fc.read(byteBuffer)) > 0)) break;
            }

            return byteBuffer.array();
        } catch (IOException e) {
            Logger.e(TAG, e);
            return null;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static byte[] readFile2BytesByMap(final String filePath) {
        return readFile2BytesByMap(FileUtils.getFileByPath(filePath));
    }

    /**
     * @param file
     * @return
     */
    public static byte[] readFile2BytesByMap(final File file) {
        if (!FileUtils.isFileExists(file)) return null;

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            int size = (int) fc.size();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
            byte[] result = new byte[size];
            mbb.get(result, 0, size);

            return result;
        } catch (IOException e) {
            Logger.e(TAG, e);
            return null;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }

    /**
     * @param bufferSize
     */
    public static void setBufferSize(final int bufferSize) {
        sBufferSize = bufferSize;
    }
}