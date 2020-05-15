package com.dev.signatureonphoto.features.color;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.response.ColorBackground;

import java.util.ArrayList;

public class ColorBackgroundAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ColorBackground> colorBackgrounds;
    private Context context;
    private LayoutInflater inflater;
    private ColorBackgroundAdapter.OnItemClickListener listener;
    int selectedPosition = -1;

    public ColorBackgroundAdapter(ArrayList<ColorBackground> colorBackgrounds, Context context, ColorBackgroundAdapter.OnItemClickListener listener) {
        this.colorBackgrounds = colorBackgrounds;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_color_background, parent, false);
        return new ColorBackgroundAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ColorBackground colorBackground = colorBackgrounds.get(position);
        final ColorBackgroundAdapter.MyViewHolder myViewHolder = (ColorBackgroundAdapter.MyViewHolder) holder;
        if (colorBackground != null) {
            if (selectedPosition == position) {
                myViewHolder.btnTemplate.setBackgroundColor(context.getResources().getColor(R.color.colorItemTemplate));
                myViewHolder.imgTick.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.imgTick.setVisibility(View.GONE);
            }
            myViewHolder.imgTemplate.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
            myViewHolder.imgTemplate.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
            myViewHolder.imgTemplate.setBackgroundColor(context.getResources().getColor(colorBackground.getImage()));
            myViewHolder.imgTemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemCheck(view, position);
                    selectedPosition = position;
                    notifyDataSetChanged();
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return colorBackgrounds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgTemplate;
        private RelativeLayout btnTemplate;
        private ImageView imgTick;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(View itemView) {
            super(itemView);

            imgTemplate = itemView.findViewById(R.id.imgTemplate);
            btnTemplate = itemView.findViewById(R.id.btnTemplate);
            imgTick = itemView.findViewById(R.id.imgTick);
        }
    }

    public interface OnItemClickListener {
        public void onItemCheck(View view, int position);
    }

    public void setOnItemClickListener(final ColorBackgroundAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}

