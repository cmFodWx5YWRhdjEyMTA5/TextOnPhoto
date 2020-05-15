package com.dev.signatureonphoto.edittext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.features.base.BaseActivity;
import com.dev.signatureonphoto.injection.component.ActivityComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class AddQuoteActivity extends BaseActivity {

    @BindView(R.id.txt_quote)
    EditText txtQuote;
    @BindView(R.id.img_background)
    ImageView imgBackground;

    @OnClick(R.id.btn_add)
    void addQuote(){
        String quote=txtQuote.getText().toString();
        if (quote.length()>0){
            Intent intent=getIntent();
            intent.putExtra(Constants.QUOTE, quote);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, R.string.input_null, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Glide.with(this).load(R.drawable.ic_bg_iap).into(imgBackground);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_quote;
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

    public void back(View view) {
        finish();
    }
}
