package com.dev.signatureonphoto.injection.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.dev.signatureonphoto.data.remote.MyQuotesService;

import retrofit2.Retrofit;

/**
 * Created by shivam on 29/5/17.
 */
@Module(includes = {NetworkModule.class})
public class ApiModule {
    @Provides
    @Singleton
    MyQuotesService provideQuoteApi(Retrofit retrofit) {
        return retrofit.create(MyQuotesService.class);
    }
}
