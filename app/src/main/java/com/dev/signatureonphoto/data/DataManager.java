package com.dev.signatureonphoto.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.dev.signatureonphoto.data.remote.MyQuotesService;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by shivam on 29/5/17.
 */
@Singleton
public class DataManager {

    private MyQuotesService myQuotesService;

    @Inject
    public DataManager(MyQuotesService myQuotesService) {
        this.myQuotesService = myQuotesService;
    }

}
