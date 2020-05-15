package com.dev.signatureonphoto.features.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.InfoApp;
import com.dev.signatureonphoto.features.base.BaseFragment;
import com.dev.signatureonphoto.features.cross.AdsCrossAdapter;
import com.dev.signatureonphoto.features.main.MainActivity;
import com.dev.signatureonphoto.features.preview.PreviewActivity;
import com.dev.signatureonphoto.features.saved.SavedActivity;
import com.dev.signatureonphoto.features.template.TemplateActivity;
import com.dev.signatureonphoto.injection.component.FragmentComponent;
import com.dev.signatureonphoto.util.AppPreference;
import com.dev.signatureonphoto.util.CheckInfoApp;
import com.dev.signatureonphoto.util.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.dev.signatureonphoto.Constants.EXTRA_ADS_CLICK_SAVED_MAIN;
import static com.dev.signatureonphoto.Constants.EXTRA_UPDATE_VERSION;
import static com.dev.signatureonphoto.Constants.GALLERY_REQUEST;
import static com.dev.signatureonphoto.Constants.MAX_ADS_CLICK_SAVED_MAIN;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.layout_template)
    LinearLayout btnTemplate;
    @BindView(R.id.layout_camera)
    LinearLayout btnCamera;
    @BindView(R.id.layout_color)
    LinearLayout btnColor;
    @BindView(R.id.txt_back_ground)
    TextView txtBackGround;
    @BindView(R.id.txt_library)
    TextView txtLibrary;
    @BindView(R.id.txt_your_image)
    TextView txtYourImage;
    @BindView(R.id.txt_signature_watermark)
    TextView txtSignatureWatermark;
    @BindView(R.id.layout_ads)
    RelativeLayout layoutAds;
    @BindView(R.id.img_background)
    ImageView imgBackground;

    private Unbinder unbinder;

    private static final int REQUEST_PERMISSION_LIBRARY = 987;
    private static final int REQUEST_PERMISSION_USER_IMAGE = 986;
    private boolean needPermission = false;

    //Admob
    @BindView(R.id.progress_bar_full)
    ProgressBar progressBarAdsFull;

    private View view;
    private InterstitialAd mInterstitialAdTemplate;
    private boolean checkLoadedAds = true;

    private SharedPreferences shareUpdate;
    private SharedPreferences.Editor editorUpdate;

    private AdView mAdView;
    private ViewPager viewPager;
    private List<InfoApp> listInfoApp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        shareUpdate = getActivity().getSharedPreferences("update_app", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        createFontText();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
            layoutAds.setVisibility(View.GONE);
        }else{
            initAdsBanner();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadAdsCross();
    }

    private void loadAdsCross() {
        listInfoApp = new ArrayList<>();
        viewPager = view.findViewById(R.id.viewPager);
        AdsCrossAdapter adsCrossAdapter = new AdsCrossAdapter(CheckInfoApp.getListCrossOld(), getActivity());
        viewPager.setAdapter(adsCrossAdapter);
        viewPager.setPadding(10, 0, 10, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!checkLoadedAds){
            if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                    AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                layoutAds.setVisibility(View.GONE);
            }else{
                initAdsBanner();
            }
        }
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onDestroyView() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_home_xmas;
    }


    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);

    }

    @Override
    protected void attachView() {

    }

    @Override
    protected void detachPresenter() {

    }


    @OnClick({R.id.layout_template, R.id.layout_camera, R.id.layout_color, R.id.layout_watermark_signature})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_color:
                if (AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(getActivity()).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    startSavedActivity();
                }else{
                    int countAds = AppPreference.getInstance(getActivity()).getKeyShowAds(EXTRA_ADS_CLICK_SAVED_MAIN, 0);
                    countAds++;
                    if (countAds >= MAX_ADS_CLICK_SAVED_MAIN) {
                        loadAdsInterstitialTemplate();
                        loadInterstitialAdTemplate();
                    } else {
                        AppPreference.getInstance(getActivity()).setKeyShowAds(EXTRA_ADS_CLICK_SAVED_MAIN, countAds);
                        startSavedActivity();
                    }
                }
                break;
            case R.id.layout_camera:
                readExternalPermission(REQUEST_PERMISSION_LIBRARY);
                if (!needPermission) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                }
                break;

            case R.id.layout_template:
                Intent intentTempLate = new Intent(getContext(), TemplateActivity.class);
                startActivityForResult(intentTempLate, Constants.REQUEST_TEMPLATE);

                break;

            case R.id.layout_watermark_signature:
                Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                intentUpdate.setData(Uri.parse(com.dev.signatureonphoto.Constants.PLAY_STORE_LINK + "dev.com.camerafilter"));
                startActivity(intentUpdate);
                break;
        }
    }

    private void startSavedActivity() {
        readExternalPermission(REQUEST_PERMISSION_USER_IMAGE);
        if (!needPermission) {
            Intent intent = new Intent(getContext(), SavedActivity.class);
            startActivityForResult(intent, Constants.REQUEST_USER_IMAGE);
        }
    }

    private void loadAdsInterstitialTemplate() {
        progressBarAdsFull.setVisibility(View.VISIBLE);
        mInterstitialAdTemplate = new InterstitialAd(getActivity());
        mInterstitialAdTemplate.setAdUnitId(getString(R.string.full_screen_saved_image));
        mInterstitialAdTemplate.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AppPreference.getInstance(getActivity()).setKeyShowAds(EXTRA_ADS_CLICK_SAVED_MAIN, 0);
                startSavedActivity();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAdTemplate.isLoaded()) {
                    progressBarAdsFull.setVisibility(View.GONE);
                    mInterstitialAdTemplate.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                progressBarAdsFull.setVisibility(View.GONE);
                startSavedActivity();
            }
        });

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> listDeny = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                listDeny.add(permissions[i]);
            }
        }
        switch (requestCode) {
            case REQUEST_PERMISSION_LIBRARY:
                if (listDeny.size() > 0 && getContext() != null) {
                    ActivityCompat.requestPermissions((Activity) getContext(), listDeny.toArray(new String[listDeny.size()]), REQUEST_PERMISSION_LIBRARY);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                }
                break;

            case REQUEST_PERMISSION_USER_IMAGE:
                if (listDeny.size() > 0 && getContext() != null) {
                    ActivityCompat.requestPermissions((Activity) getContext(), listDeny.toArray(new String[listDeny.size()]), REQUEST_PERMISSION_USER_IMAGE);
                } else {
                    Intent intent = new Intent(getContext(), SavedActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_USER_IMAGE);
                }
                break;
        }
    }

    private void readExternalPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCamera = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
            int permissionRead = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWrite = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ArrayList<String> list = new ArrayList<>();

            if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
                list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                list.add(Manifest.permission.CAMERA);
            }
            if (permissionRead != PackageManager.PERMISSION_GRANTED) {
                list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (list.size() > 0) {
                ActivityCompat.requestPermissions((Activity) getContext(), list.toArray(new String[list.size()]), requestCode);
                needPermission = true;
            } else {
                needPermission = false;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    UCrop uCrop = UCrop.of(
                            selectedUri,
                            Uri.fromFile(new File(Objects.requireNonNull(
                                    getActivity()).getCacheDir(),
                                    "S2quoteCropImageName_" + System.currentTimeMillis() + ".png")));
                    uCrop = advancedConfig(uCrop);
                    uCrop.start(getActivity(), HomeFragment.this);
                } else {
                    Toast.makeText(getContext(), getActivity().getString(R.string.toast_cannot_retrieve_selected_image), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            if (cropError != null) {
                Toast.makeText(getActivity(), cropError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            Intent intent1 = new Intent(getActivity(), MainActivity.class);
            intent1.putExtra("uri", resultUri.toString());
            startActivity(intent1);
        } else {
            Toast.makeText(getContext(), R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionQuality(100);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        return uCrop.withOptions(options);
    }


    private void createFontText() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "r0c0i - Linotte Regular.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(getContext().getAssets(), "r0c0iLinotte-Bold.ttf");
        txtYourImage.setTypeface(typefaceBold);
        txtBackGround.setTypeface(typefaceBold);
        txtLibrary.setTypeface(typefaceBold);
    }

    private void initAdsBanner() {
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                checkLoadedAds=true;
                mAdView.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                checkLoadedAds=false;
                mAdView.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
            }
        });
    }
}

