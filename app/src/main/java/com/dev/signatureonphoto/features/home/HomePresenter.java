package com.dev.signatureonphoto.features.home;

import android.content.Context;


import javax.inject.Inject;

public class HomePresenter  {
    private Context context;

    @Inject
    public HomePresenter() {
    }



    void setContext(Context context){
        this.context=context;
    }


    void cameraListenter(){

    }
}
