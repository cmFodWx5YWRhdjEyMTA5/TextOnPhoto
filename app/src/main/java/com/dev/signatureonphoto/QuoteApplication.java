package com.dev.signatureonphoto;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.dev.signatureonphoto.database.DataManager;
import com.facebook.FacebookSdk;

import io.fabric.sdk.android.Fabric;


public class QuoteApplication extends MultiDexApplication{
    public static SharedPreferences preferences;
    public static QuoteApplication get(Context context) {
        return (QuoteApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataManager.getInstance().init(this);
        FacebookSdk.setApplicationId("619393668552356");
        FacebookSdk.sdkInitialize(this);
        trackingCrashAnalytics();
    }
    private void trackingCrashAnalytics() {
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);

        // day la vo ta minh test pull
    }
}
