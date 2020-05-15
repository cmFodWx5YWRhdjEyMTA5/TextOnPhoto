package com.dev.signatureonphoto.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.dev.signatureonphoto.data.model.ItemCross;
import com.dev.signatureonphoto.features.cross.CrossItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckInfoApp {
    public static boolean appInstalledOrNot(Activity activity, String uri) {
        try {
            PackageManager pm = activity.getPackageManager();
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }


    public static String getDeviceCountryCode(Context context) {
        String countryCode;
        TelephonyManager tm;
        if(context==null){
            return "vn";
        }

        // try to get country code from TelephonyManager service
        try{
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }catch (Exception ex){
            return "vn";
        }

        if(tm != null) {
            // query first getSimCountryIso()
            countryCode = tm.getSimCountryIso();
            if (countryCode != null && countryCode.length() == 2)
                return countryCode.toLowerCase();

            if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                // special case for CDMA Devices
                countryCode = getCDMACountryIso();
            } else {
                // for 3G devices (with SIM) query getNetworkCountryIso()
                countryCode = tm.getNetworkCountryIso();
            }

            if (countryCode != null && countryCode.length() == 2)
                return countryCode.toLowerCase();
        }

        // if network country not available (tablets maybe), get country code from Locale class
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = context.getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            countryCode = context.getResources().getConfiguration().locale.getCountry();
        }

        if (countryCode != null && countryCode.length() == 2)
            return  countryCode.toLowerCase();

        // general fallback to "us"
        return "vn";
    }

    @SuppressLint("PrivateApi")
    private static String getCDMACountryIso() {
        try {
            // try to get country code from SystemProperties private class
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);

            // get homeOperator that contain MCC + MNC
            String homeOperator = ((String) get.invoke(systemProperties,
                    "ro.cdma.home.operator.numeric"));

            // first 3 chars (MCC) from homeOperator represents the country code
            int mcc = Integer.parseInt(homeOperator.substring(0, 3));

            // mapping just countries that actually use CDMA networks
            switch (mcc) {
                case 330: return "PR";
                case 310: return "US";
                case 311: return "US";
                case 312: return "US";
                case 316: return "US";
                case 283: return "AM";
                case 460: return "CN";
                case 455: return "MO";
                case 414: return "MM";
                case 619: return "SL";
                case 450: return "KR";
                case 634: return "SD";
                case 434: return "UZ";
                case 232: return "AT";
                case 204: return "NL";
                case 262: return "DE";
                case 247: return "LV";
                case 255: return "UA";
            }
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException ignored) {
        } catch (NullPointerException ignored) {
        }

        return null;
    }

    public static ArrayList<CrossItem> getListCrossOld() {
        ArrayList<CrossItem> list = new ArrayList<>();
        list.add(new CrossItem("Add watermark on Photos - Signature maker", "How to watermark photos, signature maker, design signature, watermark maker", "dev.com.camerafilter"));
        list.add(new CrossItem("QR code reader - Create qr", "Read all types of barcodes with fast and accurate speed. Support to create code quickly...", "dev.com.qrcodefastest"));
        list.add(new CrossItem("Speed test master - Wifi test", "How fast is my internet, test my internet speed, test wifi speed.", "dev.com.testwifi.networkspeedtest"));
        Collections.shuffle(list);
        return list;
    }

    public static ArrayList<ItemCross> initListCrossAdaptive() {
        ArrayList<ItemCross> listInfoApp = new ArrayList<>();
        listInfoApp.add(new ItemCross("Photo Watermark", "How to watermark photos, signature maker, design signature, watermark maker", "dev.com.camerafilter"));
        listInfoApp.add(new ItemCross("QR code scanner", "Read all types of barcodes with fast and accurate speed. Support to create code quickly...", "dev.com.qrcodefastest"));
        listInfoApp.add(new ItemCross("Wifi speed test", "How fast is my internet, test my internet speed, test wifi speed.", "dev.com.testwifi.networkspeedtest"));
        Collections.shuffle(listInfoApp);
        return listInfoApp;
    }

}
