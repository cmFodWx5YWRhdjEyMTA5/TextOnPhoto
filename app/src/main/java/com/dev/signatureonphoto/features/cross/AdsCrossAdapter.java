package com.dev.signatureonphoto.features.cross;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.util.CheckInfoApp;

import java.util.List;

public class AdsCrossAdapter extends PagerAdapter {
    private List<CrossItem> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private View view;

    public AdsCrossAdapter(List<CrossItem> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.layout_ads_cross, container, false);
        //init view
        ImageView imgIconApp, imgAdChoice,screen1,screen2,screen3;
        final TextView txtNameApp, txtDescriptionApp, txtAdver;
        Button btnInstall;

        imgIconApp = view.findViewById(R.id.img_icon_app);
        imgAdChoice = view.findViewById(R.id.img_adchoice);
        screen1=view.findViewById(R.id.screen1);
        screen2=view.findViewById(R.id.screen2);
        screen3=view.findViewById(R.id.screen3);
        txtNameApp = view.findViewById(R.id.txt_name_app);
        txtDescriptionApp = view.findViewById(R.id.txt_description_app);
        txtAdver = view.findViewById(R.id.txt_adver);
        btnInstall = view.findViewById(R.id.btn_install);
        final CrossItem crossItem=models.get(position);

        if (crossItem.getPackagename().equals("dev.com.qrcodefastest")) {
            //set title, content
            txtNameApp.setText(crossItem.getTitle());
            txtDescriptionApp.setText(crossItem.getContent());
            //set icon
            imgIconApp.setBackgroundResource(R.drawable.ic_icon_qrcode);
            //set screenshot
            screen1.setBackgroundResource(R.drawable.ic_qrcode_1);
            screen2.setBackgroundResource(R.drawable.ic_qrcode_2);
            screen3.setBackgroundResource(R.drawable.ic_qrcode_3);
        } else if (crossItem.getPackagename().equals("dev.com.camerafilter")) {
            //set title, content
            txtNameApp.setText(crossItem.getTitle());
            txtDescriptionApp.setText(crossItem.getContent());
            //set icon
            imgIconApp.setBackgroundResource(R.drawable.ic_icon_signature);
            //set screenshot
            screen1.setBackgroundResource(R.drawable.ic_signature_1);
            screen2.setBackgroundResource(R.drawable.ic_signature_2);
            screen3.setBackgroundResource(R.drawable.ic_signature_3);
        }else if (crossItem.getPackagename().equals("dev.com.testwifi.networkspeedtest")) {
            //set title, content
            txtNameApp.setText(crossItem.getTitle());
            txtDescriptionApp.setText(crossItem.getContent());
            //set icon
            imgIconApp.setBackgroundResource(R.drawable.ic_icon_speed_test);
            //set screenshot
            screen1.setBackgroundResource(R.drawable.ic_speedtest_2);
            screen2.setBackgroundResource(R.drawable.ic_speedtest_1);
            screen3.setBackgroundResource(R.drawable.ic_speedtest_3);
        }

        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                intentUpdate.setData(Uri.parse(Constants.PLAY_STORE_LINK + crossItem.getPackagename()));
                context.startActivity(intentUpdate);
            }
        });
        imgAdChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                intentUpdate.setData(Uri.parse(Constants.URL_INHOUSE_ADS));
                context.startActivity(intentUpdate);
            }
        });

        if (CheckInfoApp.appInstalledOrNot((Activity) context, crossItem.getPackagename())) {
            btnInstall.setText("OPEN");
        } else {
            btnInstall.setText("INSTALL");
        }

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
