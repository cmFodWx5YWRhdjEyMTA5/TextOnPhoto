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
import com.dev.signatureonphoto.database.Quote;
import com.dev.signatureonphoto.edittext.QuoteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class LibraryFragment extends Fragment {
    @BindView(R.id.list_quote)
    RecyclerView listQuote;

    private QuoteListener listener;
    private Unbinder unbinder;
    private LibraryAdapter adapter;
    private String[] countryList={"RU", "CN", "JP"};

    public void setListener(QuoteListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_quote_library, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        listQuote.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initData() {
        List<Quote> quotes=new ArrayList<>();
        Locale current = getResources().getConfiguration().locale;
        String country=current.getCountry();
        final JSONArray quoteLists, quoteListsEn;
        try {
//            if (country.equals(countryList[0])) {
//                quoteLists  = new JSONArray(loadJSONFromAsset(Constants.VI_JSON_LINK));
//                for (int i = 0; i < quoteLists.length(); i++) {
//                    JSONObject json_data = quoteLists.getJSONObject(i);
//                    Quote quote = new Quote();
//                    quote.setCategore(json_data.getString(Constants.VI_CATEGORY_FIELD));
//                    quote.setSource(json_data.getString(Constants.VI_SOURCE_FIELD));
//                    quote.setContent(json_data.getString(Constants.VI_CONTENT_FIELD));
//                    quote.setUpload(Constants.FALSE);
//                    quotes.add(quote);
//                }
//                quoteListsEn = new JSONArray(loadJSONFromAsset(Constants.EN_JSON_LINK));
//                for (int i = 0; i < quoteListsEn.length(); i++) {
//                    JSONObject json_data = quoteListsEn.getJSONObject(i);
//                    Quote quote = new Quote();
//                    quote.setCategore(json_data.getString(Constants.EN_LOVE_FIELD));
//                    quote.setSource(json_data.getString(Constants.EN_S2QUOTE_FIELD));
//                    quote.setContent(json_data.getString(Constants.EN_CONTENT));
//                    quote.setUpload(Constants.FALSE);
//                    quotes.add(quote);
//                }
//
//            } else
                if (!country.equals(countryList[0]) && !country.equals(countryList[1]) && !country.equals(countryList[2])){
                quoteLists  = new JSONArray(loadJSONFromAsset(Constants.EN_JSON_LINK));
                for (int i = 0; i < quoteLists.length(); i++) {
                    JSONObject json_data = quoteLists.getJSONObject(i);
                    Quote quote = new Quote();
                    quote.setCategore(json_data.getString(Constants.EN_LOVE_FIELD));
                    quote.setSource(json_data.getString(Constants.EN_S2QUOTE_FIELD));
                    quote.setContent(json_data.getString(Constants.EN_CONTENT));
                    quote.setUpload(Constants.FALSE);
                    quotes.add(quote);
                }
            }

            adapter=new LibraryAdapter(getContext(), quotes, listener);
            listQuote.setAdapter(adapter);

        } catch (JSONException e) {
        }
    }

    public String loadJSONFromAsset(String quotes) {

        String json = null;
        try {
            InputStream is = getContext().getAssets().open(quotes);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
