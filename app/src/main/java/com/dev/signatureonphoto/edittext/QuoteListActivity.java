package com.dev.signatureonphoto.edittext;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.database.DataManager;
import com.dev.signatureonphoto.database.Quote;
import com.dev.signatureonphoto.edittext.fragments.AddQuoteFragment;
import com.dev.signatureonphoto.edittext.fragments.LibraryFragment;
import com.dev.signatureonphoto.features.base.BaseActivity;
import com.dev.signatureonphoto.injection.component.ActivityComponent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;

public class QuoteListActivity extends BaseActivity implements QuoteListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.quote_pager)
    ViewPager quotePager;
    @BindView(R.id.img_background)
    ImageView imgBackground;
    @BindColor(R.color.black)
    int colorSelect;
    @BindColor(R.color.black_transparent)
    int colorUnselect;

    private QuotePagerAdapter adapter;
    private static final int QUOTE_REQUEST_CODE = 0;

    //Admob
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        Glide.with(this).load(R.drawable.ic_bg_iap).into(imgBackground);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == QUOTE_REQUEST_CODE) {
            String content = data.getStringExtra(Constants.QUOTE);
            Quote quote = new Quote();
            quote.setContent(content);
            quote.setCategore("");
            quote.setSource("");
            quote.setUpload(Constants.FALSE);
            DataManager.query().getQuoteDao().save(quote);
            AddQuoteFragment addQuoteFragment = (AddQuoteFragment) adapter.getFragments().get(1);
            if (addQuoteFragment != null) {
                addQuoteFragment.refreshData();
            }
        }
    }

    private void initData() {
        LibraryFragment libraryFragment = new LibraryFragment();
        libraryFragment.setListener(this);
        AddQuoteFragment addQuoteFragment = new AddQuoteFragment();
        addQuoteFragment.setListener(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(libraryFragment);
        fragments.add(addQuoteFragment);
        adapter = new QuotePagerAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        quotePager.setAdapter(adapter);
        tabLayout.setupWithViewPager(quotePager);
        quotePager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(quotePager));
    }

    private void initView() {
        tabLayout.setTabTextColors(colorUnselect, colorSelect);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_quote_list;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
    }

    @Override
    protected void detachPresenter() {
    }

    @Override
    public void onQuoteSelected(String quote) {
        setClipboard(quote);
        Intent intent = getIntent();
        intent.putExtra(Constants.QUOTE, quote);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void addQuote() {
        startActivityForResult(new Intent(this, AddQuoteActivity.class), QUOTE_REQUEST_CODE);
    }

    @Override
    public void onDelete() {
        AddQuoteFragment addQuoteFragment = (AddQuoteFragment) adapter.getFragments().get(1);
        addQuoteFragment.refreshData();
    }

    private Quote quoteupload;

    @Override
    public void onUpload(Quote quote) {
        quoteupload = quote;
    }

    @Override
    public void onUploadSuccess() {
        quoteupload.setUpload(Constants.TRUE);
        List<Quote> quotes = DataManager.query().getQuoteDao().queryBuilder().list();
        if (quotes.size() > 0) {
            DataManager.query().getQuoteDao().update(quoteupload);
        }
        AddQuoteFragment addQuoteFragment = (AddQuoteFragment) adapter.getFragments().get(1);
        addQuoteFragment.refreshData();
        showDialogNotifyUpload(true);
    }

    @Override
    public void ononUploadFailed() {
        quoteupload.setUpload(Constants.FALSE);
        List<Quote> quotes = DataManager.query().getQuoteDao().queryBuilder().list();
        if (quotes.size() > 0) {
            DataManager.query().getQuoteDao().update(quoteupload);
        }
        AddQuoteFragment addQuoteFragment = (AddQuoteFragment) adapter.getFragments().get(1);
        addQuoteFragment.refreshData();
        showDialogNotifyUpload(false);
    }

    public void showDialogNotifyUpload(boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialognotifyupload, null);
        final Button btnOk = (Button) alertLayout.findViewById(R.id.btnOk);
        final TextView tvTitle = (TextView) alertLayout.findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/utm_neo_san.ttf");
        tvTitle.setTypeface(font);
        if (isSuccess) {
            tvTitle.setText(getString(R.string.uploadSuccess));
        } else {
            tvTitle.setText(getString(R.string.uploadFailed));
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void setClipboard(String quote) {
        ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("", quote);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.quote_copied, Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        finish();
    }
}
