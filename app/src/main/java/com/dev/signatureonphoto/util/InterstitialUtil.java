package com.dev.signatureonphoto.util;

import android.content.Context;

import com.dev.signatureonphoto.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

public class InterstitialUtil {
    private static InterstitialUtil shareIntance;
    private PublisherInterstitialAd interstitialAd;
    private AdCloseListener adcloseListener;
    private boolean isReloaded = false;
    PublisherAdRequest.Builder publisherAdRequestBuilder;
    String[] ggTestDevices;
    public static InterstitialUtil getShareIntance() {
        if (shareIntance == null) {
            shareIntance = new InterstitialUtil();
        }
        return shareIntance;
    }

    public void init(Context context,String id) {
        interstitialAd = new PublisherInterstitialAd(context);
        interstitialAd.setAdUnitId(id);
        publisherAdRequestBuilder = new PublisherAdRequest.Builder();
        ggTestDevices = context.getResources().getStringArray(R.array.google_test_device);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                try{
                    if (adcloseListener != null) {
                        adcloseListener.onAdClose();
                    }
                    loadInterstitial();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (isReloaded == false) {
                    isReloaded = true;
                    loadInterstitial();
                }
            }
        });
        loadInterstitial();
    }

    public boolean isAdLoaded(){
        return interstitialAd.isLoaded();
    }

    private void loadInterstitial() {
        if(interstitialAd !=null && !interstitialAd.isLoading()&&!interstitialAd.isLoaded()){
            for (String testDevice : ggTestDevices) {
                publisherAdRequestBuilder.addTestDevice(testDevice);
            }
            PublisherAdRequest adRequest = publisherAdRequestBuilder.build();
            interstitialAd.loadAd(adRequest);
        }
    }

    public void showInterstitialAd(AdCloseListener adcloseListener){
        if(canShowInterstitialAd()){
            isReloaded = false;
            this.adcloseListener = adcloseListener;
            interstitialAd.show();
        }else {
            loadInterstitial();
            adcloseListener.onAdClose();
        }
    }

    private boolean canShowInterstitialAd(){
        return interstitialAd != null && interstitialAd.isLoaded();
    }

    public interface AdCloseListener {
        public void onAdClose();
    }

}
