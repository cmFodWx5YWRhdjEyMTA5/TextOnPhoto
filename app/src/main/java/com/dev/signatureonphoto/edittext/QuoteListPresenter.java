package com.dev.signatureonphoto.edittext;


import com.dev.signatureonphoto.database.DataManager;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuoteListPresenter  {
    private DataManager dataManager;

    public QuoteListPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

}