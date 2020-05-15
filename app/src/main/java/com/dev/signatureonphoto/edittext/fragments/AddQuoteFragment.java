package com.dev.signatureonphoto.edittext.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.database.DataManager;
import com.dev.signatureonphoto.database.Quote;
import com.dev.signatureonphoto.edittext.QuoteListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddQuoteFragment extends Fragment {
    @BindView(R.id.list_quote)
    RecyclerView listQuote;

    private QuoteListener listener;
    private Unbinder unbinder;
    private UserQuoteAdapter adapter;

    public void setListener(QuoteListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.btn_add)
    void addQuote() {
        if (listener != null) {
            listener.addQuote();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_quote, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void refreshData() {
        List<Quote> quotes = DataManager.query().getQuoteDao().queryBuilder().list();
        if (quotes.size() == 0) {
            Quote quote = new Quote();
            quote.setContent(Constants.textads);
            quote.setUpload(Constants.FALSE);
            quote.setCategore("");
            quote.setSource("");
            quotes.add(quote);
            DataManager.query().getQuoteDao().save(quote);
        }
        Collections.reverse(quotes);
        adapter = new UserQuoteAdapter(getContext(), quotes, listener);
        listQuote.setAdapter(adapter);
    }

    private void initData() {
        listQuote.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
