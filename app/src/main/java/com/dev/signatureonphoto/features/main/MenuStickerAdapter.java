package com.dev.signatureonphoto.features.main;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.response.StickerItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuStickerAdapter extends RecyclerView.Adapter<MenuStickerAdapter.MenuHolder> {
    private Context context;
    private List<StickerItem> stickers;
    private MenuStickerListener listener;
    private int lastSelect = 0;
    private int selected = 0;

    public MenuStickerAdapter(Context context, List<StickerItem> stickers, MenuStickerListener listener) {
        this.context = context;
        this.stickers = stickers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    class MenuHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_new)
        TextView txtNew;
        @BindView(R.id.img_preview)
        ImageView imgPreview;
        @BindView(R.id.select_view)
        View selectView;

        private int position;

        @OnClick(R.id.img_preview)
        void onMenuItemClick() {
            notifyItemChanged(lastSelect);
            lastSelect = position;
            selected = position;
            notifyItemChanged(selected);

            listener.onMenuItemSelected(position);
        }

        MenuHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(int position) {
            this.position = position;
            boolean newSticker = stickers.get(position).isNewSticker();

            if (selected == position) {
                selectView.setVisibility(View.VISIBLE);
            } else {
                selectView.setVisibility(View.GONE);
            }

            if (newSticker) {
                txtNew.setVisibility(View.VISIBLE);
            } else {
                txtNew.setVisibility(View.GONE);
            }
            Glide.with(context).load(stickers.get(position).getImageSticker()).into(imgPreview);
        }
    }
}
