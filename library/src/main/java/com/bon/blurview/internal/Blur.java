package com.bon.blurview.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by Dang on 7/8/2016.
 */
public class Blur {
    public static Bitmap of(View view, BlurFactor factor) {
        try {
            view.setDrawingCacheEnabled(true);
            view.destroyDrawingCache();
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
            Bitmap cache = view.getDrawingCache();
            Bitmap bitmap = of(view.getContext(), cache, factor);
            if (cache != null) cache.recycle();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap of(Context context, Bitmap source, BlurFactor factor) {
        try {
            int width = factor.width / factor.sampling;
            int height = factor.height / factor.sampling;

            if (Helper.hasZero(width, height)) return null;

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            canvas.scale(BlurFactor.DEFAULT_SAMPLING / (float) factor.sampling, BlurFactor.DEFAULT_SAMPLING / (float) factor.sampling);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
            PorterDuffColorFilter filter =
                    new PorterDuffColorFilter(factor.color, PorterDuff.Mode.SRC_ATOP);
            paint.setColorFilter(filter);
            canvas.drawBitmap(source, 0, 0, paint);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                try {
                    bitmap = Blur.rs(context, bitmap, factor.radius);
                } catch (RSRuntimeException e) {
                    bitmap = Blur.stack(bitmap, factor.radius, true);
                }
            } else {
                bitmap = Blur.stack(bitmap, factor.radius, true);
            }

            if (factor.sampling == BlurFactor.DEFAULT_SAMPLING) {
                return bitmap;
            } else {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, factor.width, factor.height, true);
                if (bitmap != null) bitmap.recycle();
                return scaled;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static Bitmap rs(Context context, Bitmap bitmap, int radius) throws RSRuntimeException {
        RenderScript renderScript = null;
        try {
            renderScript = RenderScript.create(context);
            renderScript.setMessageHandler(new RenderScript.RSMessageHandler());
            Allocation input =
                    Allocation.createFromBitmap(renderScript, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                            Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(renderScript, input.getType());
            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

            blur.setInput(input);
            blur.setRadius(radius);
            blur.forEach(output);
            output.copyTo(bitmap);
        } finally {
            if (renderScript != null) {
                renderScript.destroy();
            }
        }

        return bitmap;
    }

    public static Bitmap stack(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap = null;

        try {
            if (canReuseInBitmap) {
                bitmap = sentBitmap;
            } else {
                bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            }

            if (radius < BlurFactor.MIN_RADIUS) return null;

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - BlurFactor.ONE;
            int hm = h - BlurFactor.ONE;
            int wh = w * h;
            int div = radius + radius + BlurFactor.ONE;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rSum, gSum, bSum, x, y, i, p, yp, yi, yw;
            int vMin[] = new int[Math.max(w, h)];

            int divSum = (div + BlurFactor.ONE) >> BlurFactor.ONE;
            divSum *= divSum;
            int dv[] = new int[256 * divSum];
            for (i = 0; i < 256 * divSum; i++) {
                dv[i] = (i / divSum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackPointer;
            int stackStart;
            int[] sir;
            int rbs;
            int radiusOne = radius + BlurFactor.ONE;
            int routSum, goutSum, boutSum;
            int rinSum, ginSum, binSum;

            for (y = 0; y < h; y++) {
                rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;

                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = radiusOne - Math.abs(i);
                    rSum += sir[0] * rbs;
                    gSum += sir[1] * rbs;
                    bSum += sir[2] * rbs;
                    if (i > 0) {
                        rinSum += sir[0];
                        ginSum += sir[1];
                        binSum += sir[2];
                    } else {
                        routSum += sir[0];
                        goutSum += sir[1];
                        boutSum += sir[2];
                    }
                }

                stackPointer = radius;

                for (x = 0; x < w; x++) {
                    r[yi] = dv[rSum];
                    g[yi] = dv[gSum];
                    b[yi] = dv[bSum];

                    rSum -= routSum;
                    gSum -= goutSum;
                    bSum -= boutSum;

                    stackStart = stackPointer - radius + div;
                    sir = stack[stackStart % div];

                    routSum -= sir[0];
                    goutSum -= sir[1];
                    boutSum -= sir[2];

                    if (y == 0) {
                        vMin[x] = Math.min(x + radius + BlurFactor.ONE, wm);
                    }
                    p = pix[yw + vMin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];

                    rSum += rinSum;
                    gSum += ginSum;
                    bSum += binSum;

                    stackPointer = (stackPointer + BlurFactor.ONE) % div;
                    sir = stack[(stackPointer) % div];

                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];

                    rinSum -= sir[0];
                    ginSum -= sir[1];
                    binSum -= sir[2];

                    yi++;
                }

                yw += w;
            }

            for (x = 0; x < w; x++) {
                rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = radiusOne - Math.abs(i);

                    rSum += r[yi] * rbs;
                    gSum += g[yi] * rbs;
                    bSum += b[yi] * rbs;

                    if (i > 0) {
                        rinSum += sir[0];
                        ginSum += sir[1];
                        binSum += sir[2];
                    } else {
                        routSum += sir[0];
                        goutSum += sir[1];
                        boutSum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }

                yi = x;
                stackPointer = radius;

                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rSum] << 16) | (dv[gSum] << 8) | dv[bSum];

                    rSum -= routSum;
                    gSum -= goutSum;
                    bSum -= boutSum;

                    stackStart = stackPointer - radius + div;
                    sir = stack[stackStart % div];

                    routSum -= sir[0];
                    goutSum -= sir[1];
                    boutSum -= sir[2];

                    if (x == 0) {
                        vMin[y] = Math.min(y + radiusOne, hm) * w;
                    }
                    p = x + vMin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];

                    rSum += rinSum;
                    gSum += ginSum;
                    bSum += binSum;

                    stackPointer = (stackPointer + BlurFactor.ONE) % div;
                    sir = stack[stackPointer];

                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];

                    rinSum -= sir[0];
                    ginSum -= sir[1];
                    binSum -= sir[2];

                    yi += w;
                }
            }

            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
