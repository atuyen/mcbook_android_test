package com.mc.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bon.logger.Logger;
import com.bon.util.DateTimeUtils;

import com.bon.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.models.home.ItemTrainingResponse;
import com.mc.models.home.RecordItem;
import com.mc.utilities.shadow.ShadowProperty;
import com.mc.utilities.shadow.ShadowViewDrawable;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.HttpException;

import static com.mc.utilities.Constant.IMAGE_JPG;

public class AppUtils {
    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Throwable throwable) {
            // igonre
        }
        return 0;
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static Calendar getMinCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -120);
        return calendar;
    }

    public static void setShadow(Context context, View view, int postion) {
        ShadowProperty sp = new ShadowProperty()
                .setShadowColor(R.color.colorPink)
                .setShadowDy(dip2px(context, 0.5f))
                .setShadowRadius(dip2px(context, 3))
                .setShadowSide(postion);
        ShadowViewDrawable sd = new ShadowViewDrawable(sp, Color.TRANSPARENT, 0, 0);
        ViewCompat.setBackground(view, sd);
        ViewCompat.setLayerType(view, ViewCompat.LAYER_TYPE_SOFTWARE, null);
    }

    public static void setImageGlide(Context context, String urlImg, int defaultImg, ImageView viewImg) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(defaultImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(context).applyDefaultRequestOptions(options).load(urlImg).into(viewImg);
    }

    public static Bitmap resizeBitmap(String photoPath, int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((width > 0) || (height > 0)) {
            scaleFactor = Math.min(photoW / width, photoH / height);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    public static File persistImage(Bitmap bitmap, String name, Context mContext) {
        File fileAvatar;
        File filesDir = mContext.getFilesDir();
        fileAvatar = new File(filesDir, name + IMAGE_JPG);
        OutputStream os;
        try {
            os = new FileOutputStream(fileAvatar);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return fileAvatar;
        } catch (Exception e) {

        }
        return fileAvatar;
    }

    public static String formatPrice(Integer price) {
        NumberFormat formatter = new DecimalFormat("#,### đ");
        if (price == null) return null;
        return formatter.format(price);
    }

    public static <T> T getJson(String json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            Log.e("getJson err", e.toString());
        }
        return null;
    }

    public static void hideKeybroad(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showErrorDialog(Context context, String message) {
        ErrorBoxDialog dialog = new ErrorBoxDialog(context, message);
        dialog.show();
    }

    public static void setVibrator(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

    public static String setJSONString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T getJSONString(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static String getDurationString(long durationMs, boolean negativePrefix) {
        long hours = TimeUnit.MILLISECONDS.toHours(durationMs);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs);
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%s%02d:%02d:%02d",
                    negativePrefix ? "-" : "",
                    hours,
                    minutes - TimeUnit.HOURS.toMinutes(hours),
                    seconds - TimeUnit.MINUTES.toSeconds(minutes));
        }
        return String.format(Locale.getDefault(), "%s%02d:%02d",
                negativePrefix ? "-" : "",
                minutes,
                seconds - TimeUnit.MINUTES.toSeconds(minutes)
        );
    }

    public static int getErrorMessage(Throwable e) {
        try {
            if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject mainObject = new JSONObject(errorBody);
                String message = mainObject.getString("message");
                switch (message) {
                    case "{{booksUser.validation.codeIsNull}}":
                        return R.string.codeIsNull;
                    case "{{error.bookExist}}":
                        return R.string.bookExist;
                    case "{{error.bookDoesNotActive}}":
                        return R.string.bookDoesNotActive;
                    case "{{error.bookDoesNotExist}}":
                        return R.string.bookDoesNotExist;
                    case "{{error.codeDoesNotExist}}":
                        return R.string.codeDoesNotExist;
                    case "{{common.recordNotFound}}":
                        return R.string.recordNotFound;
                    case "{{common.cantFetch}}":
                        return R.string.cantFetch;
                    case "{{common.paramInvalid}}":
                        return R.string.paramInvalid;
                    case "{{error.cantIdentity}}":
                        return R.string.cantIdentity;
                    case "{{error.giftCodeRequired}}":
                        return R.string.giftCodeRequired;
                    case "{{error.giftCodeNotFound}}":
                        return R.string.giftCodeNotFound;
                    case "{{error.giftCodeUsed}}":
                        return R.string.giftCodeUsed;
                    case "{{error.promotionHasReceived}}":
                        return R.string.promotionHasReceived;
                    case "{{common.recordBookNotFound}}":
                        return R.string.recordBookNotFound;
                    case "{{common.recordDetailMyBookNotFound}}":
                        return R.string.recordDetailMyBookNotFound;
                    case "{{common.recordQuestionNotFound}}":
                        return R.string.recordQuestionNotFound;
                    case "{{common.recordChapterNotFound}}":
                        return R.string.recordChapterNotFound;
                    case "{{common.recordLessonNotFound}}":
                        return R.string.recordLessonNotFound;

                    case "{{error.codeNotFound}}":
                        return R.string.code_not_found;
                    case "{{error.partitionIsNull}}":
                        return R.string.partition_is_null;
                    case "{{error.codeNotMatch}}":
                        return R.string.code_not_match;

                    default:
                        return R.string.serverError;
                }
            } else if (e instanceof TimeoutException) {
                return R.string.timeout;
            } else
                return R.string.serverError;

        } catch (IOException | JSONException e1) {
            e1.printStackTrace();
        }

        return R.string.serverError;
    }

    public static String getErrorMessageCode(Throwable e) {
        try {
            if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject mainObject = new JSONObject(errorBody);
                return mainObject.getString("message");
            }
        } catch (IOException | JSONException e1) {
            e1.printStackTrace();
        }

        return "";
    }

    @SuppressLint("DefaultLocale")
    public static String getTimeCountDown(long millisUntilFinished) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
    }

    public static boolean isMathJax(String value) {
        String newValue = Jsoup.parse(value).text();
        final String regex = "((\\\\d*\\\\.\\\\d+)|(\\\\d+)|([\\\\+\\\\=\\\\-\\\\*\\\\/\\\\$\\\\(\\\\)]))";
//        final String regex = "(\\${1,2})((?:\\\\.|[\\s\\S])*)\\1";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(newValue);
        int find = 0;
        while (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                String a = matcher.group(i);
                if (a != null && !a.isEmpty()) find += 1;
            }
        }
        return find > 0;
    }

    public static String getFilePath(Context context, String folderDownload, int folderId, String fileName) {
        if (StringUtils.isNotNullOrEmpty(fileName) && (fileName.contains(Constant.HTTP) || fileName.contains(Constant.HTTPS)))
            fileName = AppUtils.getFileName(fileName);
        return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName() + "/" + AppContext.getUserId() + "/" + folderDownload + "/" + folderId + "/" + fileName;
    }

    public static String getFolderPath(Context context, String folderDownload, int folderId) {
        return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName() + "/" + AppContext.getUserId() + "/" + folderDownload + "/" + folderId;
    }

    public static String getRealmPath(Context context) {
        return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName() + "/" + Constant.FOLDER_REALM;
    }

    private static String getFilePathJson(Context context, String fileName) {
        if (StringUtils.isNotNullOrEmpty(fileName) && (fileName.contains(Constant.HTTP) || fileName.contains(Constant.HTTPS)))
            fileName = AppUtils.getFileName(fileName);
        return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName() + "/" + AppContext.getUserId() + "/" + fileName + Constant.TXT;
    }

    public static String getFileName(String url) {
        if (StringUtils.isNullOrEmpty(url)) return "";
        String[] bits = url.split("/");
        return bits[bits.length - 1];
    }

    public static String getLessonRealmId(int lessonId, String fileName) {
        return lessonId + "_" + getFileName(fileName) + "_" + AppContext.getUserId();
    }

    public static void writeToFile(Context context, String data, String fileName) {
        try {
            File file = new File(getFilePathJson(context, fileName));
            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName() + "/" + AppContext.getUserId());
            if (!dir.exists())
                dir.mkdir();
            Log.e("writeToFile", getFilePathJson(context, fileName));
            FileOutputStream outputstream = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputstream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readFromFile(Context context, String fileName) {

        String ret = "";
        File file = new File(getFilePathJson(context, fileName));
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e("mcb", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("mcb", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static String removeEndPath(String path) {
        return path.replace(".3gp", "");
    }

    public static String getFilePathRecoder(Context context, int bookId, int chapterId) {
        return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName()
                + "/" + AppContext.getUserId()
                + "/" + bookId + "/" + chapterId;
    }

    public static String getFilePathTempRecoder(Context context, int bookId, int chapterId) {
        return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName()
                + "/" + AppContext.getUserId()
                + "/" + bookId + "/" + chapterId + "/" + "TempRecoder";
    }

    private static String getFilePathJsonRecoder(Context context, int bookId, int chapterId, String fileName) {
        return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName() + "/" + AppContext.getUserId()
                + "/" + bookId + "/" + chapterId + "/" + fileName + ".txt";
    }

    public static void writeToFileRecoder(Context context, String data, String fileName, int bookId, int chapterId) {
        try {
            File file = new File(getFilePathJsonRecoder(context, bookId, chapterId, fileName));
            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName()
                    + "/" + AppContext.getUserId()
                    + "/" + bookId + "/" + chapterId);
            if (!dir.exists())
                dir.mkdirs();
            Log.e("writeToFileRecoder", getFilePathJsonRecoder(context, bookId, chapterId, fileName));
            FileOutputStream outputstream = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputstream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFileRecoder(Context context, int bookId, int chapterId, String fileName) {

        String ret = "";
        File file = new File(getFilePathJsonRecoder(context, bookId, chapterId, fileName));
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e("mcb", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("mcb", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static void sortlistbyTimeRecord(List<RecordItem> recordItems) {
        Collections.sort(recordItems, (t1, t2) -> {
            int date1 = 0, date2 = 0;
            try {
                String dateTime1 = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date(t1.getTime())).toString();
                String dateTime2 = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date(t2.getTime())).toString();
                String d1 = DateTimeUtils.getTimeAgos(dateTime1);
                String d2 = DateTimeUtils.getTimeAgos(dateTime2);
                date1 = Integer.parseInt(d1);
                date2 = Integer.parseInt(d2);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return date1 < date2 ? 1 : -1;
        });
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    public static boolean isTablet(Context context) {
        // 720 = tablet
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 720;
    }

    public static String getFilter(String filter) {
        return String.format("[{\"operator\":\"iLike\",\"value\":\"%s\",\"property\":\"name\"}]", filter.toLowerCase().trim());
    }

    @SuppressLint("DefaultLocale")
    public static String convertTime(int totalSecond) {
        int hours = totalSecond / 3600;
        int minutes = (totalSecond % 3600) / 60;
        int seconds = totalSecond % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
