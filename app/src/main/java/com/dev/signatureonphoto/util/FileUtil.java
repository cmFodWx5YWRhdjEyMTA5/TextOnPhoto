package com.dev.signatureonphoto.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.UUID;


public class FileUtil {


    private static final String TAG = "FileUtil";

    public static String getFolderName(String name) {
        File mediaStorageDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        name);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }
    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getNewFile(Context context, String folderName) {
        String imgName = folderName + UUID.randomUUID().toString().substring(0,8) + ".jpg";
        String strDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        strDirectory = strDirectory + "/" + folderName;
        File folder = new File(strDirectory);
        if (!folder.exists()) {
            folder.mkdir();
        }

        return new File(strDirectory, imgName);
    }
}
