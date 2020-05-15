package com.dev.signatureonphoto.features.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.features.home.HomeActivity;
import com.dev.signatureonphoto.features.purchased.PurchasedActivity;
import com.dev.signatureonphoto.util.AppPreference;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.dev.signatureonphoto.Constants.EXTRA_ADS_SCREEN_SPLASH;
import static com.dev.signatureonphoto.Constants.KEY_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.KEY_REMOVED_UNLOCK_FEATURE;
import static com.dev.signatureonphoto.Constants.KEY_UNLOCK_ALL_FEATURE;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;
import static com.dev.signatureonphoto.Constants.PRE_UNLOCK_ALL_FEATURE;

public class SplashActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.layout_loading)
    LinearLayout layoutLoading;
    private Disposable disposable;

    private InterstitialAd mInterstitialAd;
    private boolean isCheckLoadAds = true;
    private int timeMax = 7;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private BillingClient billingClientAll;
    private int progress;
    private Disposable disposableSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_xmas);
        ButterKnife.bind(this);
        share = getSharedPreferences("check_splash", MODE_PRIVATE);
        Glide.with(this).load(R.drawable.bg_gradient_splash).into((ImageView) findViewById(R.id.imgSplash));
        Glide.with(this).load(R.drawable.ic_logo).into((ImageView) findViewById(R.id.img_logo));

        setUpBillingClient();

        if (AppPreference.getInstance(SplashActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                AppPreference.getInstance(SplashActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            skip();
        } else {
            loadAdsInterstitial();
            loadInterstitialAd();
            startToMain();
            startTimeLeft();
        }
    }
    private void startTimeLeft() {
        disposableSeekbar = Observable.interval(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tick -> {
                    progress += 2;
                    seekbar.setProgress(progress);
                }, throwable -> skip());
    }

    private void setUpBillingClient() {
        billingClientAll = BillingClient.newBuilder(this).setListener(this).build();
        billingClientAll.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                Purchase.PurchasesResult result = billingClientAll.queryPurchases(BillingClient.SkuType.INAPP);
                List<Purchase> purchases = result.getPurchasesList();

                if (purchases != null && purchases.size() > 0) {
                    for (int i = 0; i < purchases.size(); i++) {
                        if (purchases.get(i).getSku().equals(KEY_REMOVED_ADS)) {
                            AppPreference.getInstance(SplashActivity.this).setKeyRate(PRE_REMOVED_ADS, true);
                        } else if (purchases.get(i).getSku().equals(KEY_UNLOCK_ALL_FEATURE)) {
                            AppPreference.getInstance(SplashActivity.this).setKeyRate(PRE_UNLOCK_ALL_FEATURE, true);
                        } else if (purchases.get(i).getSku().equals(KEY_REMOVED_UNLOCK_FEATURE)) {
                            AppPreference.getInstance(SplashActivity.this).setKeyRate(PRE_REMOVED_UNLOCK_FEATURE, true);
                        }
                    }
                } else {
                    AppPreference.getInstance(SplashActivity.this).setKeyRate(PRE_REMOVED_ADS, false);
                    AppPreference.getInstance(SplashActivity.this).setKeyRate(PRE_UNLOCK_ALL_FEATURE, false);
                    AppPreference.getInstance(SplashActivity.this).setKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        isCheckLoadAds = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCheckLoadAds = false;
    }


    private void skip() {
        if (disposable != null) {
            disposable.dispose();
            isCheckLoadAds = false;
        }
        if(disposableSeekbar!=null){
            disposableSeekbar.dispose();
        }
        Intent failedIntent = new Intent(SplashActivity.this, HomeActivity.class);
        failedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        failedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(failedIntent);
        finish();

    }
    private void disposableAll(){
        if(disposable!=null){
            disposable.dispose();
        }
        if(disposableSeekbar!=null){
            disposableSeekbar.dispose();
        }
    }


    private void startToMain() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        timeMax--;
                        if (timeMax == 0) {
                            isCheckLoadAds = false;
                            skip();
                        }
                    }
                });
    }

    private void loadAdsInterstitial() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.full_screen_splash));
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                editor = share.edit();
                editor.putBoolean("FIRST_OPEN_SPLASH", false);
                editor.apply();
                AppPreference.getInstance(SplashActivity.this).setKeyShowAds(EXTRA_ADS_SCREEN_SPLASH, 0);
                skip();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded() && isCheckLoadAds) {
                    mInterstitialAd.show();
                    layoutLoading.setVisibility(View.GONE);
                    isCheckLoadAds = false;
                    disposableAll();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (isCheckLoadAds) {
                    isCheckLoadAds = false;
                    skip();
                }
            }
        });

    }

    //Load InterstitialAd
    private void loadInterstitialAd() {
        if (mInterstitialAd != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            mInterstitialAd.loadAd(adRequest);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isCheckLoadAds) {
            isCheckLoadAds = false;
            skip();
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }
}

