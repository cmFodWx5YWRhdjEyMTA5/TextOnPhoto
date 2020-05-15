package com.dev.signatureonphoto.features.template.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.InfoApp;
import com.dev.signatureonphoto.features.main.MainActivity;
import com.dev.signatureonphoto.features.preview.PreviewActivity;
import com.dev.signatureonphoto.features.template.TemplateActivity;
import com.dev.signatureonphoto.util.AppPreference;
import com.dev.signatureonphoto.util.CheckInfoApp;
import com.dev.signatureonphoto.util.Constants;
import com.dev.signatureonphoto.util.UtilAdsCrossAdaptive;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.dev.signatureonphoto.Constants.PACKAGE_NAME_DEFAULT_ADS_CROSS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTemplate extends Fragment {
    Unbinder unbinder;
    private View root;
    @BindView(R.id.txt_nature)
    TextView txtNature;
    @BindView(R.id.ic_check_nature)
    ImageView icCheckNature;
    @BindView(R.id.rcv_nature)
    RecyclerView rcvNature;
    @BindView(R.id.rcv_macro)
    RecyclerView rcvMacro;
    @BindView(R.id.txt_love)
    TextView txtLove;
    @BindView(R.id.ic_check_love)
    ImageView icCheckLove;
    @BindView(R.id.rcv_love)
    RecyclerView rcvLove;
    @BindView(R.id.layout_nature)
    RelativeLayout layoutNature;
    @BindView(R.id.txt_macro)
    TextView txtMacro;
    @BindView(R.id.ic_check_macro)
    ImageView icCheckMacro;
    @BindView(R.id.layout_macro)
    RelativeLayout layoutMacro;
    @BindView(R.id.layout_love)
    RelativeLayout layoutLove;
    @BindView(R.id.txt_light)
    TextView txtLight;
    @BindView(R.id.ic_check_light)
    ImageView icCheckLight;
    @BindView(R.id.layout_light)
    RelativeLayout layoutLight;
    @BindView(R.id.rcv_light)
    RecyclerView rcvLight;
    @BindView(R.id.txt_life_style)
    TextView txtLifeStyle;
    @BindView(R.id.ic_check_life_style)
    ImageView icCheckLifeStyle;
    @BindView(R.id.layout_life_style)
    RelativeLayout layoutLifeStyle;
    @BindView(R.id.rcv_life_style)
    RecyclerView rcvLifeStyle;
    @BindView(R.id.layout_default_cross)
    View layoutDefaultCross;
    @BindView(R.id.btn_install)
    Button btnInstall;
    @BindView(R.id.layout_adaptive)
    RelativeLayout layoutAdaptive;


    //Admob
    private AdView adView;
    private ViewPager viewPager;
    private List<InfoApp> listInfoApp;
    private boolean checkBannerAds = true;
    private boolean checkBannerCross;


    private BackgroundAdapter natureAdapter, macroAdapter, loveAdapter, lightAdapter, lifeStyleAdapter;

    private InterstitialAd mInterstitialAdTemplate;
    ProgressDialog progressDialogAds;
    private boolean checkLoadFullAds = true;

    public FragmentTemplate() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_fragment_template, container, false);
        unbinder = ButterKnife.bind(this, root);
        initRecycleView();
        progressDialogAds = new ProgressDialog(getActivity());
        progressDialogAds.setMessage("Loading Ad...");

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            layoutAdaptive.setVisibility(View.GONE);
        }else{
            initAdsBanner();
        }
    }

    private void initAdsBanner() {
        layoutAdaptive.addView(UtilAdsCrossAdaptive.getLayoutCross(getActivity(),CheckInfoApp.initListCrossAdaptive()));
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(getActivity());
        adView.setAdUnitId(getString(R.string.adaptive_banner_screen_template));

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
                layoutAdaptive.addView(UtilAdsCrossAdaptive.getLayoutCross(getActivity(),CheckInfoApp.initListCrossAdaptive()));
            }
        });
        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(getActivity(), adWidth);
    }

    private void initRecycleView() {
        natureAdapter = new BackgroundAdapter(Constants.getNatureList(), getContext(), new BackgroundAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                macroAdapter.resetBackgroundList();
                loveAdapter.resetBackgroundList();
                lightAdapter.resetBackgroundList();
                lifeStyleAdapter.resetBackgroundList();

                if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    forwardCropActivity(Constants.getNatureList().get(position).getImage());
                } else {
                    if (Constants.getNatureList().get(position).isCheckAds()) {
                        loadAdsInterstitialTemplate("Nature", position);
                        loadInterstitialAdTemplate();
                    } else {
                        forwardCropActivity(Constants.getNatureList().get(position).getImage());
                    }
                }
            }
        });
        lifeStyleAdapter = new BackgroundAdapter(Constants.getLifeStyleList(), getContext(), new BackgroundAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                natureAdapter.resetBackgroundList();
                macroAdapter.resetBackgroundList();
                loveAdapter.resetBackgroundList();
                lightAdapter.resetBackgroundList();

                if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    forwardCropActivity(Constants.getLifeStyleList().get(position).getImage());
                }else{
                    if (Constants.getLifeStyleList().get(position).isCheckAds()) {
                        loadAdsInterstitialTemplate("LifeStyle", position);
                        loadInterstitialAdTemplate();
                    } else {
                        forwardCropActivity(Constants.getLifeStyleList().get(position).getImage());
                    }
                }
            }
        });
        macroAdapter = new BackgroundAdapter(Constants.getMacroList(), getContext(), new BackgroundAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                natureAdapter.resetBackgroundList();
                loveAdapter.resetBackgroundList();
                lightAdapter.resetBackgroundList();
                lifeStyleAdapter.resetBackgroundList();

                if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    forwardCropActivity(Constants.getMacroList().get(position).getImage());
                }else{
                    if (Constants.getMacroList().get(position).isCheckAds()) {
                        loadAdsInterstitialTemplate("Macro", position);
                        loadInterstitialAdTemplate();
                    } else {
                        forwardCropActivity(Constants.getMacroList().get(position).getImage());
                    }
                }
            }
        });
        loveAdapter = new BackgroundAdapter(Constants.getLoveList(), getContext(), new BackgroundAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                natureAdapter.resetBackgroundList();
                lightAdapter.resetBackgroundList();
                lifeStyleAdapter.resetBackgroundList();
                macroAdapter.resetBackgroundList();

                if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    forwardCropActivity(Constants.getLoveList().get(position).getImage());
                }else{
                    if (Constants.getLoveList().get(position).isCheckAds()) {
                        loadAdsInterstitialTemplate("Love", position);
                        loadInterstitialAdTemplate();
                    } else {
                        forwardCropActivity(Constants.getLoveList().get(position).getImage());
                    }
                }
            }
        });
        lightAdapter = new BackgroundAdapter(Constants.getLightList(), getContext(), new BackgroundAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                natureAdapter.resetBackgroundList();
                macroAdapter.resetBackgroundList();
                loveAdapter.resetBackgroundList();
                lifeStyleAdapter.resetBackgroundList();

                if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    forwardCropActivity(Constants.getLightList().get(position).getImage());
                }else{
                    if (Constants.getLightList().get(position).isCheckAds()) {
                        loadAdsInterstitialTemplate("Light", position);
                        loadInterstitialAdTemplate();
                    } else {
                        forwardCropActivity(Constants.getLightList().get(position).getImage());
                    }
                }
            }
        });

        rcvNature.setAdapter(natureAdapter);
        rcvNature.setHasFixedSize(true);
        rcvNature.setLayoutManager(new GridLayoutManager(getContext(), 3));

        rcvLifeStyle.setAdapter(lifeStyleAdapter);
        rcvLifeStyle.setHasFixedSize(true);
        rcvLifeStyle.setLayoutManager(new GridLayoutManager(getContext(), 3));

        rcvLove.setAdapter(loveAdapter);
        rcvLove.setHasFixedSize(true);
        rcvLove.setLayoutManager(new GridLayoutManager(getContext(), 3));

        rcvMacro.setAdapter(macroAdapter);
        rcvMacro.setHasFixedSize(true);
        rcvMacro.setLayoutManager(new GridLayoutManager(getContext(), 3));

        rcvLight.setAdapter(lightAdapter);
        rcvLight.setHasFixedSize(true);
        rcvLight.setLayoutManager(new GridLayoutManager(getContext(), 3));


    }

    private void forwardCropActivity(int resource) {
        checkLoadFullAds = false;
        Intent intentNewTemplate = new Intent(getActivity(), MainActivity.class);
        intentNewTemplate.putExtra(Constants.NAME_EXTRA_TEMPLATE, resource);
        startActivity(intentNewTemplate);
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionQuality(100);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        return uCrop.withOptions(options);
    }


    @OnClick({R.id.layout_nature, R.id.layout_macro, R.id.layout_love, R.id.layout_light, R.id.layout_life_style,
            R.id.btn_install})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_nature:
                if (rcvNature.getVisibility() == View.VISIBLE) {
                    rcvNature.setVisibility(View.GONE);
                    icCheckNature.setImageResource(R.drawable.ic_right);
                } else {
                    rcvNature.setVisibility(View.VISIBLE);
                    icCheckNature.setImageResource(R.drawable.ic_down);
                }
                break;
            case R.id.layout_macro:
                if (rcvMacro.getVisibility() == View.VISIBLE) {
                    rcvMacro.setVisibility(View.GONE);
                    icCheckMacro.setImageResource(R.drawable.ic_right);
                } else {
                    rcvMacro.setVisibility(View.VISIBLE);
                    icCheckMacro.setImageResource(R.drawable.ic_down);
                }
                break;
            case R.id.layout_love:
                if (rcvLove.getVisibility() == View.VISIBLE) {
                    rcvLove.setVisibility(View.GONE);
                    icCheckLove.setImageResource(R.drawable.ic_right);
                } else {
                    rcvLove.setVisibility(View.VISIBLE);
                    icCheckLove.setImageResource(R.drawable.ic_down);
                }
                break;
            case R.id.layout_light:
                if (rcvLight.getVisibility() == View.VISIBLE) {
                    rcvLight.setVisibility(View.GONE);
                    icCheckLight.setImageResource(R.drawable.ic_right);
                } else {
                    rcvLight.setVisibility(View.VISIBLE);
                    icCheckLight.setImageResource(R.drawable.ic_down);
                }
                break;
            case R.id.layout_life_style:
                if (rcvLifeStyle.getVisibility() == View.VISIBLE) {
                    rcvLifeStyle.setVisibility(View.GONE);
                    icCheckLifeStyle.setImageResource(R.drawable.ic_right);
                } else {
                    rcvLifeStyle.setVisibility(View.VISIBLE);
                    icCheckLifeStyle.setImageResource(R.drawable.ic_down);
                }
                break;
            case R.id.btn_install:
                if (CheckInfoApp.appInstalledOrNot(getActivity(), PACKAGE_NAME_DEFAULT_ADS_CROSS)) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, com.dev.signatureonphoto.Constants.PLAY_STORE_LINK + PACKAGE_NAME_DEFAULT_ADS_CROSS);
                    sendIntent.setType(com.dev.signatureonphoto.Constants.DATA_TYPE);
                    getActivity().startActivity(sendIntent);
                } else {
                    Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                    intentUpdate.setData(Uri.parse(com.dev.signatureonphoto.Constants.PLAY_STORE_LINK + PACKAGE_NAME_DEFAULT_ADS_CROSS));
                    getActivity().startActivity(intentUpdate);
                }
                break;
        }
    }

    private void loadAdsInterstitialTemplate(final String type, final int position) {
        checkLoadFullAds = true;
        progressDialogAds.show();
        mInterstitialAdTemplate = new InterstitialAd(getActivity());
        mInterstitialAdTemplate.setAdUnitId(getString(R.string.full_screen_saved_image));
        mInterstitialAdTemplate.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (type.equals("Nature")) {
                    forwardCropActivity(Constants.getNatureList().get(position).getImage());
                } else if (type.equals("Light")) {
                    forwardCropActivity(Constants.getLightList().get(position).getImage());
                } else if (type.equals("Love")) {
                    forwardCropActivity(Constants.getLoveList().get(position).getImage());
                } else if (type.equals("Macro")) {
                    forwardCropActivity(Constants.getMacroList().get(position).getImage());
                } else if (type.equals("LifeStyle")) {
                    forwardCropActivity(Constants.getLifeStyleList().get(position).getImage());
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAdTemplate.isLoaded() && checkLoadFullAds) {
                    checkLoadFullAds = false;
                    progressDialogAds.dismiss();
                    mInterstitialAdTemplate.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                progressDialogAds.dismiss();
                checkLoadFullAds = false;
                if (type.equals("Nature")) {
                    forwardCropActivity(Constants.getNatureList().get(position).getImage());
                } else if (type.equals("Light")) {
                    forwardCropActivity(Constants.getLightList().get(position).getImage());
                } else if (type.equals("Love")) {
                    forwardCropActivity(Constants.getLoveList().get(position).getImage());
                } else if (type.equals("Macro")) {
                    forwardCropActivity(Constants.getMacroList().get(position).getImage());
                } else if (type.equals("LifeStyle")) {
                    forwardCropActivity(Constants.getLifeStyleList().get(position).getImage());
                }
            }
        });

    }

    private void forwardTemplateActivity() {
        Intent intent1 = new Intent(getActivity(), TemplateActivity.class);
        startActivity(intent1);
    }

    //Load InterstitialAd
    private void loadInterstitialAdTemplate() {
        if (mInterstitialAdTemplate != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mInterstitialAdTemplate.loadAd(adRequest);
        }
    }

}
