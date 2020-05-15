package com.dev.signatureonphoto.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    public static AppPreference sInstance;
    public Context mContext;
    private SharedPreferences sharedPreferences;

    public static AppPreference getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPreference(context.getApplicationContext());
        }
        return sInstance;
    }

    private AppPreference(final Context context) {
        this.mContext = context;
        sharedPreferences= context.getSharedPreferences(context.getPackageName() + "_preferences",Context.MODE_PRIVATE);
    }
    public int getKeyShowAds(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    public void setKeyShowAds(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public boolean getKeyRate(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    public void setKeyRate(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String getKeyImage(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    public void setKeyImage(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }
    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
