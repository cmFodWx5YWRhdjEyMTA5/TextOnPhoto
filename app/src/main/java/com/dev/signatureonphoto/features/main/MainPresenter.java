package com.dev.signatureonphoto.features.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.dev.signatureonphoto.data.model.response.Filter;
import com.dev.signatureonphoto.util.filter.GPUImageFilterTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainPresenter {
    private static final int FILTER_GROUP_NUMBER = 3;
    private FilterListener listener;
    private String[] filterDir = {"filters/a", "filters/b", "filters/c"};

    public MainPresenter(FilterListener listener) {
        this.listener = listener;
    }

    @SuppressLint("StaticFieldLeak")
    public void loadFillterList(Context context, Bitmap currentBitmap, GPUImageFilterTools gpuImageFilterTools) {
        List<List<Filter>> items = new ArrayList<>();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                bmWidth = currentBitmap.getWidth();
                bmHeight = currentBitmap.getHeight();
                Bitmap currentThumbail = getResizedBitmap(currentBitmap, 5);
                for (int i = 0; i < FILTER_GROUP_NUMBER; i++) {
                    List<Filter> filters=new ArrayList<>();
                    try {
                        String[] paths = context.getAssets().list(filterDir[i]);
                        if (paths != null) {
                            for (String path : paths) {
                                String filterPath=filterDir[i]+"/"+path;
                                InputStream inputStream = context.getAssets().open(filterPath);
                                gpuImageFilterTools.filterImage(currentThumbail, inputStream);
                                Bitmap bitmapThumbnail = gpuImageFilterTools.getBitmap();
                                Filter filter=new Filter(bitmapThumbnail, filterPath);
                                filters.add(filter);
                            }
                            filters.add(0, new Filter(currentBitmap, ""));
                            items.add(filters);
                        }
                    } catch (IOException e) {
                        //
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (listener != null) {
                    listener.onFilterLoaded(items);
                }
            }

        }.execute();
    }

    private int bmWidth = -1, bmHeight = -1;

    private Bitmap getResizedBitmap(Bitmap bm, int divisor) {
        float scale = 1 / (float) divisor;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        if (bmWidth <= 0) bmWidth = bm.getWidth();
        if (bmHeight <= 0) bmHeight = bm.getHeight();
        return Bitmap.createBitmap(bm, 0, 0, bmWidth, bmHeight, matrix, false);
    }
}
