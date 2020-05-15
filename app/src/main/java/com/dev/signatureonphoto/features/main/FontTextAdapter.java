package com.dev.signatureonphoto.features.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.signatureonphoto.R;

import java.util.ArrayList;


public class FontTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> fontTexts;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    public int selectedPosition = -1;

    public FontTextAdapter(ArrayList<String> fontTexts, Context context, OnItemClickListener listener) {
        this.fontTexts = fontTexts;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_font_text, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String fontText = fontTexts.get(position);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (fontText != null) {
            if (selectedPosition == position) {
                myViewHolder.imgTick.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.imgTick.setVisibility(View.GONE);
            }
            //set text font
            try{
                Typeface tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts/"+ fontText);
                myViewHolder.txtFont.setTypeface(tf);
                myViewHolder.txtPosition.setText(String.valueOf(position));
            }catch (Exception ex){
            }


            myViewHolder.txtFont.setOnClickListener(new View.OnClickListener() {
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
        return fontTexts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFont;
        private ImageView imgTick;
        private ImageView imgNew;
        TextView txtPosition;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(View itemView) {
            super(itemView);
            txtFont = itemView.findViewById(R.id.txt_font);
            imgTick = itemView.findViewById(R.id.imgTickFont);
            imgNew = itemView.findViewById(R.id.img_new);
            txtPosition=itemView.findViewById(R.id.txt_position);
        }
    }

    public interface OnItemClickListener {
         void onItemCheck(View view, int position);
    }

}
