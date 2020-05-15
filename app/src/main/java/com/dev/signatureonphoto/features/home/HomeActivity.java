package com.dev.signatureonphoto.features.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.features.crosslist.CrossListActivity;
import com.dev.signatureonphoto.features.purchased.PurchasedActivity;
import com.dev.signatureonphoto.features.settings.SettingsActivity;
import com.dev.signatureonphoto.tip.TipActivity;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static com.dev.signatureonphoto.Constants.GAME_SETTING;

public class HomeActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    @BindView(R.id.img_gift_pro)
    ImageView imgGiftPro;
    @BindView(R.id.imgBgMain)
    ImageView imgBgMain;
    @BindView(R.id.progress_bar_ads_full)
    ProgressBar progressBarAdsFull;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final String TEXT_POLICY = "";
    private FirebaseAnalytics firebaseAnalytics;
    private InterstitialAd mInterstitialAdTips;
    private boolean checkLoadAdsBack = true;
    private Disposable disposable;
    private int timeMax = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_xmas);
        ButterKnife.bind(this);
        analyticsApplication();

        sharedPreferences = this.getSharedPreferences(GAME_SETTING, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, null);
        drawerFragment.setDrawerListener(this);

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
        Glide.with(this).asGif().load(R.drawable.gif_pro).into(imgGiftPro);

    }


    private void analyticsApplication() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        switch (position) {
            case 0:
                checkUpdate();
                break;
            case 1:
                processShare();
                break;
            case 2:
                String mailSubject = "Feedback";
                String mailContent = getString(R.string.mail_content);
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType(Constants.MAIL_TYPE);
                mailIntent.putExtra(Intent.EXTRA_EMAIL, Constants.MAIL_LIST);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                mailIntent.putExtra(Intent.EXTRA_TEXT, mailContent);
                startActivity(Intent.createChooser(mailIntent, mailSubject + ":"));
                break;
            case 3:
                Intent intentPolicy = new Intent(Intent.ACTION_VIEW);
                intentPolicy.setData(Uri.parse(TEXT_POLICY));
                startActivity(intentPolicy);
                break;
        }
    }

    private void checkUpdate() {
        Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
        intentUpdate.setData(Uri.parse(Constants.PLAY_STORE_LINK + getApplicationContext().getPackageName()));
        startActivity(intentUpdate);
    }

    private void processShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Constants.PLAY_STORE_LINK + getPackageName());
        sendIntent.setType(Constants.DATA_TYPE);
        startActivity(sendIntent);
    }

    private void moveTipsActivity() {
        startActivity(new Intent(HomeActivity.this, TipActivity.class));
    }

    private static String getFacebookURL(Context context, String facebookUrl, String namePage) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
//
            return "fb://page/" + namePage;
//            }
        } catch (PackageManager.NameNotFoundException e) {
            return facebookUrl; //normal web url
        }
    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.img_ads_cross, R.id.layout_vip_iap,R.id.img_settings})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_ads_cross:
                Intent intent = new Intent(HomeActivity.this, CrossListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_vip_iap:
                startActivity(new Intent(HomeActivity.this, PurchasedActivity.class));
                break;
            case R.id.img_settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
        }
    }

}
