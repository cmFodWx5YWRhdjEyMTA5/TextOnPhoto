package com.dev.signatureonphoto.features.template.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.ImageTemplate;
import com.dev.signatureonphoto.util.AppPreference;

import java.util.ArrayList;

import static com.dev.signatureonphoto.Constants.PRE_REMOVED_ADS;
import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;

public class BackgroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ImageTemplate> imageTemplates;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    int selectedPosition = -1;

    public BackgroundAdapter(ArrayList<ImageTemplate> imageTemplates, Context context, OnItemClickListener listener) {
        this.imageTemplates = imageTemplates;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_template, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ImageTemplate imageTemplate = imageTemplates.get(position);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (imageTemplate != null) {
            if (selectedPosition == position) {
                myViewHolder.btnTemplate.setBackgroundColor(context.getResources().getColor(R.color.colorItemTemplate));
                myViewHolder.imgTick.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.imgTick.setVisibility(View.GONE);
            }

            myViewHolder.imgTemplate.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
            myViewHolder.imgTemplate.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
            //Load image background
            Glide.with(context).load(imageTemplate.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.bg_gray)
                            .priority(Priority.LOW))
                    .thumbnail(0.01f)
                    .into(myViewHolder.imgTemplate);

            myViewHolder.imgTemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemCheck(view, position);
                    selectedPosition = position;
                    notifyDataSetChanged();
                }
            });

            if (AppPreference.getInstance(context).getKeyRate(PRE_REMOVED_ADS, false) ||
                    AppPreference.getInstance(context).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                myViewHolder.layoutAds.setVisibility(View.GONE);
            } else {
                if (imageTemplate.isCheckAds()) {
                    myViewHolder.layoutAds.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.layoutAds.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return imageTemplates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgTemplate;
        private RelativeLayout btnTemplate;
        private ImageView imgTick;
        ImageView layoutAds;

        @SuppressLint("WrongViewCast")
        MyViewHolder(View itemView) {
            super(itemView);

            imgTemplate = itemView.findViewById(R.id.imgTemplate);
            btnTemplate = itemView.findViewById(R.id.btnTemplate);
            imgTick = itemView.findViewById(R.id.imgTick);
            layoutAds = itemView.findViewById(R.id.img_pro);
        }
    }

    public interface OnItemClickListener {
        void onItemCheck(View view, int position);
    }

    public void resetBackgroundList() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

}
