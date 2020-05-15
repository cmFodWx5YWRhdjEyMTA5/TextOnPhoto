package com.dev.signatureonphoto.features.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.GroupFilter;
import com.dev.signatureonphoto.data.model.response.Filter;
import com.dev.signatureonphoto.util.AppPreference;
import com.dev.signatureonphoto.util.filter.ConstantsFilter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dev.signatureonphoto.Constants.PRE_REMOVED_UNLOCK_FEATURE;
import static com.dev.signatureonphoto.Constants.PRE_UNLOCK_ALL_FEATURE;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ExpandHolder> {
    private Activity context;
    private List<List<Filter>> filters;
    private boolean itemsStatus[] = {false, false, false, false, false, false};
    private FilterListener listener;
    private String[] groupNames = {"Portrail", "Nature", "Classic"};
    private SharedPreferences preferences;
    private String productId;
    private List<GroupFilter> listIap;

    FilterAdapter(Activity context, List<List<Filter>> filters, List<GroupFilter> listIAP) {
        this.context = context;
        this.filters = filters;
        preferences = context.getSharedPreferences(Constants.ADS_FILTER, Context.MODE_PRIVATE);
        this.listIap = listIAP;
    }

    public void setListener(FilterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpandHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter_group, viewGroup, false);
        return new ExpandHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpandHolder expandHolder, int i) {
        expandHolder.onBind(i);
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    class ExpandHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_filter)
        LinearLayout listFilter;
        @BindView(R.id.txt_group_item_name)
        TextView txtGroupName;
        @BindView(R.id.group_view)
        RelativeLayout groupView;
        @BindView(R.id.img_groupview)
        ImageView imgGroupView;

        int position = 0;

        ExpandHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(final int position) {
            this.position = position;
            final int itemSize = context.getResources().getDimensionPixelSize(R.dimen.item_size);
            int filterNumber = filters.get(position).size();
            final int listSize = itemSize * filterNumber;
            if (position == 0) {
                Glide.with(context)
                        .load(R.drawable.bg_filter_portrait)
                        .apply(new RequestOptions().skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(imgGroupView);
                txtGroupName.setText(groupNames[0]);
                txtGroupName.setBackgroundColor(Color.parseColor("#2da2f6"));
            } else if (position == 1) {
                Glide.with(context)
                        .load(R.drawable.bg_filter_classic)
                        .apply(new RequestOptions().skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(imgGroupView);
                txtGroupName.setText(groupNames[1]);
                txtGroupName.setBackgroundColor(Color.parseColor("#40f23d"));
            } else if (position == 2) {
                Glide.with(context)
                        .load(R.drawable.bg_filter_nature)
                        .apply(new RequestOptions().skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(imgGroupView);
                txtGroupName.setText(groupNames[2]);
                txtGroupName.setBackgroundColor(Color.parseColor("#f9fd10"));
            }

            groupView.setOnClickListener(v -> {

                if (!itemsStatus[position]) {
                    itemsStatus[position] = true;

                    listFilter.animate().x(-listSize).start();
                    new Handler().postDelayed(() -> {
                        listFilter.setVisibility(View.VISIBLE);
                        listFilter.animate().x(itemSize).setDuration(200).start();
                        listener.updateItem(position, itemSize * position);
                    }, 100);
                } else {
                    itemsStatus[position] = false;

                    listFilter.animate().x(-listSize).setDuration(300).start();
                    new Handler().postDelayed(() -> listFilter.setVisibility(View.GONE), 310);
                }
            });

            listFilter.removeAllViews();
            for (int i = 0; i < filterNumber; i++) {
                Filter filter = filters.get(position).get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.item_filter, null);
                RoundedImageView imageView = view.findViewById(R.id.img_filter_preview);
                imageView.setImageBitmap(filter.getFilterThumb());
                TextView filterName = view.findViewById(R.id.txt_filter_preview_name);
                RoundedImageView imgPro=view.findViewById(R.id.txt_pro);
                if (AppPreference.getInstance(context).getKeyRate(PRE_UNLOCK_ALL_FEATURE, false) ||
                        AppPreference.getInstance(context).getKeyRate(PRE_REMOVED_UNLOCK_FEATURE, false)) {
                    imgPro.setVisibility(View.GONE);
                }else{
                    imgPro.setVisibility(View.VISIBLE);
                }
                if (i == 0) {
                    filterName.setText(Constants.FILTER_DEFAULT_NAME);
                } else {
                    filterName.setText(Character.toString(groupNames[position].charAt(0)) + i);
                }

                if (listIap.size() > position) {
                    if (!listIap.get(position).isPurchased()) {
                        preferences.edit().putBoolean(String.valueOf(position), false).apply();
                    } else {
                        preferences.edit().putBoolean(String.valueOf(position), true).apply();
                    }
                } else {
                    preferences.edit().putBoolean(String.valueOf(position), false).apply();
                }

                boolean isOpen = preferences.getBoolean(String.valueOf(position), false);

                imageView.setOnClickListener(v -> {
                    if (position != 0) {
                        if (!isOpen) {
                            ConstantsFilter.REFRESH_LIST = true;
                            if (position == 1) {
                                productId = Constants.FILTERB;
                            } else if (position == 2) {
                                productId = Constants.FILTERC;
                            }
                        }
                    }
                    listener.onFilterSelected(filter.getFilterPath());
                });

                listFilter.addView(view);
            }
            if (itemsStatus[position]) {
                listFilter.setVisibility(View.VISIBLE);
            } else {
                listFilter.setVisibility(View.GONE);
            }
        }
    }
}
