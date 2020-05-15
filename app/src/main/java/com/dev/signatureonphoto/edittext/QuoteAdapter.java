package com.dev.signatureonphoto.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.database.Quote;

import java.util.ArrayList;

public class QuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Quote> quotes;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public QuoteAdapter(ArrayList<Quote> quotes, Context context, OnItemClickListener listener) {
        this.quotes = quotes;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_quote, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Quote quote = quotes.get(position);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (quote != null) {
            myViewHolder.tvQuote.setText(quote.getContent());
            myViewHolder.tvQuote.setOnClickListener(new View.OnClickListener() {
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
        return quotes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuote;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(View itemView) {
            super(itemView);
            tvQuote = itemView.findViewById(R.id.tvQuote);
        }
    }

    public interface OnItemClickListener {
        public void onItemCheck(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

