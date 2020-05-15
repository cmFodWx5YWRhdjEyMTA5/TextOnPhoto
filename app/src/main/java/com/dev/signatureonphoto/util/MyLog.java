package com.dev.signatureonphoto.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by D on 8/8/2017.
 */

public class MyLog {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;

    public static void openLog(Context context, String logName) {
        preferences = context.getSharedPreferences(logName, MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void putStringValueByName(Context context, String logName, String name, String value) {
        openLog(context, logName);
        editor.putString(name, value);
        editor.commit();
    }

    public static String getStringValueByName(Context context, String logName, String name) {
        openLog(context, logName);
        String values = preferences.getString(name, "");
        return values;
    }

    public static void putBooleanValueByName(Context context, String logName, String name, boolean value) {
        openLog(context, logName);
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static boolean getBooleanValueByName(Context context, String logName, String name) {
        openLog(context, logName);
        return preferences.getBoolean(name, false);
    }

    public static void putIntValueByName(Context context, String logName, String name, int value) {
        openLog(context, logName);
        editor.putInt(name, value);
        editor.commit();
    }

    public static int getIntValueByName(Context context, String logName, String name) {
        openLog(context, logName);
        return preferences.getInt(name, -1);
    }

    public static void putFloatValueByName(Context context, String logName, String name, float value) {
        openLog(context, logName);
        editor.putFloat(name, value);
        editor.commit();
    }

    public static float getFloatValueByName(Context context, String logName, String name) {
        openLog(context, logName);
        return preferences.getFloat(name, 1f);
    }

    public static void removeAll() {
        editor.clear();
        editor.commit();
    }
}
