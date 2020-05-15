package com.dev.signatureonphoto.features.main;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.response.StickerItem;
import com.dev.signatureonphoto.features.main.customview.MyImageItem;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StickerItem> stickers;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public StickerAdapter(List<StickerItem> stickers, Context context, OnItemClickListener listener) {
        this.stickers = stickers;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_sticker, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        StickerItem sticker = stickers.get(position);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (sticker != null) {

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/"+ sticker.getPath()))
                    .thumbnail(0.1f)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_adchoice))
                    .into(myViewHolder.imgSticker);


            myViewHolder.imgSticker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemCheck(view, position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MyImageItem imgSticker;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(View itemView) {
            super(itemView);
            imgSticker = itemView.findViewById(R.id.itemSticker);
        }
    }

    public interface OnItemClickListener {
         void onItemCheck(View view, int position);
    }
}
