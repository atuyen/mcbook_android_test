package com.bon.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;
import android.util.Log;

import com.bon.logger.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dang on 10/14/2015.
 */
public class DateTimeUtils {
    private static final String TAG = DateTimeUtils.class.getSimpleName();

    /**
     * calendar no time
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    public static Calendar getCalendarNoTime(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        return calendar;
    }

    /**
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hour
     * @param minute
     * @return
     */
    public static Calendar getCalendarTime(int year, int month, int dayOfMonth, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, dayOfMonth, hour, minute, 0);
        return calendar;
    }

    /**
     * convert millisecond to calendar
     *
     * @param number
     * @return
     */
    public static Calendar convertTimeStampToDateTime(float number) {
        try {
            if (number <= 0) return null;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) number);
            return calendar;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * convert calendar to string
     *
     * @param calendar
     * @param pattern
     * @return
     */
    public static String convertCalendarToString(Calendar calendar, String pattern) {
        try {
            if (calendar == null) return "";
            return convertDateToString(calendar.getTime(), pattern);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(Date date, String pattern) {
        try {
            if (date == null) return "";

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(date);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return "";
    }

    /**
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * @param timeStart
     * @param timeEnd
     * @return
     */
    public static long convertDay(String timeStart, String timeEnd) {
        long diffDays = 0;
        try {
            Date date1 = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(timeStart.replaceAll("Z$", "+0000"));
            String dateTime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
            Date dateDay1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateTime1);
            Date date2 = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(timeEnd.replaceAll("Z$", "+0000"));
            String dateTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2);
            Date dateDay2 = new SimpleDateFormat("yyyy-MM-dd").parse(dateTime2);
            long difference = dateDay2.getTime() - dateDay1.getTime();
            diffDays = difference / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diffDays;
    }

    /**
     * @param datetime
     * @return
     */
    public static String convertDateTime(String datetime) {
        SimpleDateFormat destFormat = null;
        Date convertedDate = null;
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sourceFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            convertedDate = sourceFormat.parse(datetime);

            destFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateTime = destFormat.format(convertedDate);
        return dateTime;
    }

    /**
     * @param datetime
     * @return
     */
    public static String convertDateTimeUpdate(String datetime) {
        SimpleDateFormat destFormat = null;
        Date convertedDate = null;
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sourceFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            convertedDate = sourceFormat.parse(datetime);

            destFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return destFormat.format(convertedDate);
    }


    /**
     * @param date
     * @return
     */
    public static String getCalendarConvert(String date) {
        String datetime = DateTimeUtils.convertDateTime(date);
        String[] dateTimeformat = datetime.split(" ");
        String dateConvert = dateTimeformat[0].trim();
        return dateConvert;
    }

    /**
     * @param time
     * @return
     */
    public static String getTimeConvert(String time) {
        String datetime = DateTimeUtils.convertDateTime(time);
        String[] dateTimeformat = datetime.split(" ");
        String timeConvert = dateTimeformat[1].trim();
        return timeConvert;
    }

    /**
     * @param time
     * @return
     */
    public static String getHourFormat(String time) {
        String format = "";
        String datetime = DateTimeUtils.convertDateTime(time);
        String[] dateTimeformat = datetime.split(" ");
        String timeConvert = dateTimeformat[1].trim();
        String[] hourFormat = timeConvert.split(":");
        String hour = hourFormat[0].trim();
        int h = Integer.parseInt(hour);
        if (h >= 12) {
            format = "pm";
        } else if (h < 12) {
            format = "am";
        }
        return format;
    }

    /**
     * @param timeStamp
     * @param format
     * @return
     */

    public static String getDateTime(Long timeStamp, String format) {
        try {
            DateFormat sdf = new SimpleDateFormat(format, Locale.US);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ignored) {
            return "";
        }
    }

    static Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ExtUtils.getApp().getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return ExtUtils.getApp().getResources().getConfiguration().locale;
        }
    }

    /**
     * @param timeRecord
     * @return
     */
    public static String getTimeAgosRecord(long timeRecord) {
        try {
            String result = "";
            long miliTimes = timeRecord;
            Date now = new Date();
            if (now.getTime() >= miliTimes) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - miliTimes);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - miliTimes);
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - miliTimes);
                long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - miliTimes);
                if (seconds >= 0 && seconds < 59) {
                    result = "Vừa xong";
                } else if (minutes < 60) {
                    result = minutes + " phút trước";
                } else if (hours < 24) {
                    result = hours + " giờ trước";
                } else {
                    if (days == 1) {
                        result = "Hôm qua " + getDateTime(miliTimes, "HH:mm");
                    } else {
                        result = getDateTime(miliTimes, "dd/MM");
                    }
                }
            }
            return result;
        } catch (Exception j) {
            j.printStackTrace();
        }
        return null;
    }

    /**
     * @param time
     * @return
     */
    public static String convertLongtoDate(long time) {
        try {
            String dateTime = "";
            Date date = new Date(time);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return dateTime = df.format(date);
        } catch (Exception j) {
            j.printStackTrace();
        }
        return null;
    }

    /**
     * @param dtStart
     * @return
     */

    public static String getTimeAgos(String dtStart) {
        try {
            String result = "";
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sourceFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = sourceFormat.parse(dtStart);
            long miliTimes = date.getTime();
            Date now = new Date();
            if (now.getTime() >= miliTimes) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - miliTimes);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - miliTimes);
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - miliTimes);
                long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - miliTimes);
                if (seconds >= 0 && seconds < 59) {
                    result = "Ngay bây giờ";
                } else if (minutes < 60) {
                    result = minutes + " phút trước";
                } else if (hours < 24) {
                    result = hours + " giờ trước";
                } else {
                    if (days == 1) {
                        result = "Hôm qua " + getDateTime(miliTimes, "HH:mm");
                    } else {
                        result = getDateTime(miliTimes, "dd/MM");
                    }
                }
            }
            return result;
        } catch (Exception j) {
            j.printStackTrace();
        }
        return null;
    }

    public static String getStringDatetime() {
        return new SimpleDateFormat("dd_MM_yy_").format(new Date());
    }
}

