package com.dev.signatureonphoto.features.crosslist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.ItemCross;
import com.dev.signatureonphoto.util.CheckInfoApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CrossAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ItemCross> listHistory;
    private static int NORMAL_ITEM = 0;

    public CrossAdapter(Context context, List<ItemCross> listHistory) {
        this.context = context;
        this.listHistory = listHistory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads_cross, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryHolder historyHolder = (HistoryHolder) holder;
        historyHolder.onBind(listHistory.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_ITEM;
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        private ItemCross itemCross;
        @BindView(R.id.txt_name_app)
        TextView txtNameApp;
        @BindView(R.id.txt_description)
        TextView txtDescription;
        @BindView(R.id.img_icon_app)
        ImageView imgIconApp;
        @BindView(R.id.btn_install)
        TextView btnInstall;

        HistoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            txtNameApp.setMaxLines(1);
            txtNameApp.setEllipsize(TextUtils.TruncateAt.END);
            txtDescription.setMaxLines(2);
            txtDescription.setEllipsize(TextUtils.TruncateAt.END);
            addListenerItem();
        }

        private void addListenerItem() {
            btnInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemCross.package_name.equals(context.getPackageName())){
                        if (CheckInfoApp.appInstalledOrNot((Activity) context, itemCross.package_name)) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, Constants.PLAY_STORE_LINK + itemCross.package_name);
                            sendIntent.setType(Constants.DATA_TYPE);
                            context.startActivity(sendIntent);
                        }else{
                            Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                            intentUpdate.setData(Uri.parse(Constants.PLAY_STORE_LINK + itemCross.package_name));
                            context.startActivity(intentUpdate);
                        }
                    }else{
                        Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                        intentUpdate.setData(Uri.parse(Constants.PLAY_STORE_LINK + itemCross.package_name));
                        context.startActivity(intentUpdate);
                    }
                }
            });
        }

        void onBind(ItemCross itemCross) {
            this.itemCross = itemCross;
            txtNameApp.setText(itemCross.name_app);
            txtDescription.setText(itemCross.description_app);
            if (itemCross.package_name.equals("dev.com.qrcodefastest")) {
                imgIconApp.setImageResource(R.drawable.ic_icon_qrcode);
            } else if (itemCross.package_name.equals("com.dev.quotesmaker.imagequotes")) {
                imgIconApp.setImageResource(R.drawable.ic_app);
            } else if (itemCross.package_name.equals("dev.com.camerafilter")) {
                imgIconApp.setImageResource(R.drawable.ic_icon_signature);
            }else if (itemCross.package_name.equals("dev.com.testwifi.networkspeedtest")) {
                imgIconApp.setImageResource(R.drawable.ic_icon_speed_test);
            }
            if (CheckInfoApp.appInstalledOrNot((Activity) context, itemCross.package_name)) {
                if(itemCross.package_name.equals(context.getPackageName())){
                    btnInstall.setText("Share");
                }else{
                    btnInstall.setText("Open");
                }
            } else {
                btnInstall.setText("Install");
            }
        }
    }
}
