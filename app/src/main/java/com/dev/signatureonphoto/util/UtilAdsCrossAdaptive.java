package com.dev.signatureonphoto.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.ItemCross;

import java.util.ArrayList;
import java.util.Random;

public class UtilAdsCrossAdaptive {

    public static View getLayoutCross(Context context, ArrayList<ItemCross> listCross){
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads_cross_adaptive, null);
        ItemCross itemCross = listCross.get(new Random().nextInt(listCross.size()));
        ImageView imgIcon=view.findViewById(R.id.img_icon_app);
        TextView txtName=view.findViewById(R.id.txt_name_app);
        TextView txtDescription=view.findViewById(R.id.txt_description);
        TextView txtInstall=view.findViewById(R.id.btn_install);
        ImageView imgAdchoice=view.findViewById(R.id.img_adchoice);

        txtDescription.setMaxLines(2);
        txtDescription.setEllipsize(TextUtils.TruncateAt.END);

        txtName.setText(itemCross.name_app);
        txtDescription.setText(itemCross.description_app);
        if (itemCross.package_name.equals("dev.com.qrcodefastest")) {
            imgIcon.setImageResource(R.drawable.ic_icon_qrcode);
        } else if (itemCross.package_name.equals("dev.com.camerafilter")) {
            imgIcon.setImageResource(R.drawable.ic_icon_signature);
        }else if (itemCross.package_name.equals("dev.com.testwifi.networkspeedtest")) {
            imgIcon.setImageResource(R.drawable.ic_icon_speed_test);
        }
        txtInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                intentUpdate.setData(Uri.parse(Constants.PLAY_STORE_LINK + itemCross.package_name));
                context.startActivity(intentUpdate);
            }
        });
        imgAdchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRate = new Intent(Intent.ACTION_VIEW);
                intentRate.setData(Uri.parse(Constants.URL_INHOUSE_ADS));
                context.startActivity(intentRate);
            }
        });

        return view;

    }
}
