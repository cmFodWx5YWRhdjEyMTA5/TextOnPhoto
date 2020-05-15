package com.dev.signatureonphoto.features.purchased;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.util.AppPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dev.signatureonphoto.Constants.KEY_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.KEY_REMOVED_UNLOCK_FEATURE;
import static com.dev.signatureonphoto.Constants.KEY_UNLOCK_ALL_FEATURE;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;
import static com.dev.signatureonphoto.Constants.PRE_UNLOCK_ALL_FEATURE;
import static com.dev.signatureonphoto.Constants.TRACKING_BUY_IAP_SUCCESS;


public class PurchasedActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    private static final String BUY_REMOVED_ADS = "BUY_REMOVED_ADS";
    private static final String BUY_REMOVED_UNLOCK_FEATURE = "BUY_REMOVED_UNLOCK_FEATURE";
    private static final String BUY_UNLOCK_ALL_FEATURE = "BUY_UNLOCK_ALL_FEATURE";
    @BindView(R.id.txt_price_removed)
    TextView txtPriceRemoved;
    @BindView(R.id.txt_price_old_removed)
    TextView txtPriceOldRemoved;
    @BindView(R.id.txt_buy_now_remove_ads)
    TextView txtBuyNowRemoveAds;
    @BindView(R.id.txt_price_unlimited)
    TextView txtPriceUnlimited;
    @BindView(R.id.txt_price_old_unlimited)
    TextView txtPriceOldUnlimited;
    @BindView(R.id.txt_buy_now_unlimited)
    TextView txtBuyNowUnlimited;
    @BindView(R.id.txt_price_removed_unlimited)
    TextView txtPriceRemovedUnlimited;
    @BindView(R.id.txt_price_old_remove_unlimited)
    TextView txtPriceOldRemoveUnlimited;
    @BindView(R.id.txt_buy_now_remove_unlimited)
    TextView txtBuyNowRemoveUnlimited;
    @BindView(R.id.layout_removed)
    RelativeLayout layoutRemoved;
    @BindView(R.id.layout_unlimited)
    RelativeLayout layoutUnlimited;
    @BindView(R.id.layout_removed_unlimited)
    RelativeLayout layoutRemovedUnlimited;
    @BindView(R.id.img_bg_iap)
    ImageView imgBackground;


    private BillingClient billingClientAll;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_purchased);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Glide.with(this)
                .load(R.drawable.ic_bg_iap)
                .apply(new RequestOptions().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imgBackground);
        setUpBillingClient();
        priceOld();
    }

    private void priceOld() {
        salePrice("3.3$", txtPriceOldRemoved);
        salePrice("5.5$", txtPriceOldRemoveUnlimited);
        salePrice("4.3$", txtPriceOldUnlimited);

    }

    private void salePrice(String price, TextView txtPrice) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(price);
        // Initialize a new StrikeThroughSpan to display strike through text
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        // Apply the strike through text to the span
        ssBuilder.setSpan(
                strikethroughSpan, // Span to add
                price.indexOf(price), // Start of the span (inclusive)
                price.indexOf(price) + String.valueOf(price).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        );
        // Display the spannable text to TextView
        txtPrice.setText(ssBuilder);
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
                            AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_REMOVED_ADS, true);
                            txtBuyNowRemoveAds.setText("Purchased");
                            layoutRemoved.setBackgroundResource(R.drawable.bg_gray_radius);
                        } else if (purchases.get(i).getSku().equals(KEY_UNLOCK_ALL_FEATURE)) {
                            AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_UNLOCK_ALL_FEATURE, true);
                            txtBuyNowUnlimited.setText("Purchased");
                            layoutUnlimited.setBackgroundResource(R.drawable.bg_gray_radius);
                        } else if (purchases.get(i).getSku().equals(KEY_REMOVED_UNLOCK_FEATURE)) {
                            AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_REMOVED_UNLOCK_FEATURE, true);
                            txtBuyNowRemoveUnlimited.setText("Purchased");
                            layoutRemovedUnlimited.setBackgroundResource(R.drawable.bg_gray_radius);
                        }
                    }
                } else {
                    AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_REMOVED_ADS, false);
                    AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_UNLOCK_ALL_FEATURE, false);
                    AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false);
                }
                getPriceFromServer();
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void getPriceFromServer() {
        if (billingClientAll.isReady()) {
            SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList(KEY_REMOVED_ADS, KEY_UNLOCK_ALL_FEATURE, KEY_REMOVED_UNLOCK_FEATURE))
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClientAll.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            txtPriceRemoved.setText(skuDetailsList.get(0).getPrice());
                            txtPriceRemovedUnlimited.setText(skuDetailsList.get(2).getPrice());
                            txtPriceUnlimited.setText(skuDetailsList.get(1).getPrice());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (purchases != null && purchases.size() > 0) {
            for (int i = 0; i < purchases.size(); i++) {
                if (purchases.get(i).getSku().equals(KEY_REMOVED_ADS)) {
                    AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_REMOVED_ADS, true);
                } else if (purchases.get(i).getSku().equals(KEY_UNLOCK_ALL_FEATURE)) {
                    AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_UNLOCK_ALL_FEATURE, true);
                } else if (purchases.get(i).getSku().equals(KEY_REMOVED_UNLOCK_FEATURE)) {
                    AppPreference.getInstance(PurchasedActivity.this).setKeyRate(PRE_REMOVED_UNLOCK_FEATURE, true);
                }
            }
            mFirebaseAnalytics.logEvent(TRACKING_BUY_IAP_SUCCESS, new Bundle());
        }
    }

    @OnClick({R.id.txt_buy_now_remove_ads, R.id.txt_buy_now_unlimited, R.id.txt_buy_now_remove_unlimited,
            R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_buy_now_remove_ads:
                buyIAPNow(BUY_REMOVED_ADS);
                break;
            case R.id.txt_buy_now_unlimited:
                buyIAPNow(BUY_UNLOCK_ALL_FEATURE);
                break;
            case R.id.txt_buy_now_remove_unlimited:
                buyIAPNow(BUY_REMOVED_UNLOCK_FEATURE);
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void buyIAPNow(String checkTypeIap) {
        if (billingClientAll.isReady()) {
            SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList(KEY_REMOVED_ADS, KEY_UNLOCK_ALL_FEATURE, KEY_REMOVED_UNLOCK_FEATURE))
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClientAll.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            if (checkTypeIap.equals(BUY_REMOVED_ADS) ) {
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSku(skuDetailsList.get(0).getSku()).setType(BillingClient.SkuType.INAPP).build();
                                billingClientAll.launchBillingFlow(PurchasedActivity.this, billingFlowParams);
                            } else if (checkTypeIap.equals(BUY_REMOVED_UNLOCK_FEATURE)) {
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSku(skuDetailsList.get(1).getSku()).setType(BillingClient.SkuType.INAPP).build();
                                billingClientAll.launchBillingFlow(PurchasedActivity.this, billingFlowParams);
                            } else if (checkTypeIap.equals(BUY_UNLOCK_ALL_FEATURE)) {
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSku(skuDetailsList.get(2).getSku()).setType(BillingClient.SkuType.INAPP).build();
                                billingClientAll.launchBillingFlow(PurchasedActivity.this, billingFlowParams);
                            }
                        }
                    }
                }
            });
        } else {
            Toast.makeText(PurchasedActivity.this, "Billing client not ready", Toast.LENGTH_SHORT).show();
        }
    }
}
