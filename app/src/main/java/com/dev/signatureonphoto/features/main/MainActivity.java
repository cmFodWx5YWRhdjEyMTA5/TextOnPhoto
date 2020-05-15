package com.dev.signatureonphoto.features.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.dd.ShadowLayout;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.GroupFilter;
import com.dev.signatureonphoto.data.model.response.Filter;
import com.dev.signatureonphoto.data.model.response.StickerItem;
import com.dev.signatureonphoto.edittext.EditTextActivity;
import com.dev.signatureonphoto.features.base.BaseActivity;
import com.dev.signatureonphoto.features.preview.PreviewActivity;
import com.dev.signatureonphoto.features.purchased.PurchasedActivity;
import com.dev.signatureonphoto.features.saved.SavedActivity;
import com.dev.signatureonphoto.injection.component.ActivityComponent;
import com.dev.signatureonphoto.stickerview.AlignEvent;
import com.dev.signatureonphoto.stickerview.BitmapStickerIcon;
import com.dev.signatureonphoto.stickerview.DeleteIconEvent;
import com.dev.signatureonphoto.stickerview.DrawableSticker;
import com.dev.signatureonphoto.stickerview.Sticker;
import com.dev.signatureonphoto.stickerview.StickerIconEvent;
import com.dev.signatureonphoto.stickerview.StickerView;
import com.dev.signatureonphoto.stickerview.TextSticker;
import com.dev.signatureonphoto.stickerview.ZoomIconEvent;
import com.dev.signatureonphoto.util.AppPreference;
import com.dev.signatureonphoto.util.CheckInfoApp;
import com.dev.signatureonphoto.util.Config;
import com.dev.signatureonphoto.util.Constants;
import com.dev.signatureonphoto.util.FileUtil;
import com.dev.signatureonphoto.util.MyLog;
import com.dev.signatureonphoto.util.SimpleGestureFilter;
import com.dev.signatureonphoto.util.UtilAdsCrossAdaptive;
import com.dev.signatureonphoto.util.filter.ConstantsFilter;
import com.dev.signatureonphoto.util.filter.GPUImageFilterTools;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.dev.signatureonphoto.Constants.EXTRA_ADS_SAVED_IMAGE;
import static com.dev.signatureonphoto.Constants.EXTRA_EARN_REWARD_FULL_HD;
import static com.dev.signatureonphoto.Constants.EXTRA_EARN_REWARD_HD;
import static com.dev.signatureonphoto.Constants.EXTRA_EARN_REWARD_HQ;
import static com.dev.signatureonphoto.Constants.EXTRA_USE_FEATURE_GENERATE_FULL_HD;
import static com.dev.signatureonphoto.Constants.EXTRA_USE_FEATURE_GENERATE_HD;
import static com.dev.signatureonphoto.Constants.EXTRA_USE_FEATURE_GENERATE_HQ;
import static com.dev.signatureonphoto.Constants.GALLERY_REQUEST;
import static com.dev.signatureonphoto.Constants.GAME_SETTING;
import static com.dev.signatureonphoto.Constants.LIMIT_USE_FEATURE_GENERATE;
import static com.dev.signatureonphoto.Constants.MAX_ADS_SAVED_IMAGE;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;
import static com.dev.signatureonphoto.Constants.PRE_UNLOCK_ALL_FEATURE;
import static com.dev.signatureonphoto.util.Constants.KEY_TYPE;
import static com.dev.signatureonphoto.util.Constants.NAME_EXTRA_COLOR_BACKGROUND;
import static com.dev.signatureonphoto.util.Constants.NAME_EXTRA_TEMPLATE;
import static com.dev.signatureonphoto.util.Constants.NAME_EXTRA_URI;
import static com.dev.signatureonphoto.util.Constants.PERM_RQST_CODE_CAMERA;
import static com.dev.signatureonphoto.util.Constants.REQUEST_EDITTEXT;

public class MainActivity extends BaseActivity implements Event, FilterListener, MenuStickerListener {
    private static final String TAG = MainActivity.class.getName();
    private static final int ID_RELOAD_IMAGE = 1;
    private static final int ID_RELOAD_COLOR = 0;
    private static final int INDEX_LINE = 0;
    private static final int INDEX_TYPO = 1;
    private static final int INDEX_BORDER = 2;
    private final int XMAS_STICKER = 1;
    private final int OLD_STICKER = 0;

    @BindView(R.id.skbOpacity)
    SeekBar skbOpacity;
    @BindView(R.id.skbTextSize)
    SeekBar skbTextSize;
    @BindView(R.id.skbTextSpacing)
    SeekBar skbTextSpacing;
    @BindView(R.id.skbTextStroke)
    SeekBar skbTextStroke;
    @BindView(R.id.rcv_text_borrder_color)
    RecyclerView rcvTextBorrderColor;
    @BindView(R.id.txtstyle)
    TextView txtstyle;
    @BindView(R.id.txtOpacity)
    TextView txtOpacity;
    @BindView(R.id.txtTextSize)
    TextView txtTextSize;
    @BindView(R.id.txtTextSpacing)
    TextView txtTextSpacing;
    @BindView(R.id.txtTextStroke)
    TextView txtTextStroke;
    @BindView(R.id.txt_border_color)
    TextView txtBorderColor;
    @BindView(R.id.txtBold)
    TextView txtBold;
    @BindView(R.id.txtRegular)
    TextView txtRegular;
    @BindView(R.id.txtitalic)
    TextView txtitalic;
    private boolean purchaseFilterB, purchaseFilterC, purchasePro;
    private List<GroupFilter> listProductIdIAP;
    @BindView(R.id.img_background_photo)
    ImageView imgBackgroundPhoto;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.layout_option_main)
    LinearLayout llOptionMain;
    @BindView(R.id.layout_option_background)
    LinearLayout llOptionBackground;
    @BindView(R.id.layout_template)
    LinearLayout llTemplate;
    @BindView(R.id.layout_bg)
    LinearLayout llBG;
    @BindView(R.id.layout_text)
    LinearLayout layoutText;
    @BindView(R.id.layout_option_text)
    LinearLayout llOptionText;
    @BindView(R.id.layout_sticker)
    FrameLayout llSticker;
    @BindView(R.id.img_gone_background)
    ImageView imgGoneBackground;
    @BindView(R.id.img_gone_text)
    ImageView imgGoneText;
    @BindView(R.id.img_gone_sticker)
    ImageView imgGoneSticker;
    @BindView(R.id.layout_camera)
    LinearLayout btnCamera;
    @BindView(R.id.layout_color)
    LinearLayout btnColor;
    @BindView(R.id.shadow_your_image)
    ShadowLayout shadowYourImage;
    @BindView(R.id.layout_recycler)
    LinearLayout llRecycler;
    @BindView(R.id.rcv_sticker)
    RecyclerView rcvSticker;
    @BindView(R.id.menu_sticker)
    RecyclerView menuSticker;
    @BindView(R.id.layout_text_color)
    LinearLayout llTextColor;
    @BindView(R.id.txt_background)
    TextView txtBackground;
    @BindView(R.id.txt_text)
    TextView txtText;
    @BindView(R.id.txt_sticker)
    TextView txtSticker;
    @BindView(R.id.txt_ornament)
    TextView txtOrnament;
    @BindView(R.id.txt_back_ground)
    TextView txtBackGround;
    @BindView(R.id.txt_library)
    TextView txtLibrary;
    @BindView(R.id.txt_text_style)
    TextView txtTextStyle;
    @BindView(R.id.txt_color)
    TextView txtColor;
    @BindView(R.id.txt_align)
    TextView txtAlign;
    @BindView(R.id.txt_shadow)
    TextView txtShadow;
    @BindView(R.id.txt_neon)
    TextView txtNeon;
    @BindView(R.id.txt_line)
    TextView txtLine;
    @BindView(R.id.txt_typo)
    TextView txtTypo;
    @BindView(R.id.txt_border)
    TextView txtBorder;
    @BindView(R.id.layout_rcv_line)
    LinearLayout layoutRcvLine;
    @BindView(R.id.layout_option_typo)
    LinearLayout layoutOptionTypo;
    @BindView(R.id.layout_option_border)
    LinearLayout layoutOptionBorder;
    @BindView(R.id.sticker_view)
    StickerView stickerView;
    @BindView(R.id.img_add_text)
    ImageView imgAddText;
    @BindView(R.id.layout_text_style)
    LinearLayout llTextStyle;
    @BindView(R.id.rcv_text_style)
    RecyclerView rcvTextStyle;
    @BindView(R.id.layout_text_option_color)
    LinearLayout llTextOptionColor;
    @BindView(R.id.rcv_text_color)
    RecyclerView rcvTextColor;

    @BindView(R.id.layout_align)
    LinearLayout llAlign;
    @BindView(R.id.layout_align_left)
    LinearLayout llAlignLeft;
    @BindView(R.id.layout_align_center)
    LinearLayout llAlignCenter;
    @BindView(R.id.layout_align_right)
    LinearLayout llAlignRight;
    @BindView(R.id.txt_done)
    TextView txtDone;
    @BindView(R.id.layout_shadow)
    LinearLayout llShadowText;
    TextSticker textsticker;
    DrawableSticker drawableSticker;
    @BindView(R.id.layout_option_shadow)
    LinearLayout llTextOptionShadow;
    @BindView(R.id.img_gone_shadow)
    ImageView imgGoneShadow;
    @BindView(R.id.rcv_text_shadow)
    RecyclerView rcvTextShadow;
    @BindView(R.id.layout_option_neon)
    LinearLayout llTextOptionNeon;
    @BindView(R.id.img_gone_neon)
    ImageView imgGoneNeon;
    @BindView(R.id.rcv_text_neon)
    RecyclerView rcvTextNeon;

    @BindView(R.id.layout_list_text_color)
    LinearLayout layoutListTextColor;
    @BindView(R.id.layout_text_neon)
    LinearLayout layoutTextNeon;
    @BindView(R.id.layout_rcv_text_shadow)
    LinearLayout layoutRcvTextShadow;
    @BindView(R.id.layout_rcv_sticker)
    LinearLayout layoutRcvSticker;
    @BindView(R.id.layout_save)
    LinearLayout llSave;
    @BindView(R.id.layout_ornament)
    LinearLayout llOrnament;
    @BindView(R.id.layoutImage)
    LinearLayout layoutImage;
    @BindView(R.id.pb_load_image)
    ProgressBar pbLoadImage;
    // Ornament
    @BindView(R.id.layout_option_ornament)
    LinearLayout layoutOptionOrnament;
    @BindView(R.id.layout_ornament_color)
    LinearLayout layoutOrnamentColor;
    @BindView(R.id.layout_option_line)
    LinearLayout layoutOptionLine;
    @BindView(R.id.rcv_line)
    RecyclerView rcvLine;
    @BindView(R.id.rcv_border)
    RecyclerView rcvBorder;
    @BindView(R.id.rcv_typo)
    RecyclerView rcvTypo;
    @BindView(R.id.rcv_ornament_color)
    RecyclerView rcvOrnamentColor;
    @BindView(R.id.list_filter)
    RecyclerView listFilter;
    @BindView(R.id.layout_filter)
    View layoutFilter;
    @BindView(R.id.disable_view)
    View disableView;
    @BindView(R.id.filter_loading)
    ProgressBar filterLoading;

    @BindView(R.id.img_preview)
    ImageView imgPreview;

    @BindView(R.id.layout_adaptive)
    RelativeLayout layoutAdaptive;

    private int numcount, total_count;
    @BindViews({R.id.layout_filter, R.id.layout_option_line, R.id.layout_ornament_color,
            R.id.layout_option_ornament, R.id.layout_recycler,/* R.id.layout_option_text_2,*/
            R.id.layout_text_option_color, R.id.layout_option_neon, R.id.layout_option_border,
            R.id.layout_option_shadow, /*R.id.layout_option_text_align,*/ R.id.layout_option_typo,
            R.id.layout_option_background, R.id.layout_option_text, R.id.layout_option_main})
    List<View> optionViews;
    FontTextAdapter fontAdapter;
    int finalHeight, finalWidth;
    float w = 0, h = 0, maxH;
    int width, height;
    ProgressDialog loadingDialog, noOnlineDialog;
    Dialog selectSizeDialog;
    Dialog dialog;
    PointF point;
    private StickerAdapter stickerAdapter;
    private Unbinder unbinder;
    private Activity activity;
    private ColorTextAdapter colorTextAdapter;
    private ColorTextShadowAdapter colorShadowAdapter;
    private ColorTextShadowAdapter colorNeonAdapter;
    private ColorTextShadowAdapter colorBorderTextAdapter;
    private float scale = 0;
    private String quote;
    private StickerAdapter lineAdapter, typoAdapter, borderAdapter;
    private ColorTextAdapter colorOrnamentAdapter;
    private int currentOrnament;
    private SharedPreferences sharedPreferences;
    private DisplayMetrics metrics;
    private String lang, pathFont;
    private MainPresenter presenter;
    private GPUImageFilterTools gpuImageFilterTools;
    private FilterAdapter filterAdapter;
    private LinearLayoutManager manager;
    private Bitmap bitmap = null;
    private MenuStickerAdapter menuStickerAdapter;
    private int idImage;
    private String uriImage;
    private int posQuality;
    private int idColor;
    private int imageMode = 0;
    private int sdSize = 480;
    private int hqSize = 720;
    private int hdSize = 1080;
    private int fullHdSize = 1920;
    private int currentSize = 1920;
    private String filterPath;
    public int currentPositionQuality = 0;
    private static final int AUTO_SCREEN = 0;
    private static final int SD_PX = 1;
    private static final int HQ_PX = 2;
    private static final int HD_PX = 3;
    private static final int FULL_HD_PX = 4;
    private final int totalCountFree = 5;
    private int pro;

    private InterstitialAd mInterstitialAdTips;
    private boolean checkLoadAdsBack = true;
    private AdView adView;
    private int SELECTED_RESOLUTION = 0;

    private RewardedAd rewardedAd;
    private RewardedAdCallback adCallback;
    private Disposable disposable;
    private boolean checkEarnedReward = false;
    private BillingClient billingClient;
    private boolean checkBuyFeature;
    private boolean checkLoadedReward;
    private String priceFeatureGenerate = "";
    private TextView txtTrailFullHD;
    private TextView txtTrailHD;
    private TextView txtTrailHQ;
    private TextView txtPrice;
    private boolean checkFilterPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this);
        listProductIdIAP = new ArrayList<>();
        listProductIdIAP.add(new GroupFilter(true));
        gpuImageFilterTools = new GPUImageFilterTools(this);
        activity = MainActivity.this;
        metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        unbinder = ButterKnife.bind(this);
        point = new PointF();
        lang = Locale.getDefault().getLanguage();
        loadingDialog = new ProgressDialog(this);
        Window window = loadingDialog.getWindow();
        if (window != null)
            window.requestFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setMessage(getString(R.string.cheking_internet));
        loadingDialog.setCancelable(false);
        dialog = new Dialog(this);
        window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        TextView textView = new TextView(this);
        textView.setPadding(25, 25, 25, 25);
        textView.setText(getString(R.string.open_internet));
        dialog.setContentView(textView);
        selectSizeDialog = new Dialog(this, R.style.Theme_AppCompat_Dialog_Alert);
        window = selectSizeDialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        selectSizeDialog.setContentView(R.layout.layout_select_size_dialog);
        txtTrailFullHD = selectSizeDialog.findViewById(R.id.txt_trail_full_hd);
        txtTrailHD = selectSizeDialog.findViewById(R.id.txt_trail_hd_label);
        txtTrailHQ = selectSizeDialog.findViewById(R.id.txt_trail_hq_label);
        txtPrice = selectSizeDialog.findViewById(R.id.txt_price);
        selectSizeEvent();
        sharedPreferences = getSharedPreferences(GAME_SETTING, Context.MODE_PRIVATE);
        idImage = getIntent().getIntExtra(NAME_EXTRA_TEMPLATE, 0);
        if (idImage != 0) {
            imageMode = 1;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), idImage, options);
            h = options.outHeight;
            w = options.outWidth;
            Glide.with(this).load(idImage).thumbnail(0.1f).into(imgBackgroundPhoto);
            reloadView(ID_RELOAD_IMAGE, true);
        }
        uriImage = getIntent().getStringExtra(NAME_EXTRA_URI);
        if (uriImage != null) {
            imageMode = 2;
            Uri uri = Uri.parse(uriImage);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(uri.getPath(), options);
            h = options.outHeight;
            w = options.outWidth;
            Glide.with(this).load(uriImage).into(imgBackgroundPhoto);
            reloadView(ID_RELOAD_IMAGE, true);
        }
        idColor = getIntent().getIntExtra(NAME_EXTRA_COLOR_BACKGROUND, 0);
        if (idColor != 0) {
            imageMode = 0;
            imgBackgroundPhoto.setImageResource(idColor);
            reloadView(ID_RELOAD_COLOR, true);
        }
        layoutImage.post(() -> {
            maxH = layoutImage.getHeight();
        });


        menuStickerAdapter = new MenuStickerAdapter(this, Constants.getMenuSticker(), this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        menuSticker.setLayoutManager(manager);
        menuSticker.setAdapter(menuStickerAdapter);
        stickerAdapter = new StickerAdapter(Constants.getImageSticker(), MainActivity.this, (view, position) -> {
            Drawable drawable =
                    ContextCompat.getDrawable(MainActivity.this, Constants.getImageStickerPreview().get(position).getImageSticker());
            stickerView.addSticker(new DrawableSticker(drawable, DrawableSticker.TYPE_NORMAL));
        });
        rcvSticker.setAdapter(stickerAdapter);
        rcvSticker.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 5);
        rcvSticker.setLayoutManager(gridLayoutManager);

        setStickersLineAdapter();

        setStickerBordersAdapter();

        setStickersTypographyAdapter();

        setColorBorderAdapter();

        setColorOrnamentAdapter();

        //Font adapter
        setFontAdapter();

        setColorTextAdapter();

        setColorTextShadowAdapter();

        setColorNeonAdapter();

        setColorBorderAdapter();

        listenerStickerView();
        noOnlineDialog = new ProgressDialog(MainActivity.this);
        Window window2 = noOnlineDialog.getWindow();
        if (window2 != null)
            window2.requestFeature(Window.FEATURE_NO_TITLE);
        btnColor.setVisibility(View.GONE);
        shadowYourImage.setVisibility(View.GONE);
        listenerSeekbar();

        if (AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            layoutAdaptive.setVisibility(View.GONE);
        } else {
            initAdsBanner();
            initReward();
        }
    }


    private void initReward() {
        checkEarnedReward = false;
        rewardedAd = new RewardedAd(this,
                getString(R.string.reward_click_button_full_hd));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                checkLoadedReward = true;
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                checkLoadedReward = false;
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        //Set call back
        adCallback = new RewardedAdCallback() {
            @Override
            public void onRewardedAdOpened() {
            }

            @Override
            public void onRewardedAdClosed() {
                if (!checkEarnedReward) {
                    AppPreference.getInstance(MainActivity.this).setKeyRate(EXTRA_EARN_REWARD_HQ, false);
                    AppPreference.getInstance(MainActivity.this).setKeyRate(EXTRA_EARN_REWARD_HD, false);
                    AppPreference.getInstance(MainActivity.this).setKeyRate(EXTRA_EARN_REWARD_FULL_HD, false);
                    initReward();
                } else {
                    checkLoadedReward = false;
                }
            }

            @Override
            public void onUserEarnedReward(@NonNull RewardItem reward) {
                // User earned reward.
                if (currentSize == hqSize) {
                    checkEarnedReward = true;
                    AppPreference.getInstance(MainActivity.this).setKeyRate(EXTRA_EARN_REWARD_HQ, true);
                    AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HQ, 0);
                } else if (currentSize == hdSize) {
                    checkEarnedReward = true;
                    AppPreference.getInstance(MainActivity.this).setKeyRate(EXTRA_EARN_REWARD_HD, true);
                    AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HD, 0);
                } else if (currentSize == fullHdSize) {
                    checkEarnedReward = true;
                    AppPreference.getInstance(MainActivity.this).setKeyRate(EXTRA_EARN_REWARD_FULL_HD, true);
                    AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_USE_FEATURE_GENERATE_FULL_HD, 0);
                }

            }

            @Override
            public void onRewardedAdFailedToShow(int errorCode) {
                // Ad failed to display
            }
        };
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            return cm.getActiveNetworkInfo() != null;
        }
    }

    private void listenerStickerView() {
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    textsticker = (TextSticker) sticker;
                }
                List<BitmapStickerIcon> icons = stickerView.getIcons();
                if (sticker instanceof DrawableSticker) {
                    if (icons.size() == 4) {
                        icons.remove(3);
                    }
                } else {
                    if (icons.size() < 4) {
                        BitmapStickerIcon backgroundIcon;
                        if (sticker.isBackgroundEnable()) {
                            backgroundIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.ic_change_background_blue),
                                    BitmapStickerIcon.LEFT_TOP);
                        } else {
                            backgroundIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.ic_change_background),
                                    BitmapStickerIcon.LEFT_TOP);
                        }
                        backgroundIcon.setIconEvent(new StickerIconEvent() {
                            @Override
                            public void onActionDown(StickerView stickerView, MotionEvent event) {
                                Sticker sticker = stickerView.getCurrentSticker();
                                if (sticker != null) {
                                    if (sticker.isBackgroundEnable()) {
                                        sticker.setBackgroundEnable(false);
                                    } else {
                                        sticker.setBackgroundEnable(true);
                                    }
                                }
                            }

                            @Override
                            public void onActionMove(StickerView stickerView, MotionEvent event) {
                            }

                            @Override
                            public void onActionUp(StickerView stickerView, MotionEvent event) {
                            }
                        });
                        icons.add(backgroundIcon);
                        stickerView.invalidate();
                    }
                }
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    showView(R.id.layout_option_text);
                }

                if (stickerView.getCurrentSticker() instanceof DrawableSticker) {
                    if (((DrawableSticker) stickerView.getCurrentSticker()).getTypeSticker() != DrawableSticker.TYPE_NORMAL) {
                        showView(R.id.layout_ornament_color);
                    }

                    if (((DrawableSticker) stickerView.getCurrentSticker()).getTypeSticker() == DrawableSticker.TYPE_LINE) {
                        currentOrnament = INDEX_LINE;
                    }
                    if (((DrawableSticker) stickerView.getCurrentSticker()).getTypeSticker() == DrawableSticker.TYPE_BORDER) {
                        currentOrnament = INDEX_BORDER;
                    }
                    if (((DrawableSticker) stickerView.getCurrentSticker()).getTypeSticker() == DrawableSticker.TYPE_TYPO) {
                        currentOrnament = INDEX_TYPO;
                    }


                    if (((DrawableSticker) stickerView.getCurrentSticker()).getTypeSticker() == DrawableSticker.TYPE_NORMAL) {
                        showView(R.id.layout_recycler);
                    }
                }
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                if (sticker instanceof TextSticker) {
                } else {
                }
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    textsticker = (TextSticker) sticker;
                }
                RectF rectF = sticker.getMappedBound();
                point.x = rectF.centerX();
                point.y = rectF.centerY();
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                List<BitmapStickerIcon> icons = stickerView.getIcons();
                if (sticker instanceof DrawableSticker) {
                    if (icons.size() == 4) {
                        icons.remove(3);
                    }
                } else {
                    if (icons.size() < 4) {
                        BitmapStickerIcon backgroundIcon;
                        if (sticker.isBackgroundEnable()) {
                            backgroundIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.ic_swap_bg),
                                    BitmapStickerIcon.LEFT_TOP);
                        } else {
                            backgroundIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.ic_swap_press),
                                    BitmapStickerIcon.LEFT_TOP);
                        }
                        backgroundIcon.setIconEvent(new StickerIconEvent() {
                            @Override
                            public void onActionDown(StickerView stickerView, MotionEvent event) {
                                Sticker sticker = stickerView.getCurrentSticker();
                                if (sticker != null) {
                                    if (sticker.isBackgroundEnable()) {
                                        sticker.setBackgroundEnable(false);
                                    } else {
                                        sticker.setBackgroundEnable(true);
                                    }
                                }
                            }

                            @Override
                            public void onActionMove(StickerView stickerView, MotionEvent event) {
                            }

                            @Override
                            public void onActionUp(StickerView stickerView, MotionEvent event) {
                            }
                        });
                        icons.add(backgroundIcon);
                        stickerView.invalidate();
                    } else {
                        if (sticker.isBackgroundEnable()) {
                            stickerView.getIcons().get(3).setDrawable(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.ic_swap_bg));
                        } else {
                            stickerView.getIcons().get(3).setDrawable(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.ic_swap_press));
                        }
                    }
                }
                if (sticker instanceof TextSticker) {
                    colorTextAdapter.selectedPosition = ((TextSticker) sticker).getCurrentTextColor();
                    colorTextAdapter.notifyDataSetChanged();
                    fontAdapter.selectedPosition = ((TextSticker) sticker).getCurrentTypeface();
                    fontAdapter.notifyDataSetChanged();
                    colorShadowAdapter.selectedPosition = ((TextSticker) sticker).getShadowType();
                    colorShadowAdapter.notifyDataSetChanged();
                    colorNeonAdapter.selectedPosition = ((TextSticker) sticker).getNeonType();
                    colorNeonAdapter.notifyDataSetChanged();
                }
                scale = sticker.getCurrentScale();
                sticker.rotate = true;
                RectF rectF = sticker.getMappedBound();
                point.x = rectF.centerX();
                point.y = rectF.centerY();
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                scale = sticker.getCurrentScale();
                sticker.rotate = true;
                RectF rectF = sticker.getMappedBound();
                point.x = rectF.centerX();
                point.y = rectF.centerY();
                if (sticker instanceof TextSticker) {
                } else {
                }
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    Intent intent = new Intent(MainActivity.this, EditTextActivity.class);
                    if (quote == null) {
                        quote = getString(R.string.double_click_to_edit);
                    }
                    intent.putExtra("quote", quote);
                    startActivityForResult(intent, REQUEST_EDITTEXT);
                }
            }
        });
    }

    private void initAdsBanner() {
        layoutAdaptive.addView(UtilAdsCrossAdaptive.getLayoutCross(MainActivity.this, CheckInfoApp.initListCrossAdaptive()));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_screen_edit));
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
                layoutAdaptive.removeAllViews();
                layoutAdaptive.addView(adView);
                layoutAdaptive.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                layoutAdaptive.removeAllViews();
                layoutAdaptive.addView(UtilAdsCrossAdaptive.getLayoutCross(MainActivity.this, CheckInfoApp.initListCrossAdaptive()));
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

    private void setColorOrnamentAdapter() {
        colorOrnamentAdapter = new ColorTextAdapter(Constants.getColorText(), MainActivity.this, new ColorTextAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                if (stickerView.getCurrentSticker() instanceof DrawableSticker) {
                    drawableSticker = (DrawableSticker) stickerView.getCurrentSticker();
                    drawableSticker.getDrawable().setColorFilter(getResources().getColor(Constants.getColorText().get(position).getIdColor()), PorterDuff.Mode.SRC_ATOP);
                    stickerView.invalidate();
                }
            }
        });
        rcvOrnamentColor.setAdapter(colorOrnamentAdapter);
        rcvOrnamentColor.setHasFixedSize(true);
        GridLayoutManager gridLayoutManagerColorOrnament = new GridLayoutManager(MainActivity.this, 5);
        rcvOrnamentColor.setLayoutManager(gridLayoutManagerColorOrnament);
    }

    private void setColorBorderAdapter() {
        colorBorderTextAdapter = new ColorTextShadowAdapter(Constants.getColorTextBorder(), true, MainActivity.this, new ColorTextShadowAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    textsticker.setBorderTextColor(getResources().getColor(Constants.getColorTextBorder().get(position).getIdColor()));
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
            }
        });
        rcvTextBorrderColor.setAdapter(colorBorderTextAdapter);
        rcvTextBorrderColor.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerStroke = new LinearLayoutManager(this);
        linearLayoutManagerStroke.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTextBorrderColor.setLayoutManager(linearLayoutManagerStroke);
    }

    private void setColorNeonAdapter() {
        colorNeonAdapter = new ColorTextShadowAdapter(Constants.getColorTextShadow(), false, MainActivity.this, new ColorTextShadowAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    if (position == 0) {
                        textsticker.setTextNeonDefault(8, 0, 0, position);
                    } else {
                        textsticker.setTextNeon(8, 0, 0, getResources().getColor(Constants.getColorTextShadow().get(position).getIdColor()), position);
                    }
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
            }
        });
        rcvTextNeon.setAdapter(colorNeonAdapter);
        rcvTextNeon.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerNeon = new LinearLayoutManager(this);
        linearLayoutManagerNeon.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTextNeon.setLayoutManager(linearLayoutManagerNeon);
    }

    private void setColorTextShadowAdapter() {
        colorShadowAdapter = new ColorTextShadowAdapter(Constants.getColorTextShadow(), false, MainActivity.this, new ColorTextShadowAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    if (position == 0) {
                        textsticker.setShadowLayer(1, 1, 1, getResources().getColor(R.color.colordefault), position);
                    } else {
                        textsticker.setShadowLayer(1, 1, 1, getResources().getColor(Constants.getColorTextShadow().get(position).getIdColor()), position);
                    }
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
            }
        });
        rcvTextShadow.setAdapter(colorShadowAdapter);
        rcvTextShadow.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerShadow = new LinearLayoutManager(this);
        linearLayoutManagerShadow.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTextShadow.setLayoutManager(linearLayoutManagerShadow);
    }

    private void setColorTextAdapter() {
        colorTextAdapter = new ColorTextAdapter(Constants.getColorText(), MainActivity.this, new ColorTextAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    textsticker.setTextColor(getResources().getColor(Constants.getColorText().get(position).getIdColor()), position);
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
            }
        });
        rcvTextColor.setAdapter(colorTextAdapter);
        rcvTextColor.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerColor = new LinearLayoutManager(this);
        linearLayoutManagerColor.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTextColor.setLayoutManager(linearLayoutManagerColor);
    }

    private void setStickersTypographyAdapter() {
        final ArrayList<StickerItem> listStickersTypography = StickersPreseneter.getInstance().getListSticker(this, "stickers/typography");
        typoAdapter = new StickerAdapter(listStickersTypography, MainActivity.this, new StickerAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                Glide.with(getBaseContext())
                        .asBitmap()
                        .load(Uri.parse("file:///android_asset/" + listStickersTypography.get(position).getPath()))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(getResources(), resource);
                                stickerView.addSticker(new DrawableSticker(drawable, DrawableSticker.TYPE_TYPO));
                            }
                        });

                currentOrnament = INDEX_TYPO;
                showView(R.id.layout_ornament_color);
            }
        });
        rcvTypo.setAdapter(typoAdapter);
        rcvTypo.setHasFixedSize(true);
        GridLayoutManager gridTypoManager = new GridLayoutManager(MainActivity.this, 4);
        rcvTypo.setLayoutManager(gridTypoManager);
    }

    private void setStickersLineAdapter() {
        final ArrayList<StickerItem> listStickersLine = StickersPreseneter.getInstance().getListSticker(this, "stickers/line");
        lineAdapter = new StickerAdapter(listStickersLine, MainActivity.this, (view, position) -> {
            Glide.with(getBaseContext())
                    .asBitmap()
                    .load(Uri.parse("file:///android_asset/" + listStickersLine.get(position).getPath()))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(getResources(), resource);
                            stickerView.addSticker(new DrawableSticker(drawable, DrawableSticker.TYPE_LINE));
                        }
                    });


            currentOrnament = INDEX_LINE;
            showView(R.id.layout_ornament_color);
        });
        rcvLine.setAdapter(lineAdapter);
        rcvLine.setHasFixedSize(true);
        GridLayoutManager gridLineManager = new GridLayoutManager(MainActivity.this, 4);
        rcvLine.setLayoutManager(gridLineManager);
    }

    private void setStickerBordersAdapter() {
        final ArrayList<StickerItem> listStickersBorder = StickersPreseneter.getInstance().getListSticker(this, "stickers/border");
        borderAdapter = new StickerAdapter(listStickersBorder, MainActivity.this, new StickerAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                Glide.with(getBaseContext())
                        .asBitmap()
                        .load(Uri.parse("file:///android_asset/" + listStickersBorder.get(position).getPath()))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(getResources(), resource);
                                stickerView.addSticker(new DrawableSticker(drawable, DrawableSticker.TYPE_BORDER));
                            }
                        });
                currentOrnament = INDEX_BORDER;
                showView(R.id.layout_ornament_color);
            }
        });
        rcvBorder.setAdapter(borderAdapter);
        rcvBorder.setHasFixedSize(true);
        GridLayoutManager gridBorderManager = new GridLayoutManager(MainActivity.this, 4);
        rcvBorder.setLayoutManager(gridBorderManager);
    }

    private void setFontAdapter() {
        fontAdapter = new FontTextAdapter(getListFontAssets(), MainActivity.this, (view1, position) -> {
            if (stickerView.getCurrentSticker() instanceof TextSticker) {
                textsticker = (TextSticker) stickerView.getCurrentSticker();
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/" + getListFontAssets().get(position));

                textsticker.setTypeface(typeface, position);
                textsticker.resizeText();
                stickerView.invalidate();
            }
        });
        rcvTextStyle.setAdapter(fontAdapter);
        rcvTextStyle.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTextStyle.setLayoutManager(linearLayoutManager);
    }

    private ArrayList<String> getListFontAssets() {
        ArrayList<String> fonts = new ArrayList<>();
        AssetManager assetManager = getAssets();
        try {
            String[] list = assetManager.list("fonts");
            fonts.addAll(Arrays.asList(list));
        } catch (IOException e) {

        }
        return fonts;
    }


    private void reloadView(int id, boolean addSticker) {
        pbLoadImage.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            if (w <= 0 && id != 0 || h <= 0 && id != 0) {
                Toast.makeText(getBaseContext(), getString(R.string.image_can_not_load), Toast.LENGTH_LONG).show();
            } else {
                if (w > h) {
                    h = (metrics.widthPixels * h) / w;
                    imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) h));
                    stickerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) h));
                } else if (w <= h) {
                    w = (w * maxH) / h;
                    imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams((int) w, ViewGroup.LayoutParams.MATCH_PARENT));
                    stickerView.setLayoutParams(new LinearLayout.LayoutParams((int) w, ViewGroup.LayoutParams.MATCH_PARENT));
                } else {
                    imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    stickerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                if (id == 0) {
                    imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    stickerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                loadSticker(addSticker);
                pbLoadImage.setVisibility(View.INVISIBLE);
            }
        }, 500);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view1, View view2) {
        view1.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_movedown));
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_moveup);
        animation.setStartOffset(250);
        view2.startAnimation(animation);
        view2.setVisibility(View.VISIBLE);
        view1.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayout() {
        return R.layout.main_activity;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        createFontText();
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listFilter.setLayoutManager(manager);
    }

    @Override
    protected void detachPresenter() {
    }

    @OnClick({R.id.tv_home, R.id.layout_save, R.id.layout_template, R.id.layout_bg, R.id.layout_text, R.id.img_gone_background, R.id.img_gone_text, R.id.layout_recycler, R.id.img_gone_sticker, R.id.layout_sticker, R.id.layout_camera, R.id.layout_add_text,
            R.id.img_gone_text_color, R.id.layout_align_left,
            R.id.layout_align_center, R.id.layout_align_right, R.id.img_gone_shadow, R.id.img_gone_neon, R.id.layout_ornament,
            R.id.img_gone_ornament, R.id.img_gone_ornament_color, R.id.img_gone_line, R.id.img_gone_typo, R.id.img_gone_border, R.id.layout_ornament_line, R.id.layout_ornament_typo, R.id.layout_ornament_border,
            R.id.layout_overlay, R.id.btn_filter_layout_back, R.id.disable_view, R.id.layout_iap_pro})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
                showDialogSave();
                break;
            case R.id.layout_bg:
                showView(R.id.layout_option_background);
                break;
            case R.id.layout_text:
                showView(R.id.layout_option_text);
                break;
            case R.id.img_gone_background:
                showView(R.id.layout_option_main);
                break;
            case R.id.img_gone_text:
                showView(R.id.layout_option_main);
                break;
            case R.id.layout_sticker:
//                AlertDialog.Builder builder=new AlertDialog.Builder(this);
//                builder.setTitle("Feature stickers");
//                builder.setMessage("This feature is for loyal customers only!");
//                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                builder.show();
////                showView(R.id.layout_recycler);


                break;
            case R.id.img_gone_sticker:
                showView(R.id.layout_option_ornament);
                break;
            case R.id.layout_camera:
                readExternalPermission();
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, PERM_RQST_CODE_CAMERA);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                }
                break;
            case R.id.layout_template:
                break;
            case R.id.layout_add_text:
                loadSticker(true);
                break;


            case R.id.img_gone_text_color:
                showView(R.id.layout_option_text);
                break;

            case R.id.layout_align_left:
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    textsticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
                break;
            case R.id.layout_align_center:
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    textsticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
                break;
            case R.id.layout_align_right:
                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                    textsticker = (TextSticker) stickerView.getCurrentSticker();
                    textsticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
                break;
            case R.id.img_gone_shadow:
                showView(R.id.layout_option_text);
                break;
            case R.id.img_gone_neon:
                showView(R.id.layout_option_text);
                break;
            case R.id.layout_save:
                if (checkFilterPro) {
                    if (AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                            AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                        processSavedPhoto();
                    } else {
                        showDialogItemPro();
                    }
                } else {
                    processSavedPhoto();
                }
                break;
            case R.id.layout_ornament:
                showView(R.id.layout_option_ornament);
                break;
            case R.id.img_gone_ornament:
                showView(R.id.layout_option_main);
                break;
            case R.id.img_gone_ornament_color:
                backColorOrnament();
                break;
            case R.id.img_gone_line:
                showView(R.id.layout_option_ornament);
                break;
            case R.id.img_gone_typo:
                showView(R.id.layout_option_ornament);
                break;
            case R.id.img_gone_border:
                showView(R.id.layout_option_ornament);
                break;
            case R.id.layout_ornament_line:
                showView(R.id.layout_option_line);
                break;
            case R.id.layout_ornament_typo:
                showView(R.id.layout_option_typo);
                break;
            case R.id.layout_ornament_border:
                showView(R.id.layout_option_border);
                break;
            case R.id.layout_overlay:
                if (stickerView.getWidth() > 0 && stickerView.getWidth() > 0) {
                    showView(R.id.layout_filter);
                    stickerView.setDrawSticker(false);
                    if (bitmap == null) {
                        filterLoading.setVisibility(View.VISIBLE);
                        bitmap = stickerView.createBitmap();
                        presenter.loadFillterList(this, bitmap, gpuImageFilterTools);
                    }
                    stickerView.setDrawSticker(true);
                }
                break;
            case R.id.btn_filter_layout_back:
                stickerView.setDrawSticker(true);
                showView(R.id.layout_option_main);
                break;
            case R.id.disable_view:
                break;
            case R.id.layout_iap_pro:
                startActivity(new Intent(MainActivity.this, PurchasedActivity.class));
                break;
        }
    }

    private void processSavedPhoto() {
        checkEnableFeature();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERM_RQST_CODE_WRITE);
        } else {
            selectSizeDialog.show();
        }
    }

    private void scaleStickers(float scaleX, float scaleY) {
        List<Sticker> stickers = stickerView.getStickers();
        for (Sticker sticker : stickers) {
            sticker.scaleMatrix(scaleX, scaleY);
        }
        stickerView.invalidate();
    }

    private void resizeColorImage() {
        stickerView.setVisibility(View.INVISIBLE);
        Bitmap bitmap = stickerView.createBitmap();
        imgPreview.setImageBitmap(bitmap);
        imgPreview.setVisibility(View.VISIBLE);
        int fixHeight = (currentSize * height) / width;
        float scaleX, scaleY;
        scaleX = (float) currentSize / width;
        scaleY = (float) fixHeight / height;
        scaleStickers(scaleX, scaleY);
        stickerView.setLayoutParams(new LinearLayout.LayoutParams(currentSize, fixHeight));
        imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams(currentSize, fixHeight));
        imgBackgroundPhoto.post(new Runnable() {
            @Override
            public void run() {
                stickerView.post(new Runnable() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void run() {
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                return stickerView.save(MainActivity.this, FileUtil.getNewFile(MainActivity.this, "QuotePhoto"));
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                int fixHeight = (currentSize * height) / width;
                                float scaleX, scaleY;
                                scaleX = (float) width / currentSize;
                                scaleY = (float) height / fixHeight;
                                scaleStickers(scaleX, scaleY);
                                loadingDialog.hide();

                                if (AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                                        AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                                    showPreviewScreenNormal(s);
                                } else {
                                    int countExitApp = AppPreference.getInstance(MainActivity.this).getKeyShowAds(EXTRA_ADS_SAVED_IMAGE, 0);
                                    countExitApp++;
                                    if (countExitApp >= MAX_ADS_SAVED_IMAGE) {
                                        AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_ADS_SAVED_IMAGE, 0);
                                        loadAdsInterstitialSaved(s, "Normal");
                                        loadInterstitialAdSaved();
                                    } else {
                                        AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_ADS_SAVED_IMAGE, countExitApp);
                                        showPreviewScreenNormal(s);
                                    }
                                }
                            }
                        }.execute();
                    }
                });
            }
        });
    }

    private void resizeImage() {
        Object data;
        if (imageMode == 1) {
            data = idImage;
        } else {
            data = uriImage;
        }
        stickerView.setVisibility(View.INVISIBLE);
        Bitmap bitmap = stickerView.createBitmap();
        imgPreview.setImageBitmap(bitmap);
        imgPreview.setVisibility(View.VISIBLE);
        int fixHeight = (currentSize * height) / width;
        float scaleX, scaleY;
        scaleX = (float) currentSize / width;
        scaleY = (float) fixHeight / height;
        scaleStickers(scaleX, scaleY);
        stickerView.setLayoutParams(new LinearLayout.LayoutParams(currentSize, fixHeight));
        imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams(currentSize, fixHeight));
        imgBackgroundPhoto.post(new Runnable() {
            @Override
            public void run() {
                stickerView.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this)
                                .load(data)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @SuppressLint("StaticFieldLeak")
                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (filterPath != null) {
                                                    try {
                                                        Bitmap b = ((BitmapDrawable) resource).getBitmap();
                                                        InputStream inputStream = getAssets().open(filterPath);
                                                        gpuImageFilterTools.filterImage(b, inputStream);
                                                        Bitmap filterBitmap = gpuImageFilterTools.getBitmap();
                                                        imgBackgroundPhoto.setImageBitmap(filterBitmap);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                new AsyncTask<Void, Void, String>() {
                                                    @Override
                                                    protected String doInBackground(Void... voids) {
                                                        return stickerView.save(MainActivity.this, FileUtil.getNewFile(MainActivity.this, "QuotePhoto"));
                                                    }

                                                    @Override
                                                    protected void onPostExecute(String imagePath) {
                                                        super.onPostExecute(imagePath);
                                                        File file = new File(imagePath);
                                                        if (file.exists()) {
                                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                                            options.inJustDecodeBounds = true;
                                                            BitmapFactory.decodeFile(imagePath, options);
                                                            int imageWidth = options.outWidth;
                                                            if (imageWidth != currentSize) {
                                                                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.memory_warning), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                        int fixHeight = (currentSize * height) / width;
                                                        float scaleX, scaleY;
                                                        scaleX = (float) width / currentSize;
                                                        scaleY = (float) height / fixHeight;
                                                        scaleStickers(scaleX, scaleY);
                                                        loadingDialog.hide();


                                                        stickerView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                                                        imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams(width, height));
                                                        imgPreview.setVisibility(View.GONE);
                                                        stickerView.setVisibility(View.VISIBLE);
                                                        System.gc();

                                                        //Forward preview
                                                        if (AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                                                                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                                                            showPreviewScreen(imagePath);
                                                        } else {
                                                            int countExitApp = AppPreference.getInstance(MainActivity.this).getKeyShowAds(EXTRA_ADS_SAVED_IMAGE, 0);
                                                            countExitApp++;
                                                            if (countExitApp >= MAX_ADS_SAVED_IMAGE) {
                                                                AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_ADS_SAVED_IMAGE, 0);
                                                                loadAdsInterstitialSaved(imagePath, "FullHD");
                                                                loadInterstitialAdSaved();
                                                            } else {
                                                                AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_ADS_SAVED_IMAGE, countExitApp);
                                                                showPreviewScreen(imagePath);
                                                            }
                                                        }
                                                    }
                                                }.execute();
                                            }
                                        }, 500);
                                        return false;
                                    }
                                })
                                .into(imgBackgroundPhoto);
                    }
                });
            }
        });
    }


    private void loadAdsInterstitialSaved(String imagePath, String type) {
        checkLoadAdsBack = true;
        mInterstitialAdTips = new InterstitialAd(this);
        mInterstitialAdTips.setAdUnitId(getString(R.string.full_screen_saved_image));
        mInterstitialAdTips.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_ADS_SAVED_IMAGE, 0);
                if (type.equals("FullHD")) {
                    showPreviewScreen(imagePath);
                } else if (type.equals("Normal")) {
                    showPreviewScreenNormal(imagePath);
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAdTips.isLoaded() && checkLoadAdsBack) {
                    mInterstitialAdTips.show();
                    checkLoadAdsBack = false;
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (checkLoadAdsBack) {
                    checkLoadAdsBack = false;
                    if (type.equals("FullHD")) {
                        showPreviewScreen(imagePath);
                    } else if (type.equals("Normal")) {
                        showPreviewScreenNormal(imagePath);
                    }
                }
            }
        });

    }

    private void showPreviewScreen(String imagePath) {
        Intent intentSave = new Intent(MainActivity.this, PreviewActivity.class);
        intentSave.putExtra("url", imagePath);
        intentSave.putExtra("SELECT_RESOLUTION", SELECTED_RESOLUTION);
        intentSave.putExtra(Constants.CURRENT_QUALITY, posQuality);
        startActivity(intentSave);
    }

    private void showPreviewScreenNormal(String path) {
        Intent intentSave = new Intent(MainActivity.this, PreviewActivity.class);
        intentSave.putExtra("url", path);
        intentSave.putExtra("SELECT_RESOLUTION", SELECTED_RESOLUTION);
        intentSave.putExtra(Constants.CURRENT_QUALITY, posQuality);
        startActivity(intentSave);
        stickerView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        imgBackgroundPhoto.setLayoutParams(new StickerView.LayoutParams(width, height));
        new Handler().postDelayed(() -> {
            imgPreview.setVisibility(View.GONE);
            stickerView.setVisibility(View.VISIBLE);
        }, 1000);
    }

    //Load InterstitialAd
    private void loadInterstitialAdSaved() {
        if (mInterstitialAdTips != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            mInterstitialAdTips.loadAd(adRequest);
        }
    }

    private void backColorOrnament() {
        colorOrnamentAdapter.resetSelected();
        switch (currentOrnament) {
            case INDEX_LINE:
                showView(R.id.layout_option_line);
                break;
            case INDEX_TYPO:
                showView(R.id.layout_option_typo);
                break;
            case INDEX_BORDER:
                showView(R.id.layout_option_border);
                break;
            default:
                showView(R.id.layout_option_line);
        }
    }

    private void readExternalPermission() {
        int permission = ContextCompat.checkSelfPermission(Objects.requireNonNull(MainActivity.this),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(MainActivity.this),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.permission)
                        .setTitle(R.string.Permission_required);
                builder.setPositiveButton("OK", (dialog, id) -> makeRequestReadExternal(MainActivity.this));
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                makeRequestReadExternal(MainActivity.this);
            }
        }
    }

    private void makeRequestReadExternal(Context context) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void moveUpList(View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                finalHeight = view.getHeight(); //height is ready
                finalWidth = view.getWidth();
            }
        });
        view.setOnTouchListener(new SimpleGestureFilter(MainActivity.this) {
            @Override
            public void onSwipeTop() {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_moveup_2);
                view.startAnimation(animation);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(finalWidth, finalHeight + 150);
                view.setLayoutParams(layoutParams);
                view.requestLayout();
            }

            @Override
            public void onSwipeBottom() {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_movedown_2);
                view.startAnimation(animation);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(finalWidth, finalHeight);
                view.setLayoutParams(layoutParams);
                view.requestLayout();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkEnableFeature();
        if (ConstantsFilter.REFRESH_LIST) {
            new Handler().postDelayed(() -> {
                ConstantsFilter.REFRESH_LIST = false;
                if(filterAdapter!=null){
                    filterAdapter.notifyDataSetChanged();
                }
            }, 500);
        }
        System.gc();
        Constants.openTemplateEditor = true;
        sharedPreferences = getSharedPreferences(GAME_SETTING, Context.MODE_PRIVATE);

    }


    private void checkEnableFeature() {
        int countUseFeatureGenerateHQ = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HQ, 0);
        boolean earnRewardHQ = AppPreference.getInstance(this).getKeyRate(EXTRA_EARN_REWARD_HQ, false);

        int countUseFeatureGenerateHD = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HD, 0);
        boolean earnRewardHD = AppPreference.getInstance(this).getKeyRate(EXTRA_EARN_REWARD_HD, false);

        int countUseFeatureGenerateFullHD = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_FULL_HD, 0);
        boolean earnRewardFullHD = AppPreference.getInstance(this).getKeyRate(EXTRA_EARN_REWARD_FULL_HD, false);


        //HQ
        if ((earnRewardHQ && countUseFeatureGenerateHQ < LIMIT_USE_FEATURE_GENERATE) ||
                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            txtTrailHQ.setVisibility(View.GONE);
        } else {
            txtTrailHQ.setVisibility(View.VISIBLE);
        }
        //HD
        if ((earnRewardHD && countUseFeatureGenerateHD < LIMIT_USE_FEATURE_GENERATE) ||
                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            txtTrailHD.setVisibility(View.GONE);
        } else {
            txtTrailHD.setVisibility(View.VISIBLE);
        }
        //FULL HD
        if ((earnRewardFullHD && countUseFeatureGenerateFullHD < LIMIT_USE_FEATURE_GENERATE) ||
                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            txtTrailFullHD.setVisibility(View.GONE);
        } else {
            txtTrailFullHD.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.openTemplateEditor = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == com.dev.signatureonphoto.Constants.FILTER_REQUEST_CODE) {
            filterAdapter.notifyDataSetChanged();
            SharedPreferences s = getSharedPreferences(com.dev.signatureonphoto.Constants.ADS_FILTER, Context.MODE_PRIVATE);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                final Uri selectedUri;
                selectedUri = data.getData();
                if (selectedUri != null) {
                    UCrop uCrop = UCrop.of(
                            selectedUri,
                            Uri.fromFile(new File(
                                    Objects.requireNonNull(MainActivity.this).getCacheDir(),
                                    "S2quoteCropImageName_" + System.currentTimeMillis() + ".png")));
                    uCrop = advancedConfig(uCrop);
                    uCrop.start(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_cannot_retrieve_selected_image), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            if (cropError != null) {
                Toast.makeText(MainActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == Constants.REQUEST_TEMPLATE && resultCode == RESULT_OK) {
            Bundle bundleResult = data.getBundleExtra(Constants.BUNDLE_TEMPLATE);
            bitmap = null;
            filterLoading.setVisibility(View.VISIBLE);
            listFilter.setVisibility(View.GONE);
            if (bundleResult.getString(KEY_TYPE, NAME_EXTRA_TEMPLATE).equals(NAME_EXTRA_TEMPLATE)) {
                idImage = bundleResult.getInt(Constants.NAME_EXTRA_TEMPLATE, 0);
                imageMode = 1;
                filterPath = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), idImage, options);
                h = options.outHeight;
                w = options.outWidth;
                Glide.with(this).load(idImage).thumbnail(0.1f).into(imgBackgroundPhoto);
                reloadView(ID_RELOAD_IMAGE, false);
            } else {
                idColor = bundleResult.getInt(Constants.NAME_EXTRA_COLOR_BACKGROUND, 0);
                filterPath = null;
                imageMode = 0;
                imgBackgroundPhoto.setImageResource(idColor);
                reloadView(ID_RELOAD_COLOR, false);
            }
        }
        if (requestCode == Constants.REQUEST_USER_IMAGE) {
            if (resultCode == RESULT_OK) {
                String path = data.getStringExtra("path");
                Uri uri = Uri.fromFile(new File(path));
                if (uri != null) {
                    UCrop uCrop = UCrop.of(
                            uri,
                            Uri.fromFile(new File(
                                    Objects.requireNonNull(MainActivity.this).getCacheDir(),
                                    "S2quoteCropImageName_" + System.currentTimeMillis() + ".png")));
                    uCrop = advancedConfig(uCrop);
                    uCrop.start(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_cannot_retrieve_selected_image), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == REQUEST_EDITTEXT) {
            if (resultCode == Constants.EDITTEXT_RESULT) {
                editTextResult(data);
            }
        }

    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            bitmap = null;
            uriImage = resultUri.getPath();
            imageMode = 2;
            filterPath = null;
            filterLoading.setVisibility(View.VISIBLE);
            listFilter.setVisibility(View.GONE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(resultUri.getPath(), options);
            h = options.outHeight;
            w = options.outWidth;
            Glide.with(this).load(resultUri).thumbnail(0.1f).into(imgBackgroundPhoto);
            reloadView(ID_RELOAD_IMAGE, false);
        } else {
            Toast.makeText(MainActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }


    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        return uCrop.withOptions(options);
    }

    private void editTextResult(Intent data) {
        if (stickerView.getCurrentSticker() instanceof TextSticker) {
            textsticker = (TextSticker) stickerView.getCurrentSticker();
            quote = data.getStringExtra("edittext");
            textsticker.setText(quote);
            textsticker.resizeText();
            stickerView.invalidate();
        }
    }

    private void loadSticker(boolean addSticker) {
        String path;
        quote = getString(R.string.double_click_to_edit);
        if (addSticker || stickerView.getStickerCount() == 0) {
            path = Constants.getFontVN[10];
            stickerView.addSticker(
                    new TextSticker(MainActivity.this)
                            .setText(quote)
                            .resizeText()
                            .setTypeface(Typeface.createFromAsset(getAssets(), path), 10)
                    /*, Sticker.Position.TOP*/);
            BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                    R.drawable.icon_expand),
                    BitmapStickerIcon.RIGHT_BOTOM);
            zoomIcon.setIconEvent(new ZoomIconEvent());
            BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                    R.drawable.icon_close),
                    BitmapStickerIcon.RIGHT_TOP);
            BitmapStickerIcon alignIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                    R.drawable.ic_center),
                    BitmapStickerIcon.LEFT_BOTTOM);
            BitmapStickerIcon backgroundIcon = new BitmapStickerIcon(ContextCompat.getDrawable(MainActivity.this,
                    R.drawable.ic_swap_bg),
                    BitmapStickerIcon.LEFT_TOP);
            backgroundIcon.setIconEvent(new StickerIconEvent() {
                @Override
                public void onActionDown(StickerView stickerView, MotionEvent event) {
                    Sticker sticker = stickerView.getCurrentSticker();
                    if (sticker != null) {
                        if (sticker.isBackgroundEnable()) {
                            sticker.setBackgroundEnable(false);
                        } else {
                            sticker.setBackgroundEnable(true);
                        }
                    }
                }

                @Override
                public void onActionMove(StickerView stickerView, MotionEvent event) {
                }

                @Override
                public void onActionUp(StickerView stickerView, MotionEvent event) {
                }
            });
            deleteIcon.setIconEvent(new DeleteIconEvent());
            alignIcon.setIconEvent(new AlignEvent(this));
            stickerView.setIcons(Arrays.asList(zoomIcon, deleteIcon, alignIcon, backgroundIcon));
            stickerView.setBackground(getResources().getDrawable(R.drawable.rounded_border_tv));
            stickerView.setLocked(false);
            stickerView.setConstrained(true);
        }
    }


    @Override
    public void onBackPressed() {
        if (llOptionBackground.getVisibility() == View.VISIBLE ||
                layoutOptionOrnament.getVisibility() == View.VISIBLE ||
                layoutFilter.getVisibility() == View.VISIBLE ||
                llOptionText.getVisibility() == View.VISIBLE ||
                llRecycler.getVisibility() == View.VISIBLE) {
            showView(R.id.layout_option_main);
        } else if (
                llTextOptionColor.getVisibility() == View.VISIBLE ||
                        llTextOptionShadow.getVisibility() == View.VISIBLE ||
                        llTextOptionNeon.getVisibility() == View.VISIBLE) {
            showView(R.id.layout_option_text);
        } else if (layoutOrnamentColor.getVisibility() == View.VISIBLE) {
            backColorOrnament();
        } else if (layoutOptionLine.getVisibility() == View.VISIBLE ||
                layoutOptionTypo.getVisibility() == View.VISIBLE ||
                layoutOptionBorder.getVisibility() == View.VISIBLE) {
            showView(R.id.layout_option_ornament);
        } else {
            showDialogSave();
        }
    }

    private void showDialogItemPro() {
        Dialog dialogItemPro = new Dialog(this);
        dialogItemPro.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogItemPro.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogItemPro.setContentView(R.layout.layout_save_menu);
        Window window = dialogItemPro.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialogItemPro.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button btnContinue = dialogItemPro.findViewById(R.id.btn_continue);
        ImageView imgClose=dialogItemPro.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogItemPro.dismiss();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PurchasedActivity.class));
                dialogItemPro.dismiss();
            }
        });
        dialogItemPro.show();
    }

    private void showDialogSave() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_save);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnYes = dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onAlignClick() {
        stickerView.rotateSticker(stickerView.getCurrentSticker(), 0, scale, point);
        if (stickerView.getCurrentSticker() instanceof TextSticker) {
        } else {
        }
    }

    private void showView(int viewId) {
        View hideView = null, showView = null;
        for (View view : optionViews) {
            if (view.getId() == viewId) {
                showView = view;
            }
            if (view.getVisibility() == View.VISIBLE) {
                hideView = view;
            }
        }
        if (hideView != null && showView != null) {
            if (hideView.getId() != showView.getId()) {
                slideDown(hideView, showView);
            }
        }
    }

    private void createFontText() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "r0c0i - Linotte Regular.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "r0c0iLinotte-Bold.ttf");
        txtDone.setTypeface(typefaceBold);
        tvHome.setTypeface(typefaceBold);
        txtBackGround.setTypeface(typeface);
        txtText.setTypeface(typeface);
        txtSticker.setTypeface(typeface);
        txtOrnament.setTypeface(typeface);
        txtTextStyle.setTypeface(typeface);
        txtColor.setTypeface(typeface);
        txtAlign.setTypeface(typeface);
        txtShadow.setTypeface(typeface);
        txtNeon.setTypeface(typeface);
        txtBackground.setTypeface(typeface);
        txtLibrary.setTypeface(typeface);
        txtLine.setTypeface(typeface);
        txtTypo.setTypeface(typeface);
        txtBorder.setTypeface(typeface);
        txtstyle.setTypeface(typeface);
        txtOpacity.setTypeface(typeface);
        txtTextSize.setTypeface(typeface);
        txtTextSpacing.setTypeface(typeface);
        txtBorder.setTypeface(typeface);
        txtBorderColor.setTypeface(typeface);
        txtBold.setTypeface(typefaceBold);
        txtRegular.setTypeface(typeface);
        txtitalic.setTypeface(typeface);
    }

    @Override
    public void updateItem(int position, int x) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = manager.findViewByPosition(position);
                listFilter.smoothScrollBy(view.getLeft(), 0);
            }
        }, 100);
    }

    @Override
    public void onFilterSelected(String filterPath) {
        if (filterPath.length() == 0) {
            this.filterPath = null;
            imgBackgroundPhoto.setImageBitmap(bitmap);
            checkFilterPro = false;
        } else {
            try {
                this.filterPath = filterPath;
                InputStream inputStream = getAssets().open(filterPath);
                gpuImageFilterTools.filterImage(bitmap, inputStream);
                Bitmap filterBitmap = gpuImageFilterTools.getBitmap();
                imgBackgroundPhoto.setImageBitmap(filterBitmap);
                checkFilterPro = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFilterLoaded(List<List<Filter>> filters) {
        filterLoading.setVisibility(View.GONE);
        listFilter.setVisibility(View.VISIBLE);
        filterAdapter = new FilterAdapter(this, filters, listProductIdIAP);
        filterAdapter.setListener(this);
        listFilter.setAdapter(filterAdapter);
    }

    @Override
    public void onMenuItemSelected(int position) {
        reCreateSticker(position);
    }

    private void reCreateSticker(int menuId) {
        ArrayList<StickerItem> previewSticker = null;
        ArrayList<StickerItem> stickers = null;
        switch (menuId) {
            case OLD_STICKER:
                previewSticker = Constants.getImageSticker();
                stickers = Constants.getImageStickerPreview();
                break;
            case XMAS_STICKER:
                previewSticker = Constants.getXmasSticker();
                stickers = Constants.getXmasSticker();
                break;
        }
        if (previewSticker != null) {
            ArrayList<StickerItem> finalStickers = stickers;
            stickerAdapter = new StickerAdapter(previewSticker, MainActivity.this, (view, position) -> {
                Drawable drawable =
                        ContextCompat.getDrawable(MainActivity.this, finalStickers.get(position).getImageSticker());
                stickerView.addSticker(new DrawableSticker(drawable, DrawableSticker.TYPE_NORMAL));
            });
            rcvSticker.setAdapter(stickerAdapter);
        }
    }

    private void outputImage() {
        selectSizeDialog.dismiss();
        width = stickerView.getWidth();
        height = stickerView.getHeight();
        if (width > 0 && height > 0) {
            int countAds = AppPreference.getInstance(MainActivity.this).getKeyShowAds(EXTRA_ADS_SAVED_IMAGE, 0);
            countAds++;
            if (countAds >= MAX_ADS_SAVED_IMAGE) {
                loadingDialog.setMessage("Saving & Loading Ad...");
            } else {
                loadingDialog.setMessage("Saving...");
            }

            loadingDialog.show();
            if (imageMode != 0) {
                resizeImage();
            } else {
                resizeColorImage();
            }
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.save_null_image), Toast.LENGTH_LONG).show();
        }
    }

    private void selectSizeEvent() {
        LinearLayout layoutAuto = selectSizeDialog.findViewById(R.id.layout_auto);
        LinearLayout layoutSd = selectSizeDialog.findViewById(R.id.layout_sd_size);
        LinearLayout layoutHq = selectSizeDialog.findViewById(R.id.layout_hq_size);
        RelativeLayout layoutHd = selectSizeDialog.findViewById(R.id.layout_hd_size);
        RelativeLayout layoutFullHD = selectSizeDialog.findViewById(R.id.layout_full_hd_size);
        layoutAuto.setOnClickListener(v -> {
            if (currentPositionQuality != AUTO_SCREEN) {
                refreshSelectQuality();
                layoutAuto.setBackgroundColor(getResources().getColor(R.color.select_color));
                currentPositionQuality = AUTO_SCREEN;
            }
        });
        layoutSd.setOnClickListener(v -> {
            if (currentPositionQuality != SD_PX) {
                refreshSelectQuality();
                layoutSd.setBackgroundColor(getResources().getColor(R.color.select_color));
                currentPositionQuality = SD_PX;
            }
        });
        layoutHq.setOnClickListener(v -> {
            if (currentPositionQuality != HQ_PX) {
                refreshSelectQuality();
                layoutHq.setBackgroundColor(getResources().getColor(R.color.select_color));
                currentPositionQuality = HQ_PX;
            }
        });
        layoutHd.setOnClickListener(v -> {
            if (currentPositionQuality != HD_PX) {
                refreshSelectQuality();
                layoutHd.setBackgroundColor(getResources().getColor(R.color.select_color));
                currentPositionQuality = HD_PX;
            }
        });
        layoutFullHD.setOnClickListener(v -> {
            if (currentPositionQuality != FULL_HD_PX) {
                refreshSelectQuality();
                layoutFullHD.setBackgroundColor(getResources().getColor(R.color.select_color));
                currentPositionQuality = FULL_HD_PX;
            }
        });
        selectSizeDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
            total_count = MyLog.getIntValueByName(this, Config.LOG_APP, Config.HD_COUNT);

            switch (currentPositionQuality) {
                case AUTO_SCREEN:
                    currentSize = hdSize;
                    outputImage();
                    break;
                case SD_PX:
                    currentSize = sdSize;
                    outputImage();
                    break;
                case HQ_PX:
                    currentSize = hqSize;
                    int countUseFeatureGenerateHQ = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HQ, 0);
                    boolean earnRewardHQ = AppPreference.getInstance(this).getKeyRate(EXTRA_EARN_REWARD_HQ, false);

                    if ((earnRewardHQ && countUseFeatureGenerateHQ < LIMIT_USE_FEATURE_GENERATE) ||
                            AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                            AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                        int countFeatureFullHD = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HQ, 0);
                        AppPreference.getInstance(this).setKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HQ, ++countFeatureFullHD);
                        outputImage();
                    } else {
                        showAskFeature();
                    }
                    break;
                case HD_PX:
                    currentSize = hdSize;
                    AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_ADS_SAVED_IMAGE, 2);

                    int countUseFeatureGenerateHD = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HD, 0);
                    boolean earnRewardHD = AppPreference.getInstance(this).getKeyRate(EXTRA_EARN_REWARD_HD, false);

                    if ((earnRewardHD && countUseFeatureGenerateHD < LIMIT_USE_FEATURE_GENERATE) ||
                            AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                            AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                        int countFeatureFullHD = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HD, 0);
                        AppPreference.getInstance(this).setKeyShowAds(EXTRA_USE_FEATURE_GENERATE_HD, ++countFeatureFullHD);
                        outputImage();
                    } else {
                        showAskFeature();
                    }
                    break;
                case FULL_HD_PX:
                    currentSize = fullHdSize;
                    int countUseFeatureGenerate = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_FULL_HD, 0);
                    boolean earnReward = AppPreference.getInstance(this).getKeyRate(EXTRA_EARN_REWARD_FULL_HD, false);

                    if ((earnReward && countUseFeatureGenerate < LIMIT_USE_FEATURE_GENERATE) ||
                            AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                            AppPreference.getInstance(MainActivity.this).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                        int countFeatureFullHD = AppPreference.getInstance(this).getKeyShowAds(EXTRA_USE_FEATURE_GENERATE_FULL_HD, 0);
                        AppPreference.getInstance(this).setKeyShowAds(EXTRA_USE_FEATURE_GENERATE_FULL_HD, ++countFeatureFullHD);
                        outputImage();
                    } else {
                        showAskFeature();
                    }
                    break;
            }
        });
        selectSizeDialog.findViewById(R.id.btn_cancel).setOnClickListener(view -> {
            selectSizeDialog.dismiss();
        });
        selectSizeDialog.setOnDismissListener(dialogInterface -> {
            refreshSelectQuality();
            layoutAuto.setBackgroundColor(getResources().getColor(R.color.select_color));
            SELECTED_RESOLUTION = currentPositionQuality;
            posQuality = currentPositionQuality;
            currentPositionQuality = AUTO_SCREEN;
        });
    }

    private ProgressDialog progressDialog;

    private void showAskFeature() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Ads Reward...");
        progressDialog.setCancelable(false);
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_buy_feature);
        dialog.setCancelable(false);
        TextView txtBuyNow = dialog.findViewById(R.id.txt_buy_now);
        TextView txtWatchNow = dialog.findViewById(R.id.txt_watch_now);
        ImageView imgClose = dialog.findViewById(R.id.img_cancel);
        TextView txtPrice = dialog.findViewById(R.id.txt_price);
        txtPrice.setText(priceFeatureGenerate);
        txtWatchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    dialog.dismiss();
                    progressDialog.show();
                    if (checkLoadedReward) {
                        showAdsReward();
                    } else {
                        initReward();
                        showAdsReward();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No network connection. Please turn on wifi or 3G/4G", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PurchasedActivity.class));
                dialog.dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showAdsReward() {
        if (rewardedAd.isLoaded()) {
            progressDialog.dismiss();
            rewardedAd.show(MainActivity.this, adCallback);
        } else {
            disposable = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tick -> {
                        if (tick <= 7) {
                            if (rewardedAd.isLoaded()) {
                                progressDialog.dismiss();
                                rewardedAd.show(MainActivity.this, adCallback);
                                if (disposable != null) {
                                    disposable.dispose();
                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            if (disposable != null) {
                                disposable.dispose();
                            }
                            Toast.makeText(activity, "Advertising is not available yet. Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void refreshSelectQuality() {
        switch (currentPositionQuality) {
            case AUTO_SCREEN:
                selectSizeDialog.findViewById(R.id.layout_auto).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case SD_PX:
                selectSizeDialog.findViewById(R.id.layout_sd_size).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case HQ_PX:
                selectSizeDialog.findViewById(R.id.layout_hq_size).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case HD_PX:
                selectSizeDialog.findViewById(R.id.layout_hd_size).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case FULL_HD_PX:
                selectSizeDialog.findViewById(R.id.layout_full_hd_size).setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    @OnClick({R.id.layout_text_bold, R.id.layout_text_italic, R.id.layout_text_regular})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_text_regular:
                textsticker.setTextStyle(Typeface.NORMAL);
                textsticker.resizeText();
                stickerView.invalidate();
                break;
            case R.id.layout_text_bold:
                textsticker.setTextStyle(Typeface.BOLD);
                textsticker.resizeText();
                stickerView.invalidate();
                break;
            case R.id.layout_text_italic:
                textsticker.setTextStyle(Typeface.ITALIC);
                textsticker.resizeText();
                stickerView.invalidate();
                break;
        }
    }

    public void listenerSeekbar() {
        skbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (textsticker != null) {
                    int perOpacity = (progress * 255) / 100;
                    textsticker.setAlpha(perOpacity);
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        skbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float perTextSize = (float) 2 / 100 * progress;
                if (progress > pro) {
                    stickerView.setTextSizeUp(textsticker);
                    pro = progress;
                } else {
                    stickerView.setTextSizeDown(textsticker);
                    pro = progress;
                }
                //textsticker.resizeText(progress);
                stickerView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        skbTextSpacing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float perSpacing = ((float) 2 / 100) * progress;
                textsticker.setLineSpacing(0, perSpacing);
                textsticker.resizeText();
                stickerView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        skbTextStroke.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (textsticker != null) {
                    float perStroke = (float) 6 / 100 * progress;
                    if (perStroke == 0) {
                        perStroke = 0.2f;
                    }
                    textsticker.setStrokeWidth(perStroke);
                    textsticker.resizeText();
                    stickerView.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
