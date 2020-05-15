package com.dev.signatureonphoto.features.saved;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.features.home.HomeActivity;
import com.dev.signatureonphoto.features.purchased.PurchasedActivity;
import com.dev.signatureonphoto.features.share.ShareActivity;
import com.dev.signatureonphoto.util.AppPreference;
import com.dev.signatureonphoto.util.CheckInfoApp;
import com.dev.signatureonphoto.util.UtilAdsCrossAdaptive;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;


public class SavedActivity extends AppCompatActivity implements SavedMvp {
    @BindView(R.id.recycle_gallery)
    RecyclerView recyclerViewGallery;
    @BindView(R.id.img_back)
    TextView imgBack;
    @BindView(R.id.layout_ads)
    RelativeLayout layoutAdsSmallBanner;
    @BindView(R.id.img_vip)
    ImageView imgVip;

    private SavedAdapter savedAdapter;
    private SavedPresenter savedPresenter;
    private InterstitialAd mInterstitialAd;
    private Animation animationClick;

    //Admob
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_saved);
        ButterKnife.bind(this);
        savedPresenter = new SavedPresenter();
        initComponent();
        savedPresenter.setSavedMvp(this);
        savedPresenter.readImageSaved();
        loadAnimation();
        Glide.with(this).load(R.drawable.gif_pro).into(imgVip);
        if (AppPreference.getInstance(SavedActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                AppPreference.getInstance(SavedActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            layoutAdsSmallBanner.setVisibility(View.GONE);
        }else{
            initAdsBanner();
        }
    }

    private void initAdsBanner() {
        layoutAdsSmallBanner.addView(UtilAdsCrossAdaptive.getLayoutCross(SavedActivity.this,CheckInfoApp.initListCrossAdaptive()));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_screen_saved));

        loadBanner();
    }
    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                layoutAdsSmallBanner.removeAllViews();
                layoutAdsSmallBanner.addView(adView);
                layoutAdsSmallBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                layoutAdsSmallBanner.removeAllViews();
                layoutAdsSmallBanner.addView(UtilAdsCrossAdaptive.getLayoutCross(SavedActivity.this,CheckInfoApp.initListCrossAdaptive()));
            }
        });
        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(this, adWidth);
    }


    private void loadAnimation() {
        animationClick = AnimationUtils.loadAnimation(this, R.anim.anim_click);
    }

    private void initComponent() {
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));
    }


    @OnClick({R.id.img_back,R.id.img_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                imgBack.startAnimation(animationClick);
                finish();
                break;
            case R.id.img_vip:
                startActivity(new Intent(SavedActivity.this, PurchasedActivity.class));
                break;
        }
    }

    @Override
    public void loadedGallery(ArrayList<GalleryItem> listGallery) {
        Collections.sort(listGallery, new SortGalleryDate());
        savedAdapter = new SavedAdapter(this, listGallery,this);
        recyclerViewGallery.setAdapter(savedAdapter);
    }

    @Override
    public void itemOnClicked(String path) {
        Intent intent = new Intent(SavedActivity.this, ShareActivity.class);
        intent.putExtra("url", path);
        startActivity(intent);
    }

}
