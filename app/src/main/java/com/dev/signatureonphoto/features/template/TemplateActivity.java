package com.dev.signatureonphoto.features.template;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;

import com.dev.signatureonphoto.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TemplateActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_template);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager() {
        viewPager.setAdapter(new com.dev.signatureonphoto.features.template.PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(TemplateActivity.this, R.color.color_ads));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
