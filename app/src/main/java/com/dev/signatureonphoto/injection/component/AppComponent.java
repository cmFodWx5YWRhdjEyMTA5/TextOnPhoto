package com.dev.signatureonphoto.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;


import com.dev.signatureonphoto.data.DataManager;
import com.dev.signatureonphoto.injection.ApplicationContext;
import com.dev.signatureonphoto.injection.module.AppModule;
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    DataManager apiManager();
}
