package com.dev.signatureonphoto.features.finish;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.features.home.HomeActivity;
import com.dev.signatureonphoto.features.saved.SavedActivity;
import com.dev.signatureonphoto.util.Constants;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinishActivity extends AppCompatActivity {

    @BindView(R.id.img_background_finish)
    ImageView imgBackgroundFinish;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.text_finish)
    ImageView textFinish;
    @BindView(R.id.layout_create_new)
    LinearLayout layoutCreateNew;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.txt_create_new)
    TextView txtCreateNew;
    @BindView(R.id.txt_user_image)
    TextView txtUserImage;
    @BindView(R.id.layout_user_image)
    LinearLayout layoutUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        ButterKnife.bind(this);
        createFontText();
        Glide.with(this).load(R.drawable.ic_bg_splash).into(imgBackgroundFinish);
    }

    @OnClick({ R.id.layout_create_new, R.id.btnBack, R.id.layout_user_image, R.id.txtHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtHome:
                Intent intenthome = new Intent(FinishActivity.this, HomeActivity.class);
                intenthome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);
                finish();
                break;
            case R.id.layout_create_new:
                Intent createIntent = new Intent(FinishActivity.this, HomeActivity.class);
                createIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(createIntent);
                finish();
                break;
            case R.id.layout_user_image:
                Intent createImageIntent = new Intent(FinishActivity.this, SavedActivity.class);
                createImageIntent.putExtra(Constants.ISFROMCONGRAT,true);
                createImageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(createImageIntent);
                finish();
            case R.id.btnBack:
                finish();
                break;
        }
    }

    private void createFontText() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "r0c0i - Linotte Regular.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "r0c0iLinotte-Bold.ttf");
        txtContent.setTypeface(typeface);
        txtCreateNew.setTypeface(typefaceBold);
        txtUserImage.setTypeface(typefaceBold);
    }
}
