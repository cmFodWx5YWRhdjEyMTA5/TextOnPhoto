package com.dev.signatureonphoto.stickerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static java.lang.Math.round;



class StickerUtils {
    private static final String TAG = "StickerView";

    public static String saveImageToGallery(@NonNull Context context, @NonNull File file, @NonNull Bitmap bmp) {
        OutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);

            /**Compress image**/
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            /**Update image to gallery**/
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), file.getName());
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, "saveImageToGallery: the path of bmp is " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public static void notifySystemGallery(@NonNull Context context, @NonNull File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("bmp should not be null");
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(),
                    file.getName(), null);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File couldn't be found");
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    @NonNull
    public static RectF trapToRect(@NonNull float[] array) {
        RectF r = new RectF();
        trapToRect(r, array);
        return r;
    }

    public static void trapToRect(@NonNull RectF r, @NonNull float[] array) {
        r.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
                Float.NEGATIVE_INFINITY);
        for (int i = 1; i < array.length; i += 2) {
            float x = round(array[i - 1] * 10) / 10.f;
            float y = round(array[i] * 10) / 10.f;
            r.left = (x < r.left) ? x : r.left;
            r.top = (y < r.top) ? y : r.top;
            r.right = (x > r.right) ? x : r.right;
            r.bottom = (y > r.bottom) ? y : r.bottom;
        }
        r.sort();
    }
}
