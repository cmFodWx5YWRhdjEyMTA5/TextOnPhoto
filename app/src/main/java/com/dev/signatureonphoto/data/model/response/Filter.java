package com.dev.signatureonphoto.data.model.response;

import android.graphics.Bitmap;

public class Filter {
    private Bitmap filterThumb;
    private String filterPath;

    public Filter(Bitmap filterThumb, String filterPath) {
        this.filterThumb = filterThumb;
        this.filterPath = filterPath;
    }

    public Bitmap getFilterThumb() {
        return filterThumb;
    }

    public String getFilterPath() {
        return filterPath;
    }
}
