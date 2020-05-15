package com.dev.signatureonphoto.edittext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.database.Quote;
import com.dev.signatureonphoto.features.base.BaseActivity;
import com.dev.signatureonphoto.injection.component.ActivityComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTextActivity extends BaseActivity {
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.edtQuote)
    EditText edtQuote;
    @BindView(R.id.btnRandom)
    LinearLayout btnRandom;
    @BindView(R.id.layout_clear_all)
    LinearLayout layoutClearAll;
    @BindView(R.id.img_background)
    ImageView imgBackground;

    private ArrayList<Quote> quoteList = new ArrayList<>();
    private static final String TAG = EditTextActivity.class.getName();
    private static final int QUOTE_REQUEST_CODE = 0;
    private String[] countryList = {"RU", "CN", "JP"};
    private Animation animationClick;

    @OnClick(R.id.btn_more_quotes)
    void getMoreQuotes() {
        startActivityForResult(new Intent(this, QuoteListActivity.class), QUOTE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == QUOTE_REQUEST_CODE) {
            edtQuote.setText(data.getStringExtra(Constants.QUOTE));
        }
    }

    private void sendEditText(int edittextResult) {
        Intent intent = getIntent();
        intent.putExtra(Constants.TEXT_EDITED, edtQuote.getText().toString());
        setResult(edittextResult, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getJson();
        initEvent();
//        final Intent intent = getIntent();
//        String quote = intent.getStringExtra(Constants.QUOTE);
//        edtQuote.setText(quote);
        animationClick = AnimationUtils.loadAnimation(this, R.anim.anim_click);
        Glide.with(this).load(R.drawable.ic_bg_iap).into(imgBackground);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_edittext;
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

    private void initEvent() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCancel.startAnimation(animationClick);
                finish();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSave.startAnimation(animationClick);
                sendEditText(com.dev.signatureonphoto.util.Constants.EDITTEXT_RESULT);
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRandom.startAnimation(animationClick);
                if (quoteList.size() > 0) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(quoteList.size());
                    edtQuote.setText(quoteList.get(randomIndex).getContent());
                }
            }
        });
    }

    public String loadJSONFromAsset(String quotes) {

        String json = null;
        try {
            InputStream is = getAssets().open(quotes);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getJson() {
        Locale current = getResources().getConfiguration().locale;
        String country = current.getCountry();
        final JSONArray quoteLists, quoteListsEn;
        try {
//            if (country.equals(countryList[0])) {
//                quoteLists = new JSONArray(loadJSONFromAsset(Constants.VI_JSON_LINK));
//                for (int i = 0; i < quoteLists.length(); i++) {
//                    JSONObject json_data = quoteLists.getJSONObject(i);
//                    Quote quote = new Quote();
//                    quote.setCategore(json_data.getString(Constants.VI_CATEGORY_FIELD));
//                    quote.setSource(json_data.getString(Constants.VI_SOURCE_FIELD));
//                    quote.setContent(json_data.getString(Constants.VI_CONTENT_FIELD));
//                    quote.setUpload(Constants.FALSE);
//                    this.quoteList.add(quote);
//                }
//                quoteListsEn = new JSONArray(loadJSONFromAsset(Constants.EN_JSON_LINK));
//                for (int i = 0; i < quoteListsEn.length(); i++) {
//                    JSONObject json_data = quoteListsEn.getJSONObject(i);
//                    Quote quote = new Quote();
//                    quote.setCategore(json_data.getString(Constants.EN_LOVE_FIELD));
//                    quote.setSource(json_data.getString(Constants.EN_S2QUOTE_FIELD));
//                    quote.setContent(json_data.getString(Constants.EN_CONTENT));
//                    quote.setUpload(Constants.FALSE);
//                    this.quoteList.add(quote);
//                }
//
//            } else
                if (!country.equals(countryList[0]) && !country.equals(countryList[1]) && !country.equals(countryList[2])) {
                quoteLists = new JSONArray(loadJSONFromAsset(Constants.EN_JSON_LINK));
                for (int i = 0; i < quoteLists.length(); i++) {
                    JSONObject json_data = quoteLists.getJSONObject(i);
                    Quote quote = new Quote();
                    quote.setCategore(json_data.getString(Constants.EN_LOVE_FIELD));
                    quote.setSource(json_data.getString(Constants.EN_S2QUOTE_FIELD));
                    quote.setContent(json_data.getString(Constants.EN_CONTENT));
                    quote.setUpload(Constants.FALSE);
                    this.quoteList.add(quote);
                }
            }

        } catch (JSONException e) {
        }
    }

    @OnClick({ R.id.layout_clear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_clear_all:
                layoutClearAll.startAnimation(animationClick);
                edtQuote.setText("");
                break;
        }
    }
}
