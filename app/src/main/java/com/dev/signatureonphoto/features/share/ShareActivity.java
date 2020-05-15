package com.dev.signatureonphoto.features.share;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dev.signatureonphoto.BuildConfig;
import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.InfoApp;
import com.dev.signatureonphoto.features.cross.AdsCrossAdapter;
import com.dev.signatureonphoto.features.home.HomeActivity;
import com.dev.signatureonphoto.features.preview.PreviewActivity;
import com.dev.signatureonphoto.features.splash.SplashActivity;
import com.dev.signatureonphoto.util.AppPreference;
import com.dev.signatureonphoto.util.CheckInfoApp;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.jackandphantom.circularimageview.RoundedImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;

import static com.dev.signatureonphoto.Constants.COUNT_RATE;
import static com.dev.signatureonphoto.Constants.EXTRA_ADS_CREATE_NEW_SHARE;
import static com.dev.signatureonphoto.Constants.GAME_SETTING;
import static com.dev.signatureonphoto.Constants.MAX_ADS_EXIT_APP;
import static com.dev.signatureonphoto.Constants.PRE_COUNT_RATE;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.img_background_photo)
    RoundedImage imgBackgroundPhoto;
    @BindView(R.id.txt_create_new)
    TextView txtCreateNew;
    @BindView(R.id.progress_bar_ads_full)
    ProgressBar progressBarAdsFull;
    @BindView(R.id.layout_ads)
    RelativeLayout layoutAdsNative;

    ShareDialog shareDialog;
    ProgressDialog progressDialog;
    String path;

    private InterstitialAd mInterstitialAdTips;
    private boolean checkLoadAdsBack = true;
    private AdView mAdView;
    private boolean checkShowRateApp;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ViewPager viewPager;
    private List<InfoApp> listInfoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences(GAME_SETTING, Context.MODE_PRIVATE);
        initProgressDilog();
        getImageFromIntent();
        loadImageFromIntent();

        initShareDialog();
        if (AppPreference.getInstance(ShareActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                AppPreference.getInstance(ShareActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            layoutAdsNative.setVisibility(View.GONE);
        }else{
            initAdsBanner();
            loadAdsCross();
        }
    }

    private void loadAdsCross() {
        listInfoApp = new ArrayList<>();
        viewPager = findViewById(R.id.viewPager);
        AdsCrossAdapter adsCrossAdapter = new AdsCrossAdapter(CheckInfoApp.getListCrossOld(), this);
        viewPager.setAdapter(adsCrossAdapter);
        viewPager.setPadding(10, 0, 10, 0);
    }
    private void initAdsBanner() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }

        //Check show rate app
        if(checkShowRateApp){
            checkShowRateApp=false;
            if (!Prefs.with(ShareActivity.this).readBoolean("rate", false)) {
                int countRate = sharedPreferences.getInt(PRE_COUNT_RATE, 0);
                countRate++;
                if (countRate >= COUNT_RATE) {
                    editor = sharedPreferences.edit();
                    editor.putInt(PRE_COUNT_RATE, 0);
                    editor.commit();
                    showDialogRateApp(2);
                } else {
                    editor = sharedPreferences.edit();
                    editor.putInt(PRE_COUNT_RATE, countRate);
                    editor.commit();
                }
            }
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void initShareDialog() {
        shareDialog = new ShareDialog(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "r0c0i - Linotte Regular.ttf");
        txtCreateNew.setTypeface(typeface);
    }

    private void loadImageFromIntent() {
        progressDialog.show();
        Glide.with(this).load(path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressDialog.dismiss();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressDialog.dismiss();
                return false;
            }
        }).into(imgBackgroundPhoto);
    }

    private void getImageFromIntent() {
        if (getIntent() != null) {
            path = getIntent().getStringExtra("url");
        }
    }

    private void initProgressDilog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.load_image));
    }


    @OnClick({R.id.btn_instagram, R.id.btn_facebook, R.id.btn_messenger,R.id.btn_whatsapp,
            R.id.btn_more,R.id.btn_twitter,
            R.id.btnBack, R.id.txt_create_new})
    public void onClick(View view) {
        Uri uri;
        File f = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                    ShareActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", f);
        } else {
            uri = Uri.fromFile(f.getAbsoluteFile());
        }

        switch (view.getId()) {
            case R.id.btn_facebook:
                checkShowRateApp=true;
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(BitmapFactory.decodeFile(path))
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#Quote on photo #Add quotes")
                                .build())
                        .build();

                shareDialog.show(content);
                break;
            case R.id.btn_messenger:
                checkShowRateApp=true;
                try {
                    String mimeType = "image/*";
                    ShareToMessengerParams shareToMessengerParams =
                            ShareToMessengerParams.newBuilder(uri, mimeType)
                                    .build();

                    // Sharing from an Activity
                    MessengerUtils.shareToMessenger(
                            this,
                            1,
                            shareToMessengerParams);
                } catch (ActivityNotFoundException exception) {
                    exception.getLocalizedMessage();
                }
                break;
            case R.id.btn_instagram:
                checkShowRateApp=true;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setPackage("com.instagram.android");
                startActivity(Intent.createChooser(share, "Share to"));
                break;
            case R.id.btn_more:
                checkShowRateApp=true;
                Intent intentMore = new Intent(Intent.ACTION_SEND);
                intentMore.putExtra(Intent.EXTRA_STREAM, uri);
                intentMore.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentMore.setType("image/jpg");
                startActivity(intentMore);
                break;
            case R.id.btn_whatsapp:
                checkShowRateApp=true;
                shareMedia(Constants.WHATAPP_PACKAGE, uri);
                break;
            case R.id.btn_twitter:
                checkShowRateApp=true;
                shareMedia(Constants.TWITTER_PACKAGE, uri);
                break;
            case R.id.btnBack:
                finish();
                break;
            case R.id.txt_create_new:

                int countExitApp = AppPreference.getInstance(this).getKeyShowAds(EXTRA_ADS_CREATE_NEW_SHARE, 0);
                countExitApp++;
                if (countExitApp >= MAX_ADS_EXIT_APP) {
                    AppPreference.getInstance(this).setKeyShowAds(EXTRA_ADS_CREATE_NEW_SHARE, 0);
                    loadAdsInterstitialExitApp();
                    loadInterstitialAdExitApp();
                } else {
                    AppPreference.getInstance(this).setKeyShowAds(EXTRA_ADS_CREATE_NEW_SHARE, countExitApp);
                    startActivityHome();
                }
        }
    }

    private void startActivityHome() {
        Intent intent = new Intent(ShareActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void loadAdsInterstitialExitApp() {
        progressBarAdsFull.setVisibility(View.VISIBLE);
        mInterstitialAdTips = new InterstitialAd(this);
        mInterstitialAdTips.setAdUnitId(getString(R.string.full_screen_create_new_share));
        mInterstitialAdTips.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                AppPreference.getInstance(ShareActivity.this).setKeyShowAds(EXTRA_ADS_CREATE_NEW_SHARE,0);
                startActivityHome();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAdTips.isLoaded() && checkLoadAdsBack) {
                    mInterstitialAdTips.show();
                    checkLoadAdsBack = false;
                    progressBarAdsFull.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                progressBarAdsFull.setVisibility(View.GONE);
                if (checkLoadAdsBack) {
                    checkLoadAdsBack = false;
                    startActivityHome();
                }
            }
        });

    }

    //Load InterstitialAd
    private void loadInterstitialAdExitApp() {
        if (mInterstitialAdTips != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            mInterstitialAdTips.loadAd(adRequest);
        }
    }
    private void shareMedia(String packageName, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        boolean installed = checkAppInstall(packageName);
        if (installed) {
            intent.setPackage(packageName);
            startActivity(Intent.createChooser(intent, "Share to"));
        } else {
            boolean isTwitter = packageName.equals(Constants.TWITTER_PACKAGE);
            String message = getString(R.string.twitter_not_install);
            if (!isTwitter) {
                message = getString(R.string.whatsapp_not_install);
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    Animation animBorder1, animBorder2, animBorder3, animBorder4, animBorder5;
    Animation animYellow1, animYellow2, animYellow3, animYellow4, animYellow5;

    private void showDialogRateApp(int action) {
        final Dialog dialog1 = new Dialog(this);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_rate_app);
        Typeface type = Typeface.createFromAsset(getAssets(), "r0c0i - Linotte Regular.ttf");

        final TextView btnRate = dialog1.findViewById(R.id.btnRate);
        TextView btnLater = dialog1.findViewById(R.id.btnLater);
        TextView tvTitleDialog = dialog1.findViewById(R.id.tvTitleDialog);
        tvTitleDialog.setTypeface(type);
        tvTitleDialog.setTypeface(type);
        btnLater.setTypeface(type);
        AtomicInteger Rate = new AtomicInteger();

        animBorder1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);

        animYellow1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);

        //border
        ImageView imgBorder1 = dialog1.findViewById(R.id.img_stars1);
        ImageView imgBorder2 = dialog1.findViewById(R.id.img_stars2);
        ImageView imgBorder3 = dialog1.findViewById(R.id.img_stars3);
        ImageView imgBorder4 = dialog1.findViewById(R.id.img_stars4);
        ImageView imgBorder5 = dialog1.findViewById(R.id.img_stars5);

        //yellow
        ImageView imgStarYellow1 = dialog1.findViewById(R.id.img_stars_1);
        ImageView imgStarYellow2 = dialog1.findViewById(R.id.img_stars_2);
        ImageView imgStarYellow3 = dialog1.findViewById(R.id.img_stars_3);
        ImageView imgStarYellow4 = dialog1.findViewById(R.id.img_stars_4);
        ImageView imgStarYellow5 = dialog1.findViewById(R.id.img_stars_5);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);

        Animation animationY1 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);
        Animation animationY2 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);
        Animation animationY3 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);
        Animation animationY4 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);

        animation.setStartOffset(200);
        animation1.setStartOffset(400);
        animation2.setStartOffset(600);
        animation3.setStartOffset(800);

        animationY1.setStartOffset(200);
        animationY2.setStartOffset(400);
        animationY3.setStartOffset(600);
        animationY4.setStartOffset(800);

        imgBorder2.startAnimation(animation);
        imgBorder3.startAnimation(animation1);
        imgBorder4.startAnimation(animation2);
        imgBorder5.startAnimation(animation3);

        imgStarYellow2.startAnimation(animationY1);
        imgStarYellow3.startAnimation(animationY2);
        imgStarYellow4.startAnimation(animationY3);
        imgStarYellow5.startAnimation(animationY4);

        imgBorder1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_stars_border));
        imgStarYellow1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow));


        imgBorder1.setOnClickListener(v -> {
            Rate.set(1);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder2);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder3);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder4);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
        });
        imgBorder2.setOnClickListener(v -> {
            Rate.set(2);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder3);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder4);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
        });
        imgBorder3.setOnClickListener(v -> {
            Rate.set(3);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder3);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder4);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
        });
        imgBorder4.setOnClickListener(v -> {
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder3);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder4);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
            Rate.set(4);
        });
        imgBorder5.setOnClickListener(v -> {
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder3);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder4);
            Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder5);
            Rate.set(5);
        });
        btnRate.setOnClickListener(v -> {
            Toast.makeText(ShareActivity.this, getString(R.string.sms_thank_you_rate), Toast.LENGTH_SHORT).show();
            Prefs.with(ShareActivity.this).writeBoolean("rate", true);
            Intent i = new Intent("android.intent.action.VIEW");
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(i);
            dialog1.dismiss();

        });
        btnLater.setOnClickListener(v -> {
            if (action == 1) {
                dialog1.dismiss();
                animBorder1.cancel();
                animBorder4.cancel();
                animBorder2.cancel();
                animBorder3.cancel();
                animBorder5.cancel();
            } else if (action == 2) {
                dialog1.dismiss();
                Prefs.with(ShareActivity.this).writeBoolean("rate", false);
            }
        });

        dialog1.setCancelable(false);
        dialog1.show();
    }
}
