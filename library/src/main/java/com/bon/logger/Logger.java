package com.bon.logger;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bon.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A logger that uses the standard Android Log class to log exceptions, and also
 * logs them to a file on the device. Requires permission WRITE_EXTERNAL_STORAGE
 * in AndroidManifest.xml.
 */
public class Logger {
    private static final String tagDefault = Logger.class.getSimpleName();
    private static boolean isEnableLog = true;
    private static String path = "";

    @SuppressLint("SdCardPath")
    private static String dirPath = "/mnt/sdcard/logger";
    private static String name = "logger";
    private static String suffix = "log";

    /**
     * get error message from exception
     *
     * @param tag
     * @param msg
     * @return
     */
    public static String getMessageError(String tag, String msg) {
        // The log will be shown in log cat.
        StringBuilder bufferLog = new StringBuilder();

        try {
            Throwable throwable = new Throwable().fillInStackTrace();
            if (throwable != null && throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
                StackTraceElement caller = throwable.getStackTrace()[2];
                if (caller != null) {
                    bufferLog.append(caller.getClassName());
                    bufferLog.append(".");
                    bufferLog.append(caller.getMethodName());
                    bufferLog.append("( ");
                    bufferLog.append(caller.getFileName());
                    bufferLog.append(": ");
                    bufferLog.append(caller.getLineNumber());
                    bufferLog.append(")");
                    bufferLog.append(" : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // append tag
        bufferLog.append(tag + "    ");

        // append message
        bufferLog.append(msg);

        return bufferLog.toString();
    }

    /**
     * Building Message
     *
     * @param msg The message you would like logged.
     * @return Message String
     */
    private static String buildMessage(TypeLog typeLog, String tag, String msg) {
        try {
            String bufferLog = getMessageError(tag, msg);

            // The log will be written in the log file.
            if (isEnableLog) {
                String fileLog = typeLog.name() + "    " + bufferLog;
                LogFile.logToFile(path, fileLog);
            }

            return bufferLog;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * get message from exception
     *
     * @param e
     * @return
     */
    private static String getMessageException(Exception e) {
        try {
            if (e == null) return "Exception is null";
            e.printStackTrace();
            return e.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    /**
     * Send a DEBUG log message.
     */
    public static void d(String msg) {
        try {
            if (isEnableLog) {
                Log.d(tagDefault, buildMessage(TypeLog.DEBUG, tagDefault, msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a DEBUG log message.
     *
     * @param ex
     */
    public static void d(Exception ex) {
        try {
            if (isEnableLog) {
                Log.d(tagDefault, buildMessage(TypeLog.DEBUG, tagDefault, getMessageException(ex)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    d(msg);
                } else {
                    Log.d(tag, buildMessage(TypeLog.DEBUG, tag, msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag
     * @param ex
     */
    public static void d(String tag, Exception ex) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    d(ex);
                } else {
                    Log.d(tag, buildMessage(TypeLog.DEBUG, tag, getMessageException(ex)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send an ERROR log message.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
        try {
            if (isEnableLog) {
                Log.e(tagDefault, buildMessage(TypeLog.ERROR, tagDefault, msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send an ERROR log message.
     *
     * @param ex
     */
    public static void e(Exception ex) {
        try {
            if (isEnableLog) {
                Log.e(tagDefault, buildMessage(TypeLog.ERROR, tagDefault, getMessageException(ex)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a ERROR log message.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    e(msg);
                } else {
                    Log.e(tag, buildMessage(TypeLog.ERROR, tag, msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a ERROR log message.
     *
     * @param tag
     * @param ex
     */
    public static void e(String tag, Exception ex) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    e(ex);
                } else {
                    Log.e(tag, buildMessage(TypeLog.ERROR, tag, getMessageException(ex)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a INFO log message.
     *
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    i(msg);
                } else {
                    Log.i(tag, buildMessage(TypeLog.INFO, tag, msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a INFO log message.
     *
     * @param tag
     * @param ex
     */
    public static void i(String tag, Exception ex) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    i(ex);
                } else {
                    Log.i(tag, buildMessage(TypeLog.INFO, tag, getMessageException(ex)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send an INFO log message.
     *
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
        try {
            if (isEnableLog) {
                Log.i(tagDefault, buildMessage(TypeLog.INFO, tagDefault, msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send an INFO log message.
     *
     * @param ex
     */
    public static void i(Exception ex) {
        try {
            if (isEnableLog) {
                Log.i(tagDefault, buildMessage(TypeLog.INFO, tagDefault, getMessageException(ex)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    v(msg);
                } else {
                    Log.v(tag, buildMessage(TypeLog.VERBOSE, tag, msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag
     * @param ex
     */
    public static void v(String tag, Exception ex) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    v(ex);
                } else {
                    Log.v(tag, buildMessage(TypeLog.VERBOSE, tag, getMessageException(ex)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg The message you would like logged.
     */
    public static void v(String msg) {
        try {
            if (isEnableLog) {
                Log.v(tagDefault, buildMessage(TypeLog.VERBOSE, tagDefault, msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param ex
     */
    public static void v(Exception ex) {
        try {
            if (isEnableLog) {
                Log.v(tagDefault, buildMessage(TypeLog.VERBOSE, tagDefault, getMessageException(ex)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a WARN log message.
     *
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    w(msg);
                } else {
                    Log.w(tag, buildMessage(TypeLog.WARN, tag, msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a WARN log message.
     *
     * @param tag
     * @param ex
     */
    public static void w(String tag, Exception ex) {
        try {
            if (isEnableLog) {
                if (StringUtils.isEmpty(tag)) {
                    w(ex);
                } else {
                    Log.w(tag, buildMessage(TypeLog.WARN, tag, getMessageException(ex)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a WARN log message
     *
     * @param msg The message you would like logged.
     */
    public static void w(String msg) {
        try {
            if (isEnableLog) {
                Log.w(tagDefault, buildMessage(TypeLog.WARN, tagDefault, msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a WARN log message
     *
     * @param ex
     */
    public static void w(Exception ex) {
        try {
            if (isEnableLog) {
                Log.w(tagDefault, buildMessage(TypeLog.WARN, tagDefault, getMessageException(ex)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * enable or disable the log
     *
     * @param isEnableLog whether to enable the log
     */
    public static void setEnableLog(boolean isEnableLog) {
        Logger.isEnableLog = isEnableLog;
    }

    /**
     * @param path
     */
    public static void setPathSaveLog(String path) {
        try {
            Logger.path = path;
            // create folder to storage log file
            createLogDir(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set the log file path The log file path will be: dirPath +
     * <p>
     * name + Formatted time +suffix
     *
     * @param dirPath the log file dir path,such as "/mnt/sdcard/dzanglogger"
     * @param name    the log file base file name, such as "log"
     * @param suffix  the log file suffix, such as "log"
     */
    @SuppressLint("SimpleDateFormat")
    public static void setPathSaveLog(String dirPath, String name,
                                      String suffix) {
        try {
            Logger.dirPath = dirPath;
            Logger.name = name;
            Logger.suffix = suffix;

            // day error
            SimpleDateFormat fdf = new SimpleDateFormat("yyyy-MM-dd");
            String myDateString = fdf.format(new Date());

            // file name
            String buffer = name + "-" + myDateString + "." + suffix;
            File file = new File(dirPath, buffer);
            setPathSaveLog(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create the Directory from the path
     *
     * @param path
     */
    private static void createLogDir(String path) {
        try {
            if (isEnableLog) {
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    if (!file.getParentFile().mkdirs()) {
                        Log.e(tagDefault, "The \"Log Dir\" can not be created!");
                    } else {
                        Log.i(tagDefault, "The \"Log Dir\" was successfully created! -" + file.getParent());
                    }
                }
            } else {
                Log.i(tagDefault, "The \"Log Dir\" was not allow be created!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * type of log
     */
    private enum TypeLog {
        INFO, DEBUG, VERBOSE, WARN, ERROR
    }
}