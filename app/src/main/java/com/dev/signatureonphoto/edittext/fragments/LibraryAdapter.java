package com.dev.signatureonphoto.edittext.fragments;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.database.Quote;
import com.dev.signatureonphoto.edittext.QuoteListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {
    private Context context;
    private List<Quote> quotes;
    private QuoteListener listener;

    public LibraryAdapter(Context context, List<Quote> quotes, QuoteListener listener) {
        this.context = context;
        this.quotes = quotes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quote_library, parent, false);
        return new LibraryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryHolder holder, int position) {
        holder.onBind(quotes.get(position));
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    class LibraryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_content)
        TextView txtContent;

        @OnClick(R.id.btn_copy)
        void onQuoteCopy() {
            if(listener!=null){
                listener.onQuoteSelected(quote.getContent());
            }
        }

        private Quote quote;

        LibraryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(Quote quote) {
            this.quote=quote;
            txtContent.setText(quote.getContent());
        }
    }
}
