package com.bon.image;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.bon.application.ExtApplication;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    // CAMERA
    public static final int CAMERA_REQUEST = 100;
    public static final int REQUEST_PICK_CONTENT = 101;
    public static final int REQUEST_CROP_PICTURE = 102;
    public static final String IMAGE_JPG = ".jpg";
    public static final String IMAGE_PNG = ".png";

    /**
     * get random image name
     *
     * @return
     */
    public static String getImageNameJpg() {
        return UUID.randomUUID().toString() + IMAGE_JPG;
    }

    public static String getImageNamePng() {
        return UUID.randomUUID().toString() + IMAGE_PNG;
    }

    /**
     * get image url from sd card
     *
     * @return
     */
    public static File getImageUrlJpg() {
        return new File(ExtApplication.getPathProject(), getImageNameJpg());
    }

    public static File getImageUrlPng() {
        return new File(ExtApplication.getPathProject(), getImageNamePng());
    }

    /**
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriFromFile(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * @param filePath
     * @return
     */
    public static String getUriImage(String filePath) {
        if (StringUtils.isEmpty(filePath)) return null;

        File file = new File(filePath);
        if (!file.exists()) return null;

        return Uri.fromFile(file).toString();
    }

    /**
     * capture image
     *
     * @param activity
     * @param imageUri
     */
    public static void captureCamera(Activity activity, Uri imageUri) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(intent, CAMERA_REQUEST);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * capture image
     *
     * @param fragment
     * @param imageUri
     */
    public static void captureCamera(Fragment fragment, Uri imageUri) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fragment.startActivityForResult(intent, CAMERA_REQUEST);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * choose image from gallery
     *
     * @param activity
     * @param titleChooseImage
     */
    public static void chooseImageFromGallery(Activity activity, String titleChooseImage) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(Intent.createChooser(intent, titleChooseImage), REQUEST_PICK_CONTENT);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(Intent.createChooser(intent, titleChooseImage), REQUEST_PICK_CONTENT);
        }
    }

    /**
     * choose image from gallery
     *
     * @param fragment
     * @param titleChooseImage
     */
    public static void chooseImageFromGallery(Fragment fragment, String titleChooseImage) {
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                fragment.startActivityForResult(Intent.createChooser(intent, titleChooseImage), REQUEST_PICK_CONTENT);
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                fragment.startActivityForResult(Intent.createChooser(intent, titleChooseImage), REQUEST_PICK_CONTENT);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param filePath
     */
    public static void displayLengthFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() <= 0) return;

            // bytes
            double bytes = file.length();
            // kilobytes
            double kilobytes = (bytes / 1024);
            // megabytes
            double megabytes = (kilobytes / 1024);
            // gigabytes
            double gigabytes = (megabytes / 1024);
            // terabytes
            double terabytes = (gigabytes / 1024);
            // petabytes
            double petabytes = (terabytes / 1024);
            // exabytes
            double exabytes = (petabytes / 1024);
            // zettabytes
            double zettabytes = (exabytes / 1024);
            // yottabytes
            double yottabytes = (zettabytes / 1024);

            System.out.println("bytes : " + bytes);
            System.out.println("kilobytes : " + kilobytes);
            System.out.println("megabytes : " + megabytes);
            System.out.println("gigabytes : " + gigabytes);
            System.out.println("terabytes : " + terabytes);
            System.out.println("petabytes : " + petabytes);
            System.out.println("exabytes : " + exabytes);
            System.out.println("zettabytes : " + zettabytes);
            System.out.println("yottabytes : " + yottabytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * decode bitmap from path
     *
     * @param path
     * @return
     */
    public static Bitmap rotateImageWhenCaptureOrSelectFromGallery(String path) {
        try {
            if (StringUtils.isEmpty(path)) return null;
            File file = new File(path);
            if (!file.exists()) return null;

            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;

            int scale = 0;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            // decode with inSampleSize
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = scale;
            // get bitmap from sdcard
            Bitmap bmOrigin = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            // rotate image
            Bitmap bmRotate = bmOrigin;
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                matrix.postRotate(180);
                bmRotate = Bitmap.createBitmap(bmOrigin, 0, 0, bmOrigin.getWidth(), bmOrigin.getHeight(), matrix, true);
                return bmRotate;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
                bmRotate = Bitmap.createBitmap(bmOrigin, 0, 0, bmOrigin.getWidth(), bmOrigin.getHeight(), matrix, true);
                return bmRotate;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
                bmRotate = Bitmap.createBitmap(bmOrigin, 0, 0, bmOrigin.getWidth(), bmOrigin.getHeight(), matrix, true);
                return bmRotate;
            }

            return bmRotate;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param bitmap
     * @param scale  (0.5 = 50%)
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, float scale) {
        try {
            return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), true);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * save bit map to sdcard
     *
     * @param bitmap
     * @param path
     */
    public static boolean saveBitmap(Bitmap bitmap, String path) {
        boolean result = false;
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(path);
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            bitmap.recycle();

            return result;
        } catch (Exception e) {
            result = false;
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                Logger.e(TAG, e);
            }
        }

        return result;
    }

    /**
     * get bitmap from view
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        try {
            if (view.getWidth() == 0 || view.getHeight() == 0) {
                return null;
            }

            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * convert bitmap to base 64
     *
     * @param bitmap
     * @return
     */
    public static String encodeBitmapToBase64(Bitmap bitmap) {
        try {
            if (bitmap == null) return null;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * convert base 64 to bitmap
     *
     * @param base64
     * @return
     */
    public static Bitmap decodeBitmapFromBase64(String base64) {
        try {
            byte[] decodedByte = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public static Drawable setColorFilterToImage(Context context, int imageId, int color) {
        // set color filter
        Drawable drawable = null;

        try {
            drawable = context.getResources().getDrawable(imageId);
            drawable.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_ATOP));
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }

        return drawable;
    }

    public static boolean isImage(String path) {
        try {
            if (StringUtils.isEmpty(path)) return false;
            return path.toLowerCase().contains(".jpg") || path.toLowerCase().contains(".jpeg") ||
                    path.toLowerCase().contains(".png") || path.toLowerCase().contains(".bmp") ? true : false;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * @param view
     * @param globalLayoutListener
     */
    public static void autoLayoutImageScaleFourThree(View view, ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener) {
        try {
            view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                try {
                    // Ensure you call it only once
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
                    }

                    // height, width
                    int width = view.getWidth();

                    // image 4/3
                    int height = width * 70 / 100;

                    // set layout param
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.width = width;
                    layoutParams.height = height;
                    view.setLayoutParams(layoutParams);
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param photoFile
     */
    public static void displayToGallery(Context context, File photoFile) {
        try {
            if (photoFile == null || !photoFile.exists()) {
                return;
            }

            String photoPath = photoFile.getAbsolutePath();
            String photoName = photoFile.getName();
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(photoFile)));
        } catch (FileNotFoundException e) {
            Logger.e(TAG, e);
        }
    }
}
