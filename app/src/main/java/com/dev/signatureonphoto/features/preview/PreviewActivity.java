package com.dev.signatureonphoto.features.preview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.BuildConfig;
import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.InfoApp;
import com.dev.signatureonphoto.features.cross.AdsCrossAdapter;
import com.dev.signatureonphoto.features.finish.FinishActivity;
import com.dev.signatureonphoto.features.home.HomeActivity;
import com.dev.signatureonphoto.features.main.MainActivity;
import com.dev.signatureonphoto.features.purchased.PurchasedActivity;
import com.dev.signatureonphoto.features.saved.SavedActivity;
import com.dev.signatureonphoto.features.share.ShareActivity;
import com.dev.signatureonphoto.util.AppPreference;
import com.dev.signatureonphoto.util.CheckInfoApp;
import com.dev.signatureonphoto.util.Config;
import com.dev.signatureonphoto.util.MyLog;
import com.dev.signatureonphoto.util.UtilAdsCrossAdaptive;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jackandphantom.circularimageview.RoundedImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.prefs.Prefs;

import static com.dev.signatureonphoto.Constants.COUNT_RATE;
import static com.dev.signatureonphoto.Constants.EXTRA_ADS_SET_WALLPAPER;
import static com.dev.signatureonphoto.Constants.GAME_SETTING;
import static com.dev.signatureonphoto.Constants.MAX_ADS_SET_WALLPAPER;
import static com.dev.signatureonphoto.Constants.MAX_COUNT_CLICK_SAVED_PREVIEW;
import static com.dev.signatureonphoto.Constants.PRE_COUNT_CLICK_SAVED_PREVIEW;
import static com.dev.signatureonphoto.Constants.PRE_COUNT_RATE;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;

public class PreviewActivity extends AppCompatActivity implements ShareDialog.CallbackDialog {
    private static final int AUTO_SCREEN = 0;
    private static final int SD_PX = 1;
    private static final int HQ_PX = 2;
    private static final int HD_PX = 3;
    private static final int FULL_HD_PX = 4;
    @BindView(R.id.txt_edit)
    TextView txtEdit;
    @BindView(R.id.txt_home)
    TextView txtHome;
    @BindView(R.id.txt_save)
    TextView txtSave;
    @BindView(R.id.layout_save)
    LinearLayout layoutSave;
    @BindView(R.id.txt_set_wp)
    TextView txtSetWp;
    @BindView(R.id.layout_set_wp)
    LinearLayout layoutSetWp;
    @BindView(R.id.txt_share)
    TextView txtShare;
    @BindView(R.id.layout_share)
    LinearLayout layoutShare;
    @BindView(R.id.img_preview)
    RoundedImage imgPreview;
    @BindView(R.id.txt_resolution)
    TextView txtResolution;
    @BindView(R.id.txt_alert)
    TextView txtAlert;
    @BindView(R.id.layout_ads)
    RelativeLayout layoutAdsNative;
    @BindView(R.id.img_vip)
    ImageView imgVip;

    com.facebook.share.widget.ShareDialog shareDialogFacebook;
    ShareDialog shareDialog;
    private int currentQuality;
    private boolean isSaved = false;
    private String path;
    private boolean allowShowAds = true;
    private int numcount;
    private int total_count;

    //Admob
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private InterstitialAd mInterstitialAdWallpaper;
    private InterstitialAd mInterstitialAdSaved;
    private boolean checkLoadAdsBack = true;

    private boolean checkShareDone;
    private boolean checkClickShareOrWall;
    private boolean checkClickSetWallpaper = true;
    private ProgressDialog progressDialog;
    private boolean checkClickWallpaper = false;
    private AdView adView;
    private boolean checkShowRateApp;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Animation animation;
    private ViewPager viewPager;
    private List<InfoApp> listInfoApp;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        sharedPreferences = this.getSharedPreferences(GAME_SETTING, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Set wallpaper");
        progressDialog.setCancelable(false);
        createFontText();
        total_count = MyLog.getIntValueByName(this, Config.LOG_APP, Config.HD_COUNT);

        shareDialog = new ShareDialog(this);
        shareDialog.setCallbackDialog(this);

        currentQuality = getIntent().getIntExtra(com.dev.signatureonphoto.util.Constants.CURRENT_QUALITY, 0);
        glideToImageView();
        Glide.with(this).load(R.drawable.gif_pro).into(imgVip);

        if (AppPreference.getInstance(PreviewActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                AppPreference.getInstance(PreviewActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            layoutAdsNative.setVisibility(View.GONE);
        } else {
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
    protected void onStop() {
        super.onStop();
        checkLoadAdsBack = false;
    }

    @OnClick({R.id.txt_edit, R.id.txt_home, R.id.layout_save, R.id.layout_set_wp, R.id.layout_share,
    R.id.img_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_edit:
                finish();
                break;
            case R.id.txt_home:
                Intent intent = new Intent(PreviewActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.layout_save:
                if (AppPreference.getInstance(PreviewActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(PreviewActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    startActivity(new Intent(PreviewActivity.this, SavedActivity.class));
                } else {
                    int countExitApp = AppPreference.getInstance(PreviewActivity.this).getKeyShowAds(PRE_COUNT_CLICK_SAVED_PREVIEW, 0);
                    countExitApp++;
                    if (countExitApp >= MAX_COUNT_CLICK_SAVED_PREVIEW) {
                        AppPreference.getInstance(PreviewActivity.this).setKeyShowAds(PRE_COUNT_CLICK_SAVED_PREVIEW, 0);
                        loadAdsInterstitialSaved();
                        loadInterstitialAdSaved();
                    } else {
                        AppPreference.getInstance(PreviewActivity.this).setKeyShowAds(PRE_COUNT_CLICK_SAVED_PREVIEW, countExitApp);
                        startActivity(new Intent(PreviewActivity.this, SavedActivity.class));
                    }
                }

                break;
            case R.id.layout_set_wp:
                checkShowRateApp = true;
                if (checkClickSetWallpaper) {
                    checkClickSetWallpaper = false;
                    checkClickShareOrWall = true;
                    checkClickWallpaper = true;

                    int countExitApp = AppPreference.getInstance(this).getKeyShowAds(EXTRA_ADS_SET_WALLPAPER, 0);
                    countExitApp++;
                    if (countExitApp >= MAX_ADS_SET_WALLPAPER) {
                        progressDialog.setMessage("Set wallpaper & Loading Ad...");
                    } else {
                        progressDialog.setMessage("Set wallpaper");
                    }

                    progressDialog.show();
                    setWallpaper();
                }
                break;
            case R.id.layout_share:
                checkShowRateApp = true;
                Uri uri;
                if (path == null) {
                    return;
                }
                File f = new File(path);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(
                            PreviewActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider", f);
                } else {
                    uri = Uri.fromFile(f.getAbsoluteFile());
                }
                shareImageUri(uri);
                break;
            case R.id.img_vip:
                startActivity(new Intent(PreviewActivity.this, PurchasedActivity.class));
                break;

        }
    }

    private void loadAdsInterstitialWallpaper() {
        checkLoadAdsBack = true;
        progressBar.setVisibility(View.VISIBLE);
        mInterstitialAdWallpaper = new InterstitialAd(this);
        mInterstitialAdWallpaper.setAdUnitId(getString(R.string.full_screen_set_wallpaper));
        mInterstitialAdWallpaper.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                checkClickSetWallpaper = true;
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(PreviewActivity.this, FinishActivity.class));
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAdWallpaper.isLoaded() && checkLoadAdsBack) {
                    progressDialog.dismiss();
                    checkLoadAdsBack = false;
                    progressBar.setVisibility(View.GONE);
                    mInterstitialAdWallpaper.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                checkClickSetWallpaper = true;
                checkLoadAdsBack = false;
                progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
                startActivity(new Intent(PreviewActivity.this, FinishActivity.class));
            }
        });

    }

    //Load InterstitialAd
    private void loadInterstitialAdWallpaper() {
        if (mInterstitialAdWallpaper != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mInterstitialAdWallpaper.loadAd(adRequest);
        }
    }

    private void loadAdsInterstitialSaved() {
        checkLoadAdsBack = true;
        progressBar.setVisibility(View.VISIBLE);
        mInterstitialAdSaved = new InterstitialAd(this);
        mInterstitialAdSaved.setAdUnitId(getString(R.string.full_screen_click_saved_preview));
        mInterstitialAdSaved.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                AppPreference.getInstance(PreviewActivity.this).setKeyShowAds(PRE_COUNT_CLICK_SAVED_PREVIEW, 0);
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(PreviewActivity.this, SavedActivity.class));
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAdSaved.isLoaded() && checkLoadAdsBack) {
                    progressDialog.dismiss();
                    checkLoadAdsBack = false;
                    progressBar.setVisibility(View.GONE);
                    mInterstitialAdSaved.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                checkLoadAdsBack = false;
                progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
                startActivity(new Intent(PreviewActivity.this, SavedActivity.class));
            }
        });

    }

    //Load InterstitialAd
    private void loadInterstitialAdSaved() {
        if (mInterstitialAdSaved != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mInterstitialAdSaved.loadAd(adRequest);
        }
    }

    private void shareImageUri(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/jpg");
        startActivity(intent);
    }

    private void glideToImageView() {
        path = getIntent().getStringExtra("url");
        Uri photoUri = Uri.fromFile(new File(path));

        Glide.with(this).load(photoUri).into(imgPreview);

        int resolution = getIntent().getIntExtra("SELECT_RESOLUTION", 0);
        if (resolution == AUTO_SCREEN) {
            txtResolution.setText("Auto(Device Screen)");
            txtAlert.setVisibility(View.VISIBLE);
            txtAlert.startAnimation(animation);
        } else if (resolution == SD_PX) {
            txtResolution.setText("SD(480px)");
            txtAlert.setVisibility(View.VISIBLE);
            txtAlert.startAnimation(animation);
        } else if (resolution == HQ_PX) {
            txtResolution.setText("HD(720px)");
            txtAlert.setVisibility(View.VISIBLE);
            txtAlert.startAnimation(animation);
        } else if (resolution == HD_PX) {
            txtResolution.setText("HD(1080px)");
            txtAlert.setVisibility(View.VISIBLE);
            txtAlert.startAnimation(animation);
        } else if (resolution == FULL_HD_PX) {
            txtResolution.setText("Full HD(1920px)");
            txtAlert.setVisibility(View.GONE);
        }
    }

    private void createFontText() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "r0c0i - Linotte Regular.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "r0c0iLinotte-Bold.ttf");
        txtEdit.setTypeface(typefaceBold);
        txtHome.setTypeface(typefaceBold);
        txtSave.setTypeface(typeface);
        txtSetWp.setTypeface(typeface);
        txtShare.setTypeface(typeface);
    }


    @SuppressLint("StaticFieldLeak")
    private void setWallpaper() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    wallpaperManager.setBitmap(bitmap);
                    //Check load ads

                    if (AppPreference.getInstance(PreviewActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                            AppPreference.getInstance(PreviewActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                        progressDialog.dismiss();
                        startActivity(new Intent(PreviewActivity.this, FinishActivity.class));
                        checkClickSetWallpaper = true;
                    } else {
                        int countExitApp = AppPreference.getInstance(PreviewActivity.this).getKeyShowAds(EXTRA_ADS_SET_WALLPAPER, 0);
                        countExitApp++;
                        if (countExitApp >= MAX_ADS_SET_WALLPAPER) {
                            AppPreference.getInstance(PreviewActivity.this).setKeyShowAds(EXTRA_ADS_SET_WALLPAPER, 0);
                            loadAdsInterstitialWallpaper();
                            loadInterstitialAdWallpaper();
                        } else {
                            AppPreference.getInstance(PreviewActivity.this).setKeyShowAds(EXTRA_ADS_SET_WALLPAPER, countExitApp);
                            progressDialog.dismiss();
                            startActivity(new Intent(PreviewActivity.this, FinishActivity.class));
                            checkClickSetWallpaper = true;
                        }
                    }
                } catch (IOException e) {
                }
            }
        }.execute();

    }

    @Override
    public void onClick(int shareType) {
        shareDialog.dismiss();
        Uri uri;
        File f = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                    PreviewActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", f);
        } else {
            uri = Uri.fromFile(f.getAbsoluteFile());
        }
        switch (shareType) {
            case ShareDialog.SHARE_FACEBOOK:
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(BitmapFactory.decodeFile(path))
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                shareDialogFacebook.show(content);
                break;
            case ShareDialog.SHARE_INSTAGRAMS:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(Constants.IMAGE_TYPE);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setPackage(Constants.INSTAGRAM_PACKAGE);
                startActivity(Intent.createChooser(share, Constants.SHARE_TITLE));
                break;
            case ShareDialog.SHARE_MESSENGER:
                try {
                    ShareToMessengerParams shareToMessengerParams =
                            ShareToMessengerParams.newBuilder(uri, Constants.IMAGE_TYPE)
                                    .build();

                    MessengerUtils.shareToMessenger(
                            this,
                            1,
                            shareToMessengerParams);
                } catch (ActivityNotFoundException exception) {
                    exception.getLocalizedMessage();
                }
                break;
            case ShareDialog.SHARE_TWITER:
                shareMedia(Constants.TWITTER_PACKAGE, uri);
                break;
            case ShareDialog.SHARE_WHAT_APP:
                shareMedia(Constants.WHATAPP_PACKAGE, uri);
                break;
        }
    }

    public void shareMedia(String packageName, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(Constants.IMAGE_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        boolean installed = checkAppInstall(packageName);
        if (installed) {
            intent.setPackage(packageName);
            startActivity(Intent.createChooser(intent, Constants.SHARE_TITLE));
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


    @Override
    protected void onPause() {
        super.onPause();
        allowShowAds = false;

    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLoadAdsBack = true;
        if (checkShowRateApp) {
            checkShowRateApp = false;
            if (!Prefs.with(PreviewActivity.this).readBoolean("rate", false)) {
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
            Toast.makeText(PreviewActivity.this, getString(R.string.sms_thank_you_rate), Toast.LENGTH_SHORT).show();
            Prefs.with(PreviewActivity.this).writeBoolean("rate", true);
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
                Prefs.with(PreviewActivity.this).writeBoolean("rate", false);
            }
        });

        dialog1.setCancelable(false);
        dialog1.show();
    }
}
