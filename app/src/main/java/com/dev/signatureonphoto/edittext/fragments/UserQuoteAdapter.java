package com.dev.signatureonphoto.edittext.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.database.DataManager;
import com.dev.signatureonphoto.database.Quote;
import com.dev.signatureonphoto.edittext.QuoteListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserQuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Quote> quotes;
    private QuoteListener listener;

    final int AD_TYPE = 1;
    final int CONTENT = 2;

    public UserQuoteAdapter(Context context, List<Quote> quotes, QuoteListener listener) {
        this.context = context;
        this.quotes = quotes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CONTENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_quote, parent, false);
                return new QuoteHolder(view);
            case AD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_in_list, parent, false);
                return new AdsHolder(view, context);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case CONTENT:
                ((QuoteHolder) holder).onBind(quotes.get(position));
                break;
            case AD_TYPE:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (quotes.get(position).getContent().equals(Constants.textads)) {
            return AD_TYPE;
        } else {
            return CONTENT;
        }
    }

    public class AdsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_native_ads)
        LinearLayout layoutAds;
        Context context;

        AdsHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

    }


    class QuoteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_content)
        TextView txtContent;
        @BindView(R.id.btn_upload)
        LinearLayout btn_upload;
        @BindView(R.id.ic_upload)
        ImageView ic_upload;
        @BindView(R.id.txt_like)
        TextView txt_like;

        private Quote quote;

        @OnClick({R.id.btn_copy, R.id.btn_delete, R.id.btn_upload})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_copy:
                    listener.onQuoteSelected(quote.getContent());
                    break;
                case R.id.btn_delete:
                    DataManager.query().getQuoteDao().delete(quote);
                    notifyDataSetChanged();
                    listener.onDelete();
                    break;
                case R.id.btn_upload:
                    if (listener != null) {
                        listener.onUpload(quote);
                    }
                    break;
            }
        }

        QuoteHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(Quote quote) {
            this.quote = quote;
            txtContent.setText(quote.getContent());
            if (quote.getUpload().equals(Constants.TRUE)) {
                btn_upload.setEnabled(false);
                ic_upload.setColorFilter(context.getResources().getColor(R.color.defaultColor),PorterDuff.Mode.SRC_ATOP);
                txt_like.setTextColor(context.getResources().getColor(R.color.defaultColor));
            } else {
                btn_upload.setEnabled(true);
                ic_upload.setColorFilter(Color.BLUE,PorterDuff.Mode.SRC_ATOP);
                txt_like.setTextColor(Color.BLUE);
            }
        }
    }
}
