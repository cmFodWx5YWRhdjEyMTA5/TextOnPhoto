package com.dev.signatureonphoto.features.crosslist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.ItemCross;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CrossListActivity extends AppCompatActivity {
    @BindView(R.id.recycler_cross)
    RecyclerView recyclerViewCross;
    @BindView(R.id.img_adchoice)
    ImageView imgAdchoice;
    @BindView(R.id.txt_adchoice)
    TextView txtAdchoice;

    private Animation animation;
    private Animation animationHiden;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cross_list);
        ButterKnife.bind(this);
        loadAdsCross();
        initAnimation();
    }
    private void initAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_adchoice);
        animationHiden = AnimationUtils.loadAnimation(this, R.anim.anim_adchoice_hiden);
    }

    private void loadAdsCross() {
        CrossAdapter adsCrossAdapter = new CrossAdapter(this, initListCross());
        recyclerViewCross.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCross.setAdapter(adsCrossAdapter);
    }

    private List<ItemCross> initListCross() {
        List<ItemCross> listInfoApp = new ArrayList<>();
        listInfoApp.add(new ItemCross("Watermark photo", "How to watermark photos, signature maker, design signature, watermark maker", "dev.com.camerafilter"));
        listInfoApp.add(new ItemCross("QR code free", "Read all types of barcodes with fast and accurate speed. Support to create code quickly...", "dev.com.qrcodefastest"));
        listInfoApp.add(new ItemCross("Quotes picture", "Create photos of your own style by inserting text. Quotes to photo, quotes with photo, quotes for picture, create quotes", "com.dev.quotesmaker.imagequotes"));
        listInfoApp.add(new ItemCross("Wifi speed test", "How fast is my internet, test my internet speed, test wifi speed.", "dev.com.testwifi.networkspeedtest"));
        Collections.shuffle(listInfoApp);
        return listInfoApp;
    }

    @OnClick({R.id.img_back, R.id.img_adchoice,R.id.txt_adchoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_adchoice:
                txtAdchoice.startAnimation(animation);
                txtAdchoice.setVisibility(View.VISIBLE);

                new CountDownTimer(6000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        txtAdchoice.startAnimation(animationHiden);
                        txtAdchoice.setVisibility(View.GONE);
                    }
                }.start();
                break;
            case R.id.txt_adchoice:
                Intent intentRate = new Intent(Intent.ACTION_VIEW);
                intentRate.setData(Uri.parse(Constants.URL_INHOUSE_ADS));
                startActivity(intentRate);
                break;
        }
    }
}
